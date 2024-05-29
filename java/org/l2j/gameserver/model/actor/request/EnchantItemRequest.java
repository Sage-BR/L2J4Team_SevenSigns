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
package org.l2j.gameserver.model.actor.request;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.ItemHolder;
import org.l2j.gameserver.model.item.instance.Item;

/**
 * @author UnAfraid
 */
public class EnchantItemRequest extends AbstractRequest
{
	private volatile int _enchantingItemObjectId;
	private volatile int _enchantingScrollObjectId;
	private volatile int _supportItemObjectId;
	private volatile int _enchantingItemCurrentEnchantLevel;
	private final Map<Integer, Integer> _multiEnchantingItems = new ConcurrentHashMap<>();
	private final Map<Integer, ItemHolder> _multiFailRewardItems = new ConcurrentHashMap<>();
	private final Map<Integer, int[]> _successEnchant = new ConcurrentHashMap<>();
	private final Map<Integer, Integer> _failureEnchant = new ConcurrentHashMap<>();
	
	public EnchantItemRequest(Player player, int enchantingScrollObjectId)
	{
		super(player);
		_enchantingScrollObjectId = enchantingScrollObjectId;
	}
	
	public void setMultiSuccessEnchantList(Map<Integer, int[]> list)
	{
		_successEnchant.putAll(list);
	}
	
	public void setMultiFailureEnchantList(Map<Integer, Integer> list)
	{
		_failureEnchant.putAll(list);
	}
	
	public void clearMultiSuccessEnchantList()
	{
		_successEnchant.clear();
	}
	
	public void clearMultiFailureEnchantList()
	{
		_failureEnchant.clear();
	}
	
	public Map<Integer, int[]> getMultiSuccessEnchantList()
	{
		return _successEnchant;
	}
	
	public Map<Integer, Integer> getMultiFailureEnchantList()
	{
		return _failureEnchant;
	}
	
	public Item getEnchantingItem()
	{
		return getActiveChar().getInventory().getItemByObjectId(_enchantingItemObjectId);
	}
	
	public void setEnchantingItem(int objectId)
	{
		_enchantingItemObjectId = objectId;
	}
	
	public Item getEnchantingScroll()
	{
		return getActiveChar().getInventory().getItemByObjectId(_enchantingScrollObjectId);
	}
	
	public void setEnchantingScroll(int objectId)
	{
		_enchantingScrollObjectId = objectId;
	}
	
	public Item getSupportItem()
	{
		return getActiveChar().getInventory().getItemByObjectId(_supportItemObjectId);
	}
	
	public void setSupportItem(int objectId)
	{
		_supportItemObjectId = objectId;
	}
	
	public void setEnchantLevel(int enchantLevel)
	{
		_enchantingItemCurrentEnchantLevel = enchantLevel;
	}
	
	public int getEnchantLevel()
	{
		return _enchantingItemCurrentEnchantLevel;
	}
	
	public void addMultiEnchantingItems(int slot, int objectId)
	{
		_multiEnchantingItems.put(slot, objectId);
	}
	
	public int getMultiEnchantingItemsBySlot(int slot)
	{
		return _multiEnchantingItems.getOrDefault(slot, -1);
	}
	
	public void changeMultiEnchantingItemsBySlot(int slot, int objectId)
	{
		_multiEnchantingItems.replace(slot, objectId);
	}
	
	public boolean checkMultiEnchantingItemsByObjectId(int objectId)
	{
		return _multiEnchantingItems.containsValue(objectId);
	}
	
	public int getMultiEnchantingItemsCount()
	{
		return _multiEnchantingItems.size();
	}
	
	public void clearMultiEnchantingItemsBySlot()
	{
		_multiEnchantingItems.clear();
	}
	
	public String getMultiEnchantingItemsLits()
	{
		return _multiEnchantingItems.toString();
	}
	
	public void addMultiEnchantFailItems(ItemHolder itemHolder)
	{
		_multiFailRewardItems.put(getMultiFailItemsCount() + 1, itemHolder);
	}
	
	public int getMultiFailItemsCount()
	{
		return _multiFailRewardItems.size();
	}
	
	public void clearMultiFailReward()
	{
		_multiFailRewardItems.clear();
	}
	
	public Map<Integer, ItemHolder> getMultiEnchantFailItems()
	{
		return _multiFailRewardItems;
	}
	
	@Override
	public boolean isItemRequest()
	{
		return true;
	}
	
	@Override
	public boolean canWorkWith(AbstractRequest request)
	{
		return !request.isItemRequest();
	}
	
	@Override
	public boolean isUsing(int objectId)
	{
		return (objectId > 0) && ((objectId == _enchantingItemObjectId) || (objectId == _enchantingScrollObjectId) || (objectId == _supportItemObjectId));
	}
}
