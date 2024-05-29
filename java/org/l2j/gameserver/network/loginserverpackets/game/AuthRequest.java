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
package org.l2j.gameserver.network.loginserverpackets.game;

import java.util.List;

import org.l2j.commons.network.WritablePacket;

public class AuthRequest extends WritablePacket
{
	/**
	 * Format: cccSddb c desired ID c accept alternative ID c reserve Host s ExternalHostName s InetranlHostName d max players d hexid size b hexid
	 * @param id
	 * @param acceptAlternate
	 * @param hexid
	 * @param port
	 * @param reserveHost
	 * @param maxplayer
	 * @param subnets the subnets lists
	 * @param hosts the hosts list
	 */
	public AuthRequest(int id, boolean acceptAlternate, byte[] hexid, int port, boolean reserveHost, int maxplayer, List<String> subnets, List<String> hosts)
	{
		writeByte(0x01);
		writeByte(id);
		writeByte(acceptAlternate ? 0x01 : 0x00);
		writeByte(reserveHost ? 0x01 : 0x00);
		writeShort(port);
		writeInt(maxplayer);
		writeInt(hexid.length);
		writeBytes(hexid);
		writeInt(subnets.size());
		for (int i = 0; i < subnets.size(); i++)
		{
			writeString(subnets.get(i));
			writeString(hosts.get(i));
		}
	}
}