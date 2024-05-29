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
package org.l2j.loginserver.network.serverpackets;

import org.l2j.commons.network.WritablePacket;
import org.l2j.loginserver.network.LoginServerPackets;

/**
 * <pre>
 * Format: dd b dddd s
 * d: session id
 * d: protocol revision
 * b: 0x90 bytes : 0x80 bytes for the scrambled RSA public key
 *                 0x10 bytes at 0x00
 * d: unknow
 * d: unknow
 * d: unknow
 * d: unknow
 * s: blowfish key
 * </pre>
 */
public class Init extends WritablePacket
{
	private final int _sessionId;
	
	private final byte[] _publicKey;
	private final byte[] _blowfishKey;
	
	public Init(byte[] publickey, byte[] blowfishkey, int sessionId)
	{
		_sessionId = sessionId;
		_publicKey = publickey;
		_blowfishKey = blowfishkey;
	}
	
	@Override
	public void write()
	{
		LoginServerPackets.INIT.writeId(this);
		writeInt(_sessionId); // session id
		writeInt(0x0000c621); // protocol revision
		
		writeBytes(_publicKey); // RSA Public Key
		
		// unk GG related?
		writeInt(0x29DD954E);
		writeInt(0x77C39CFC);
		writeInt(0x97ADB620);
		writeInt(0x07BDE0F7);
		
		writeBytes(_blowfishKey); // BlowFish key
		writeByte(0); // null termination ;)
	}
}
