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

import org.l2j.gameserver.model.VehiclePathPoint;
import org.l2j.gameserver.network.ServerPackets;

public class ExAirShipTeleportList extends ServerPacket
{
	private final int _dockId;
	private final VehiclePathPoint[][] _teleports;
	private final int[] _fuelConsumption;
	
	public ExAirShipTeleportList(int dockId, VehiclePathPoint[][] teleports, int[] fuelConsumption)
	{
		_dockId = dockId;
		_teleports = teleports;
		_fuelConsumption = fuelConsumption;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_AIR_SHIP_TELEPORT_LIST.writeId(this);
		writeInt(_dockId);
		if (_teleports != null)
		{
			writeInt(_teleports.length);
			for (int i = 0; i < _teleports.length; i++)
			{
				writeInt(i - 1);
				writeInt(_fuelConsumption[i]);
				final VehiclePathPoint[] path = _teleports[i];
				final VehiclePathPoint dst = path[path.length - 1];
				writeInt(dst.getX());
				writeInt(dst.getY());
				writeInt(dst.getZ());
			}
		}
		else
		{
			writeInt(0);
		}
	}
}
