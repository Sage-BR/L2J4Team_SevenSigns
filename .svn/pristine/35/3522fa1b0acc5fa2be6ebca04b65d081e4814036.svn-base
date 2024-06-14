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
package org.l2jmobius.gameserver.model.holders;

import org.l2jmobius.gameserver.enums.RevengeType;
import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.actor.Player;

/**
 * @author Mobius
 */
public class RevengeHistoryHolder
{
	private final String _killerName;
	private final String _killerClanName;
	private final int _killerLevel;
	private final int _killerRaceId;
	private final int _killerClassId;
	private final long _killTime;
	private final String _victimName;
	private final String _victimClanName;
	private final int _victimLevel;
	private final int _victimRaceId;
	private final int _victimClassId;
	private RevengeType _type;
	private boolean _wasShared;
	private long _shareTime;
	private int _showLocationRemaining;
	private int _teleportRemaining;
	private int _sharedTeleportRemaining;
	
	public RevengeHistoryHolder(Player killer, Player victim, RevengeType type)
	{
		_type = type;
		_wasShared = false;
		_killerName = killer.getName();
		_killerClanName = killer.getClan() == null ? "" : killer.getClan().getName();
		_killerLevel = killer.getLevel();
		_killerRaceId = killer.getRace().ordinal();
		_killerClassId = killer.getClassId().getId();
		_killTime = System.currentTimeMillis();
		_shareTime = 0;
		_showLocationRemaining = 5;
		_teleportRemaining = 5;
		_sharedTeleportRemaining = 1;
		_victimName = victim.getName();
		_victimClanName = victim.getClan() == null ? "" : victim.getClan().getName();
		_victimLevel = victim.getLevel();
		_victimRaceId = victim.getRace().ordinal();
		_victimClassId = victim.getClassId().getId();
	}
	
	public RevengeHistoryHolder(Player killer, Player victim, RevengeType type, int sharedTeleportRemaining, long killTime, long shareTime)
	{
		_type = type;
		_wasShared = true;
		_killerName = killer.getName();
		_killerClanName = killer.getClan() == null ? "" : killer.getClan().getName();
		_killerLevel = killer.getLevel();
		_killerRaceId = killer.getRace().ordinal();
		_killerClassId = killer.getClassId().getId();
		_killTime = killTime;
		_shareTime = shareTime;
		_showLocationRemaining = 0;
		_teleportRemaining = 0;
		_sharedTeleportRemaining = sharedTeleportRemaining;
		_victimName = victim.getName();
		_victimClanName = victim.getClan() == null ? "" : victim.getClan().getName();
		_victimLevel = victim.getLevel();
		_victimRaceId = victim.getRace().ordinal();
		_victimClassId = victim.getClassId().getId();
	}
	
	public RevengeHistoryHolder(StatSet killer, StatSet victim, RevengeType type, boolean wasShared, int showLocationRemaining, int teleportRemaining, int sharedTeleportRemaining, long killTime, long shareTime)
	{
		_type = type;
		_wasShared = wasShared;
		_killerName = killer.getString("name");
		_killerClanName = killer.getString("clan");
		_killerLevel = killer.getInt("level");
		_killerRaceId = killer.getInt("race");
		_killerClassId = killer.getInt("class");
		_killTime = killTime;
		_shareTime = shareTime;
		_showLocationRemaining = showLocationRemaining;
		_teleportRemaining = teleportRemaining;
		_sharedTeleportRemaining = sharedTeleportRemaining;
		_victimName = victim.getString("name");
		_victimClanName = victim.getString("clan");
		_victimLevel = victim.getInt("level");
		_victimRaceId = victim.getInt("race");
		_victimClassId = victim.getInt("class");
	}
	
	public RevengeType getType()
	{
		return _type;
	}
	
	public void setType(RevengeType type)
	{
		_type = type;
	}
	
	public boolean wasShared()
	{
		return _wasShared;
	}
	
	public void setShared(boolean wasShared)
	{
		_wasShared = wasShared;
	}
	
	public String getKillerName()
	{
		return _killerName;
	}
	
	public String getKillerClanName()
	{
		return _killerClanName;
	}
	
	public int getKillerLevel()
	{
		return _killerLevel;
	}
	
	public int getKillerRaceId()
	{
		return _killerRaceId;
	}
	
	public int getKillerClassId()
	{
		return _killerClassId;
	}
	
	public long getKillTime()
	{
		return _killTime;
	}
	
	public long getShareTime()
	{
		return _shareTime;
	}
	
	public void setShareTime(long shareTime)
	{
		_shareTime = shareTime;
	}
	
	public int getShowLocationRemaining()
	{
		return _showLocationRemaining;
	}
	
	public void setShowLocationRemaining(int count)
	{
		_showLocationRemaining = count;
	}
	
	public int getTeleportRemaining()
	{
		return _teleportRemaining;
	}
	
	public void setTeleportRemaining(int count)
	{
		_teleportRemaining = count;
	}
	
	public int getSharedTeleportRemaining()
	{
		return _sharedTeleportRemaining;
	}
	
	public void setSharedTeleportRemaining(int count)
	{
		_sharedTeleportRemaining = count;
	}
	
	public String getVictimName()
	{
		return _victimName;
	}
	
	public String getVictimClanName()
	{
		return _victimClanName;
	}
	
	public int getVictimLevel()
	{
		return _victimLevel;
	}
	
	public int getVictimRaceId()
	{
		return _victimRaceId;
	}
	
	public int getVictimClassId()
	{
		return _victimClassId;
	}
}
