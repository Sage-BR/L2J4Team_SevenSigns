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
import org.l2j.loginserver.network.LoginClient;
import org.l2j.loginserver.network.serverpackets.ServerList;

/**
 * <pre>
 * Format: ddc
 * d: fist part of session id
 * d: second part of session id
 * c: ?
 * </pre>
 */
public class RequestServerList implements LoginClientPacket
{
	private int _skey1;
	private int _skey2;
	@SuppressWarnings("unused")
	private int _data3;
	
	@Override
	public void read(ReadablePacket packet)
	{
		if (packet.getRemainingLength() >= 8)
		{
			_skey1 = packet.readInt(); // loginOk 1
			_skey2 = packet.readInt(); // loginOk 2
		}
	}
	
	@Override
	public void run(LoginClient client)
	{
		if (client.getSessionKey().checkLoginPair(_skey1, _skey2))
		{
			client.sendPacket(new ServerList(client));
		}
		else
		{
			client.close(LoginFailReason.REASON_ACCESS_FAILED);
		}
	}
}
