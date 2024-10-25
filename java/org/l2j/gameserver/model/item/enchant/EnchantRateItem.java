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
package org.l2j.gameserver.model.item.enchant;

import java.util.HashSet;
import java.util.Set;

import org.l2j.gameserver.model.item.ItemTemplate;

/**
 * @author UnAfraid, Mobius
 */
public class EnchantRateItem
{
	private final String _name;
	private final Set<Integer> _items = new HashSet<>();
	private long _slot;
	private Boolean _isMagicWeapon = null;
	
	public EnchantRateItem(String name)
	{
		_name = name;
	}
	
	/**
	 * @return name of enchant group.
	 */
	public String getName()
	{
		return _name;
	}
	
	/**
	 * Adds item id verification.
	 * @param id
	 */
	public void addItemId(int id)
	{
		_items.add(id);
	}
	
	/**
	 * Adds body slot verification.
	 * @param slot
	 */
	public void addSlot(long slot)
	{
		_slot |= slot;
	}
	
	/**
	 * Adds magic weapon verification.
	 * @param magicWeapon
	 */
	public void setMagicWeapon(boolean magicWeapon)
	{
		_isMagicWeapon = magicWeapon ? Boolean.TRUE : Boolean.FALSE;
	}
	
	/**
	 * @param item
	 * @return {@code true} if item can be used with this rate group, {@code false} otherwise.
	 */
	public boolean validate(ItemTemplate item)
	{
		if (!_items.isEmpty() && !_items.contains(item.getId()))
		{
			return false;
		}
		else if ((_slot != 0) && ((item.getBodyPart() & _slot) == 0))
		{
			return false;
		}
		else if ((_isMagicWeapon != null) && (item.isMagicWeapon() != _isMagicWeapon.booleanValue()))
		{
			return false;
		}
		return true;
	}
}
