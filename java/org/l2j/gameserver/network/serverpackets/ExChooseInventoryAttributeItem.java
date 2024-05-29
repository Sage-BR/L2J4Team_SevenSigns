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

import java.util.HashSet;
import java.util.Set;

import org.l2j.gameserver.data.xml.ElementalAttributeData;
import org.l2j.gameserver.enums.AttributeType;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author Kerberos
 */
public class ExChooseInventoryAttributeItem extends ServerPacket
{
	private final int _itemId;
	private final long _count;
	private final AttributeType _atribute;
	private final int _level;
	private final Set<Integer> _items = new HashSet<>();
	
	public ExChooseInventoryAttributeItem(Player player, Item stone)
	{
		_itemId = stone.getDisplayId();
		_count = stone.getCount();
		_atribute = ElementalAttributeData.getInstance().getItemElement(_itemId);
		if (_atribute == AttributeType.NONE)
		{
			throw new IllegalArgumentException("Undefined Atribute item: " + stone);
		}
		_level = ElementalAttributeData.getInstance().getMaxElementLevel(_itemId);
		// Register only items that can be put an attribute stone/crystal
		for (Item item : player.getInventory().getItems())
		{
			if (item.isElementable())
			{
				_items.add(item.getObjectId());
			}
		}
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_CHOOSE_INVENTORY_ATTRIBUTE_ITEM.writeId(this);
		writeInt(_itemId);
		writeLong(_count);
		writeInt(_atribute == AttributeType.FIRE); // Fire
		writeInt(_atribute == AttributeType.WATER); // Water
		writeInt(_atribute == AttributeType.WIND); // Wind
		writeInt(_atribute == AttributeType.EARTH); // Earth
		writeInt(_atribute == AttributeType.HOLY); // Holy
		writeInt(_atribute == AttributeType.DARK); // Unholy
		writeInt(_level); // Item max attribute level
		writeInt(_items.size());
		_items.forEach(this::writeInt);
	}
}
