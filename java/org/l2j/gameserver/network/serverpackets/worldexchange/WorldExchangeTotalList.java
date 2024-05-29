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
package org.l2j.gameserver.network.serverpackets.worldexchange;

import java.util.Collection;

import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

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
	public void write()
	{
		ServerPackets.EX_WORLD_EXCHANGE_TOTAL_LIST.writeId(this);
		writeInt(_itemIds.size());
		for (int id : _itemIds)
		{
			writeInt(id); // ItemClassID
			writeLong(0); // MinPricePerPiece
			writeLong(0); // Price
			writeLong(1); // Amount
		}
	}
}
