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
 * @author Maktakien
 */
public class MoveToLocationInVehicle extends ServerPacket
{
	private final int _objectId;
	private final int _boatId;
	private final Location _destination;
	private final Location _origin;
	
	/**
	 * @param player
	 * @param destination
	 * @param origin
	 */
	public MoveToLocationInVehicle(Player player, Location destination, Location origin)
	{
		_objectId = player.getObjectId();
		_boatId = player.getBoat().getObjectId();
		_destination = destination;
		_origin = origin;
	}
	
	@Override
	public void write()
	{
		ServerPackets.MOVE_TO_LOCATION_IN_VEHICLE.writeId(this);
		writeInt(_objectId);
		writeInt(_boatId);
		writeInt(_destination.getX());
		writeInt(_destination.getY());
		writeInt(_destination.getZ());
		writeInt(_origin.getX());
		writeInt(_origin.getY());
		writeInt(_origin.getZ());
	}
}
