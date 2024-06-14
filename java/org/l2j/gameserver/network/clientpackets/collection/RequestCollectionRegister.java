/*
 * This file is part of the L2J 4Team Project.
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
package org.l2j.gameserver.network.clientpackets.collection;

import org.l2j.gameserver.data.xml.CollectionData;
import org.l2j.gameserver.data.xml.OptionData;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.CollectionDataHolder;
import org.l2j.gameserver.model.holders.ItemEnchantHolder;
import org.l2j.gameserver.model.holders.PlayerCollectionData;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.options.Options;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.ConfirmDlg;
import org.l2j.gameserver.network.serverpackets.SystemMessage;
import org.l2j.gameserver.network.serverpackets.collection.ExCollectionComplete;
import org.l2j.gameserver.network.serverpackets.collection.ExCollectionRegister;

/**
 * @author Berezkin Nikolay, Mobius
 */
public class RequestCollectionRegister extends ClientPacket
{
	private int _collectionId;
	private int _index;
	private int _itemObjId;
	
	@Override
	protected void readImpl()
	{
		_collectionId = readShort();
		_index = readInt();
		_itemObjId = readInt();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		final Item item = player.getInventory().getItemByObjectId(_itemObjId);
		if (item == null)
		{
			player.sendMessage("Item not found.");
			return;
		}
		
		final CollectionDataHolder collection = CollectionData.getInstance().getCollection(_collectionId);
		if (collection == null)
		{
			player.sendMessage("Could not find collection.");
			return;
		}
		
		long count = 0;
		for (ItemEnchantHolder data : collection.getItems())
		{
			if ((data.getId() == item.getId()) && ((data.getEnchantLevel() == 0) || (data.getEnchantLevel() == item.getEnchantLevel())))
			{
				count = data.getCount();
				break;
			}
		}
		if ((count == 0) || (item.getCount() < count) || item.isEquipped())
		{
			player.sendMessage("Incorrect item count.");
			return;
		}
		
		PlayerCollectionData currentColl = null;
		for (PlayerCollectionData coll : player.getCollections())
		{
			if (coll.getCollectionId() == _collectionId)
			{
				currentColl = coll;
				break;
			}
		}
		
		if ((currentColl != null) && (currentColl.getIndex() == _index))
		{
			player.sendPacket(new ExCollectionRegister(false, _collectionId, _index, new ItemEnchantHolder(item.getId(), count, item.getEnchantLevel())));
			player.sendPacket(SystemMessageId.THIS_ITEM_CANNOT_BE_ADDED_TO_YOUR_COLLECTION);
			player.sendPacket(new ConfirmDlg("Collection already registered;"));
			return;
		}
		
		player.destroyItem("Collection", item, count, player, true);
		
		player.sendPacket(new ExCollectionRegister(true, _collectionId, _index, new ItemEnchantHolder(item.getId(), count, item.getEnchantLevel())));
		
		player.getCollections().add(new PlayerCollectionData(_collectionId, item.getId(), _index));
		
		int completeCount = 0;
		for (PlayerCollectionData coll : player.getCollections())
		{
			if (coll.getCollectionId() == _collectionId)
			{
				completeCount++;
			}
		}
		if (completeCount == collection.getCompleteCount())
		{
			player.sendPacket(new ExCollectionComplete(_collectionId));
			
			// TODO: CollectionData.getInstance().getCollection(_collectionId).getName()
			player.sendPacket(new SystemMessage(SystemMessageId.S1_COLLECTION_IS_COMPLETED).addString(""));
			
			// Apply collection option if all requirements are met.
			final Options options = OptionData.getInstance().getOptions(collection.getOptionId());
			if (options != null)
			{
				options.apply(player);
			}
		}
	}
}
