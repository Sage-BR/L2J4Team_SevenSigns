/*
 * This file is part of the L2J Mobius project.
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
package org.l2jmobius.gameserver.instancemanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import org.l2jmobius.commons.database.DatabaseFactory;
import org.l2jmobius.gameserver.enums.RevengeType;
import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.World;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.actor.Summon;
import org.l2jmobius.gameserver.model.clan.ClanMember;
import org.l2jmobius.gameserver.model.holders.RevengeHistoryHolder;
import org.l2jmobius.gameserver.model.holders.SkillHolder;
import org.l2jmobius.gameserver.model.itemcontainer.Inventory;
import org.l2jmobius.gameserver.model.zone.ZoneId;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.serverpackets.revenge.ExPvpBookShareRevengeKillerLocation;
import org.l2jmobius.gameserver.network.serverpackets.revenge.ExPvpBookShareRevengeList;
import org.l2jmobius.gameserver.network.serverpackets.revenge.ExPvpBookShareRevengeNewRevengeInfo;

/**
 * @author Mobius
 */
public class RevengeHistoryManager
{
	private static final Logger LOGGER = Logger.getLogger(RevengeHistoryManager.class.getName());
	
	private static final Map<Integer, List<RevengeHistoryHolder>> REVENGE_HISTORY = new ConcurrentHashMap<>();
	private static final String DELETE_REVENGE_HISTORY = "TRUNCATE TABLE character_revenge_history";
	private static final String INSERT_REVENGE_HISTORY = "INSERT INTO character_revenge_history (charId, type, killer_name, killer_clan, killer_level, killer_race, killer_class, victim_name, victim_clan, victim_level, victim_race, victim_class, shared, show_location_remaining, teleport_remaining, shared_teleport_remaining, kill_time, share_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final SkillHolder HIDE_SKILL = new SkillHolder(922, 1);
	private static final long REVENGE_DURATION = 21600000; // Six hours.
	private static final int[] LOCATION_PRICE =
	{
		0,
		50000,
		100000,
		100000,
		200000
	};
	private static final int[] TELEPORT_PRICE =
	{
		10,
		50,
		100,
		100,
		200
	};
	
	protected RevengeHistoryManager()
	{
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM character_revenge_history"))
		{
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				final int charId = rs.getInt("charId");
				final List<RevengeHistoryHolder> history = REVENGE_HISTORY.containsKey(charId) ? REVENGE_HISTORY.get(charId) : new CopyOnWriteArrayList<>();
				final StatSet killer = new StatSet();
				killer.set("name", rs.getString("killer_name"));
				killer.set("clan", rs.getString("killer_clan"));
				killer.set("level", rs.getInt("killer_level"));
				killer.set("race", rs.getInt("killer_race"));
				killer.set("class", rs.getInt("killer_class"));
				final StatSet victim = new StatSet();
				victim.set("name", rs.getString("victim_name"));
				victim.set("clan", rs.getString("victim_clan"));
				victim.set("level", rs.getInt("victim_level"));
				victim.set("race", rs.getInt("victim_race"));
				victim.set("class", rs.getInt("victim_class"));
				history.add(new RevengeHistoryHolder(killer, victim, RevengeType.values()[rs.getInt("type")], rs.getBoolean("shared"), rs.getInt("show_location_remaining"), rs.getInt("teleport_remaining"), rs.getInt("shared_teleport_remaining"), rs.getLong("kill_time"), rs.getLong("share_time")));
				REVENGE_HISTORY.put(charId, history);
			}
		}
		catch (Exception e)
		{
			LOGGER.warning("Failed loading revenge history! " + e);
		}
	}
	
	public void storeMe()
	{
		for (Entry<Integer, List<RevengeHistoryHolder>> entry : REVENGE_HISTORY.entrySet())
		{
			final List<RevengeHistoryHolder> history = entry.getValue();
			if (history != null)
			{
				final long currentTime = System.currentTimeMillis();
				final List<RevengeHistoryHolder> removals = new ArrayList<>();
				for (RevengeHistoryHolder holder : history)
				{
					if (((holder.getKillTime() != 0) && ((holder.getKillTime() + REVENGE_DURATION) < currentTime)) || //
						((holder.getShareTime() != 0) && ((holder.getShareTime() + REVENGE_DURATION) < currentTime)))
					{
						removals.add(holder);
					}
				}
				for (RevengeHistoryHolder holder : removals)
				{
					history.remove(holder);
				}
			}
		}
		
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement ps1 = con.prepareStatement(DELETE_REVENGE_HISTORY);
			PreparedStatement ps2 = con.prepareStatement(INSERT_REVENGE_HISTORY))
		{
			ps1.execute();
			
			for (Entry<Integer, List<RevengeHistoryHolder>> entry : REVENGE_HISTORY.entrySet())
			{
				final List<RevengeHistoryHolder> history = entry.getValue();
				if ((history == null) || history.isEmpty())
				{
					continue;
				}
				
				for (RevengeHistoryHolder holder : history)
				{
					ps2.clearParameters();
					ps2.setInt(1, entry.getKey());
					ps2.setInt(2, holder.getType().ordinal());
					ps2.setString(3, holder.getKillerName());
					ps2.setString(4, holder.getKillerClanName());
					ps2.setInt(5, holder.getKillerLevel());
					ps2.setInt(6, holder.getKillerRaceId());
					ps2.setInt(7, holder.getKillerClassId());
					ps2.setString(8, holder.getVictimName());
					ps2.setString(9, holder.getVictimClanName());
					ps2.setInt(10, holder.getVictimLevel());
					ps2.setInt(11, holder.getVictimRaceId());
					ps2.setInt(12, holder.getVictimClassId());
					ps2.setBoolean(13, holder.wasShared());
					ps2.setInt(14, holder.getShowLocationRemaining());
					ps2.setInt(15, holder.getTeleportRemaining());
					ps2.setInt(16, holder.getSharedTeleportRemaining());
					ps2.setLong(17, holder.getKillTime());
					ps2.setLong(18, holder.getShareTime());
					ps2.addBatch();
				}
			}
			ps2.executeBatch();
		}
		catch (Exception e)
		{
			LOGGER.warning(getClass().getSimpleName() + " Error while saving revenge history. " + e);
		}
	}
	
	public void addNewKill(Player victim, Player killer)
	{
		try
		{
			boolean found = false;
			final int victimObjectId = victim.getObjectId();
			final long currentTime = System.currentTimeMillis();
			final List<RevengeHistoryHolder> removals = new ArrayList<>();
			final List<RevengeHistoryHolder> history = REVENGE_HISTORY.containsKey(victimObjectId) ? REVENGE_HISTORY.get(victimObjectId) : new CopyOnWriteArrayList<>();
			for (RevengeHistoryHolder holder : history)
			{
				if (((holder.getKillTime() != 0) && ((holder.getKillTime() + REVENGE_DURATION) < currentTime)) || //
					((holder.getShareTime() != 0) && ((holder.getShareTime() + REVENGE_DURATION) < currentTime)))
				{
					removals.add(holder);
				}
				else if (holder.getKillerName().equals(killer.getName()))
				{
					found = true;
				}
			}
			
			history.removeAll(removals);
			
			if (!found)
			{
				history.add(new RevengeHistoryHolder(killer, victim, RevengeType.REVENGE));
				REVENGE_HISTORY.put(victimObjectId, history);
				victim.sendPacket(new ExPvpBookShareRevengeNewRevengeInfo(victim.getName(), killer.getName(), RevengeType.REVENGE));
				victim.sendPacket(new ExPvpBookShareRevengeList(victim));
			}
		}
		catch (Exception e)
		{
			LOGGER.warning(getClass().getSimpleName() + ": Failed adding revenge history! " + e);
		}
	}
	
	public void locateKiller(Player player, String killerName)
	{
		final List<RevengeHistoryHolder> history = REVENGE_HISTORY.get(player.getObjectId());
		if (history == null)
		{
			return;
		}
		
		RevengeHistoryHolder revenge = null;
		for (RevengeHistoryHolder holder : history)
		{
			if (holder.getKillerName().equals(killerName))
			{
				revenge = holder;
				break;
			}
		}
		
		if (revenge == null)
		{
			return;
		}
		
		final Player killer = World.getInstance().getPlayer(killerName);
		if ((killer == null) || !killer.isOnline())
		{
			player.sendPacket(SystemMessageId.THE_ENEMY_IS_OFFLINE_AND_CANNOT_BE_FOUND_RIGHT_NOW);
			return;
		}
		
		if (killer.isInsideZone(ZoneId.PEACE) || killer.isInInstance() || killer.isInTimedHuntingZone() || killer.isInsideZone(ZoneId.SIEGE) //
			|| player.isDead() || player.isInInstance() || player.isInTimedHuntingZone() || player.isInsideZone(ZoneId.SIEGE))
		{
			player.sendPacket(SystemMessageId.THE_CHARACTER_IS_IN_A_LOCATION_WHERE_IT_IS_IMPOSSIBLE_TO_USE_THIS_FUNCTION);
			return;
		}
		
		if (revenge.getShowLocationRemaining() > 0)
		{
			final int price = LOCATION_PRICE[Math.min(LOCATION_PRICE.length - revenge.getShowLocationRemaining(), LOCATION_PRICE.length - 1)];
			if (player.reduceAdena("Revenge find location", price, player, true))
			{
				revenge.setShowLocationRemaining(revenge.getShowLocationRemaining() - 1);
				player.sendPacket(new ExPvpBookShareRevengeKillerLocation(killer));
				player.sendPacket(new ExPvpBookShareRevengeList(player));
			}
		}
	}
	
	private boolean checkTeleportConditions(Player player, Player killer)
	{
		if ((killer == null) || !killer.isOnline())
		{
			player.sendPacket(SystemMessageId.THE_ENEMY_IS_OFFLINE_AND_CANNOT_BE_FOUND_RIGHT_NOW);
			return false;
		}
		if (killer.isTeleporting() || killer.isInsideZone(ZoneId.PEACE) || killer.isInInstance() || killer.isInTimedHuntingZone() || killer.isInsideZone(ZoneId.SIEGE) || killer.isInsideZone(ZoneId.NO_BOOKMARK))
		{
			player.sendPacket(SystemMessageId.THE_CHARACTER_IS_IN_A_LOCATION_WHERE_IT_IS_IMPOSSIBLE_TO_USE_THIS_FUNCTION);
			return false;
		}
		if (killer.isDead())
		{
			player.sendPacket(SystemMessageId.THE_CHARACTER_IS_IN_A_LOCATION_WHERE_IT_IS_IMPOSSIBLE_TO_USE_THIS_FUNCTION);
			return false;
		}
		
		if (player.isInInstance() || player.isInTimedHuntingZone() || player.isInsideZone(ZoneId.SIEGE))
		{
			player.sendPacket(SystemMessageId.THE_CHARACTER_IS_IN_A_LOCATION_WHERE_IT_IS_IMPOSSIBLE_TO_USE_THIS_FUNCTION);
			return false;
		}
		if (player.isDead())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_USE_TELEPORT_WHILE_YOU_ARE_DEAD);
			return false;
		}
		if (player.isInCombat() || player.isDisabled())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_TELEPORT_WHILE_IN_COMBAT);
			return false;
		}
		
		return true;
	}
	
	public void teleportToKiller(Player player, String killerName)
	{
		final List<RevengeHistoryHolder> history = REVENGE_HISTORY.get(player.getObjectId());
		if (history == null)
		{
			return;
		}
		
		RevengeHistoryHolder revenge = null;
		for (RevengeHistoryHolder holder : history)
		{
			if (holder.getKillerName().equals(killerName))
			{
				revenge = holder;
				break;
			}
		}
		
		if (revenge == null)
		{
			return;
		}
		
		if (revenge.wasShared())
		{
			return;
		}
		
		final Player killer = World.getInstance().getPlayer(killerName);
		if (!checkTeleportConditions(player, killer))
		{
			return;
		}
		
		if (revenge.getTeleportRemaining() > 0)
		{
			final int price = TELEPORT_PRICE[Math.min(TELEPORT_PRICE.length - revenge.getTeleportRemaining(), TELEPORT_PRICE.length - 1)];
			if (player.destroyItemByItemId("Revenge Teleport", Inventory.LCOIN_ID, price, player, true))
			{
				revenge.setTeleportRemaining(revenge.getTeleportRemaining() - 1);
				HIDE_SKILL.getSkill().applyEffects(player, player);
				for (Summon summon : player.getServitorsAndPets())
				{
					HIDE_SKILL.getSkill().applyEffects(summon, summon);
				}
				player.teleToLocation(killer.getLocation());
			}
		}
	}
	
	public void teleportToSharedKiller(Player player, String victimName, String killerName)
	{
		if (player.getName().equals(killerName))
		{
			return;
		}
		
		final List<RevengeHistoryHolder> history = REVENGE_HISTORY.get(player.getObjectId());
		if (history == null)
		{
			return;
		}
		
		RevengeHistoryHolder revenge = null;
		for (RevengeHistoryHolder holder : history)
		{
			if (holder.getVictimName().equals(victimName) && holder.getKillerName().equals(killerName))
			{
				revenge = holder;
				break;
			}
		}
		
		if (revenge == null)
		{
			return;
		}
		
		if (!revenge.wasShared())
		{
			return;
		}
		
		final Player killer = World.getInstance().getPlayer(killerName);
		if (!checkTeleportConditions(player, killer))
		{
			return;
		}
		
		if ((revenge.getSharedTeleportRemaining() > 0) && player.destroyItemByItemId("Revenge Teleport", Inventory.LCOIN_ID, 100, player, true))
		{
			revenge.setSharedTeleportRemaining(revenge.getSharedTeleportRemaining() - 1);
			HIDE_SKILL.getSkill().applyEffects(player, player);
			for (Summon summon : player.getServitorsAndPets())
			{
				HIDE_SKILL.getSkill().applyEffects(summon, summon);
			}
			player.teleToLocation(killer.getLocation());
		}
	}
	
	public void requestHelp(Player player, Player killer, int type)
	{
		final List<RevengeHistoryHolder> history = REVENGE_HISTORY.get(player.getObjectId());
		if (history == null)
		{
			return;
		}
		
		RevengeHistoryHolder revenge = null;
		for (RevengeHistoryHolder holder : history)
		{
			if (holder.getKillerName().equals(killer.getName()))
			{
				revenge = holder;
				break;
			}
		}
		
		if (revenge == null)
		{
			return;
		}
		
		if (revenge.wasShared())
		{
			return;
		}
		
		if (player.reduceAdena("Revenge request help", 100000, player, true))
		{
			final long currentTime = System.currentTimeMillis();
			revenge.setShared(true);
			revenge.setType(RevengeType.OWN_HELP_REQUEST);
			revenge.setShareTime(currentTime);
			
			final List<Player> targets = new LinkedList<>();
			if (type == 1)
			{
				if (player.getClan() != null)
				{
					for (ClanMember member : player.getClan().getMembers())
					{
						if (member.isOnline())
						{
							targets.add(member.getPlayer());
						}
						else
						{
							saveToRevengeHistory(player, killer, revenge, currentTime, member.getObjectId());
						}
					}
				}
			}
			else if (type == 2)
			{
				for (Integer playerObjectId : RankManager.getInstance().getTop50())
				{
					final Player plr = World.getInstance().getPlayer(playerObjectId);
					if (plr != null)
					{
						targets.add(plr);
					}
					else
					{
						saveToRevengeHistory(player, killer, revenge, currentTime, playerObjectId);
					}
				}
			}
			
			for (Player target : targets)
			{
				if (target == killer)
				{
					continue;
				}
				
				final int targetObjectId = target.getObjectId();
				saveToRevengeHistory(player, killer, revenge, currentTime, targetObjectId);
				
				target.sendPacket(new ExPvpBookShareRevengeNewRevengeInfo(player.getName(), killer.getName(), RevengeType.HELP_REQUEST));
				target.sendPacket(new ExPvpBookShareRevengeList(target));
			}
		}
		player.sendPacket(new ExPvpBookShareRevengeList(player));
	}
	
	private void saveToRevengeHistory(Player player, Player killer, RevengeHistoryHolder revenge, long currentTime, int objectId)
	{
		final List<RevengeHistoryHolder> targetHistory = REVENGE_HISTORY.containsKey(objectId) ? REVENGE_HISTORY.get(objectId) : new CopyOnWriteArrayList<>();
		for (RevengeHistoryHolder holder : targetHistory)
		{
			if (holder.getVictimName().equals(player.getName()) && holder.getKillerName().equals(killer.getName()) && (holder != revenge))
			{
				targetHistory.remove(holder);
				break;
			}
		}
		
		targetHistory.add(new RevengeHistoryHolder(killer, player, RevengeType.HELP_REQUEST, 1, revenge.getKillTime(), currentTime));
		REVENGE_HISTORY.put(objectId, targetHistory);
	}
	
	public Collection<RevengeHistoryHolder> getHistory(Player player)
	{
		return REVENGE_HISTORY.get(player.getObjectId());
	}
	
	public static RevengeHistoryManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final RevengeHistoryManager INSTANCE = new RevengeHistoryManager();
	}
}
