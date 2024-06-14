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
package org.l2jmobius.gameserver.network.serverpackets.storereview;

import java.util.List;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.enums.PrivateStoreType;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.clientpackets.storereview.ExRequestPrivateStoreSearchList;
import org.l2jmobius.gameserver.network.clientpackets.storereview.ExRequestPrivateStoreSearchList.ShopItem;
import org.l2jmobius.gameserver.network.serverpackets.AbstractItemPacket;

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
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_PRIVATE_STORE_SEARCH_ITEM.writeId(this, buffer);
		buffer.writeByte(_page); // cPage
		buffer.writeByte(_maxPage); // cMaxPage
		buffer.writeInt(_nSize); // nSize
		if (_nSize > 0)
		{
			for (int itemIndex = (_page - 1) * ExRequestPrivateStoreSearchList.MAX_ITEM_PER_PAGE; (itemIndex < (_page * ExRequestPrivateStoreSearchList.MAX_ITEM_PER_PAGE)) && (itemIndex < _items.size()); itemIndex++)
			{
				final ShopItem shopItem = _items.get(itemIndex);
				buffer.writeSizedString(shopItem.getOwner().getName()); // Vendor name
				buffer.writeInt(shopItem.getOwner().getObjectId());
				buffer.writeByte(shopItem.getStoreType() == PrivateStoreType.PACKAGE_SELL ? 2 : shopItem.getStoreType() == PrivateStoreType.SELL ? 0 : 1); // store type (maybe "sold"/buy/Package (translated as Total Score...))
				buffer.writeLong(shopItem.getPrice()); // Price
				buffer.writeInt(shopItem.getOwner().getX()); // X
				buffer.writeInt(shopItem.getOwner().getY()); // Y
				buffer.writeInt(shopItem.getOwner().getZ()); // Z
				buffer.writeInt(calculatePacketSize(shopItem.getItemInfo() /* , shopItem.getCount() */)); // size
				writeItem(shopItem.getItemInfo(), shopItem.getCount(), buffer); // itemAssemble
			}
		}
	}
}