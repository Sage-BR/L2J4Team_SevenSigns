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
package org.l2j.gameserver.network.serverpackets.storereview;

import java.util.List;

import org.l2j.gameserver.enums.PrivateStoreType;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.clientpackets.storereview.ExRequestPrivateStoreSearchList;
import org.l2j.gameserver.network.clientpackets.storereview.ExRequestPrivateStoreSearchList.ShopItem;
import org.l2j.gameserver.network.serverpackets.AbstractItemPacket;

/**
 * @author Mobius
 */
public class ExPrivateStoreSearchItem extends AbstractItemPacket
{
	private final int _page;
	private final int _maxPage;
	private final int _nSize;
	private final List<ShopItem> _items;
	
	public ExPrivateStoreSearchItem(int page, int maxPage, int nSize, List<ShopItem> items)
	{
		_page = page;
		_maxPage = maxPage;
		_nSize = nSize;
		_items = items;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_PRIVATE_STORE_SEARCH_ITEM.writeId(this);
		writeByte(_page); // cPage
		writeByte(_maxPage); // cMaxPage
		writeInt(_nSize); // nSize
		if (_nSize > 0)
		{
			for (int itemIndex = (_page - 1) * ExRequestPrivateStoreSearchList.MAX_ITEM_PER_PAGE; (itemIndex < (_page * ExRequestPrivateStoreSearchList.MAX_ITEM_PER_PAGE)) && (itemIndex < _items.size()); itemIndex++)
			{
				final ShopItem shopItem = _items.get(itemIndex);
				writeSizedString(shopItem.getOwner().getName()); // Vendor name
				writeInt(shopItem.getOwner().getObjectId());
				writeByte(shopItem.getStoreType() == PrivateStoreType.PACKAGE_SELL ? 2 : shopItem.getStoreType() == PrivateStoreType.SELL ? 0 : 1); // store type (maybe "sold"/buy/Package (translated as Total Score...))
				writeLong(shopItem.getPrice()); // Price
				writeInt(shopItem.getOwner().getX()); // X
				writeInt(shopItem.getOwner().getY()); // Y
				writeInt(shopItem.getOwner().getZ()); // Z
				writeInt(calculatePacketSize(shopItem.getItemInfo() /* , shopItem.getCount() */)); // size
				writeItem(shopItem.getItemInfo(), shopItem.getCount()); // itemAssemble
			}
		}
	}
}