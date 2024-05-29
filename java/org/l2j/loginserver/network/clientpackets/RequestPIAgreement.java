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
import org.l2j.loginserver.network.LoginClient;
import org.l2j.loginserver.network.serverpackets.PIAgreementAck;

/**
 * @author UnAfraid
 */
public class RequestPIAgreement implements LoginClientPacket
{
	private int _accountId;
	private int _status;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_accountId = packet.readInt();
		_status = packet.readByte();
	}
	
	@Override
	public void run(LoginClient client)
	{
		client.sendPacket(new PIAgreementAck(_accountId, _status));
	}
}
