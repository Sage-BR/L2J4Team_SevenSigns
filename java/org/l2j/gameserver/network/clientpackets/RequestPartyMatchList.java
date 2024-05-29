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
import org.l2j.gameserver.enums.MatchingRoomType;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.matching.MatchingRoom;
import org.l2j.gameserver.model.matching.PartyMatchingRoom;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.serverpackets.PartyRoomInfo;

/**
 * author: Gnacik
 */
public class RequestPartyMatchList implements ClientPacket
{
	private int _roomId;
	private int _maxMembers;
	private int _minLevel;
	private int _maxLevel;
	private int _lootType;
	private String _roomTitle;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_roomId = packet.readInt();
		_maxMembers = packet.readInt();
		_minLevel = packet.readInt();
		_maxLevel = packet.readInt();
		_lootType = packet.readInt();
		_roomTitle = packet.readString();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if ((_roomId <= 0) && (player.getMatchingRoom() == null))
		{
			final PartyMatchingRoom room = new PartyMatchingRoom(_roomTitle, _lootType, _minLevel, _maxLevel, _maxMembers, player);
			player.setMatchingRoom(room);
		}
		else
		{
			final MatchingRoom room = player.getMatchingRoom();
			if ((room.getId() == _roomId) && (room.getRoomType() == MatchingRoomType.PARTY) && room.isLeader(player))
			{
				room.setLootType(_lootType);
				room.setMinLevel(_minLevel);
				room.setMaxLevel(_maxLevel);
				room.setMaxMembers(_maxMembers);
				room.setTitle(_roomTitle);
				
				final PartyRoomInfo packet = new PartyRoomInfo((PartyMatchingRoom) room);
				for (Player member : room.getMembers())
				{
					member.sendPacket(packet);
				}
			}
		}
	}
}
