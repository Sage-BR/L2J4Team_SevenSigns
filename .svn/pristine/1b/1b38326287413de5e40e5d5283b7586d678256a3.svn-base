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
package org.l2jmobius.gameserver.network.serverpackets.randomcraft;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Mode
 */
public class ExCraftRandomMake extends ServerPacket
{
	private final int _itemId;
	private final long _itemCount;
	
	public ExCraftRandomMake(int itemId, long itemCount)
	{
		_itemId = itemId;
		_itemCount = itemCount;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_CRAFT_RANDOM_MAKE.writeId(this, buffer);
		buffer.writeByte(0); // Close window
		buffer.writeShort(0x0F); // Unknown
		buffer.writeInt(_itemId);
		buffer.writeLong(_itemCount);
		buffer.writeByte(0); // Enchantment level
	}
}
