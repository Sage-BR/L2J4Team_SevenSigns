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
package org.l2j.gameserver.model.holders;

import org.l2j.gameserver.model.interfaces.IUniqueId;

/**
 * @author Index, Mobius
 */
public class UniqueItemEnchantHolder extends ItemEnchantHolder implements IUniqueId
{
	private final int _objectId;
	
	public UniqueItemEnchantHolder(int id, int objectId)
	{
		this(id, objectId, 1);
	}
	
	public UniqueItemEnchantHolder(int id, int objectId, long count)
	{
		super(id, count);
		_objectId = objectId;
	}
	
	public UniqueItemEnchantHolder(ItemEnchantHolder itemHolder, int objectId)
	{
		super(itemHolder.getId(), itemHolder.getCount(), itemHolder.getEnchantLevel());
		_objectId = objectId;
	}
	
	@Override
	public int getObjectId()
	{
		return _objectId;
	}
	
	@Override
	public String toString()
	{
		return "[" + getClass().getSimpleName() + "] ID: " + getId() + ", object ID: " + _objectId + ", count: " + getCount() + ", enchant level: " + getEnchantLevel();
	}
}
