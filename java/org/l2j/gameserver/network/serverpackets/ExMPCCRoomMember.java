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
package org.l2j.gameserver.network.serverpackets;

import org.l2j.gameserver.enums.MatchingMemberType;
import org.l2j.gameserver.instancemanager.MapRegionManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.matching.CommandChannelMatchingRoom;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author Sdw
 */
public class ExMPCCRoomMember extends ServerPacket
{
	private final CommandChannelMatchingRoom _room;
	private final MatchingMemberType _type;
	
	public ExMPCCRoomMember(Player player, CommandChannelMatchingRoom room)
	{
		_room = room;
		_type = room.getMemberType(player);
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_MPCC_ROOM_MEMBER.writeId(this);
		writeInt(_type.ordinal());
		writeInt(_room.getMembersCount());
		for (Player member : _room.getMembers())
		{
			writeInt(member.getObjectId());
			writeString(member.getName());
			writeInt(member.getLevel());
			writeInt(member.getClassId().getId());
			writeInt(MapRegionManager.getInstance().getBBs(member.getLocation()));
			writeInt(_room.getMemberType(member).ordinal());
		}
	}
}
