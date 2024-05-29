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
package org.l2j.gameserver.network.serverpackets.friend;

import java.util.LinkedList;
import java.util.List;

import org.l2j.gameserver.data.sql.CharInfoTable;
import org.l2j.gameserver.model.World;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * Support for "Chat with Friends" dialog. <br />
 * This packet is sent only at login.
 * @author Tempy
 */
public class L2FriendList extends ServerPacket
{
	private final List<FriendInfo> _info = new LinkedList<>();
	
	private static class FriendInfo
	{
		int _objId;
		String _name;
		int _level;
		int _classId;
		boolean _online;
		
		public FriendInfo(int objId, String name, boolean online, int level, int classId)
		{
			_objId = objId;
			_name = name;
			_online = online;
			_level = level;
			_classId = classId;
		}
	}
	
	public L2FriendList(Player player)
	{
		for (int objId : player.getFriendList())
		{
			final String name = CharInfoTable.getInstance().getNameById(objId);
			final Player player1 = World.getInstance().getPlayer(objId);
			boolean online = false;
			int level = 0;
			int classId = 0;
			if (player1 != null)
			{
				online = true;
				level = player1.getLevel();
				classId = player1.getClassId().getId();
			}
			else
			{
				level = CharInfoTable.getInstance().getLevelById(objId);
				classId = CharInfoTable.getInstance().getClassIdById(objId);
			}
			_info.add(new FriendInfo(objId, name, online, level, classId));
		}
	}
	
	@Override
	public void write()
	{
		ServerPackets.L2_FRIEND_LIST.writeId(this);
		writeInt(_info.size());
		for (FriendInfo info : _info)
		{
			writeInt(info._objId); // character id
			writeString(info._name);
			writeInt(info._online); // online
			writeInt(info._online ? info._objId : 0); // object id if online
			writeInt(info._level);
			writeInt(info._classId);
			writeShort(0);
		}
	}
}