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
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.PacketLogger;
import org.l2j.gameserver.network.serverpackets.ExNeedToChangeName;

/**
 * Reply for {@link ExNeedToChangeName}
 * @author JIV
 */
public class RequestExChangeName implements ClientPacket
{
	private String _newName;
	private int _type;
	private int _charSlot;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_type = packet.readInt();
		_newName = packet.readString();
		_charSlot = packet.readInt();
	}
	
	@Override
	public void run(GameClient client)
	{
		PacketLogger.info("Recieved " + getClass().getSimpleName() + " name: " + _newName + " type: " + _type + " CharSlot: " + _charSlot);
	}
}
