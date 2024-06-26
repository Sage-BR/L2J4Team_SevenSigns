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

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.data.sql.ClanTable;
import org.l2j.gameserver.enums.SiegeClanType;
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
public class MercenaryCastleWarCastleSiegeDefenderList extends ServerPacket
{
	private final int _castleId;
	
	public MercenaryCastleWarCastleSiegeDefenderList(int castleId)
	{
		_castleId = castleId;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_MERCENARY_CASTLEWAR_CASTLE_SIEGE_DEFENDER_LIST.writeId(this, buffer);
		
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
			final int size = castle.getSiege().getDefenderWaitingClans().size() + castle.getSiege().getDefenderClans().size() + (castle.getOwner() != null ? 1 : 0);
			buffer.writeInt(size);
			buffer.writeInt(size);
			
			// Owners.
			final Clan owner = castle.getOwner();
			if (owner != null)
			{
				buffer.writeInt(owner.getId());
				buffer.writeString(owner.getName());
				buffer.writeString(owner.getLeaderName());
				buffer.writeInt(owner.getCrestId());
				buffer.writeInt(0); // time (seconds)
				buffer.writeInt(SiegeClanType.OWNER.ordinal());
				
				buffer.writeInt(owner.isRecruitMercenary());
				buffer.writeLong(owner.getRewardMercenary());
				buffer.writeInt(owner.getMapMercenary().size());
				buffer.writeLong(0);
				buffer.writeLong(0);
				if (owner.getAllyId() != 0)
				{
					buffer.writeInt(owner.getAllyId());
					buffer.writeString(owner.getAllyName());
					buffer.writeString("");
					buffer.writeInt(owner.getAllyCrestId());
				}
				else
				{
					buffer.writeInt(0);
					buffer.writeString("");
					buffer.writeString("");
					buffer.writeInt(0);
				}
			}
			
			// Defenders.
			for (SiegeClan clan : castle.getSiege().getDefenderClans())
			{
				final Clan defender = ClanTable.getInstance().getClan(clan.getClanId());
				if ((defender == null) || (defender == castle.getOwner()))
				{
					continue;
				}
				
				buffer.writeInt(defender.getId());
				buffer.writeString(defender.getName());
				buffer.writeString(defender.getLeaderName());
				buffer.writeInt(defender.getCrestId());
				buffer.writeInt(0); // time (seconds)
				buffer.writeInt(SiegeClanType.DEFENDER.ordinal());
				
				buffer.writeInt(defender.isRecruitMercenary());
				buffer.writeLong(defender.getRewardMercenary());
				buffer.writeInt(defender.getMapMercenary().size());
				buffer.writeLong(0);
				buffer.writeLong(0);
				if (defender.getAllyId() != 0)
				{
					buffer.writeInt(defender.getAllyId());
					buffer.writeString(defender.getAllyName());
					buffer.writeString("");
					buffer.writeInt(defender.getAllyCrestId());
				}
				else
				{
					buffer.writeInt(0);
					buffer.writeString("");
					buffer.writeString("");
					buffer.writeInt(0);
				}
			}
			
			// Defenders waiting.
			for (SiegeClan clan : castle.getSiege().getDefenderWaitingClans())
			{
				final Clan defender = ClanTable.getInstance().getClan(clan.getClanId());
				if (defender == null)
				{
					continue;
				}
				
				buffer.writeInt(defender.getId());
				buffer.writeString(defender.getName());
				buffer.writeString(defender.getLeaderName());
				buffer.writeInt(defender.getCrestId());
				buffer.writeInt(0); // time (seconds)
				buffer.writeInt(SiegeClanType.DEFENDER_PENDING.ordinal());
				
				buffer.writeInt(defender.isRecruitMercenary());
				buffer.writeLong(defender.getRewardMercenary());
				buffer.writeInt(defender.getMapMercenary().size());
				buffer.writeLong(0);
				buffer.writeLong(0);
				if (defender.getAllyId() != 0)
				{
					buffer.writeInt(defender.getAllyId());
					buffer.writeString(defender.getAllyName());
					buffer.writeString("");
					buffer.writeInt(defender.getAllyCrestId());
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
