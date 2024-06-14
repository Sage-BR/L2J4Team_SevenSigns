/*
 * This file is part of the L2J Mobius project.
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
package org.l2jmobius.gameserver.network.serverpackets;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;

public class PvpBookList extends ServerPacket
{
	public PvpBookList()
	{
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_PVPBOOK_LIST.writeId(this, buffer);
		final int size = 1;
		buffer.writeInt(4); // show killer's location count
		buffer.writeInt(5); // teleport count
		buffer.writeInt(size); // killer count
		for (int i = 0; i < size; i++)
		{
			buffer.writeSizedString("killer" + i); // killer name
			buffer.writeSizedString("clanKiller" + i); // killer clan name
			buffer.writeInt(15); // killer level
			buffer.writeInt(2); // killer race
			buffer.writeInt(10); // killer class
			buffer.writeInt((int) LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()); // kill time
			buffer.writeByte(1); // is online
		}
	}
}
