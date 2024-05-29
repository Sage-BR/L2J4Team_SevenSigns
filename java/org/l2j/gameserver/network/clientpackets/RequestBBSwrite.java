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

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.handler.CommunityBoardHandler;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.GameClient;

/**
 * RequestBBSwrite client packet implementation.
 * @author -Wooden-, Zoey76
 */
public class RequestBBSwrite implements ClientPacket
{
	private String _url;
	private String _arg1;
	private String _arg2;
	private String _arg3;
	private String _arg4;
	private String _arg5;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_url = packet.readString();
		_arg1 = packet.readString();
		_arg2 = packet.readString();
		_arg3 = packet.readString();
		_arg4 = packet.readString();
		_arg5 = packet.readString();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		CommunityBoardHandler.getInstance().handleWriteCommand(player, _url, _arg1, _arg2, _arg3, _arg4, _arg5);
	}
}