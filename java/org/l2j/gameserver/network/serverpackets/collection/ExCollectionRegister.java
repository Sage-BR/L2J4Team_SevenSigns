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
package org.l2j.gameserver.network.serverpackets.collection;

import org.l2j.gameserver.model.holders.ItemEnchantHolder;
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
	public void write()
	{
		ServerPackets.EX_COLLECTION_REGISTER.writeId(this);
		writeShort(_collectionId);
		writeByte(_success); // success
		writeByte(0); // recursive reward
		writeShort(249); // 256 - size so far
		writeByte(_index); // slot index
		writeInt(_collectionInfo.getId()); // item classId
		writeShort(_collectionInfo.getEnchantLevel()); // enchant level
		writeByte(0); // is blessed
		writeByte(0); // blessed conditions
		writeInt((int) _collectionInfo.getCount()); // amount
	}
}
