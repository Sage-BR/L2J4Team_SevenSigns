/*
 * This file is part of the L2J Mobius project.
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
package org.l2jmobius.gameserver.model.holders;

import org.l2jmobius.gameserver.enums.WorldExchangeItemStatusType;
import org.l2jmobius.gameserver.enums.WorldExchangeItemSubType;
import org.l2jmobius.gameserver.model.ItemInfo;
import org.l2jmobius.gameserver.model.item.instance.Item;

/**
 * @author Index
 */
public class WorldExchangeHolder
{
	private final long _worldExchangeId;
	private final Item _itemInstance;
	private final ItemInfo _itemInfo;
	private final long _price;
	private final int _oldOwnerId;
	private WorldExchangeItemStatusType _storeType;
	private final WorldExchangeItemSubType _category;
	private final long _startTime;
	private long _endTime;
	private boolean _hasChanges;
	
	public WorldExchangeHolder(long worldExchangeId, Item itemInstance, ItemInfo itemInfo, long price, int oldOwnerId, WorldExchangeItemStatusType storeType, WorldExchangeItemSubType category, long startTime, long endTime, boolean hasChanges)
	{
		_worldExchangeId = worldExchangeId;
		_itemInstance = itemInstance;
		_itemInfo = itemInfo;
		_price = price;
		_oldOwnerId = oldOwnerId;
		_storeType = storeType;
		_category = category;
		_startTime = startTime;
		_endTime = endTime;
		_hasChanges = hasChanges;
	}
	
	public long getWorldExchangeId()
	{
		return _worldExchangeId;
	}
	
	public Item getItemInstance()
	{
		return _itemInstance;
	}
	
	public ItemInfo getItemInfo()
	{
		return _itemInfo;
	}
	
	public long getPrice()
	{
		return _price;
	}
	
	public int getOldOwnerId()
	{
		return _oldOwnerId;
	}
	
	public WorldExchangeItemStatusType getStoreType()
	{
		return _storeType;
	}
	
	public void setStoreType(WorldExchangeItemStatusType storeType)
	{
		_storeType = storeType;
	}
	
	public WorldExchangeItemSubType getCategory()
	{
		return _category;
	}
	
	public long getStartTime()
	{
		return _startTime;
	}
	
	public long getEndTime()
	{
		return _endTime;
	}
	
	public void setEndTime(long endTime)
	{
		_endTime = endTime;
	}
	
	public boolean hasChanges()
	{
		if (_hasChanges) // TODO: Fix logic.
		{
			_hasChanges = false;
			return true;
		}
		return false;
	}
	
	public void setHasChanges(boolean hasChanges)
	{
		_hasChanges = hasChanges;
	}
}
