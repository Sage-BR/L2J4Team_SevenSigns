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
package org.l2j.loginserver.network;

import java.util.logging.Logger;

import org.l2j.commons.network.PacketHandlerInterface;
import org.l2j.commons.network.ReadablePacket;
import org.l2j.commons.util.CommonUtil;
import org.l2j.loginserver.network.clientpackets.LoginClientPacket;

/**
 * @author Mobius
 */
public class LoginPacketHandler implements PacketHandlerInterface<LoginClient>
{
	protected static final Logger LOGGER = Logger.getLogger(LoginPacketHandler.class.getName());
	
	@Override
	public void handle(LoginClient client, ReadablePacket packet)
	{
		// Read packet id.
		final int packetId;
		try
		{
			packetId = packet.readByte();
		}
		catch (Exception e)
		{
			LOGGER.warning("LoginPacketHandler: Problem receiving packet id from " + client);
			LOGGER.warning(CommonUtil.getStackTrace(e));
			client.disconnect();
			return;
		}
		
		// Check if packet id is within valid range.
		if ((packetId < 0) || (packetId >= LoginClientPackets.PACKET_ARRAY.length))
		{
			return;
		}
		
		// Find packet enum.
		final LoginClientPackets packetEnum = LoginClientPackets.PACKET_ARRAY[packetId];
		// Check connection state.
		if ((packetEnum == null) || !packetEnum.getConnectionStates().contains(client.getConnectionState()))
		{
			return;
		}
		
		// Create new LoginClientPacket.
		final LoginClientPacket newPacket = packetEnum.newPacket();
		if (newPacket == null)
		{
			return;
		}
		
		// Packet read and run.
		try
		{
			newPacket.read(packet);
			newPacket.run(client);
		}
		catch (Exception e)
		{
			LOGGER.warning("LoginPacketHandler: Problem with " + client + " [Packet: 0x" + Integer.toHexString(packetId).toUpperCase() + "]");
			LOGGER.warning(CommonUtil.getStackTrace(e));
		}
	}
}
