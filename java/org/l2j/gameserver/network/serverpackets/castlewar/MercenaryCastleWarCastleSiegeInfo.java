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
import org.l2j.gameserver.instancemanager.CastleManager;
import org.l2j.gameserver.model.siege.Castle;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Mobius
 */
public class MercenaryCastleWarCastleSiegeInfo extends ServerPacket
{
	private final int _castleId;
	
	public MercenaryCastleWarCastleSiegeInfo(int castleId)
	{
		_castleId = castleId;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_MERCENARY_CASTLEWAR_CASTLE_SIEGE_INFO.writeId(this, buffer);
		
		buffer.writeInt(_castleId);
		
		final Castle castle = CastleManager.getInstance().getCastleById(_castleId);
		if (castle == null)
		{
			buffer.writeInt(0);
			buffer.writeInt(0);
			buffer.writeSizedString("-");
			buffer.writeSizedString("-");
			buffer.writeInt(0);
			buffer.writeInt(0);
			buffer.writeInt(0);
		}
		else
		{
			buffer.writeInt(0); // seconds?
			buffer.writeInt(0); // crest?
			
			buffer.writeSizedString(castle.getOwner() != null ? castle.getOwner().getName() : "-");
			buffer.writeSizedString(castle.getOwner() != null ? castle.getOwner().getLeaderName() : "-");
			
			buffer.writeInt(0); // crest?
			buffer.writeInt(castle.getSiege().getAttackerClans().size());
			buffer.writeInt(castle.getSiege().getDefenderClans().size() + castle.getSiege().getDefenderWaitingClans().size());
		}
	}
}
