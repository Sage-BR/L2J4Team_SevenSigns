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
package org.l2j.gameserver.network.clientpackets;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.network.GameClient;

/**
 * Format: (c) dddd d: dx d: dy d: dz d: AirShip id ??
 * @author -Wooden-
 */
public class ExGetOnAirShip implements ClientPacket
{
	@SuppressWarnings("unused")
	private int _x;
	@SuppressWarnings("unused")
	private int _y;
	@SuppressWarnings("unused")
	private int _z;
	@SuppressWarnings("unused")
	private int _shipId;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_x = packet.readInt();
		_y = packet.readInt();
		_z = packet.readInt();
		_shipId = packet.readInt();
	}
	
	@Override
	public void run(GameClient client)
	{
		// PacketLogger.info("[T1:ExGetOnAirShip] x: " + _x);
		// PacketLogger.info("[T1:ExGetOnAirShip] y: " + _y);
		// PacketLogger.info("[T1:ExGetOnAirShip] z: " + _z);
		// PacketLogger.info("[T1:ExGetOnAirShip] ship ID: " + _shipId);
	}
}
