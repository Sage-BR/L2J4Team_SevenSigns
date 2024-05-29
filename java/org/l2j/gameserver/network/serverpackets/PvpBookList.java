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

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.l2j.gameserver.network.ServerPackets;

public class PvpBookList extends ServerPacket
{
	public PvpBookList()
	{
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_PVPBOOK_LIST.writeId(this);
		final int size = 1;
		writeInt(4); // show killer's location count
		writeInt(5); // teleport count
		writeInt(size); // killer count
		for (int i = 0; i < size; i++)
		{
			writeSizedString("killer" + i); // killer name
			writeSizedString("clanKiller" + i); // killer clan name
			writeInt(15); // killer level
			writeInt(2); // killer race
			writeInt(10); // killer class
			writeInt((int) LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()); // kill time
			writeByte(1); // is online
		}
	}
}
