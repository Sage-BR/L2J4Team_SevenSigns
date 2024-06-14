/*
 * This file is part of the L2J Mobius project.
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
package org.l2jmobius.gameserver.network.serverpackets.teleports;

import org.l2jmobius.Config;
import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.model.holders.SharedTeleportHolder;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

/**
 * @author NasSeKa
 */
public class ExShowSharedLocationTeleportUi extends ServerPacket
{
	private final SharedTeleportHolder _teleport;
	
	public ExShowSharedLocationTeleportUi(SharedTeleportHolder teleport)
	{
		_teleport = teleport;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_SHARED_POSITION_TELEPORT_UI.writeId(this, buffer);
		buffer.writeSizedString(_teleport.getName());
		buffer.writeInt(_teleport.getId());
		buffer.writeInt(_teleport.getCount());
		buffer.writeShort(150);
		buffer.writeInt(_teleport.getLocation().getX());
		buffer.writeInt(_teleport.getLocation().getY());
		buffer.writeInt(_teleport.getLocation().getZ());
		buffer.writeLong(Config.TELEPORT_SHARE_LOCATION_COST);
	}
}
