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
import org.l2j.gameserver.model.buylist.Product;
import org.l2j.gameserver.model.buylist.ProductList;
import org.l2j.gameserver.network.ServerPackets;

public class BuyList extends AbstractItemPacket
{
	private final int _listId;
	private final Collection<Product> _list;
	private final long _money;
	private final int _inventorySlots;
	private final double _castleTaxRate;
	
	public BuyList(ProductList list, Player player, double castleTaxRate)
	{
		_listId = list.getListId();
		_list = list.getProducts();
		_money = player.getAdena();
		_inventorySlots = player.getInventory().getNonQuestSize();
		_castleTaxRate = castleTaxRate;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_BUY_SELL_LIST.writeId(this);
		writeInt(0); // Type BUY
		writeLong(_money); // current money
		writeInt(_listId);
		writeInt(_inventorySlots);
		writeShort(_list.size());
		for (Product product : _list)
		{
			if ((product.getCount() > 0) || !product.hasLimitedStock())
			{
				writeItem(product);
				writeLong((long) (product.getPrice() * (1.0 + _castleTaxRate + product.getBaseTaxRate())));
			}
		}
	}
}
