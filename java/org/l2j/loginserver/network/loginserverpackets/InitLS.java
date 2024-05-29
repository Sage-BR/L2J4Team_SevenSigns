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
package org.l2j.loginserver.network.loginserverpackets;

import org.l2j.commons.network.WritablePacket;
import org.l2j.loginserver.LoginServer;

/**
 * @author -Wooden-
 */
public class InitLS extends WritablePacket
{
	// ID 0x00
	// format
	// d proto rev
	// d key size
	// b key
	
	public InitLS(byte[] publickey)
	{
		writeByte(0x00);
		writeInt(LoginServer.PROTOCOL_REV);
		writeInt(publickey.length);
		writeBytes(publickey);
	}
}
