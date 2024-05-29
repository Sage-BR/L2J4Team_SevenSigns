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
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author UnAfraid
 */
public class ExTeleportToLocationActivate extends ServerPacket
{
	private final int _objectId;
	private final Location _loc;
	
	public ExTeleportToLocationActivate(Creature creature)
	{
		_objectId = creature.getObjectId();
		_loc = creature.getLocation();
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_TELEPORT_TO_LOCATION_ACTIVATE.writeId(this);
		writeInt(_objectId);
		writeInt(_loc.getX());
		writeInt(_loc.getY());
		writeInt(_loc.getZ());
		writeInt(0); // Unknown (this isn't instanceId)
		writeInt(_loc.getHeading());
		writeInt(0); // Unknown
	}
}