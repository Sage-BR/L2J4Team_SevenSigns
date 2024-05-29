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

import java.util.Collection;

import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author Migi, DS
 */
public class ExReplyPostItemList extends AbstractItemPacket
{
	private final int _sendType;
	private final Player _player;
	private final Collection<Item> _itemList;
	
	public ExReplyPostItemList(int sendType, Player player)
	{
		_sendType = sendType;
		_player = player;
		_itemList = _player.getInventory().getAvailableItems(true, false, false);
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_REPLY_POST_ITEM_LIST.writeId(this);
		writeByte(_sendType);
		writeInt(_itemList.size());
		if (_sendType == 2)
		{
			writeInt(_itemList.size());
			for (Item item : _itemList)
			{
				writeItem(item);
			}
		}
	}
}
