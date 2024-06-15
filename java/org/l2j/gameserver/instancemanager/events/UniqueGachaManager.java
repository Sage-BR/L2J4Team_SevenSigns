/*
 * This file is part of the L2J 4Team Project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.l2j.gameserver.instancemanager.events;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.l2j.commons.database.DatabaseFactory;
import org.l2j.commons.threads.ThreadPool;
import org.l2j.commons.util.Rnd;
import org.l2j.gameserver.data.xml.ItemData;
import org.l2j.gameserver.enums.UniqueGachaRank;
import org.l2j.gameserver.instancemanager.QuestManager;
import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.events.Containers;
import org.l2j.gameserver.model.events.EventType;
import org.l2j.gameserver.model.events.impl.creature.player.OnPlayerLogin;
import org.l2j.gameserver.model.events.impl.creature.player.OnPlayerLogout;
import org.l2j.gameserver.model.events.listeners.ConsumerEventListener;
import org.l2j.gameserver.model.holders.GachaItemHolder;
import org.l2j.gameserver.model.holders.GachaItemTimeStampHolder;
import org.l2j.gameserver.model.holders.ItemHolder;
import org.l2j.gameserver.model.item.ItemTemplate;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.itemcontainer.GachaWarehouse;
import org.l2j.gameserver.model.itemcontainer.PlayerInventory;
import org.l2j.gameserver.model.quest.Quest;
import org.l2j.gameserver.network.serverpackets.gacha.UniqueGachaSidebarInfo;

public class UniqueGachaManager
{
	/**
	 * @implNote 0.000001 MAXIMAL OF MINIMUM VALUE
	 */
	public static final int MINIMUM_CHANCE = 1_000_000;
	public static final int MINIMUM_CHANCE_AFTER_DOT = 6;
	public static final String GACHA_PLAYER_VARIABLE = "GACHA_ROLL_COUNT";
	public static final String GACHA_LOCK_PLAYER_VARIABLE = "UniqueGachaRoll";
	
	private static final LinkedList<GachaItemTimeStampHolder> EMPTY_LINKED_LIST = new LinkedList<>();
	
	private final Set<GachaItemHolder> _visibleItems = new HashSet<>();
	private final Map<UniqueGachaRank, Set<GachaItemHolder>> _rewardItems = new HashMap<>();
	private final Map<UniqueGachaRank, Integer> _rewardChance = new HashMap<>();
	private final Map<Integer, Long> _gameCosts = new HashMap<>();
	
	private final Map<Player, GachaWarehouse> _temporaryWarehouse = new HashMap<>();
	private final Map<Integer, LinkedList<GachaItemTimeStampHolder>> _gachaHistory = new HashMap<>();
	
	private boolean _isActive;
	private long _activeUntilPeriod;
	private int _guaranteeRoll;
	private boolean _showProbability;
	private int _currencyItemId;
	private int _totalRewardCount = 0;
	private int _totalChanceSumma = 0;
	
	public UniqueGachaManager()
	{
		restoreGachaHistory(_gachaHistory);
		ThreadPool.scheduleAtFixedRate(() -> storeGachaHistory(_gachaHistory), TimeUnit.MINUTES.toMillis(5), TimeUnit.MINUTES.toMillis(5));
		Containers.Global().addListener(new ConsumerEventListener(Containers.Global(), EventType.ON_PLAYER_LOGIN, (OnPlayerLogin event) -> onPlayerLogin(event), this));
		Containers.Global().addListener(new ConsumerEventListener(Containers.Global(), EventType.ON_PLAYER_LOGOUT, (OnPlayerLogout event) -> onPlayerLogout(event), this));
	}
	
	public void reload()
	{
		final Quest quest = QuestManager.getInstance().getQuest("UniqueGacha");
		if (quest == null)
		{
			return;
		}
		
		_totalRewardCount = 0;
		_totalChanceSumma = 0;
		_visibleItems.clear();
		_rewardItems.clear();
		_rewardChance.clear();
		_gameCosts.clear();
		quest.notifyEvent("RELOAD", null, null);
	}
	
	public void setParameters(StatSet params)
	{
		_isActive = params.getBoolean("isActive", false);
		_activeUntilPeriod = params.getLong("activeUntilPeriod", 0L);
		_guaranteeRoll = params.getInt("guaranteeRoll", 200);
		_showProbability = params.getBoolean("showProbability", false);
		_currencyItemId = params.getInt("currencyItemId", 57);
	}
	
	public void addReward(UniqueGachaRank rank, int itemId, long itemCount, int itemChance, int enchantLevel)
	{
		_totalRewardCount += 1;
		_totalChanceSumma += itemChance;
		_rewardItems.putIfAbsent(rank, new HashSet<>());
		_rewardItems.get(rank).add(new GachaItemHolder(itemId, itemCount, itemChance, enchantLevel, rank));
	}
	
	public void addGameCost(int rollCount, long itemCount)
	{
		_gameCosts.put(rollCount, itemCount);
	}
	
	public void recalculateChances()
	{
		for (Entry<UniqueGachaRank, Set<GachaItemHolder>> entry : _rewardItems.entrySet())
		{
			int totalChance = 0;
			for (GachaItemHolder item : entry.getValue())
			{
				totalChance += item.getItemChance();
			}
			_rewardChance.put(entry.getKey(), totalChance);
		}
		
		recalculateVisibleItems();
	}
	
	private void recalculateVisibleItems()
	{
		final List<GachaItemHolder> rewards = new ArrayList<>(_rewardItems.getOrDefault(UniqueGachaRank.RANK_UR, Set.of()));
		rewards.sort(Comparator.comparingInt(GachaItemHolder::getItemChance));
		if (rewards.isEmpty())
		{
			return;
		}
		
		_visibleItems.clear();
		_visibleItems.addAll(rewards.subList(0, Math.min(5, (rewards.size()))));
		rewards.clear();
	}
	
	private void onPlayerLogin(OnPlayerLogin event)
	{
		final Player player = event == null ? null : event.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (isActive())
		{
			GachaWarehouse warehouse = _temporaryWarehouse.getOrDefault(player, null);
			if (warehouse == null)
			{
				warehouse = new GachaWarehouse(player);
				warehouse.restore();
				_temporaryWarehouse.put(player, warehouse);
			}
		}
		player.sendPacket(isActive() ? UniqueGachaSidebarInfo.GACHA_ON : UniqueGachaSidebarInfo.GACHA_OFF);
	}
	
	private void onPlayerLogout(OnPlayerLogout event)
	{
		final Player player = event == null ? null : event.getPlayer();
		if (player == null)
		{
			return;
		}
		
		GachaWarehouse warehouse = _temporaryWarehouse.getOrDefault(player, null);
		if (warehouse != null)
		{
			warehouse.deleteMe();
			_temporaryWarehouse.remove(player);
		}
	}
	
	public Set<GachaItemHolder> getVisibleItems()
	{
		return _visibleItems;
	}
	
	public Map<UniqueGachaRank, Set<GachaItemHolder>> getRewardItems()
	{
		return _rewardItems;
	}
	
	public Map<Integer, Long> getGameCosts()
	{
		return _gameCosts;
	}
	
	public boolean isActive()
	{
		return _isActive;
	}
	
	public long getActiveUntilPeriod()
	{
		return _activeUntilPeriod;
	}
	
	public int getGuaranteeRoll()
	{
		return _guaranteeRoll;
	}
	
	public boolean isShowProbability()
	{
		return _showProbability;
	}
	
	public int getCurrencyItemId()
	{
		return _currencyItemId;
	}
	
	public int getCurrencyCount(Player player)
	{
		final long count = player.getInventory().getInventoryItemCount(getCurrencyItemId(), -1);
		return (int) Math.min(Integer.MAX_VALUE, count);
	}
	
	private void removeCurrency(Player player, long count)
	{
		final PlayerInventory inv = player.getInventory();
		if ((inv == null) || !inv.canManipulateWithItemId(_currencyItemId))
		{
			return;
		}
		
		final Item item = inv.getItemByItemId(_currencyItemId);
		if (item == null)
		{
			return;
		}
		
		player.destroyItemByItemId("gacha", item.getId(), count, player, true);
	}
	
	public int getStepsToGuaranteedReward(Player player)
	{
		return _guaranteeRoll - player.getVariables().getInt(GACHA_PLAYER_VARIABLE, 0);
	}
	
	public Entry<List<GachaItemHolder>, Boolean> tryToRoll(Player player, int rollCount)
	{
		boolean rare = false;
		final List<GachaItemHolder> rewards = new ArrayList<>();
		if (!checkRequirements(player, rollCount))
		{
			return returnEmptyList();
		}
		try
		{
			player.getVariables().set(GACHA_LOCK_PLAYER_VARIABLE, true);
			removeCurrency(player, _gameCosts.get(rollCount));
			int playerRollProgress = player.getVariables().getInt(GACHA_PLAYER_VARIABLE, 0);
			for (int roll = 0; roll < rollCount; roll++)
			{
				playerRollProgress += 1;
				final boolean isGuaranteed = playerRollProgress >= _guaranteeRoll;
				playerRollProgress = isGuaranteed ? 0 : playerRollProgress;
				final UniqueGachaRank rank = randomRank(isGuaranteed);
				final GachaItemHolder item = getRandomReward(rank);
				rare = rare || (item.getRank().equals(UniqueGachaRank.RANK_SR) || item.getRank().equals(UniqueGachaRank.RANK_UR));
				addItemToTemporaryWarehouse(player, item);
				rewards.add(item);
				addToHistory(player, item, roll);
			}
			player.getVariables().set(GACHA_PLAYER_VARIABLE, playerRollProgress);
		}
		finally
		{
			player.getVariables().remove(GACHA_LOCK_PLAYER_VARIABLE);
		}
		
		return new AbstractMap.SimpleEntry<>(rewards, rare);
	}
	
	private boolean checkRequirements(Player player, int rollCount)
	{
		if (player.getVariables().getBoolean(GACHA_LOCK_PLAYER_VARIABLE, false))
		{
			return false;
		}
		
		final long currencyCount = _gameCosts.getOrDefault(rollCount, -1L);
		if (currencyCount == -1L)
		{
			return false;
		}
		
		final PlayerInventory inv = player.getInventory();
		if ((inv == null) || !inv.canManipulateWithItemId(_currencyItemId) || (getCurrencyCount(player) < currencyCount))
		{
			return false;
		}
		
		final GachaWarehouse warehouse = _temporaryWarehouse.getOrDefault(player, null);
		if (warehouse == null)
		{
			return false;
		}
		
		if (warehouse.getSize() >= GachaWarehouse.MAXIMUM_TEMPORARY_WAREHOUSE_COUNT)
		{
			return false;
		}
		
		return true;
	}
	
	private void addToHistory(Player player, GachaItemHolder item, int roll)
	{
		final String count = String.valueOf(roll);
		final String timeStamp = String.valueOf(System.currentTimeMillis() / 1000L) + "0".repeat(3 - count.length()) + (count);
		_gachaHistory.computeIfAbsent(player.getObjectId(), v -> new LinkedList<>()).addLast(new GachaItemTimeStampHolder(item.getId(), item.getCount(), item.getEnchantLevel(), item.getRank(), Long.parseLong(timeStamp), false));
	}
	
	private boolean addItemToTemporaryWarehouse(Player player, GachaItemHolder reward)
	{
		final GachaWarehouse warehouse = _temporaryWarehouse.getOrDefault(player, null);
		if (warehouse == null)
		{
			return false;
		}
		
		final ItemTemplate template = ItemData.getInstance().getTemplate(reward.getId());
		if (template == null)
		{
			return false;
		}
		
		boolean isSuccess = false;
		if (!template.isStackable())
		{
			for (long index = 0; index < reward.getCount(); index++)
			{
				final Item item = warehouse.addItem("Gacha Reward", reward.getId(), reward.getCount(), player, null);
				if (item == null)
				{
					isSuccess = false;
					break;
				}
				isSuccess = true;
			}
		}
		else
		{
			isSuccess = warehouse.addItem("Gacha Reward", reward.getId(), reward.getCount(), player, null) != null;
		}
		
		return isSuccess;
	}
	
	public boolean receiveItemsFromTemporaryWarehouse(Player player, List<ItemHolder> requestedItems)
	{
		final GachaWarehouse warehouse = _temporaryWarehouse.getOrDefault(player, null);
		final PlayerInventory inventory = player.getInventory();
		if ((warehouse == null) || (inventory == null))
		{
			return false;
		}
		
		// Check items.
		for (ItemHolder requestedItem : requestedItems)
		{
			final ItemTemplate template = ItemData.getInstance().getTemplate(requestedItem.getId());
			final List<Item> item = template == null ? null : template.isStackable() ? List.of(warehouse.getItemByItemId(requestedItem.getId())) : new ArrayList<>(warehouse.getAllItemsByItemId(requestedItem.getId()));
			if ((item == null) || item.isEmpty() || ((template != null) && template.isStackable() ? item.get(0).getCount() < requestedItem.getCount() : item.size() < requestedItem.getCount()))
			{
				return false;
			}
		}
		
		for (ItemHolder requestedItem : requestedItems)
		{
			final ItemTemplate template = ItemData.getInstance().getTemplate(requestedItem.getId());
			final List<Item> item = template.isStackable() ? List.of(warehouse.getItemByItemId(requestedItem.getId())) : new ArrayList<>(warehouse.getAllItemsByItemId(requestedItem.getId()));
			for (Item wareHouseItem : item)
			{
				warehouse.transferItem("Gacha receive from Warehouse", wareHouseItem.getObjectId(), (template.isStackable() ? requestedItem.getCount() : 1), inventory, player, null);
			}
		}
		player.sendItemList();
		
		return true;
	}
	
	public Collection<Item> getTemporaryWarehouse(Player player)
	{
		final GachaWarehouse warehouse = _temporaryWarehouse.getOrDefault(player, null);
		if (warehouse == null)
		{
			return Collections.emptyList();
		}
		
		return warehouse.getItems();
	}
	
	private UniqueGachaRank randomRank(boolean isGuaranteed)
	{
		final int rollRank = Rnd.get(0, _totalChanceSumma);
		final UniqueGachaRank rank;
		int sumChance = 0;
		if ((isGuaranteed && (Rnd.get(0, 100) <= 10)) || (rollRank <= (sumChance += _rewardChance.getOrDefault(UniqueGachaRank.RANK_UR, 0))))
		{
			rank = UniqueGachaRank.RANK_UR;
		}
		else if (isGuaranteed || (rollRank <= (sumChance + _rewardChance.getOrDefault(UniqueGachaRank.RANK_SR, 0))))
		{
			rank = UniqueGachaRank.RANK_SR;
		}
		else
		{
			rank = UniqueGachaRank.RANK_R;
		}
		return rank;
	}
	
	// Old method with Collections.shuffle
	// private GachaItemHolder getRandomReward(UniqueGachaRank rank)
	// {
	// final List<GachaItemHolder> rollRewards = new ArrayList<>(_rewardItems.getOrDefault(rank, Set.of()));
	// Collections.shuffle(rollRewards);
	// int rollChance = 0;
	// final int rollReward = Rnd.get(0, _rewardChance.getOrDefault(rank, MINIMUM_CHANCE));
	// GachaItemHolder rewardItem = null;
	// for (GachaItemHolder item : rollRewards)
	// {
	// rewardItem = item;
	// rollChance += item.getItemChance();
	// if (rollChance >= rollReward)
	// {
	// break;
	// }
	// }
	// return rewardItem;
	// }
	
	private GachaItemHolder getRandomReward(UniqueGachaRank rank)
	{
		final Set<GachaItemHolder> rollRewards = _rewardItems.getOrDefault(rank, Collections.emptySet());
		int totalItems = rollRewards.size();
		if (totalItems == 0)
		{
			return null; // or handle the case where no rewards are available for the given rank.
		}
		
		int rollChance = 0;
		GachaItemHolder rewardItem = null;
		
		// Calculate total chances.
		for (GachaItemHolder item : rollRewards)
		{
			rollChance += item.getItemChance();
		}
		
		// Generate random index.
		int randomIndex = Rnd.get(0, rollChance);
		int cumulativeChance = 0;
		
		// Find the reward item corresponding to the random index.
		for (GachaItemHolder item : rollRewards)
		{
			cumulativeChance += item.getItemChance();
			if (randomIndex < cumulativeChance)
			{
				rewardItem = item;
				break;
			}
		}
		
		return rewardItem;
	}
	
	public int getTotalRewardCount()
	{
		return _totalRewardCount;
	}
	
	public List<GachaItemTimeStampHolder> getGachaCharacterHistory(Player player)
	{
		return _gachaHistory.getOrDefault(player.getObjectId(), EMPTY_LINKED_LIST);
	}
	
	private static void restoreGachaHistory(Map<Integer, LinkedList<GachaItemTimeStampHolder>> history)
	{
		try (Connection con = DatabaseFactory.getConnection();
			Statement st = con.createStatement();
			ResultSet rset = st.executeQuery("SELECT * FROM character_gacha_history ORDER BY receive_time ASC LIMIT 110"))
		{
			while (rset.next())
			{
				final int characterId = rset.getInt("char_id");
				final int itemId = rset.getInt("item_id");
				final long itemCount = rset.getLong("item_count");
				final int enchantLevel = rset.getInt("item_enchant");
				final int itemRank = rset.getInt("item_rank");
				final long receiveTime = rset.getLong("receive_time");
				history.computeIfAbsent(characterId, v -> new LinkedList<>()).addLast(new GachaItemTimeStampHolder(itemId, itemCount, enchantLevel, UniqueGachaRank.getRankByClientId(itemRank), receiveTime, true));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static void storeGachaHistory(Map<Integer, LinkedList<GachaItemTimeStampHolder>> history)
	{
		final Map<Integer, LinkedList<GachaItemTimeStampHolder>> map = new HashMap<>(history);
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement statement = con.prepareStatement("INSERT INTO `character_gacha_history`(`char_id`, `item_id`, `item_count`, `item_enchant`, `item_rank`, `receive_time`) VALUES(?, ?, ?, ?, ?, ?)"))
		{
			boolean containsUpdate = false;
			for (Entry<Integer, LinkedList<GachaItemTimeStampHolder>> entry : map.entrySet())
			{
				final int charId = entry.getKey();
				final LinkedList<GachaItemTimeStampHolder> list = new LinkedList<>(entry.getValue());
				for (GachaItemTimeStampHolder item : list)
				{
					if (item.getStoredStatus())
					{
						continue;
					}
					statement.setInt(1, charId);
					statement.setInt(2, item.getId());
					statement.setLong(3, item.getCount());
					statement.setInt(4, item.getEnchantLevel());
					statement.setInt(5, item.getRank().getClientId());
					statement.setLong(6, item.getTimeStamp());
					statement.addBatch();
					containsUpdate = true;
				}
			}
			if (containsUpdate)
			{
				statement.executeBatch();
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	private static Entry<List<GachaItemHolder>, Boolean> returnEmptyList()
	{
		return new AbstractMap.SimpleEntry<>(Collections.emptyList(), false);
	}
	
	public static UniqueGachaManager getInstance()
	{
		return UniqueGachaManager.SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final UniqueGachaManager INSTANCE = new UniqueGachaManager();
	}
}
