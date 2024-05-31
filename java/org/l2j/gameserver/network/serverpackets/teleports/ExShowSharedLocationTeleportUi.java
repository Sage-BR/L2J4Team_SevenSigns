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
package org.l2j.gameserver.network.serverpackets.teleports;

import org.l2j.Config;
import org.l2j.gameserver.model.holders.SharedTeleportHolder;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

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
	public void write()
	{
		ServerPackets.EX_SHARED_POSITION_TELEPORT_UI.writeId(this);
		writeSizedString(_teleport.getName());
		writeInt(_teleport.getId());
		writeInt(_teleport.getCount());
		writeShort(150);
		writeInt(_teleport.getLocation().getX());
		writeInt(_teleport.getLocation().getY());
		writeInt(_teleport.getLocation().getZ());
		writeLong(Config.TELEPORT_SHARE_LOCATION_COST);
	}
}
