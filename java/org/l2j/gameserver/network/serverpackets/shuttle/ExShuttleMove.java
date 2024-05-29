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
package org.l2j.gameserver.network.serverpackets.shuttle;

import org.l2j.gameserver.model.actor.instance.Shuttle;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author UnAfraid
 */
public class ExShuttleMove extends ServerPacket
{
	private final Shuttle _shuttle;
	private final int _x;
	private final int _y;
	private final int _z;
	
	public ExShuttleMove(Shuttle shuttle, int x, int y, int z)
	{
		_shuttle = shuttle;
		_x = x;
		_y = y;
		_z = z;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_SUTTLE_MOVE.writeId(this);
		writeInt(_shuttle.getObjectId());
		writeInt((int) _shuttle.getStat().getMoveSpeed());
		writeInt((int) _shuttle.getStat().getRotationSpeed());
		writeInt(_x);
		writeInt(_y);
		writeInt(_z);
	}
}
