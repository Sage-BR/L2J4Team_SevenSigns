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

import java.util.List;

/**
 * @author Berezkin Nikolay
 */
public class CollectionDataHolder
{
	private final int _collectionId;
	private final int _optionId;
	private final int _category;
	private final int _completeCount;
	private final List<ItemEnchantHolder> _items;
	
	public CollectionDataHolder(int collectionId, int optionId, int category, int completeCount, List<ItemEnchantHolder> items)
	{
		_collectionId = collectionId;
		_optionId = optionId;
		_category = category;
		_completeCount = completeCount;
		_items = items;
	}
	
	public int getCollectionId()
	{
		return _collectionId;
	}
	
	public int getOptionId()
	{
		return _optionId;
	}
	
	public int getCategory()
	{
		return _category;
	}
	
	public int getCompleteCount()
	{
		return _completeCount;
	}
	
	public List<ItemEnchantHolder> getItems()
	{
		return _items;
	}
}
