/*
 * This file is part of the L2J 4Team Project.
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
package org.l2j.gameserver.network.loginserverpackets.login;

import org.l2j.commons.network.base.BaseReadablePacket;

/**
 * @author -Wooden-
 */
public class AuthResponse extends BaseReadablePacket
{
	private final int _serverId;
	private final String _serverName;
	
	public AuthResponse(byte[] decrypt)
	{
		super(decrypt);
		readByte(); // Packet id, it is already processed.
		
		_serverId = readByte();
		_serverName = readString();
	}
	
	public int getServerId()
	{
		return _serverId;
	}
	
	public String getServerName()
	{
		return _serverName;
	}
}
