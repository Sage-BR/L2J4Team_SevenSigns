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
package org.l2jmobius.gameserver.network.serverpackets.attributechange;

import java.util.List;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.model.ItemInfo;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.AbstractItemPacket;

/**
 * @author Mobius
 */
public class ExChangeAttributeItemList extends AbstractItemPacket
{
	private final int _type;
	private final int _itemId;
	private final List<ItemInfo> _itemsList;
	
	public ExChangeAttributeItemList(int type, int itemId, List<ItemInfo> itemList)
	{
		_type = type;
		_itemId = itemId;
		_itemsList = itemList;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_CHANGE_ATTRIBUTE_ITEM_LIST.writeId(this, buffer);
		buffer.writeByte(_type);
		buffer.writeInt(_itemId);
		buffer.writeInt(_itemsList.size());
		for (ItemInfo item : _itemsList)
		{
			writeItem(item, buffer);
		}
	}
}
