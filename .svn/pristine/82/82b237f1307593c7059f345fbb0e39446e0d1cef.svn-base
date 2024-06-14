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
package org.l2jmobius.gameserver.network.serverpackets.collection;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.data.xml.CollectionData;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.PlayerCollectionData;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Berezkin Nikolay, Mobius
 */
public class ExCollectionInfo extends ServerPacket
{
	final Player _player;
	final int _category;
	final Set<Integer> _collectionIds = new HashSet<>();
	final List<Integer> _favoriteIds;
	
	public ExCollectionInfo(Player player, int category)
	{
		_player = player;
		_category = category;
		for (PlayerCollectionData collection : player.getCollections())
		{
			if (CollectionData.getInstance().getCollection(collection.getCollectionId()).getCategory() == category)
			{
				_collectionIds.add(collection.getCollectionId());
			}
		}
		_favoriteIds = player.getCollectionFavorites();
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_COLLECTION_INFO.writeId(this, buffer);
		buffer.writeInt(_collectionIds.size()); // size
		final List<PlayerCollectionData> currentCollection = new LinkedList<>();
		for (int id : _collectionIds)
		{
			currentCollection.clear();
			for (PlayerCollectionData collection : _player.getCollections())
			{
				if (collection.getCollectionId() == id)
				{
					currentCollection.add(collection);
				}
			}
			
			buffer.writeInt(currentCollection.size());
			for (PlayerCollectionData collection : currentCollection)
			{
				buffer.writeByte(collection.getIndex());
				buffer.writeInt(collection.getItemId());
				buffer.writeByte(CollectionData.getInstance().getCollection(id).getItems().get(collection.getIndex()).getEnchantLevel()); // enchant level
				buffer.writeByte(0); // bless
				buffer.writeByte(0); // bless Condition
				buffer.writeInt(1); // amount
			}
			buffer.writeShort(id);
		}
		
		// favoriteList
		buffer.writeInt(_favoriteIds.size());
		for (int id : _favoriteIds)
		{
			buffer.writeShort(id);
		}
		
		// rewardList
		buffer.writeInt(0);
		
		buffer.writeByte(_category);
		buffer.writeShort(0);
	}
}
