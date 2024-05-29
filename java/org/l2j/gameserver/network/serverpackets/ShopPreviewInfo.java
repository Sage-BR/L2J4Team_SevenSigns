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

import java.util.Map;

import org.l2j.gameserver.model.itemcontainer.Inventory;
import org.l2j.gameserver.network.ServerPackets;

/**
 ** @author Gnacik
 */
public class ShopPreviewInfo extends ServerPacket
{
	private final Map<Integer, Integer> _itemlist;
	
	public ShopPreviewInfo(Map<Integer, Integer> itemlist)
	{
		_itemlist = itemlist;
	}
	
	@Override
	public void write()
	{
		ServerPackets.SHOP_PREVIEW_INFO.writeId(this);
		writeInt(Inventory.PAPERDOLL_TOTALSLOTS);
		// Slots
		writeInt(getFromList(Inventory.PAPERDOLL_UNDER));
		writeInt(getFromList(Inventory.PAPERDOLL_REAR));
		writeInt(getFromList(Inventory.PAPERDOLL_LEAR));
		writeInt(getFromList(Inventory.PAPERDOLL_NECK));
		writeInt(getFromList(Inventory.PAPERDOLL_RFINGER));
		writeInt(getFromList(Inventory.PAPERDOLL_LFINGER));
		writeInt(getFromList(Inventory.PAPERDOLL_HEAD));
		writeInt(getFromList(Inventory.PAPERDOLL_RHAND));
		writeInt(getFromList(Inventory.PAPERDOLL_LHAND));
		writeInt(getFromList(Inventory.PAPERDOLL_GLOVES));
		writeInt(getFromList(Inventory.PAPERDOLL_CHEST));
		writeInt(getFromList(Inventory.PAPERDOLL_LEGS));
		writeInt(getFromList(Inventory.PAPERDOLL_FEET));
		writeInt(getFromList(Inventory.PAPERDOLL_CLOAK));
		writeInt(getFromList(Inventory.PAPERDOLL_RHAND));
		writeInt(getFromList(Inventory.PAPERDOLL_HAIR));
		writeInt(getFromList(Inventory.PAPERDOLL_HAIR2));
		writeInt(getFromList(Inventory.PAPERDOLL_RBRACELET));
		writeInt(getFromList(Inventory.PAPERDOLL_LBRACELET));
	}
	
	private int getFromList(int key)
	{
		return (_itemlist.containsKey(key) ? _itemlist.get(key) : 0);
	}
}