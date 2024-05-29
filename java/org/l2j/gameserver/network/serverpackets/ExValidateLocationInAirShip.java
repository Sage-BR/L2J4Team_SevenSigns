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
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.ServerPackets;

/**
 * update 27.8.10
 * @author kerberos JIV
 */
public class ExValidateLocationInAirShip extends ServerPacket
{
	private final Player _player;
	private final int _shipId;
	private final int _heading;
	private final Location _loc;
	
	public ExValidateLocationInAirShip(Player player)
	{
		_player = player;
		_shipId = _player.getAirShip().getObjectId();
		_loc = player.getInVehiclePosition();
		_heading = player.getHeading();
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_VALIDATE_LOCATION_IN_AIR_SHIP.writeId(this);
		writeInt(_player.getObjectId());
		writeInt(_shipId);
		writeInt(_loc.getX());
		writeInt(_loc.getY());
		writeInt(_loc.getZ());
		writeInt(_heading);
	}
}
