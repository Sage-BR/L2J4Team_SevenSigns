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

import org.l2j.gameserver.model.TeleportBookmark;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author ShanSoft
 */
public class ExGetBookMarkInfoPacket extends ServerPacket
{
	private final Player _player;
	
	public ExGetBookMarkInfoPacket(Player player)
	{
		_player = player;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_GET_BOOK_MARK_INFO.writeId(this);
		writeInt(0); // Dummy
		writeInt(_player.getBookMarkSlot());
		writeInt(_player.getTeleportBookmarks().size());
		for (TeleportBookmark tpbm : _player.getTeleportBookmarks())
		{
			writeInt(tpbm.getId());
			writeInt(tpbm.getX());
			writeInt(tpbm.getY());
			writeInt(tpbm.getZ());
			writeString(tpbm.getName());
			writeInt(tpbm.getIcon());
			writeString(tpbm.getTag());
		}
	}
}
