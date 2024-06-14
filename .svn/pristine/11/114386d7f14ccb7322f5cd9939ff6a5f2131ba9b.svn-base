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
import org.l2jmobius.gameserver.instancemanager.PrivateStoreHistoryManager.ItemHistoryTransaction;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.AbstractItemPacket;

/**
 * @author Mobius
 */
public class ExPrivateStoreSearchHistory extends AbstractItemPacket
{
	private final int _page;
	private final int _maxPage;
	private final List<ItemHistoryTransaction> _history;
	
	public ExPrivateStoreSearchHistory(int page, int maxPage, List<ItemHistoryTransaction> history)
	{
		_page = page;
		_maxPage = maxPage;
		_history = history;
	}
	
	/**
	 * 338 struct _S_EX_PRIVATE_STORE_SEARCH_HISTORY { var int cCurrentPage; var int cMaxPage; var array<_pkPSSearchHistory> histories; }; struct _pkPSSearchHistory { var int nClassID; var int cStoreType; var int cEnchant; var INT64 nPrice; var INT64 nAmount; }; // S: FE D502 01 - cPage 01 -
	 * cMaxPage E6000000 - nSize nClassID cStoreType cEnchant nPrice nAmount 4E000000 00 00 7F96980000000000 0100000000000000 4F000000 00 00 7F96980000000000 0100000000000000 5B000000 00 00 80C3C90100000000 0100000000000000 62000000 00 00 002D310100000000 0100000000000000 6E000000 00 00
	 * 80841E0000000000 0100000000000000 C6000000 00 00 FF117A0000000000 0100000000000000
	 */
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_PRIVATE_STORE_SEARCH_HISTORY.writeId(this, buffer);
		
		buffer.writeByte(_page); // cPage
		buffer.writeByte(_maxPage); // cMaxPage
		
		buffer.writeInt(_history.size()); // nSize -> Items count for loop below
		
		for (int i = 0; i < _history.size(); i++)
		{
			final ItemHistoryTransaction transaction = _history.get(i);
			buffer.writeInt(transaction.getItemId()); // itemId
			buffer.writeByte(transaction.getTransactionType() == PrivateStoreType.SELL ? 0x00 : 0x01); // cStoreType
			buffer.writeByte(transaction.getEnchantLevel()); // cEnchant
			buffer.writeLong(transaction.getPrice() / transaction.getCount()); // nPrice
			buffer.writeLong(transaction.getCount()); // nAmount
		}
	}
}