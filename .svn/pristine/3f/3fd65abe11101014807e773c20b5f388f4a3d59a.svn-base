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

import org.l2jmobius.gameserver.instancemanager.PrivateStoreHistoryManager;
import org.l2jmobius.gameserver.model.ItemInfo;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.AbstractItemPacket;

/**
 * @author Mobius
 */
public class RequestPrivateStoreSearchStatistics extends AbstractItemPacket
{
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_PRIVATE_STORE_SEARCH_STATISTICS.writeId(this, buffer);
		
		final List<PrivateStoreHistoryManager.ItemHistoryTransaction> mostItems = PrivateStoreHistoryManager.getInstance().getTopMostItem();
		final List<PrivateStoreHistoryManager.ItemHistoryTransaction> highestItems = PrivateStoreHistoryManager.getInstance().getTopHighestItem();
		
		buffer.writeInt(Math.min(mostItems.size(), 5));
		for (int i = 0; i < Math.min(mostItems.size(), 5); i++)
		{
			buffer.writeInt((int) mostItems.get(i).getCount());
			ItemInfo itemInfo = new ItemInfo(new Item(mostItems.get(i).getItemId()));
			buffer.writeInt(calculatePacketSize(itemInfo /* , mostItems.get(i).getCount() */));
			writeItem(itemInfo, mostItems.get(i).getCount(), buffer);
		}
		
		buffer.writeInt(Math.min(highestItems.size(), 5));
		for (int i = 0; i < Math.min(highestItems.size(), 5); i++)
		{
			buffer.writeLong(highestItems.get(i).getPrice());
			ItemInfo itemInfo = new ItemInfo(new Item(highestItems.get(i).getItemId()));
			buffer.writeInt(calculatePacketSize(itemInfo /* , highestItems.get(i).getCount() */));
			writeItem(itemInfo, highestItems.get(i).getCount(), buffer);
		}
	}
}
