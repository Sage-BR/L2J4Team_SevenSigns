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
package org.l2j.gameserver.network.serverpackets.commission;

import java.util.Collection;

import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.AbstractItemPacket;

/**
 * @author NosBit
 */
public class ExResponseCommissionItemList extends AbstractItemPacket
{
	private final int _sendType;
	private final Collection<Item> _items;
	
	public ExResponseCommissionItemList(int sendType, Collection<Item> items)
	{
		_sendType = sendType;
		_items = items;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_RESPONSE_COMMISSION_ITEM_LIST.writeId(this);
		writeByte(_sendType);
		if (_sendType == 2)
		{
			writeInt(_items.size());
			writeInt(_items.size());
			for (Item item : _items)
			{
				writeItem(item);
			}
		}
		else
		{
			writeInt(0);
			writeInt(0);
		}
	}
}
