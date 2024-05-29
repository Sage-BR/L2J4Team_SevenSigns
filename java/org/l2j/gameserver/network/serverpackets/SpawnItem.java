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

public class SpawnItem extends ServerPacket
{
	private final Item _item;
	
	public SpawnItem(Item item)
	{
		_item = item;
	}
	
	@Override
	public void write()
	{
		ServerPackets.SPAWN_ITEM.writeId(this);
		writeInt(_item.getObjectId());
		writeInt(_item.getDisplayId());
		writeInt(_item.getX());
		writeInt(_item.getY());
		writeInt(_item.getZ());
		// only show item count if it is a stackable item
		writeInt(_item.isStackable());
		writeLong(_item.getCount());
		writeInt(0); // c2
		writeByte(_item.getEnchantLevel()); // Grand Crusade
		writeByte(_item.getAugmentation() != null); // Grand Crusade
		writeByte(_item.getSpecialAbilities().size()); // Grand Crusade
		writeByte(0);
	}
}
