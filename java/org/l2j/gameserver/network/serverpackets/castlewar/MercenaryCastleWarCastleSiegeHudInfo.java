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

import org.l2j.gameserver.instancemanager.CastleManager;
import org.l2j.gameserver.instancemanager.SiegeManager;
import org.l2j.gameserver.model.siege.Castle;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Mobius
 */
public class MercenaryCastleWarCastleSiegeHudInfo extends ServerPacket
{
	private final int _castleId;
	
	public MercenaryCastleWarCastleSiegeHudInfo(int castleId)
	{
		_castleId = castleId;
	}
	
	@Override
	public void write()
	{
		final Castle castle = CastleManager.getInstance().getCastleById(_castleId);
		if (castle == null)
		{
			return;
		}
		
		ServerPackets.EX_MERCENARY_CASTLEWAR_CASTLE_SIEGE_HUD_INFO.writeId(this);
		writeInt(_castleId);
		if (castle.getSiege().isInProgress())
		{
			writeInt(1);
			writeInt(0);
			writeInt((int) (((CastleManager.getInstance().getCastleById(_castleId).getSiegeDate().getTimeInMillis() + (SiegeManager.getInstance().getSiegeLength() * 60000)) - System.currentTimeMillis()) / 1000));
		}
		else
		{
			writeInt(0);
			writeInt(0);
			writeInt((int) ((CastleManager.getInstance().getCastleById(_castleId).getSiegeDate().getTimeInMillis() - System.currentTimeMillis()) / 1000));
		}
	}
}
