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

import java.util.logging.Logger;

import org.l2j.Config;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.serverpackets.KeyPacket;

/**
 * @version $Revision: 1.5.2.8.2.8 $ $Date: 2005/04/02 10:43:04 $
 */
public class ProtocolVersion implements ClientPacket
{
	private static final Logger LOGGER_ACCOUNTING = Logger.getLogger("accounting");
	
	private int _version;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_version = packet.readInt();
	}
	
	@Override
	public void run(GameClient client)
	{
		// This packet is never encrypted.
		if (_version == -2)
		{
			// This is just a ping attempt from the new C2 client.
			client.disconnect();
		}
		else if (!Config.PROTOCOL_LIST.contains(_version))
		{
			LOGGER_ACCOUNTING.warning("Wrong protocol version " + _version + ", " + client);
			client.setProtocolOk(false);
			client.close(new KeyPacket(client.enableCrypt(), 0));
		}
		else
		{
			client.setProtocolVersion(_version);
			client.setProtocolOk(true);
			client.sendPacket(new KeyPacket(client.enableCrypt(), 1));
		}
	}
}
