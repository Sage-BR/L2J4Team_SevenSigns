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
package org.l2j.gameserver.network.serverpackets;

import org.l2j.gameserver.model.Location;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.ServerPackets;

public class ExGetOnAirShip extends ServerPacket
{
	private final int _playerId;
	private final int _airShipId;
	private final Location _pos;
	
	public ExGetOnAirShip(Player player, Creature ship)
	{
		_playerId = player.getObjectId();
		_airShipId = ship.getObjectId();
		_pos = player.getInVehiclePosition();
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_GET_ON_AIR_SHIP.writeId(this);
		writeInt(_playerId);
		writeInt(_airShipId);
		writeInt(_pos.getX());
		writeInt(_pos.getY());
		writeInt(_pos.getZ());
	}
}
