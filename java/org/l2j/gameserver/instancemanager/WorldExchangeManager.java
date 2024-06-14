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
package org.l2j.gameserver.instancemanager;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;

import org.l2j.Config;
import org.l2j.commons.database.DatabaseFactory;
import org.l2j.commons.threads.ThreadPool;
import org.l2j.commons.util.IXmlReader;
import org.l2j.gameserver.enums.ItemLocation;
import org.l2j.gameserver.enums.WorldExchangeItemStatusType;
import org.l2j.gameserver.enums.WorldExchangeItemSubType;
import org.l2j.gameserver.enums.WorldExchangeSortType;
import org.l2j.gameserver.model.ItemInfo;
import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.VariationInstance;
import org.l2j.gameserver.model.World;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.WorldExchangeHolder;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.itemcontainer.Inventory;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.serverpackets.InventoryUpdate;
import org.l2j.gameserver.network.serverpackets.SystemMessage;
import org.l2j.gameserver.network.serverpackets.worldexchange.WorldExchangeBuyItem;
import org.l2j.gameserver.network.serverpackets.worldexchange.WorldExchangeRegisterItem;
import org.l2j.gameserver.network.serverpackets.worldexchange.WorldExchangeSellCompleteAlarm;
import org.l2j.gameserver.network.serverpackets.worldexchange.WorldExchangeSettleList;
import org.l2j.gameserver.network.serverpackets.worldexchange.WorldExchangeSettleRecvResult;

/**
 * @author Index
 */
public class WorldExchangeManager implements IXmlReader
{
	private static final Logger LOGGER = Logger.getLogger(WorldExchangeManager.class.getName());
	
	private static final String SELECT_ALL_ITEMS = "SELECT * FROM `items` WHERE `loc`=?";
	private static final String RESTORE_INFO = "SELECT * FROM world_exchange_items";
	private static final String INSERT_WORLD_EXCHANGE = "REPLACE INTO world_exchange_items (`world_exchange_id`, `item_object_id`, `item_status`, `category_id`, `price`, `old_owner_id`, `start_time`, `end_time`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	
	private final Map<Long, WorldExchangeHolder> _itemBids = new ConcurrentHashMap<>();
	private final Map<Integer, WorldExchangeItemSubType> _itemCategories = new ConcurrentHashMap<>();
	private final Map<String, Map<Integer, String>> _localItemNames = new HashMap<>(new HashMap<>());
	private long _lastWorldExchangeId = 0;
	
	private ScheduledFuture<?> _checkStatus = null;
	
	public WorldExchangeManager()
	{
		if (!Config.ENABLE_WORLD_EXCHANGE)
		{
			return;
		}
		
		firstLoad();
		if (_checkStatus == null)
		{
			_checkStatus = ThreadPool.scheduleAtFixedRate(this::checkBidStatus, Config.WORLD_EXCHANGE_SAVE_INTERVAL, Config.WORLD_EXCHANGE_SAVE_INTERVAL);
		}
	}
	
	@Override
	public void load()
	{
		if (Config.MULTILANG_ENABLE)
		{
			_localItemNames.clear();
			for (String lang : Config.MULTILANG_ALLOWED)
			{
				final File file = new File("data/lang/" + lang + "/ItemNameLocalisation.xml");
				if (!file.isFile())
				{
					continue;
				}
				
				parseDatapackFile("data/lang/" + lang + "/ItemNameLocalisation.xml");
				final int size = _localItemNames.get(lang).size();
				if (size == 0)
				{
					_localItemNames.remove(lang);
				}
				else
				{
					LOGGER.log(Level.INFO, getClass().getSimpleName() + ": Loaded ItemName localisations for [" + lang + "].");
				}
			}
		}
		
		if (!Config.MULTILANG_DEFAULT.equals(Config.WORLD_EXCHANGE_DEFAULT_LANG) && !_localItemNames.containsKey(Config.WORLD_EXCHANGE_DEFAULT_LANG))
		{
			parseDatapackFile("data/lang/" + Config.WORLD_EXCHANGE_DEFAULT_LANG + "/ItemNameLocalisation.xml");
		}
	}
	
	@Override
	public void parseDocument(Document doc, File f)
	{
		final ConcurrentHashMap<Integer, String> local = new ConcurrentHashMap<>();
		forEach(doc, "list", listNode ->
		{
			forEach(listNode, "blessed", itemNode ->
			{
				StatSet itemSet = new StatSet(parseAttributes(itemNode));
				local.put(-1, itemSet.getString("name"));
			});
			forEach(listNode, "item", itemNode ->
			{
				StatSet itemSet = new StatSet(parseAttributes(itemNode));
				local.put(itemSet.getInt("id"), itemSet.getString("name"));
			});
		});
		_localItemNames.put(doc.getDocumentURI().split("data/lang/")[1].split("/")[0], local);
	}
	
	public Map<Integer, String> getItemLocalByLang(String lang)
	{
		return _localItemNames.get(lang);
	}
	
	synchronized void firstLoad()
	{
		final Map<Integer, Item> itemInstances = loadItemInstances();
		loadItemBids(itemInstances);
		load();
	}
	
	/**
	 * Little task which check and update bid items if it needs.
	 */
	private void checkBidStatus()
	{
		if (!Config.ENABLE_WORLD_EXCHANGE)
		{
			return;
		}
		
		for (Entry<Long, WorldExchangeHolder> entry : _itemBids.entrySet())
		{
			final WorldExchangeHolder holder = entry.getValue();
			final long currentTime = System.currentTimeMillis();
			final long endTime = holder.getEndTime();
			if (endTime > currentTime)
			{
				continue;
			}
			
			switch (holder.getStoreType())
			{
				case WORLD_EXCHANGE_NONE:
				{
					_itemBids.remove(entry.getKey());
					continue;
				}
				case WORLD_EXCHANGE_REGISTERED:
				{
					holder.setEndTime(calculateDate(Config.WORLD_EXCHANGE_ITEM_BACK_PERIOD));
					holder.setStoreType(WorldExchangeItemStatusType.WORLD_EXCHANGE_OUT_TIME);
					_itemBids.replace(entry.getKey(), holder);
					insert(entry.getKey(), false);
					break;
				}
				case WORLD_EXCHANGE_SOLD:
				case WORLD_EXCHANGE_OUT_TIME:
				{
					holder.setStoreType(WorldExchangeItemStatusType.WORLD_EXCHANGE_NONE);
					insert(entry.getKey(), true);
					Item item = holder.getItemInstance();
					item.setItemLocation(ItemLocation.VOID);
					item.updateDatabase(!Config.WORLD_EXCHANGE_LAZY_UPDATE);
					break;
				}
			}
		}
	}
	
	/**
	 * Load items from database for make proper holders
	 * @return
	 */
	synchronized Map<Integer, Item> loadItemInstances()
	{
		if (!Config.ENABLE_WORLD_EXCHANGE)
		{
			return Collections.emptyMap();
		}
		
		final Map<Integer, Item> itemInstances = new HashMap<>();
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(SELECT_ALL_ITEMS))
		{
			ps.setString(1, ItemLocation.EXCHANGE.name());
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				final Item itemInstance = new Item(rs);
				itemInstances.put(itemInstance.getObjectId(), itemInstance);
			}
		}
		catch (SQLException e)
		{
			LOGGER.log(Level.WARNING, getClass().getSimpleName() + ": Failed loading items instances.", e);
		}
		
		return itemInstances;
	}
	
	/**
	 * Loading all items, which used or using in World Exchange.
	 * @param itemInstances
	 */
	private synchronized void loadItemBids(Map<Integer, Item> itemInstances)
	{
		if (!Config.ENABLE_WORLD_EXCHANGE)
		{
			return;
		}
		
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(RESTORE_INFO))
		{
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				boolean needChange = false;
				final long worldExchangeId = rs.getLong("world_exchange_id");
				_lastWorldExchangeId = Math.max(worldExchangeId, _lastWorldExchangeId);
				final Item itemInstance = itemInstances.get(rs.getInt("item_object_id"));
				WorldExchangeItemStatusType storeType = WorldExchangeItemStatusType.getWorldExchangeItemStatusType(rs.getInt("item_status"));
				
				if (storeType == WorldExchangeItemStatusType.WORLD_EXCHANGE_NONE)
				{
					continue;
				}
				
				if (itemInstance == null)
				{
					LOGGER.warning(getClass().getSimpleName() + ": Failed loading commission item with world exchange id " + worldExchangeId + " because item instance does not exist or failed to load.");
					continue;
				}
				
				final WorldExchangeItemSubType categoryId = WorldExchangeItemSubType.getWorldExchangeItemSubType(rs.getInt("category_id"));
				final long price = rs.getLong("price");
				final int bidPlayerObjectId = rs.getInt("old_owner_id");
				final long startTime = rs.getLong("start_time");
				long endTime = rs.getLong("end_time");
				if (endTime < System.currentTimeMillis())
				{
					if ((storeType == WorldExchangeItemStatusType.WORLD_EXCHANGE_OUT_TIME) || (storeType == WorldExchangeItemStatusType.WORLD_EXCHANGE_SOLD))
					{
						itemInstance.setItemLocation(ItemLocation.VOID);
						itemInstance.updateDatabase(true);
						continue;
					}
					endTime = calculateDate(Config.WORLD_EXCHANGE_ITEM_BACK_PERIOD);
					storeType = WorldExchangeItemStatusType.WORLD_EXCHANGE_OUT_TIME;
					needChange = true;
				}
				_itemBids.put(worldExchangeId, new WorldExchangeHolder(worldExchangeId, itemInstance, new ItemInfo(itemInstance), price, bidPlayerObjectId, storeType, categoryId, startTime, endTime, needChange));
			}
		}
		catch (Exception e)
		{
			LOGGER.log(Level.WARNING, getClass().getSimpleName() + ": Failed loading bid items.", e);
		}
	}
	
	private long calculateFeeForRegister(Player player, int objectId, long amount, long priceForEach)
	{
		final Item itemToRemove = player.getInventory().getItemByObjectId(objectId);
		if (itemToRemove.getId() == Inventory.ADENA_ID)
		{
			return priceForEach * 100L;
		}
		return Math.round(priceForEach * (itemToRemove.getId() == Inventory.ADENA_ID ? 1 : amount) * Config.WORLD_EXCHANGE_ADENA_FEE);
	}
	
	/**
	 * Forwarded from client packet "ExWorldExchangeRegisterItem" for check ops and register item if it can in World Exchange system
	 * @param player
	 * @param itemObjectId
	 * @param amount
	 * @param priceForEach
	 */
	public synchronized void registerItemBid(Player player, int itemObjectId, long amount, long priceForEach)
	{
		if (!Config.ENABLE_WORLD_EXCHANGE)
		{
			return;
		}
		
		final Map<WorldExchangeItemStatusType, List<WorldExchangeHolder>> playerBids = getPlayerBids(player.getObjectId());
		if (playerBids.size() >= 10)
		{
			player.sendPacket(new SystemMessage(SystemMessageId.NO_SLOTS_AVAILABLE));
			player.sendPacket(WorldExchangeRegisterItem.FAIL);
			return;
		}
		if (player.getInventory().getItemByObjectId(itemObjectId) == null)
		{
			player.sendPacket(new SystemMessage(SystemMessageId.THE_ITEM_IS_NOT_FOUND));
			player.sendPacket(WorldExchangeRegisterItem.FAIL);
			return;
		}
		if ((amount < 1) || (priceForEach < 1) || ((amount * priceForEach) < 1))
		{
			player.sendPacket(new SystemMessage(SystemMessageId.INCORRECT_ITEM_COUNT_2));
			player.sendPacket(WorldExchangeRegisterItem.FAIL);
			return;
		}
		
		final Item item = player.getInventory().getItemByObjectId(itemObjectId);
		long feePrice = calculateFeeForRegister(player, itemObjectId, amount, priceForEach);
		if ((Config.WORLD_EXCHANGE_MAX_ADENA_FEE != -1) && (feePrice > Config.WORLD_EXCHANGE_MAX_ADENA_FEE))
		{
			feePrice = Config.WORLD_EXCHANGE_MAX_ADENA_FEE;
		}
		if (feePrice > player.getAdena())
		{
			player.sendPacket(new SystemMessage(SystemMessageId.NOT_ENOUGH_ADENA));
			player.sendPacket(WorldExchangeRegisterItem.FAIL);
			return;
		}
		if (feePrice < 1)
		{
			player.sendPacket(new SystemMessage(SystemMessageId.INCORRECT_ITEM_COUNT_2));
			player.sendPacket(WorldExchangeRegisterItem.FAIL);
			return;
		}
		
		final long freeId = getNextId();
		final InventoryUpdate iu = new InventoryUpdate();
		if (item.isStackable() && (player.getInventory().getInventoryItemCount(item.getId(), -1) > amount))
		{
			iu.addModifiedItem(item);
		}
		else
		{
			iu.addRemovedItem(item);
		}
		
		final Item itemInstance = player.getInventory().detachItem("World Exchange Registration", item, amount, ItemLocation.EXCHANGE, player, null);
		if (itemInstance == null)
		{
			player.sendPacket(new SystemMessage(SystemMessageId.THE_ITEM_IS_NOT_FOUND));
			player.sendPacket(WorldExchangeRegisterItem.FAIL);
			return;
		}
		
		final WorldExchangeItemSubType category = _itemCategories.get(itemInstance.getId());
		if (category == null)
		{
			player.sendPacket(new SystemMessage(SystemMessageId.THE_ITEM_YOU_REGISTERED_HAS_BEEN_SOLD));
			player.sendPacket(WorldExchangeRegisterItem.FAIL);
			return;
		}
		
		player.sendInventoryUpdate(iu);
		player.getInventory().reduceAdena("World Exchange Registration", feePrice, player, null);
		final long endTime = calculateDate(Config.WORLD_EXCHANGE_ITEM_SELL_PERIOD);
		_itemBids.put(freeId, new WorldExchangeHolder(freeId, itemInstance, new ItemInfo(itemInstance), priceForEach, player.getObjectId(), WorldExchangeItemStatusType.WORLD_EXCHANGE_REGISTERED, category, System.currentTimeMillis(), endTime, true));
		player.sendPacket(new WorldExchangeRegisterItem(itemObjectId, amount, (byte) 1));
		if (!Config.WORLD_EXCHANGE_LAZY_UPDATE)
		{
			insert(freeId, false);
		}
	}
	
	private synchronized long getNextId()
	{
		return _lastWorldExchangeId++;
	}
	
	private long calculateDate(int days)
	{
		final Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, days);
		return calendar.getTimeInMillis();
	}
	
	/**
	 * Forwarded from ExWorldExchangeSettleRecvResult for make Action, because client send only WORLD EXCHANGE Index without anu addition info.
	 * @param player
	 * @param worldExchangeIndex
	 */
	public void getItemStatusAndMakeAction(Player player, long worldExchangeIndex)
	{
		if (!Config.ENABLE_WORLD_EXCHANGE)
		{
			return;
		}
		
		final WorldExchangeHolder worldExchangeItem = _itemBids.get(worldExchangeIndex);
		if (worldExchangeItem == null)
		{
			player.sendPacket(new SystemMessage(SystemMessageId.THE_ITEM_IS_NOT_FOUND));
			player.sendPacket(WorldExchangeSettleRecvResult.FAIL);
			return;
		}
		
		final WorldExchangeItemStatusType storeType = worldExchangeItem.getStoreType();
		switch (storeType)
		{
			case WORLD_EXCHANGE_REGISTERED:
			{
				cancelBid(player, worldExchangeItem);
				break;
			}
			case WORLD_EXCHANGE_SOLD:
			{
				takeBidMoney(player, worldExchangeItem);
				break;
			}
			case WORLD_EXCHANGE_OUT_TIME:
			{
				returnItem(player, worldExchangeItem);
				break;
			}
		}
	}
	
	/**
	 * Forwarded from getItemStatusAndMakeAction / remove item and holder from active bid and take it back to owner.
	 * @param player
	 * @param worldExchangeItem
	 */
	private void cancelBid(Player player, WorldExchangeHolder worldExchangeItem)
	{
		if (!Config.ENABLE_WORLD_EXCHANGE)
		{
			return;
		}
		
		if (worldExchangeItem.getStoreType() == WorldExchangeItemStatusType.WORLD_EXCHANGE_NONE)
		{
			player.sendPacket(new WorldExchangeSettleList(player));
			player.sendPacket(new SystemMessage(SystemMessageId.THE_ITEM_IS_NOT_FOUND));
			player.sendPacket(WorldExchangeSettleRecvResult.FAIL);
			return;
		}
		
		if (!_itemBids.containsKey(worldExchangeItem.getWorldExchangeId()))
		{
			player.sendPacket(new WorldExchangeSettleList(player));
			player.sendPacket(new SystemMessage(SystemMessageId.THE_ITEM_IS_NOT_FOUND));
			player.sendPacket(WorldExchangeSettleRecvResult.FAIL);
			return;
		}
		
		if (_itemBids.get(worldExchangeItem.getWorldExchangeId()) != worldExchangeItem)
		{
			player.sendPacket(new WorldExchangeSettleList(player));
			player.sendPacket(new SystemMessage(SystemMessageId.THE_ITEM_IS_NOT_FOUND));
			player.sendPacket(WorldExchangeSettleRecvResult.FAIL);
			return;
		}
		
		if (player.getObjectId() != worldExchangeItem.getOldOwnerId())
		{
			player.sendPacket(new SystemMessage(SystemMessageId.ITEM_OUT_OF_STOCK));
			player.sendPacket(WorldExchangeSettleRecvResult.FAIL);
			return;
		}
		
		if (worldExchangeItem.getStoreType() == WorldExchangeItemStatusType.WORLD_EXCHANGE_SOLD)
		{
			player.sendPacket(new WorldExchangeSettleList(player));
			player.sendPacket(new SystemMessage(SystemMessageId.THE_ITEM_YOU_REGISTERED_HAS_BEEN_SOLD));
			player.sendPacket(WorldExchangeSettleRecvResult.FAIL);
			return;
		}
		
		player.sendPacket(new WorldExchangeSettleRecvResult(worldExchangeItem.getItemInstance().getObjectId(), worldExchangeItem.getItemInstance().getCount(), (byte) 1));
		player.getInventory().addItem("World Exchange Cancellation", worldExchangeItem.getItemInstance(), player, player);
		worldExchangeItem.setStoreType(WorldExchangeItemStatusType.WORLD_EXCHANGE_NONE);
		worldExchangeItem.setHasChanges(true);
		_itemBids.replace(worldExchangeItem.getWorldExchangeId(), worldExchangeItem);
		if (!Config.WORLD_EXCHANGE_LAZY_UPDATE)
		{
			insert(worldExchangeItem.getWorldExchangeId(), true);
		}
	}
	
	/**
	 * Forwarded from getItemStatusAndMakeAction / takes money from bid.
	 * @param player
	 * @param worldExchangeItem
	 */
	private void takeBidMoney(Player player, WorldExchangeHolder worldExchangeItem)
	{
		if (!Config.ENABLE_WORLD_EXCHANGE)
		{
			return;
		}
		
		if (worldExchangeItem.getStoreType() == WorldExchangeItemStatusType.WORLD_EXCHANGE_NONE)
		{
			player.sendPacket(new WorldExchangeSettleList(player));
			player.sendPacket(new SystemMessage(SystemMessageId.THE_ITEM_IS_NOT_FOUND));
			player.sendPacket(WorldExchangeSettleRecvResult.FAIL);
			return;
		}
		
		if (!_itemBids.containsKey(worldExchangeItem.getWorldExchangeId()))
		{
			player.sendPacket(new WorldExchangeSettleList(player));
			player.sendPacket(new SystemMessage(SystemMessageId.THE_ITEM_IS_NOT_FOUND));
			player.sendPacket(WorldExchangeSettleRecvResult.FAIL);
			return;
		}
		
		if (_itemBids.get(worldExchangeItem.getWorldExchangeId()) != worldExchangeItem)
		{
			player.sendPacket(new WorldExchangeSettleList(player));
			player.sendPacket(new SystemMessage(SystemMessageId.THE_ITEM_IS_NOT_FOUND));
			player.sendPacket(WorldExchangeSettleRecvResult.FAIL);
			return;
		}
		
		if (player.getObjectId() != worldExchangeItem.getOldOwnerId())
		{
			player.sendPacket(new SystemMessage(SystemMessageId.THE_ITEM_IS_NOT_FOUND));
			player.sendPacket(WorldExchangeSettleRecvResult.FAIL);
			return;
		}
		
		if (worldExchangeItem.getStoreType() != WorldExchangeItemStatusType.WORLD_EXCHANGE_SOLD)
		{
			player.sendPacket(new WorldExchangeSettleList(player));
			player.sendPacket(new SystemMessage(SystemMessageId.THE_ITEM_YOU_REGISTERED_HAS_BEEN_SOLD));
			player.sendPacket(WorldExchangeSettleRecvResult.FAIL);
			return;
		}
		
		if (worldExchangeItem.getEndTime() < System.currentTimeMillis())
		{
			player.sendPacket(new WorldExchangeSettleList(player));
			player.sendPacket(new SystemMessage(SystemMessageId.THE_REGISTRATION_PERIOD_FOR_THE_ITEM_YOU_REGISTERED_HAS_EXPIRED));
			player.sendPacket(WorldExchangeSettleRecvResult.FAIL);
			return;
		}
		
		player.sendPacket(new WorldExchangeSettleRecvResult(worldExchangeItem.getItemInstance().getObjectId(), worldExchangeItem.getItemInstance().getCount(), (byte) 1));
		final long fee = Math.round(((worldExchangeItem.getPrice() * Config.WORLD_EXCHANGE_LCOIN_TAX) * 100) / 100);
		final long returnPrice = worldExchangeItem.getPrice() - Math.min(fee, (Config.WORLD_EXCHANGE_MAX_LCOIN_TAX != -1 ? Config.WORLD_EXCHANGE_MAX_LCOIN_TAX : Long.MAX_VALUE)); // floating-point accuracy workaround :D
		player.getInventory().addItem("World Exchange Took Money", Inventory.LCOIN_ID, (returnPrice), player, null);
		worldExchangeItem.setStoreType(WorldExchangeItemStatusType.WORLD_EXCHANGE_NONE);
		Item item = worldExchangeItem.getItemInstance();
		item.setItemLocation(ItemLocation.VOID);
		item.updateDatabase(!Config.WORLD_EXCHANGE_LAZY_UPDATE);
		worldExchangeItem.setHasChanges(true);
		_itemBids.replace(worldExchangeItem.getWorldExchangeId(), worldExchangeItem);
		if (!Config.WORLD_EXCHANGE_LAZY_UPDATE)
		{
			insert(worldExchangeItem.getWorldExchangeId(), true);
		}
	}
	
	/**
	 * Forwarded from getItemStatusAndMakeAction / take back item which placed on World Exchange.
	 * @param player
	 * @param worldExchangeItem
	 */
	private void returnItem(Player player, WorldExchangeHolder worldExchangeItem)
	{
		if (!Config.ENABLE_WORLD_EXCHANGE)
		{
			return;
		}
		
		if (worldExchangeItem.getStoreType() == WorldExchangeItemStatusType.WORLD_EXCHANGE_NONE)
		{
			player.sendPacket(new WorldExchangeSettleList(player));
			player.sendPacket(new SystemMessage(SystemMessageId.THE_ITEM_IS_NOT_FOUND));
			player.sendPacket(WorldExchangeSettleRecvResult.FAIL);
			return;
		}
		
		if (!_itemBids.containsKey(worldExchangeItem.getWorldExchangeId()))
		{
			player.sendPacket(new WorldExchangeSettleList(player));
			player.sendPacket(new SystemMessage(SystemMessageId.THE_ITEM_IS_NOT_FOUND));
			player.sendPacket(WorldExchangeSettleRecvResult.FAIL);
			return;
		}
		
		if (_itemBids.get(worldExchangeItem.getWorldExchangeId()) != worldExchangeItem)
		{
			player.sendPacket(new WorldExchangeSettleList(player));
			player.sendPacket(new SystemMessage(SystemMessageId.ITEM_OUT_OF_STOCK));
			player.sendPacket(WorldExchangeSettleRecvResult.FAIL);
			return;
		}
		
		if (player.getObjectId() != worldExchangeItem.getOldOwnerId())
		{
			player.sendPacket(new SystemMessage(SystemMessageId.ITEM_TO_BE_TRADED_DOES_NOT_EXIST));
			player.sendPacket(WorldExchangeSettleRecvResult.FAIL);
			return;
		}
		
		if (worldExchangeItem.getStoreType() != WorldExchangeItemStatusType.WORLD_EXCHANGE_OUT_TIME)
		{
			player.sendPacket(new WorldExchangeSettleList(player));
			player.sendPacket(new SystemMessage(SystemMessageId.ITEM_OUT_OF_STOCK));
			player.sendPacket(WorldExchangeSettleRecvResult.FAIL);
			return;
		}
		
		if (worldExchangeItem.getEndTime() < System.currentTimeMillis())
		{
			player.sendPacket(new WorldExchangeSettleList(player));
			player.sendPacket(new SystemMessage(SystemMessageId.THE_REGISTRATION_PERIOD_FOR_THE_ITEM_YOU_REGISTERED_HAS_EXPIRED));
			player.sendPacket(WorldExchangeSettleRecvResult.FAIL);
			return;
		}
		
		player.sendPacket(new WorldExchangeSettleRecvResult(worldExchangeItem.getItemInstance().getObjectId(), worldExchangeItem.getItemInstance().getCount(), (byte) 1));
		player.getInventory().addItem("World Exchange Took Out Time Item Back", worldExchangeItem.getItemInstance(), player, null);
		worldExchangeItem.setStoreType(WorldExchangeItemStatusType.WORLD_EXCHANGE_NONE);
		worldExchangeItem.setHasChanges(true);
		_itemBids.replace(worldExchangeItem.getWorldExchangeId(), worldExchangeItem);
		if (!Config.WORLD_EXCHANGE_LAZY_UPDATE)
		{
			insert(worldExchangeItem.getWorldExchangeId(), true);
		}
	}
	
	/**
	 * Forwarded from ExWorldExchangeBuyItem / request for but item and create a visible clone for old owner.
	 * @param player
	 * @param worldExchangeId
	 */
	public void buyItem(Player player, long worldExchangeId)
	{
		if (!Config.ENABLE_WORLD_EXCHANGE)
		{
			return;
		}
		
		if (!_itemBids.containsKey(worldExchangeId))
		{
			player.sendPacket(new SystemMessage(SystemMessageId.THE_ITEM_IS_NOT_FOUND));
			player.sendPacket(WorldExchangeBuyItem.FAIL);
			return;
		}
		
		final WorldExchangeHolder worldExchangeItem = _itemBids.get(worldExchangeId);
		if (worldExchangeItem.getStoreType() == WorldExchangeItemStatusType.WORLD_EXCHANGE_NONE)
		{
			player.sendPacket(new SystemMessage(SystemMessageId.THE_ITEM_IS_NOT_FOUND));
			player.sendPacket(WorldExchangeBuyItem.FAIL);
			return;
		}
		
		if (worldExchangeItem.getStoreType() != WorldExchangeItemStatusType.WORLD_EXCHANGE_REGISTERED)
		{
			player.sendPacket(new SystemMessage(SystemMessageId.ITEM_OUT_OF_STOCK));
			player.sendPacket(WorldExchangeBuyItem.FAIL);
			return;
		}
		
		final Item lcoin = player.getInventory().getItemByItemId(Inventory.LCOIN_ID);
		if ((lcoin == null) || (lcoin.getCount() < worldExchangeItem.getPrice()))
		{
			player.sendPacket(new SystemMessage(SystemMessageId.YOU_DO_NOT_HAVE_ENOUGH_L2_COINS_ADD_MORE_L2_COINS_AND_TRY_AGAIN));
			player.sendPacket(WorldExchangeBuyItem.FAIL);
			return;
		}
		
		player.getInventory().destroyItem("World Exchange Buying", lcoin, worldExchangeItem.getPrice(), player, null);
		final Item newItem = createItem(worldExchangeItem.getItemInstance(), player);
		final long destroyTime = calculateDate(Config.WORLD_EXCHANGE_PAYMENT_TAKE_PERIOD);
		WorldExchangeHolder newHolder = new WorldExchangeHolder(worldExchangeId, newItem, new ItemInfo(newItem), worldExchangeItem.getPrice(), worldExchangeItem.getOldOwnerId(), WorldExchangeItemStatusType.WORLD_EXCHANGE_SOLD, worldExchangeItem.getCategory(), worldExchangeItem.getStartTime(), destroyTime, true);
		_itemBids.replace(worldExchangeId, worldExchangeItem, newHolder);
		if (!Config.WORLD_EXCHANGE_LAZY_UPDATE)
		{
			insert(worldExchangeItem.getWorldExchangeId(), false);
		}
		final Item receivedItem = player.getInventory().addItem("World Exchange Buying", worldExchangeItem.getItemInstance(), player, null);
		player.sendPacket(new WorldExchangeBuyItem(receivedItem.getObjectId(), receivedItem.getCount(), (byte) 1));
		final SystemMessage sm;
		if (receivedItem.getEnchantLevel() > 0)
		{
			if (receivedItem.getCount() < 2)
			{
				sm = new SystemMessage(SystemMessageId.YOU_VE_OBTAINED_S1_S2_4);
				sm.addByte(receivedItem.getEnchantLevel());
				sm.addItemName(receivedItem);
			}
			else
			{
				sm = new SystemMessage(SystemMessageId.YOU_VE_OBTAINED_S1_S2_X_S3);
				sm.addItemName(receivedItem);
				sm.addLong(receivedItem.getCount());
				sm.addByte(receivedItem.getEnchantLevel());
			}
		}
		else
		{
			sm = new SystemMessage(SystemMessageId.YOU_HAVE_OBTAINED_S1_X_S2);
			sm.addItemName(receivedItem);
			sm.addLong(receivedItem.getCount());
		}
		
		player.sendPacket(sm);
		for (Player oldOwner : World.getInstance().getPlayers())
		{
			if (oldOwner.getObjectId() == newHolder.getOldOwnerId())
			{
				oldOwner.sendPacket(new WorldExchangeSellCompleteAlarm(newItem.getId(), newItem.getCount()));
				break;
			}
		}
	}
	
	/**
	 * Create a new item for make it visible in UI for old owner.
	 * @param oldItem item from holder which will be "cloned"
	 * @param requestor
	 * @return cloned item
	 */
	private Item createItem(Item oldItem, Player requestor)
	{
		final Item newItem = new Item(oldItem.getId());
		newItem.setOwnerId(requestor.getObjectId());
		newItem.setEnchantLevel(oldItem.getEnchantLevel() < 1 ? 0 : oldItem.getEnchantLevel());
		newItem.setItemLocation(ItemLocation.EXCHANGE);
		newItem.setCount(oldItem.getCount());
		newItem.setVisualId(oldItem.getVisualId(), false);
		newItem.setBlessed(oldItem.isBlessed());
		newItem.setOwnerId(oldItem.getOwnerId());
		newItem.updateDatabase(true); // in any case it will be store in database
		final VariationInstance vi = oldItem.getAugmentation();
		if (vi != null)
		{
			newItem.setAugmentation(vi, true);
		}
		final InventoryUpdate iu = new InventoryUpdate();
		iu.addRemovedItem(newItem);
		requestor.sendInventoryUpdate(iu);
		return newItem;
	}
	
	/**
	 * @param ownerId
	 * @param type
	 * @param sortType
	 * @param lang
	 * @return items, which player can buy
	 */
	public List<WorldExchangeHolder> getItemBids(int ownerId, WorldExchangeItemSubType type, WorldExchangeSortType sortType, String lang)
	{
		if (!Config.ENABLE_WORLD_EXCHANGE)
		{
			return Collections.emptyList();
		}
		
		final List<WorldExchangeHolder> returnList = new ArrayList<>();
		for (WorldExchangeHolder holder : _itemBids.values())
		{
			if (holder.getStoreType() == WorldExchangeItemStatusType.WORLD_EXCHANGE_NONE)
			{
				continue;
			}
			
			if ((holder.getOldOwnerId() == ownerId) || (holder.getCategory() != type))
			{
				continue;
			}
			
			if (holder.getStoreType() == WorldExchangeItemStatusType.WORLD_EXCHANGE_REGISTERED)
			{
				returnList.add(holder);
			}
		}
		
		return sortList(returnList, sortType, lang);
	}
	
	/**
	 * @param ids
	 * @param sortType
	 * @param lang
	 * @return items with the same id (used in registration, where shows similar items with price)
	 */
	public List<WorldExchangeHolder> getItemBids(List<Integer> ids, WorldExchangeSortType sortType, String lang)
	{
		if (!Config.ENABLE_WORLD_EXCHANGE)
		{
			return Collections.emptyList();
		}
		
		final List<WorldExchangeHolder> returnList = new ArrayList<>();
		for (WorldExchangeHolder holder : _itemBids.values())
		{
			if (holder.getStoreType() == WorldExchangeItemStatusType.WORLD_EXCHANGE_NONE)
			{
				continue;
			}
			
			if (ids.contains(holder.getItemInstance().getId()) && (holder.getStoreType() == WorldExchangeItemStatusType.WORLD_EXCHANGE_REGISTERED))
			{
				returnList.add(holder);
			}
		}
		
		return sortList(returnList, sortType, lang);
	}
	
	/**
	 * @param unsortedList
	 * @param sortType
	 * @param lang
	 * @return sort items by type if it needs 399 - that max value which can been in list buffer size - 32768 - list has 11 + cycle of 82 bytes - 32768 / 82 = 399.6 = 32718 for item info + 50 reserved = 32729 item info and initial data + 39 reserved
	 */
	private List<WorldExchangeHolder> sortList(List<WorldExchangeHolder> unsortedList, WorldExchangeSortType sortType, String lang)
	{
		final List<WorldExchangeHolder> sortedList = new ArrayList<>(unsortedList);
		switch (sortType)
		{
			case PRICE_ASCE:
			{
				Collections.sort(sortedList, Comparator.comparing(WorldExchangeHolder::getPrice));
				break;
			}
			case PRICE_DESC:
			{
				Collections.sort(sortedList, Comparator.comparing(WorldExchangeHolder::getPrice));
				Collections.reverse(sortedList);
				break;
			}
			case ITEM_NAME_ASCE:
			{
				if ((lang == null) || (!lang.equals("en") && _localItemNames.containsKey(lang)))
				{
					Collections.sort(sortedList, Comparator.comparing(o -> getItemName(lang, o.getItemInstance().getId(), o.getItemInstance().isBlessed())));
				}
				else
				{
					Collections.sort(sortedList, Comparator.comparing(o -> (o.getItemInstance().isBlessed() ? "Blessed " : "") + o.getItemInstance().getItemName()));
				}
				break;
			}
			case ITEM_NAME_DESC:
			{
				if ((lang == null) || (!lang.equals("en") && _localItemNames.containsKey(lang)))
				{
					Collections.sort(sortedList, Comparator.comparing(o -> getItemName(lang, o.getItemInstance().getId(), o.getItemInstance().isBlessed())));
				}
				else
				{
					Collections.sort(sortedList, Comparator.comparing(o -> (o.getItemInstance().isBlessed() ? "Blessed " : "") + o.getItemInstance().getItemName()));
				}
				Collections.reverse(sortedList);
				break;
			}
			case PRICE_PER_PIECE_ASCE:
			{
				Collections.sort(sortedList, Comparator.comparingLong(WorldExchangeHolder::getPrice));
				break;
			}
			case PRICE_PER_PIECE_DESC:
			{
				Collections.sort(sortedList, Comparator.comparingLong(WorldExchangeHolder::getPrice).reversed());
				break;
			}
		}
		
		if (sortedList.size() > 399)
		{
			return sortedList.subList(0, 399);
		}
		
		return sortedList;
	}
	
	private String getItemName(String lang, int id, boolean isBlessed)
	{
		if (!_localItemNames.containsKey(lang))
		{
			return "";
		}
		
		final Map<Integer, String> names = _localItemNames.get(lang);
		final String name = names.get(id);
		if (name == null)
		{
			return "";
		}
		
		if (isBlessed)
		{
			return names.get(-1) + " " + name;
		}
		
		return name;
	}
	
	/**
	 * @param ownerId
	 * @return items which will bid player
	 */
	public Map<WorldExchangeItemStatusType, List<WorldExchangeHolder>> getPlayerBids(int ownerId)
	{
		if (!Config.ENABLE_WORLD_EXCHANGE)
		{
			return Collections.emptyMap();
		}
		
		final List<WorldExchangeHolder> registered = new ArrayList<>();
		final List<WorldExchangeHolder> sold = new ArrayList<>();
		final List<WorldExchangeHolder> outTime = new ArrayList<>();
		for (WorldExchangeHolder holder : _itemBids.values())
		{
			if (holder.getStoreType() == WorldExchangeItemStatusType.WORLD_EXCHANGE_NONE)
			{
				continue;
			}
			
			if (holder.getOldOwnerId() != ownerId)
			{
				continue;
			}
			
			switch (holder.getStoreType())
			{
				case WORLD_EXCHANGE_REGISTERED:
				{
					registered.add(holder);
					break;
				}
				case WORLD_EXCHANGE_SOLD:
				{
					sold.add(holder);
					break;
				}
				case WORLD_EXCHANGE_OUT_TIME:
				{
					outTime.add(holder);
					break;
				}
			}
		}
		
		final EnumMap<WorldExchangeItemStatusType, List<WorldExchangeHolder>> returnMap = new EnumMap<>(WorldExchangeItemStatusType.class);
		returnMap.put(WorldExchangeItemStatusType.WORLD_EXCHANGE_REGISTERED, registered);
		returnMap.put(WorldExchangeItemStatusType.WORLD_EXCHANGE_SOLD, sold);
		returnMap.put(WorldExchangeItemStatusType.WORLD_EXCHANGE_OUT_TIME, outTime);
		return returnMap;
	}
	
	public void addCategoryType(List<Integer> itemIds, int category)
	{
		if (!Config.ENABLE_WORLD_EXCHANGE)
		{
			return;
		}
		
		for (int itemId : itemIds)
		{
			final WorldExchangeItemSubType type = WorldExchangeItemSubType.getWorldExchangeItemSubType(category);
			if (type == null)
			{
				LOGGER.warning(getClass().getSimpleName() + ": Non existent category type [" + category + "] for item id " + itemId + "!");
				continue;
			}
			
			_itemCategories.putIfAbsent(itemId, type);
		}
	}
	
	/**
	 * Will send player alarm on WorldEnter if player has success sold items or items, if time is out
	 * @param player
	 */
	public void checkPlayerSellAlarm(Player player)
	{
		if (!Config.ENABLE_WORLD_EXCHANGE)
		{
			return;
		}
		
		for (WorldExchangeHolder holder : _itemBids.values())
		{
			if ((holder.getOldOwnerId() == player.getObjectId()) && ((holder.getStoreType() == WorldExchangeItemStatusType.WORLD_EXCHANGE_SOLD) || (holder.getStoreType() == WorldExchangeItemStatusType.WORLD_EXCHANGE_OUT_TIME)))
			{
				player.sendPacket(new WorldExchangeSellCompleteAlarm(holder.getItemInstance().getId(), holder.getItemInstance().getCount()));
				break;
			}
		}
	}
	
	public void storeMe()
	{
		if (!Config.ENABLE_WORLD_EXCHANGE || !Config.WORLD_EXCHANGE_LAZY_UPDATE)
		{
			return;
		}
		
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement statement = con.prepareStatement(INSERT_WORLD_EXCHANGE))
		{
			for (WorldExchangeHolder holder : _itemBids.values())
			{
				if (!holder.hasChanges())
				{
					continue;
				}
				
				statement.setLong(1, holder.getWorldExchangeId());
				statement.setLong(2, holder.getItemInstance().getObjectId());
				statement.setInt(3, holder.getStoreType().getId());
				statement.setInt(4, holder.getCategory().getId());
				statement.setLong(5, holder.getPrice());
				statement.setInt(6, holder.getOldOwnerId());
				statement.setLong(7, holder.getStartTime());
				statement.setLong(8, holder.getEndTime());
				statement.addBatch();
			}
			statement.executeBatch();
			statement.closeOnCompletion();
		}
		catch (SQLException e)
		{
			LOGGER.log(Level.SEVERE, "Error while saving World Exchange item bids:\n", e);
		}
	}
	
	public void insert(long worldExchangeId, boolean remove)
	{
		if (Config.WORLD_EXCHANGE_LAZY_UPDATE)
		{
			return;
		}
		
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement statement = con.prepareStatement(INSERT_WORLD_EXCHANGE))
		{
			WorldExchangeHolder holder = _itemBids.get(worldExchangeId);
			statement.setLong(1, holder.getWorldExchangeId());
			statement.setLong(2, holder.getItemInstance().getObjectId());
			statement.setInt(3, holder.getStoreType().getId());
			statement.setInt(4, holder.getCategory().getId());
			statement.setLong(5, holder.getPrice());
			statement.setInt(6, holder.getOldOwnerId());
			statement.setString(7, String.valueOf(holder.getStartTime()));
			statement.setString(8, String.valueOf(holder.getEndTime()));
			statement.execute();
			if (remove)
			{
				_itemBids.remove(worldExchangeId);
			}
		}
		catch (SQLException e)
		{
			LOGGER.log(Level.SEVERE, "Error while saving World Exchange item bid " + worldExchangeId + "\n", e);
		}
	}
	
	/**
	 * Returns the average price of the specified item.
	 * @param itemId the ID of the item
	 * @return the average price, or 0 if there are no items with the specified ID
	 */
	public long getAveragePriceOfItem(int itemId)
	{
		long totalPrice = 0;
		long totalItemCount = 0;
		for (WorldExchangeHolder holder : _itemBids.values())
		{
			if (holder.getItemInstance().getTemplate().getId() != itemId)
			{
				continue;
			}
			
			totalItemCount++;
			totalPrice += holder.getPrice();
		}
		return totalItemCount == 0 ? 0 : totalPrice / totalItemCount;
	}
	
	public static WorldExchangeManager getInstance()
	{
		return WorldExchangeManager.SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final WorldExchangeManager INSTANCE = new WorldExchangeManager();
	}
}
