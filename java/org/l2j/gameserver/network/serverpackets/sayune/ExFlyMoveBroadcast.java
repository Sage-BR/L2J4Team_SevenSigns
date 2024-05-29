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
package org.l2j.gameserver.network.serverpackets.sayune;

import org.l2j.gameserver.enums.SayuneType;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.interfaces.ILocational;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author UnAfraid
 */
public class ExFlyMoveBroadcast extends ServerPacket
{
	private final int _objectId;
	private final int _mapId;
	private final ILocational _currentLoc;
	private final ILocational _targetLoc;
	private final SayuneType _type;
	
	public ExFlyMoveBroadcast(Player player, SayuneType type, int mapId, ILocational targetLoc)
	{
		_objectId = player.getObjectId();
		_type = type;
		_mapId = mapId;
		_currentLoc = player;
		_targetLoc = targetLoc;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_FLY_MOVE_BROADCAST.writeId(this);
		writeInt(_objectId);
		writeInt(_type.ordinal());
		writeInt(_mapId);
		writeInt(_targetLoc.getX());
		writeInt(_targetLoc.getY());
		writeInt(_targetLoc.getZ());
		writeInt(0); // ?
		writeInt(_currentLoc.getX());
		writeInt(_currentLoc.getY());
		writeInt(_currentLoc.getZ());
	}
}
