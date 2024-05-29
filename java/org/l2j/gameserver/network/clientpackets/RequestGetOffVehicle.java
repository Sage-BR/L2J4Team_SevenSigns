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
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.zone.ZoneId;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.serverpackets.ActionFailed;
import org.l2j.gameserver.network.serverpackets.GetOffVehicle;
import org.l2j.gameserver.network.serverpackets.StopMoveInVehicle;

/**
 * @author Maktakien
 */
public class RequestGetOffVehicle implements ClientPacket
{
	private int _boatId;
	private int _x;
	private int _y;
	private int _z;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_boatId = packet.readInt();
		_x = packet.readInt();
		_y = packet.readInt();
		_z = packet.readInt();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		if (!player.isInBoat() || (player.getBoat().getObjectId() != _boatId) || player.getBoat().isMoving() || !player.isInsideRadius3D(_x, _y, _z, 1000))
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		player.broadcastPacket(new StopMoveInVehicle(player, _boatId));
		player.setVehicle(null);
		player.setInVehiclePosition(null);
		player.sendPacket(ActionFailed.STATIC_PACKET);
		player.broadcastPacket(new GetOffVehicle(player.getObjectId(), _boatId, _x, _y, _z));
		player.setXYZ(_x, _y, _z);
		player.setInsideZone(ZoneId.PEACE, false);
		player.revalidateZone(true);
	}
}
