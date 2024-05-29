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
package org.l2j.gameserver.network.clientpackets.shuttle;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.model.World;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.instance.Shuttle;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.PacketLogger;
import org.l2j.gameserver.network.clientpackets.ClientPacket;

/**
 * @author UnAfraid
 */
public class RequestShuttleGetOn implements ClientPacket
{
	private int _x;
	private int _y;
	private int _z;
	
	@Override
	public void read(ReadablePacket packet)
	{
		packet.readInt(); // charId
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
		
		// TODO: better way?
		for (Shuttle shuttle : World.getInstance().getVisibleObjects(player, Shuttle.class))
		{
			if (shuttle.calculateDistance3D(player) < 1000)
			{
				shuttle.addPassenger(player);
				player.getInVehiclePosition().setXYZ(_x, _y, _z);
				break;
			}
			PacketLogger.info(getClass().getSimpleName() + ": range between char and shuttle: " + shuttle.calculateDistance3D(player));
		}
	}
}
