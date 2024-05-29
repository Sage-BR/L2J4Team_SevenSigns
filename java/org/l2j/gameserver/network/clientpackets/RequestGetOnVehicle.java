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
package org.l2j.gameserver.network.clientpackets;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.instancemanager.BoatManager;
import org.l2j.gameserver.model.Location;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.instance.Boat;
import org.l2j.gameserver.model.zone.ZoneId;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.serverpackets.ActionFailed;
import org.l2j.gameserver.network.serverpackets.GetOnVehicle;

/**
 * @version $Revision: 1.1.4.3 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestGetOnVehicle implements ClientPacket
{
	private int _boatId;
	private Location _pos;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_boatId = packet.readInt();
		_pos = new Location(packet.readInt(), packet.readInt(), packet.readInt());
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		Boat boat;
		if (player.isInBoat())
		{
			boat = player.getBoat();
			if (boat.getObjectId() != _boatId)
			{
				player.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
		}
		else
		{
			boat = BoatManager.getInstance().getBoat(_boatId);
			if ((boat == null) || boat.isMoving() || !player.isInsideRadius3D(boat, 1000))
			{
				player.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
		}
		
		player.setInVehiclePosition(_pos);
		player.setVehicle(boat);
		player.broadcastPacket(new GetOnVehicle(player.getObjectId(), boat.getObjectId(), _pos));
		player.setXYZ(boat.getX(), boat.getY(), boat.getZ());
		player.setInsideZone(ZoneId.PEACE, true);
		player.revalidateZone(true);
	}
}
