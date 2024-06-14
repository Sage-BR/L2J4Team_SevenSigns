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

import java.util.Collection;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Mobius
 */
public class WorldExchangeTotalList extends ServerPacket
{
	private final Collection<Integer> _itemIds;
	
	public WorldExchangeTotalList(Collection<Integer> itemIds)
	{
		_itemIds = itemIds;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_WORLD_EXCHANGE_TOTAL_LIST.writeId(this, buffer);
		buffer.writeInt(_itemIds.size());
		for (int id : _itemIds)
		{
			buffer.writeInt(id); // ItemClassID
			buffer.writeLong(0); // MinPricePerPiece
			buffer.writeLong(0); // Price
			buffer.writeLong(1); // Amount
		}
	}
}
