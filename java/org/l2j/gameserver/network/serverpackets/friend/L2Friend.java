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

import org.l2j.gameserver.data.sql.CharInfoTable;
import org.l2j.gameserver.model.World;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * Support for "Chat with Friends" dialog. <br />
 * Add new friend or delete.
 * @author JIV
 */
public class L2Friend extends ServerPacket
{
	private final boolean _action;
	private final boolean _online;
	private final int _objid;
	private final String _name;
	
	/**
	 * @param action - true for adding, false for remove
	 * @param objId
	 */
	public L2Friend(boolean action, int objId)
	{
		_action = action;
		_objid = objId;
		_name = CharInfoTable.getInstance().getNameById(objId);
		_online = World.getInstance().getPlayer(objId) != null;
	}
	
	@Override
	public void write()
	{
		ServerPackets.L2_FRIEND.writeId(this);
		writeInt(_action ? 1 : 3); // 1-add 3-remove
		writeInt(_objid);
		writeString(_name);
		writeInt(_online);
		writeInt(_online ? _objid : 0);
	}
}
