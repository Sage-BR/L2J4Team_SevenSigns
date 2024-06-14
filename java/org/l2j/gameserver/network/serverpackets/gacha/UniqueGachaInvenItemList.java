/*
 * This file is part of the L2J 4Team Project.
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
package org.l2j.gameserver.network.serverpackets.gacha;

import java.util.List;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

public class UniqueGachaInvenItemList extends ServerPacket
{
	private final int _currPage;
	private final int _maxPages;
	private final List<Item> _items;
	
	public UniqueGachaInvenItemList(int currPage, int maxPages, List<Item> items)
	{
		_currPage = currPage;
		_maxPages = maxPages;
		_items = items;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_UNIQUE_GACHA_INVEN_ITEM_LIST.writeId(this, buffer);
		buffer.writeByte(_currPage);
		buffer.writeByte(_maxPages);
		buffer.writeInt(_items.size());
		for (Item item : _items)
		{
			buffer.writeInt(item.getDisplayId());
			buffer.writeLong(item.getCount());
		}
	}
}
