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

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.l2j.gameserver.enums.MatchingMemberType;
import org.l2j.gameserver.instancemanager.InstanceManager;
import org.l2j.gameserver.instancemanager.MapRegionManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.matching.PartyMatchingRoom;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author Gnacik
 */
public class ExPartyRoomMember extends ServerPacket
{
	private final PartyMatchingRoom _room;
	private final MatchingMemberType _type;
	
	public ExPartyRoomMember(Player player, PartyMatchingRoom room)
	{
		_room = room;
		_type = room.getMemberType(player);
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_PARTY_ROOM_MEMBER.writeId(this);
		writeInt(_type.ordinal());
		writeInt(_room.getMembersCount());
		for (Player member : _room.getMembers())
		{
			writeInt(member.getObjectId());
			writeString(member.getName());
			writeInt(member.getActiveClass());
			writeInt(member.getLevel());
			writeInt(MapRegionManager.getInstance().getBBs(member.getLocation()));
			writeInt(_room.getMemberType(member).ordinal());
			final Map<Integer, Long> instanceTimes = InstanceManager.getInstance().getAllInstanceTimes(member);
			writeInt(instanceTimes.size());
			for (Entry<Integer, Long> entry : instanceTimes.entrySet())
			{
				final long instanceTime = TimeUnit.MILLISECONDS.toSeconds(entry.getValue() - System.currentTimeMillis());
				writeInt(entry.getKey());
				writeInt((int) instanceTime);
			}
		}
	}
}