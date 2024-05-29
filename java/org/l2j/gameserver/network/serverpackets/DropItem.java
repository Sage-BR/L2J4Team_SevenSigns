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

import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.network.ServerPackets;

public class DropItem extends ServerPacket
{
	private final Item _item;
	private final int _objectId;
	
	/**
	 * Constructor of the DropItem server packet
	 * @param item : Item designating the item
	 * @param playerObjId : int designating the player ID who dropped the item
	 */
	public DropItem(Item item, int playerObjId)
	{
		_item = item;
		_objectId = playerObjId;
	}
	
	@Override
	public void write()
	{
		ServerPackets.DROP_ITEM.writeId(this);
		writeInt(_objectId);
		writeInt(_item.getObjectId());
		writeInt(_item.getDisplayId());
		writeInt(_item.getX());
		writeInt(_item.getY());
		writeInt(_item.getZ());
		// only show item count if it is a stackable item
		writeByte(_item.isStackable());
		writeLong(_item.getCount());
		writeInt(0);
		writeByte(_item.getEnchantLevel() > 0);
		writeInt(0);
		writeByte(_item.getEnchantLevel()); // Grand Crusade
		writeByte(_item.getAugmentation() != null); // Grand Crusade
		writeByte(_item.getSpecialAbilities().size()); // Grand Crusade
		writeByte(0);
	}
}
