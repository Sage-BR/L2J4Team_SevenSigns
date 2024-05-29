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

import org.l2j.gameserver.model.WorldObject;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.network.ServerPackets;

public class MoveToPawn extends ServerPacket
{
	private final int _objectId;
	private final int _targetId;
	private final int _distance;
	private final int _x;
	private final int _y;
	private final int _z;
	private final int _tx;
	private final int _ty;
	private final int _tz;
	
	public MoveToPawn(Creature creature, WorldObject target, int distance)
	{
		_objectId = creature.getObjectId();
		_targetId = target.getObjectId();
		_distance = distance;
		_x = creature.getX();
		_y = creature.getY();
		_z = creature.getZ();
		_tx = target.getX();
		_ty = target.getY();
		_tz = target.getZ();
	}
	
	@Override
	public void write()
	{
		ServerPackets.MOVE_TO_PAWN.writeId(this);
		writeInt(_objectId);
		writeInt(_targetId);
		writeInt(_distance);
		writeInt(_x);
		writeInt(_y);
		writeInt(_z);
		writeInt(_tx);
		writeInt(_ty);
		writeInt(_tz);
	}
}
