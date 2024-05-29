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

import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.network.ServerPackets;

public class MoveToLocation extends ServerPacket
{
	private final int _objectId;
	private final int _x;
	private final int _y;
	private final int _z;
	private final int _xDst;
	private final int _yDst;
	private final int _zDst;
	
	public MoveToLocation(Creature creature)
	{
		_objectId = creature.getObjectId();
		_x = creature.getX();
		_y = creature.getY();
		_z = creature.getZ();
		_xDst = creature.getXdestination();
		_yDst = creature.getYdestination();
		_zDst = creature.getZdestination();
	}
	
	@Override
	public void write()
	{
		ServerPackets.MOVE_TO_LOCATION.writeId(this);
		writeInt(_objectId);
		writeInt(_xDst);
		writeInt(_yDst);
		writeInt(_zDst);
		writeInt(_x);
		writeInt(_y);
		writeInt(_z);
	}
}
