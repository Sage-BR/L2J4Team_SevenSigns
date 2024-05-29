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

import org.l2j.gameserver.data.sql.ClanTable;
import org.l2j.gameserver.enums.SiegeClanType;
import org.l2j.gameserver.instancemanager.CastleManager;
import org.l2j.gameserver.model.SiegeClan;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.model.siege.Castle;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Mobius
 */
public class MercenaryCastleWarCastleSiegeDefenderList extends ServerPacket
{
	private final int _castleId;
	
	public MercenaryCastleWarCastleSiegeDefenderList(int castleId)
	{
		_castleId = castleId;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_MERCENARY_CASTLEWAR_CASTLE_SIEGE_DEFENDER_LIST.writeId(this);
		
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
			final int size = castle.getSiege().getDefenderWaitingClans().size() + castle.getSiege().getDefenderClans().size() + (castle.getOwner() != null ? 1 : 0);
			writeInt(size);
			writeInt(size);
			
			// Owners.
			final Clan owner = castle.getOwner();
			if (owner != null)
			{
				writeInt(owner.getId());
				writeString(owner.getName());
				writeString(owner.getLeaderName());
				writeInt(owner.getAllyCrestId());
				writeInt(0); // time (seconds)
				writeInt(SiegeClanType.OWNER.ordinal());
				
				writeInt(0); // 286
				writeInt(0); // 286
				writeInt(0); // 286
				writeInt(0); // 286
				
				writeInt(owner.getAllyId());
				writeString(owner.getAllyName());
				writeString(""); // Ally Leader Name
				writeInt(owner.getAllyCrestId());
			}
			
			// Defenders.
			for (SiegeClan clan : castle.getSiege().getDefenderClans())
			{
				final Clan defender = ClanTable.getInstance().getClan(clan.getClanId());
				if ((defender == null) || (defender == castle.getOwner()))
				{
					continue;
				}
				
				writeInt(defender.getId());
				writeString(defender.getName());
				writeString(defender.getLeaderName());
				writeInt(defender.getCrestId());
				writeInt(0); // time (seconds)
				writeInt(SiegeClanType.DEFENDER.ordinal());
				
				writeInt(0); // 286
				writeInt(0); // 286
				writeInt(0); // 286
				writeInt(0); // 286
				
				writeInt(defender.getAllyId());
				writeString(defender.getAllyName());
				writeString(""); // AllyLeaderName
				writeInt(defender.getAllyCrestId());
			}
			
			// Defenders waiting.
			for (SiegeClan clan : castle.getSiege().getDefenderWaitingClans())
			{
				final Clan defender = ClanTable.getInstance().getClan(clan.getClanId());
				if (defender == null)
				{
					continue;
				}
				
				writeInt(defender.getId());
				writeString(defender.getName());
				writeString(defender.getLeaderName());
				writeInt(defender.getCrestId());
				writeInt(0); // time (seconds)
				writeInt(SiegeClanType.DEFENDER_PENDING.ordinal());
				
				writeInt(0); // 286
				writeInt(0); // 286
				writeInt(0); // 286
				writeInt(0); // 286
				
				writeInt(defender.getAllyId());
				writeString(defender.getAllyName());
				writeString(""); // AllyLeaderName
				writeInt(defender.getAllyCrestId());
			}
		}
	}
}
