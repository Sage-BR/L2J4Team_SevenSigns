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

import org.l2j.gameserver.enums.ExManagePartyRoomMemberType;
import org.l2j.gameserver.enums.MatchingMemberType;
import org.l2j.gameserver.instancemanager.MapRegionManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.matching.CommandChannelMatchingRoom;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author Gnacik
 */
public class ExManageMpccRoomMember extends ServerPacket
{
	private final Player _player;
	private final MatchingMemberType _memberType;
	private final ExManagePartyRoomMemberType _type;
	
	public ExManageMpccRoomMember(Player player, CommandChannelMatchingRoom room, ExManagePartyRoomMemberType mode)
	{
		_player = player;
		_memberType = room.getMemberType(player);
		_type = mode;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_MANAGE_PARTY_ROOM_MEMBER.writeId(this);
		writeInt(_type.ordinal());
		writeInt(_player.getObjectId());
		writeString(_player.getName());
		writeInt(_player.getClassId().getId());
		writeInt(_player.getLevel());
		writeInt(MapRegionManager.getInstance().getBBs(_player.getLocation()));
		writeInt(_memberType.ordinal());
	}
}
