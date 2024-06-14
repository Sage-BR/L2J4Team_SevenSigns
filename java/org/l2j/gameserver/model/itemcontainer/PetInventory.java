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
package org.l2j.gameserver.model.itemcontainer;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.l2j.gameserver.data.xml.ItemData;
import org.l2j.gameserver.enums.ItemLocation;
import org.l2j.gameserver.model.actor.instance.Pet;
import org.l2j.gameserver.model.item.ItemTemplate;
import org.l2j.gameserver.model.item.instance.Item;

public class PetInventory extends Inventory
{
	private final Pet _owner;
	
	public PetInventory(Pet owner)
	{
		_owner = owner;
	}
	
	@Override
	public Pet getOwner()
	{
		return _owner;
	}
	
	@Override
	public int getOwnerId()
	{
		return getOwner() == null ? 0 : _owner.getControlObjectId();
	}
	
	/**
	 * Refresh the weight of equipment loaded
	 */
	@Override
	protected void refreshWeight()
	{
		super.refreshWeight();
		_owner.updateAndBroadcastStatus(1);
	}
	
	@Override
	public Collection<Item> getItems()
	{
		final List<Item> equippedItems = new LinkedList<>();
		for (Item item : super.getItems())
		{
			if (item.isEquipped())
			{
				equippedItems.add(item);
			}
		}
		return equippedItems;
	}
	
	public boolean validateCapacity(Item item)
	{
		int slots = 0;
		if (!(item.isStackable() && (getItemByItemId(item.getId()) != null)) && !item.getTemplate().hasExImmediateEffect())
		{
			slots++;
		}
		return validateCapacity(slots);
	}
	
	@Override
	public boolean validateCapacity(long slots)
	{
		return ((_items.size() + slots) <= _owner.getInventoryLimit());
	}
	
	public boolean validateWeight(Item item, long count)
	{
		int weight = 0;
		final ItemTemplate template = ItemData.getInstance().getTemplate(item.getId());
		if (template == null)
		{
			return false;
		}
		weight += count * template.getWeight();
		return validateWeight(weight);
	}
	
	@Override
	public boolean validateWeight(long weight)
	{
		return ((_totalWeight + weight) <= _owner.getMaxLoad());
	}
	
	@Override
	protected ItemLocation getBaseLocation()
	{
		return ItemLocation.PET;
	}
	
	@Override
	protected ItemLocation getEquipLocation()
	{
		return ItemLocation.PET_EQUIP;
	}
	
	public void transferItemsToOwner()
	{
		for (Item item : _items)
		{
			getOwner().transferItem("return", item.getObjectId(), item.getCount(), getOwner().getOwner().getInventory(), getOwner().getOwner(), getOwner());
		}
	}
}
