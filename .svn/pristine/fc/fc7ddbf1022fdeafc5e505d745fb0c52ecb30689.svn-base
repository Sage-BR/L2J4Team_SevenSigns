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
package org.l2jmobius.gameserver.network.serverpackets.worldexchange;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Index
 */
public class WorldExchangeRegisterItem extends ServerPacket
{
	public static final WorldExchangeRegisterItem FAIL = new WorldExchangeRegisterItem(-1, -1L, (byte) 0);
	
	private final int _itemObjectId;
	private final long _itemAmount;
	private final byte _type;
	
	public WorldExchangeRegisterItem(int itemObjectId, long itemAmount, byte type)
	{
		_itemObjectId = itemObjectId;
		_itemAmount = itemAmount;
		_type = type;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_WORLD_EXCHANGE_REGI_ITEM.writeId(this, buffer);
		buffer.writeInt(_itemObjectId);
		buffer.writeLong(_itemAmount);
		buffer.writeByte(_type);
	}
}
