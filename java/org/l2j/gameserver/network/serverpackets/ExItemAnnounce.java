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

import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author NviX, Mobius
 */
public class ExItemAnnounce extends ServerPacket
{
	public static final int ENCHANT = 0;
	public static final int RANDOM_CRAFT = 2;
	public static final int SPECIAL_CREATION = 3;
	public static final int COMPOUND = 8;
	public static final int UPGRADE = 10;
	
	private final Item _item;
	private final int _type;
	private final String _announceName;
	
	public ExItemAnnounce(Player player, Item item, int type)
	{
		_item = item;
		_type = type;
		if (player.getClientSettings().isAnnounceEnabled())
		{
			_announceName = player.getName();
		}
		else if ("ru".equals(player.getLang()))
		{
			_announceName = "Некто";
			
		}
		else
		{
			_announceName = "Someone";
		}
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_ITEM_ANNOUNCE.writeId(this);
		// _type
		// 0 - enchant
		// 1 - item get from container
		// 2 - item get from random creation
		// 3 - item get from special creation
		// 4 - item get from private workshop
		// 5 - item get from secret shop
		// 6 - item get from limited craft
		// 7 - fire and item get from container
		// 8 - item get from compound
		// 9 - item get from craft system but fancy
		// 10 - item get from upgrade
		// 11 and others - null item name by item_id and icon from chest.
		writeByte(_type); // announce type
		writeSizedString(_announceName); // name of player
		writeInt(_item.getId()); // item id
		writeByte(_item.getEnchantLevel()); // enchant level
		writeInt(0); // chest item id
	}
}