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
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.network.ServerPackets;

public class GMViewWarehouseWithdrawList extends AbstractItemPacket
{
	private final int _sendType;
	private final Collection<Item> _items;
	private final String _playerName;
	private final long _money;
	
	public GMViewWarehouseWithdrawList(int sendType, Player player)
	{
		_sendType = sendType;
		_items = player.getWarehouse().getItems();
		_playerName = player.getName();
		_money = player.getWarehouse().getAdena();
	}
	
	public GMViewWarehouseWithdrawList(int sendType, Clan clan)
	{
		_sendType = sendType;
		_playerName = clan.getLeaderName();
		_items = clan.getWarehouse().getItems();
		_money = clan.getWarehouse().getAdena();
	}
	
	@Override
	public void write()
	{
		ServerPackets.GM_VIEW_WAREHOUSE_WITHDRAW_LIST.writeId(this);
		writeByte(_sendType);
		if (_sendType == 2)
		{
			writeInt(_items.size());
			writeInt(_items.size());
			for (Item item : _items)
			{
				writeItem(item);
				writeInt(item.getObjectId());
			}
		}
		else
		{
			writeString(_playerName);
			writeLong(_money);
			writeInt(_items.size());
		}
	}
}
