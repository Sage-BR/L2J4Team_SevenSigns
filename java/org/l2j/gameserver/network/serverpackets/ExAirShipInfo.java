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

import org.l2j.gameserver.model.actor.instance.AirShip;
import org.l2j.gameserver.network.ServerPackets;

public class ExAirShipInfo extends ServerPacket
{
	// store some parameters, because they can be changed during broadcast
	private final AirShip _ship;
	private final int _x;
	private final int _y;
	private final int _z;
	private final int _heading;
	private final int _moveSpeed;
	private final int _rotationSpeed;
	private final int _captain;
	private final int _helm;
	
	public ExAirShipInfo(AirShip ship)
	{
		_ship = ship;
		_x = ship.getX();
		_y = ship.getY();
		_z = ship.getZ();
		_heading = ship.getHeading();
		_moveSpeed = (int) ship.getStat().getMoveSpeed();
		_rotationSpeed = (int) ship.getStat().getRotationSpeed();
		_captain = ship.getCaptainId();
		_helm = ship.getHelmObjectId();
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_AIR_SHIP_INFO.writeId(this);
		writeInt(_ship.getObjectId());
		writeInt(_x);
		writeInt(_y);
		writeInt(_z);
		writeInt(_heading);
		writeInt(_captain);
		writeInt(_moveSpeed);
		writeInt(_rotationSpeed);
		writeInt(_helm);
		if (_helm != 0)
		{
			// TODO: unhardcode these!
			writeInt(0x16e); // Controller X
			writeInt(0x00); // Controller Y
			writeInt(0x6b); // Controller Z
			writeInt(0x15c); // Captain X
			writeInt(0x00); // Captain Y
			writeInt(0x69); // Captain Z
		}
		else
		{
			writeInt(0);
			writeInt(0);
			writeInt(0);
			writeInt(0);
			writeInt(0);
			writeInt(0);
		}
		writeInt(_ship.getFuel());
		writeInt(_ship.getMaxFuel());
	}
}
