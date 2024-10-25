/*
 * This file is part of the L2J 4Team Project.
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

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.model.Location;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author UnAfraid
 */
public class ExValidateLocationInShuttle extends ServerPacket
{
	private final Player _player;
	private final int _shipId;
	private final int _heading;
	private final Location _loc;
	
	public ExValidateLocationInShuttle(Player player)
	{
		_player = player;
		_shipId = _player.getShuttle().getObjectId();
		_loc = player.getInVehiclePosition();
		_heading = player.getHeading();
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_VALIDATE_LOCATION_IN_SHUTTLE.writeId(this, buffer);
		buffer.writeInt(_player.getObjectId());
		buffer.writeInt(_shipId);
		buffer.writeInt(_loc.getX());
		buffer.writeInt(_loc.getY());
		buffer.writeInt(_loc.getZ());
		buffer.writeInt(_heading);
	}
}
