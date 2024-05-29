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

public class ExMoveToLocationAirShip extends ServerPacket
{
	private final int _objId;
	private final int _tx;
	private final int _ty;
	private final int _tz;
	private final int _x;
	private final int _y;
	private final int _z;
	
	public ExMoveToLocationAirShip(Creature creature)
	{
		_objId = creature.getObjectId();
		_tx = creature.getXdestination();
		_ty = creature.getYdestination();
		_tz = creature.getZdestination();
		_x = creature.getX();
		_y = creature.getY();
		_z = creature.getZ();
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_MOVE_TO_LOCATION_AIR_SHIP.writeId(this);
		writeInt(_objId);
		writeInt(_tx);
		writeInt(_ty);
		writeInt(_tz);
		writeInt(_x);
		writeInt(_y);
		writeInt(_z);
	}
}
