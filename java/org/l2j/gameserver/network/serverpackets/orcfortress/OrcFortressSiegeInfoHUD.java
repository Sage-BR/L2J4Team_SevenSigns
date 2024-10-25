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
package org.l2j.gameserver.network.serverpackets.orcfortress;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Serenitty
 */
public class OrcFortressSiegeInfoHUD extends ServerPacket
{
	private final int _fortressId;
	private final int _siegeState;
	private final int _nowTime;
	private final int _remainTime;
	
	public OrcFortressSiegeInfoHUD(int fortressId, int siegeState, int nowTime, int remainTime)
	{
		_fortressId = fortressId;
		_siegeState = siegeState;
		_nowTime = nowTime;
		_remainTime = remainTime;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_ADEN_FORTRESS_SIEGE_HUD_INFO.writeId(this, buffer);
		buffer.writeInt(_fortressId);
		buffer.writeInt(_siegeState);
		buffer.writeInt(_nowTime);
		buffer.writeInt(_remainTime);
	}
}
