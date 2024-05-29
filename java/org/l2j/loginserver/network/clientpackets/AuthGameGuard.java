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
package org.l2j.loginserver.network.clientpackets;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.loginserver.enums.LoginFailReason;
import org.l2j.loginserver.network.ConnectionState;
import org.l2j.loginserver.network.LoginClient;
import org.l2j.loginserver.network.serverpackets.GGAuth;

/**
 * Format: ddddd
 * @author -Wooden-
 */
public class AuthGameGuard implements LoginClientPacket
{
	private int _sessionId;
	
	@SuppressWarnings("unused")
	private int _data1;
	@SuppressWarnings("unused")
	private int _data2;
	@SuppressWarnings("unused")
	private int _data3;
	@SuppressWarnings("unused")
	private int _data4;
	
	@Override
	public void read(ReadablePacket packet)
	{
		if (packet.getRemainingLength() >= 20)
		{
			_sessionId = packet.readInt();
			_data1 = packet.readInt();
			_data2 = packet.readInt();
			_data3 = packet.readInt();
			_data4 = packet.readInt();
		}
	}
	
	@Override
	public void run(LoginClient client)
	{
		if (_sessionId == client.getSessionId())
		{
			client.setConnectionState(ConnectionState.AUTHED_GG);
			client.sendPacket(new GGAuth(client.getSessionId()));
		}
		else
		{
			client.close(LoginFailReason.REASON_ACCESS_FAILED);
		}
	}
}
