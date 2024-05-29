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

import org.l2j.gameserver.model.TradeItem;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.ServerPackets;

public class PrivateStoreManageListSell extends AbstractItemPacket
{
	private final int _sendType;
	private final int _objId;
	private final long _playerAdena;
	private final boolean _packageSale;
	private final Collection<TradeItem> _itemList;
	private final Collection<TradeItem> _sellList;
	
	public PrivateStoreManageListSell(int sendType, Player player, boolean isPackageSale)
	{
		_sendType = sendType;
		_objId = player.getObjectId();
		_playerAdena = player.getAdena();
		player.getSellList().updateItems();
		_packageSale = isPackageSale;
		_itemList = player.getInventory().getAvailableItems(player.getSellList());
		_sellList = player.getSellList().getItems();
	}
	
	@Override
	public void write()
	{
		ServerPackets.PRIVATE_STORE_MANAGE_LIST.writeId(this);
		writeByte(_sendType);
		if (_sendType == 2)
		{
			writeInt(_itemList.size());
			writeInt(_itemList.size());
			for (TradeItem item : _itemList)
			{
				writeItem(item);
				writeLong(item.getItem().getReferencePrice() * 2);
			}
		}
		else
		{
			writeInt(_objId);
			writeInt(_packageSale);
			writeLong(_playerAdena);
			writeInt(0);
			for (TradeItem item : _itemList)
			{
				writeItem(item);
				writeLong(item.getItem().getReferencePrice() * 2);
			}
			writeInt(0);
			for (TradeItem item2 : _sellList)
			{
				writeItem(item2);
				writeLong(item2.getPrice());
				writeLong(item2.getItem().getReferencePrice() * 2);
			}
		}
	}
}
