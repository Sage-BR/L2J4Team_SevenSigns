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
package org.l2jmobius.gameserver.model.item.enchant;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.l2jmobius.gameserver.model.holders.ItemChanceHolder;
import org.l2jmobius.gameserver.model.item.type.CrystalType;

/**
 * @author Index
 */
public class RewardItemsOnFailure
{
	private final Map<CrystalType, Map<Integer, ItemChanceHolder>> _rewards = new EnumMap<>(CrystalType.class);
	private int _minEnchantLevel = Integer.MAX_VALUE;
	private int _maxEnchantLevel = Integer.MIN_VALUE;
	
	public RewardItemsOnFailure()
	{
	}
	
	public void addItemToHolder(int itemId, CrystalType grade, int enchantLevel, long count, double chance)
	{
		final ItemChanceHolder item = new ItemChanceHolder(itemId, chance, count);
		_rewards.computeIfAbsent(grade, v -> new HashMap<>()).put(enchantLevel, item);
		_minEnchantLevel = Math.min(_minEnchantLevel, enchantLevel);
		_maxEnchantLevel = Math.max(_maxEnchantLevel, enchantLevel);
	}
	
	public ItemChanceHolder getRewardItem(CrystalType grade, int enchantLevel)
	{
		return _rewards.getOrDefault(grade, new HashMap<>()).getOrDefault(enchantLevel, null);
	}
	
	public boolean checkIfRewardUnavailable(CrystalType grade, int enchantLevel)
	{
		// reversed available
		if (_minEnchantLevel > enchantLevel)
		{
			return true;
		}
		
		if (_maxEnchantLevel < enchantLevel)
		{
			return true;
		}
		
		if (!_rewards.containsKey(grade))
		{
			return true;
		}
		
		return !_rewards.get(grade).containsKey(enchantLevel);
	}
	
	public int size()
	{
		int count = 0;
		for (Map<Integer, ItemChanceHolder> rewards : _rewards.values())
		{
			count += rewards.size();
		}
		return count;
	}
}
