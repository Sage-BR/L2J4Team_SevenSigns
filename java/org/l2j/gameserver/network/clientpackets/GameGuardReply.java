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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.PacketLogger;

/**
 * Format: c dddd
 * @author KenM
 */
public class GameGuardReply implements ClientPacket
{
	private static final byte[] VALID =
	{
		(byte) 0x88,
		0x40,
		0x1c,
		(byte) 0xa7,
		(byte) 0x83,
		0x42,
		(byte) 0xe9,
		0x15,
		(byte) 0xde,
		(byte) 0xc3,
		0x68,
		(byte) 0xf6,
		0x2d,
		0x23,
		(byte) 0xf1,
		0x3f,
		(byte) 0xee,
		0x68,
		0x5b,
		(byte) 0xc5,
	};
	
	private final byte[] _reply = new byte[8];
	
	@Override
	public void read(ReadablePacket packet)
	{
		_reply[0] = (byte) packet.readByte();
		_reply[1] = (byte) packet.readByte();
		_reply[2] = (byte) packet.readByte();
		_reply[3] = (byte) packet.readByte();
		packet.readInt();
		_reply[4] = (byte) packet.readByte();
		_reply[5] = (byte) packet.readByte();
		_reply[6] = (byte) packet.readByte();
		_reply[7] = (byte) packet.readByte();
	}
	
	@Override
	public void run(GameClient client)
	{
		try
		{
			final MessageDigest md = MessageDigest.getInstance("SHA");
			final byte[] result = md.digest(_reply);
			if (Arrays.equals(result, VALID))
			{
				client.setGameGuardOk(true);
			}
		}
		catch (NoSuchAlgorithmException e)
		{
			PacketLogger.warning(getClass().getSimpleName() + ": " + e.getMessage());
		}
	}
}
