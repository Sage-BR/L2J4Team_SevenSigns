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
package org.l2j.gameserver.network.serverpackets.collection;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.model.holders.ItemEnchantHolder;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Berezkin Nikolay, Index
 */
public class ExCollectionRegister extends ServerPacket
{
	private final int _success;
	private final int _collectionId;
	private final int _index;
	private final ItemEnchantHolder _collectionInfo;
	
	public ExCollectionRegister(boolean success, int collectionId, int index, ItemEnchantHolder collectionInfo)
	{
		_success = success ? 1 : 0;
		_collectionId = collectionId;
		_index = index;
		_collectionInfo = collectionInfo;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_COLLECTION_REGISTER.writeId(this, buffer);
		buffer.writeShort(_collectionId);
		buffer.writeByte(_success); // success
		buffer.writeByte(0); // recursive reward
		buffer.writeShort(249); // 256 - size so far
		buffer.writeByte(_index); // slot index
		buffer.writeInt(_collectionInfo.getId()); // item classId
		buffer.writeShort(_collectionInfo.getEnchantLevel()); // enchant level
		buffer.writeByte(0); // is blessed
		buffer.writeByte(0); // blessed conditions
		buffer.writeInt((int) _collectionInfo.getCount()); // amount
	}
}
