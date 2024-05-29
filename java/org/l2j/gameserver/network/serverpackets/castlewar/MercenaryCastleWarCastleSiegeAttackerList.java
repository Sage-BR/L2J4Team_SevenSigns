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
package org.l2j.gameserver.network.serverpackets.castlewar;

import java.util.Collection;

import org.l2j.gameserver.data.sql.ClanTable;
import org.l2j.gameserver.instancemanager.CastleManager;
import org.l2j.gameserver.model.SiegeClan;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.model.siege.Castle;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Mobius
 */
public class MercenaryCastleWarCastleSiegeAttackerList extends ServerPacket
{
	private final int _castleId;
	
	public MercenaryCastleWarCastleSiegeAttackerList(int castleId)
	{
		_castleId = castleId;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_MERCENARY_CASTLEWAR_CASTLE_SIEGE_ATTACKER_LIST.writeId(this);
		
		writeInt(_castleId);
		writeInt(0);
		writeInt(1);
		writeInt(0);
		
		final Castle castle = CastleManager.getInstance().getCastleById(_castleId);
		if (castle == null)
		{
			writeInt(0);
			writeInt(0);
		}
		else
		{
			final Collection<SiegeClan> attackers = castle.getSiege().getAttackerClans();
			writeInt(attackers.size());
			writeInt(attackers.size());
			for (SiegeClan siegeClan : attackers)
			{
				final Clan clan = ClanTable.getInstance().getClan(siegeClan.getClanId());
				if (clan == null)
				{
					continue;
				}
				
				writeInt(clan.getId());
				writeString(clan.getName());
				writeString(clan.getLeaderName());
				writeInt(clan.getCrestId());
				writeInt(0); // time
				
				writeInt(0); // 286
				writeInt(0); // 286
				writeInt(0); // 286
				writeInt(0); // 286
				
				writeInt(clan.getAllyId());
				writeString(clan.getAllyName());
				writeString(""); // Ally Leader name
				writeInt(clan.getAllyCrestId());
			}
		}
	}
}
