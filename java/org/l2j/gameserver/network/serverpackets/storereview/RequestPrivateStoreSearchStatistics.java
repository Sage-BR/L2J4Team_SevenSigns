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

import org.l2j.gameserver.instancemanager.PrivateStoreHistoryManager;
import org.l2j.gameserver.model.ItemInfo;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.AbstractItemPacket;

/**
 * @author Mobius
 */
public class RequestPrivateStoreSearchStatistics extends AbstractItemPacket
{
	@Override
	public void write()
	{
		ServerPackets.EX_PRIVATE_STORE_SEARCH_STATISTICS.writeId(this);
		
		final List<PrivateStoreHistoryManager.ItemHistoryTransaction> mostItems = PrivateStoreHistoryManager.getInstance().getTopMostItem();
		final List<PrivateStoreHistoryManager.ItemHistoryTransaction> highestItems = PrivateStoreHistoryManager.getInstance().getTopHighestItem();
		
		writeInt(Math.min(mostItems.size(), 5));
		for (int i = 0; i < Math.min(mostItems.size(), 5); i++)
		{
			writeInt((int) mostItems.get(i).getCount());
			ItemInfo itemInfo = new ItemInfo(new Item(mostItems.get(i).getItemId()));
			writeInt(calculatePacketSize(itemInfo /* , mostItems.get(i).getCount() */));
			writeItem(itemInfo, mostItems.get(i).getCount());
		}
		
		writeInt(Math.min(highestItems.size(), 5));
		for (int i = 0; i < Math.min(highestItems.size(), 5); i++)
		{
			writeLong(highestItems.get(i).getPrice());
			ItemInfo itemInfo = new ItemInfo(new Item(highestItems.get(i).getItemId()));
			writeInt(calculatePacketSize(itemInfo /* , highestItems.get(i).getCount() */));
			writeItem(itemInfo, highestItems.get(i).getCount());
		}
	}
}
