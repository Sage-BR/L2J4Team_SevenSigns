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
package org.l2jmobius.gameserver.network.serverpackets.revenge;

import java.util.Collection;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.instancemanager.RevengeHistoryManager;
import org.l2jmobius.gameserver.model.World;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.RevengeHistoryHolder;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

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
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_PVPBOOK_SHARE_REVENGE_LIST.writeId(this, buffer);
		if (_history == null)
		{
			buffer.writeByte(1); // CurrentPage
			buffer.writeByte(1); // MaxPage
			buffer.writeInt(0);
		}
		else
		{
			buffer.writeByte(1); // CurrentPage
			buffer.writeByte(1); // MaxPage
			buffer.writeInt(_history.size());
			for (RevengeHistoryHolder holder : _history)
			{
				buffer.writeInt(holder.getType().ordinal()); // ShareType (2 - help request, 1 - revenge, 0 - both)
				buffer.writeInt((int) (holder.getKillTime() / 1000)); // KilledTime
				buffer.writeInt(holder.getShowLocationRemaining()); // ShowKillerCount
				buffer.writeInt(holder.getTeleportRemaining()); // TeleportKillerCount
				buffer.writeInt(holder.getSharedTeleportRemaining()); // SharedTeleportKillerCount
				buffer.writeInt(0); // KilledUserDBID
				buffer.writeSizedString(holder.getVictimName()); // KilledUserName
				buffer.writeSizedString(holder.getVictimClanName()); // KilledUserPledgeName
				buffer.writeInt(holder.getVictimLevel()); // KilledUserLevel
				buffer.writeInt(holder.getVictimRaceId()); // KilledUserRace
				buffer.writeInt(holder.getVictimClassId()); // KilledUserClass
				buffer.writeInt(0); // KillUserDBID
				buffer.writeSizedString(holder.getKillerName()); // KillUserName
				buffer.writeSizedString(holder.getKillerClanName()); // KillUserPledgeName
				buffer.writeInt(holder.getKillerLevel()); // KillUserLevel
				buffer.writeInt(holder.getKillerRaceId()); // KillUserRace
				buffer.writeInt(holder.getKillerClassId()); // KillUserClass
				Player killer = World.getInstance().getPlayer(holder.getKillerName());
				buffer.writeInt((killer != null) && killer.isOnline() ? 2 : 0); // KillUserOnline (2 - online, 0 - offline)
				buffer.writeInt(0); // KillUserKarma
				buffer.writeInt((int) (holder.getShareTime() / 1000)); // nSharedTime
			}
		}
	}
}
