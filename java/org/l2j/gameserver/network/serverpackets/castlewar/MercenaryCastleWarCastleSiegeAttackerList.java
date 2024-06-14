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
package org.l2j.gameserver.network.serverpackets.castlewar;

import java.util.Collection;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.data.sql.ClanTable;
import org.l2j.gameserver.instancemanager.CastleManager;
import org.l2j.gameserver.model.SiegeClan;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.model.siege.Castle;
import org.l2j.gameserver.network.GameClient;
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
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_MERCENARY_CASTLEWAR_CASTLE_SIEGE_ATTACKER_LIST.writeId(this, buffer);
		
		buffer.writeInt(_castleId);
		buffer.writeInt(0);
		buffer.writeInt(1);
		buffer.writeInt(0);
		
		final Castle castle = CastleManager.getInstance().getCastleById(_castleId);
		if (castle == null)
		{
			buffer.writeInt(0);
			buffer.writeInt(0);
		}
		else
		{
			final Collection<SiegeClan> attackers = castle.getSiege().getAttackerClans();
			buffer.writeInt(attackers.size());
			buffer.writeInt(attackers.size());
			for (SiegeClan siegeClan : attackers)
			{
				final Clan clan = ClanTable.getInstance().getClan(siegeClan.getClanId());
				if (clan == null)
				{
					continue;
				}
				
				buffer.writeInt(clan.getId());
				buffer.writeString(clan.getName());
				buffer.writeString(clan.getLeaderName());
				buffer.writeInt(clan.getCrestId());
				buffer.writeInt(0); // time
				
				buffer.writeInt(clan.isRecruitMercenary());
				buffer.writeLong(clan.getRewardMercenary());
				buffer.writeInt(clan.getMapMercenary().size());
				buffer.writeLong(0);
				buffer.writeLong(0);
				if (clan.getAllyId() != 0)
				{
					buffer.writeInt(clan.getAllyId());
					buffer.writeString(clan.getAllyName());
					buffer.writeString("");
					buffer.writeInt(clan.getAllyCrestId());
				}
				else
				{
					buffer.writeInt(0);
					buffer.writeString("");
					buffer.writeString("");
					buffer.writeInt(0);
				}
			}
		}
	}
}
