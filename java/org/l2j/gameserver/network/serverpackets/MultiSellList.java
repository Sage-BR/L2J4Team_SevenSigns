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

import static org.l2j.gameserver.data.xml.MultisellData.PAGE_SIZE;

import org.l2j.gameserver.data.xml.ItemData;
import org.l2j.gameserver.model.ItemInfo;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.ItemChanceHolder;
import org.l2j.gameserver.model.holders.MultisellEntryHolder;
import org.l2j.gameserver.model.holders.PreparedMultisellListHolder;
import org.l2j.gameserver.model.item.ItemTemplate;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.network.ServerPackets;

public class MultiSellList extends AbstractItemPacket
{
	private final Player _player;
	private int _size;
	private int _index;
	private final PreparedMultisellListHolder _list;
	private final boolean _finished;
	private final int _type;
	
	public MultiSellList(Player player, PreparedMultisellListHolder list, int index, int type)
	{
		_player = player;
		_list = list;
		_index = index;
		_size = list.getEntries().size() - index;
		if (_size > PAGE_SIZE)
		{
			_finished = false;
			_size = PAGE_SIZE;
		}
		else
		{
			_finished = true;
		}
		_type = type;
	}
	
	public MultiSellList(Player player, PreparedMultisellListHolder list, int index)
	{
		this(player, list, index, 0);
	}
	
	@Override
	public void write()
	{
		ServerPackets.MULTI_SELL_LIST.writeId(this);
		writeByte(0); // Helios
		writeInt(_list.getId()); // list id
		writeByte(_type); // 196?
		writeInt(1 + (_index / PAGE_SIZE)); // page started from 1
		writeInt(_finished); // finished
		writeInt(PAGE_SIZE); // size of pages
		writeInt(_size); // list length
		writeByte(0); // Grand Crusade
		writeByte(_list.isChanceMultisell()); // new multisell window
		writeInt(32); // Helios - Always 32
		while (_size-- > 0)
		{
			ItemInfo itemEnchantment = _list.getItemEnchantment(_index);
			final MultisellEntryHolder entry = _list.getEntries().get(_index++);
			if ((itemEnchantment == null) && _list.isMaintainEnchantment())
			{
				SEARCH: for (ItemChanceHolder holder : entry.getIngredients())
				{
					final Item item = _player.getInventory().getItemByItemId(holder.getId());
					if ((item != null) && item.isEquipable())
					{
						itemEnchantment = new ItemInfo(item);
						break SEARCH;
					}
				}
			}
			writeInt(_index); // Entry ID. Start from 1.
			writeByte(entry.isStackable());
			// Those values will be passed down to MultiSellChoose packet.
			writeShort(itemEnchantment != null ? itemEnchantment.getEnchantLevel() : 0); // enchant level
			writeItemAugment(itemEnchantment);
			writeItemElemental(itemEnchantment);
			writeItemEnsoulOptions(itemEnchantment);
			writeByte(0); // 286
			writeShort(entry.getProducts().size());
			writeShort(entry.getIngredients().size());
			for (ItemChanceHolder product : entry.getProducts())
			{
				final ItemTemplate template = ItemData.getInstance().getTemplate(product.getId());
				final ItemInfo displayItemEnchantment = _list.isMaintainEnchantment() && (itemEnchantment != null) && (template != null) && template.getClass().equals(itemEnchantment.getItem().getClass()) ? itemEnchantment : null;
				if (template != null)
				{
					writeInt(template.getDisplayId());
					writeLong(template.getBodyPart());
					writeShort(template.getType2());
				}
				else
				{
					writeInt(product.getId());
					writeLong(0);
					writeShort(65535);
				}
				writeLong(_list.getProductCount(product));
				writeShort(product.getEnchantmentLevel() > 0 ? product.getEnchantmentLevel() : displayItemEnchantment != null ? displayItemEnchantment.getEnchantLevel() : 0); // enchant level
				writeInt((int) (product.getChance() * 1000000)); // chance
				writeItemAugment(displayItemEnchantment);
				writeItemElemental(displayItemEnchantment);
				writeItemEnsoulOptions(displayItemEnchantment);
				writeByte(0); // 286
			}
			for (ItemChanceHolder ingredient : entry.getIngredients())
			{
				final ItemTemplate template = ItemData.getInstance().getTemplate(ingredient.getId());
				final ItemInfo displayItemEnchantment = (itemEnchantment != null) && (template != null) && template.getClass().equals(itemEnchantment.getItem().getClass()) ? itemEnchantment : null;
				if (template != null)
				{
					writeInt(template.getDisplayId());
					writeShort(template.getType2());
				}
				else
				{
					writeInt(ingredient.getId());
					writeShort(65535);
				}
				writeLong(_list.getIngredientCount(ingredient));
				writeShort(ingredient.getEnchantmentLevel() > 0 ? ingredient.getEnchantmentLevel() : displayItemEnchantment != null ? displayItemEnchantment.getEnchantLevel() : 0); // enchant level
				writeItemAugment(displayItemEnchantment);
				writeItemElemental(displayItemEnchantment);
				writeItemEnsoulOptions(displayItemEnchantment);
				writeByte(0); // 286
			}
		}
	}
}