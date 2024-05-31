/*
 * This file is part of the L2J 4Team project.
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.AbstractMap.SimpleEntry;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.l2j.Config;
import org.l2j.commons.database.DatabaseFactory;
import org.l2j.commons.threads.ThreadPool;
import org.l2j.gameserver.data.sql.CharInfoTable;
import org.l2j.gameserver.enums.MailType;
import org.l2j.gameserver.model.Message;
import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.World;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.events.EventDispatcher;
import org.l2j.gameserver.model.events.EventType;
import org.l2j.gameserver.model.events.impl.item.OnItemPurgeReward;
import org.l2j.gameserver.model.holders.PurgePlayerHolder;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.itemcontainer.Mail;
import org.l2j.gameserver.network.serverpackets.subjugation.ExSubjugationSidebar;

/**
 * @author Berezkin Nikolay
 */
public class PurgeRankingManager
{
	protected static final Logger LOGGER = Logger.getLogger(PurgeRankingManager.class.getName());
	
	private static final Map<Integer, Map<Integer, StatSet>> _ranking = new HashMap<>();
	private static final String RESTORE_SUBJUGATION = "SELECT *, `points` + `keys` * 1000000 as `total` FROM `character_purge` WHERE `category`=? ORDER BY `total` DESC";
	private static final String DELETE_SUBJUGATION = "DELETE FROM character_purge WHERE charId=? and category=?";
	
	public PurgeRankingManager()
	{
		updateRankingFromDB();
		
		int nextDate = Calendar.getInstance().get(Calendar.MINUTE);
		while ((nextDate % 5) != 0)
		{
			nextDate = nextDate + 1;
		}
		
		ThreadPool.scheduleAtFixedRate(this::updateRankingFromDB, (nextDate - Calendar.getInstance().get(Calendar.MINUTE)) > 0 ? (long) (nextDate - Calendar.getInstance().get(Calendar.MINUTE)) * 60 * 1000 : (long) ((nextDate + 60) - Calendar.getInstance().get(Calendar.MINUTE)) * 60 * 1000, 300000);
	}
	
	private void updateRankingFromDB()
	{
		// Weekly rewards.
		final long lastPurgeRewards = GlobalVariablesManager.getInstance().getLong(GlobalVariablesManager.PURGE_REWARD_TIME, 0);
		if ((Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) && ((System.currentTimeMillis() - lastPurgeRewards) > (604800000 /* 1 week */ - 600000 /* task delay x2 */)))
		{
			GlobalVariablesManager.getInstance().set(GlobalVariablesManager.PURGE_REWARD_TIME, System.currentTimeMillis());
			for (int category = 1; category <= 9; category++)
			{
				if (getTop5(category) != null)
				{
					int counter = 0;
					for (Entry<String, Integer> purgeData : getTop5(category).entrySet())
					{
						final int charId = CharInfoTable.getInstance().getIdByName(purgeData.getKey());
						final Message msg = new Message(charId, Config.SUBJUGATION_TOPIC_HEADER, Config.SUBJUGATION_TOPIC_BODY, MailType.PURGE_REWARD);
						final Mail attachment = msg.createAttachments();
						int reward;
						switch (category)
						{
							case 1:
							{
								reward = 95460;
								break;
							}
							case 2:
							{
								reward = 95461;
								break;
							}
							case 3:
							{
								reward = 95462;
								break;
							}
							case 4:
							{
								reward = 95463;
								break;
							}
							case 5:
							{
								reward = 95464;
								break;
							}
							case 6:
							{
								reward = 95465;
								break;
							}
							case 7:
							{
								reward = 96724;
								break;
							}
							case 8:
							{
								reward = 97225;
								break;
							}
							case 9:
							{
								reward = 95466;
								break;
							}
							default:
							{
								throw new IllegalStateException("Unexpected value: " + category);
							}
						}
						attachment.addItem("Purge reward", reward, 5 - counter, null, null);
						MailManager.getInstance().sendMessage(msg);
						
						// Notify to scripts.
						final Player player = World.getInstance().getPlayer(charId);
						final Item item = attachment.getItemByItemId(reward);
						if (player != null)
						{
							if (EventDispatcher.getInstance().hasListener(EventType.ON_ITEM_PURGE_REWARD))
							{
								EventDispatcher.getInstance().notifyEventAsync(new OnItemPurgeReward(player, item));
							}
						}
						
						try (Connection con = DatabaseFactory.getConnection())
						{
							try (PreparedStatement st = con.prepareStatement(DELETE_SUBJUGATION))
							{
								st.setInt(1, charId);
								st.setInt(2, category);
								st.execute();
							}
							catch (Exception e)
							{
								LOGGER.log(Level.SEVERE, "Failed to delete character subjugation info " + charId, e);
							}
						}
						catch (Exception e)
						{
							LOGGER.log(Level.SEVERE, "Failed to delete character subjugation info " + charId, e);
						}
						
						final Player onlinePlayer = World.getInstance().getPlayer(charId);
						if (onlinePlayer != null)
						{
							onlinePlayer.getPurgePoints().clear();
							onlinePlayer.sendPacket(new ExSubjugationSidebar(null, new PurgePlayerHolder(0, 0, 0)));
						}
						
						counter++;
					}
				}
			}
		}
		
		// Clear ranking.
		_ranking.clear();
		
		// Restore ranking.
		for (int category = 1; category <= 9; category++)
		{
			restoreByCategories(category);
		}
	}
	
	private void restoreByCategories(int category)
	{
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement statement = con.prepareStatement(RESTORE_SUBJUGATION))
		{
			statement.setInt(1, category);
			try (ResultSet rset = statement.executeQuery())
			{
				int rank = 1;
				Map<Integer, StatSet> rankingInCategory = new HashMap<>();
				while (rset.next())
				{
					final StatSet set = new StatSet();
					set.set("charId", rset.getInt("charId"));
					set.set("points", rset.getInt("total"));
					rankingInCategory.put(rank, set);
					rank++;
				}
				_ranking.put(category, rankingInCategory);
			}
			// LOGGER.info(getClass().getSimpleName() + ": Loaded " + _ranking.get(category).size() + " records for category " + category + ".");
		}
		catch (Exception e)
		{
			LOGGER.log(Level.SEVERE, "Could not restore subjugation ranking data", e);
		}
	}
	
	public Map<String, Integer> getTop5(int category)
	{
		Map<String, Integer> top5 = new HashMap<>();
		for (int i = 1; i <= 5; i++)
		{
			try
			{
				if (_ranking.get(category) == null)
				{
					continue;
				}
				
				final StatSet ss = _ranking.get(category).get(i);
				if (ss == null)
				{
					continue;
				}
				
				final String charName = CharInfoTable.getInstance().getNameById(ss.getInt("charId"));
				final int points = ss.getInt("points");
				top5.put(charName, points);
			}
			catch (IndexOutOfBoundsException ignored)
			{
			}
		}
		return top5.entrySet().stream().sorted(Entry.<String, Integer> comparingByValue().reversed()).collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
	}
	
	public SimpleEntry<Integer, Integer> getPlayerRating(int category, int charId)
	{
		if (_ranking.get(category) == null)
		{
			return new SimpleEntry<>(0, 0);
		}
		
		final Optional<Entry<Integer, StatSet>> player = _ranking.get(category).entrySet().stream().filter(it -> it.getValue().getInt("charId") == charId).findFirst();
		if (player.isPresent())
		{
			if (player.get().getValue() == null)
			{
				return new SimpleEntry<>(0, 0);
			}
			return new SimpleEntry<>(player.get().getKey(), player.get().getValue().getInt("points"));
		}
		
		return new SimpleEntry<>(0, 0);
	}
	
	public static PurgeRankingManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final PurgeRankingManager INSTANCE = new PurgeRankingManager();
	}
}
