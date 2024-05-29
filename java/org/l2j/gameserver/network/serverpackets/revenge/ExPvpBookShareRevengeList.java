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
package org.l2j.gameserver.network.serverpackets.revenge;

import java.util.Collection;

import org.l2j.gameserver.instancemanager.RevengeHistoryManager;
import org.l2j.gameserver.model.World;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.RevengeHistoryHolder;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Mobius
 */
public class ExPvpBookShareRevengeList extends ServerPacket
{
	private final Collection<RevengeHistoryHolder> _history;
	
	public ExPvpBookShareRevengeList(Player player)
	{
		_history = RevengeHistoryManager.getInstance().getHistory(player);
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_PVPBOOK_SHARE_REVENGE_LIST.writeId(this);
		if (_history == null)
		{
			writeByte(1); // CurrentPage
			writeByte(1); // MaxPage
			writeInt(0);
		}
		else
		{
			writeByte(1); // CurrentPage
			writeByte(1); // MaxPage
			writeInt(_history.size());
			for (RevengeHistoryHolder holder : _history)
			{
				writeInt(holder.getType().ordinal()); // ShareType (2 - help request, 1 - revenge, 0 - both)
				writeInt((int) (holder.getKillTime() / 1000)); // KilledTime
				writeInt(holder.getShowLocationRemaining()); // ShowKillerCount
				writeInt(holder.getTeleportRemaining()); // TeleportKillerCount
				writeInt(holder.getSharedTeleportRemaining()); // SharedTeleportKillerCount
				writeInt(0); // KilledUserDBID
				writeSizedString(holder.getVictimName()); // KilledUserName
				writeSizedString(holder.getVictimClanName()); // KilledUserPledgeName
				writeInt(holder.getVictimLevel()); // KilledUserLevel
				writeInt(holder.getVictimRaceId()); // KilledUserRace
				writeInt(holder.getVictimClassId()); // KilledUserClass
				writeInt(0); // KillUserDBID
				writeSizedString(holder.getKillerName()); // KillUserName
				writeSizedString(holder.getKillerClanName()); // KillUserPledgeName
				writeInt(holder.getKillerLevel()); // KillUserLevel
				writeInt(holder.getKillerRaceId()); // KillUserRace
				writeInt(holder.getKillerClassId()); // KillUserClass
				Player killer = World.getInstance().getPlayer(holder.getKillerName());
				writeInt((killer != null) && killer.isOnline() ? 2 : 0); // KillUserOnline (2 - online, 0 - offline)
				writeInt(0); // KillUserKarma
				writeInt((int) (holder.getShareTime() / 1000)); // nSharedTime
			}
		}
	}
}
