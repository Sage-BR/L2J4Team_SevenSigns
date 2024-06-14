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
package org.l2jmobius.gameserver.model.item.combination;

import java.util.EnumMap;
import java.util.Map;

import org.l2jmobius.gameserver.model.StatSet;

/**
 * @author UnAfraid, Mobius
 */
public class CombinationItem
{
	private final int _itemOne;
	private final int _enchantOne;
	private final int _itemTwo;
	private final int _enchantTwo;
	private final long _commission;
	private final float _chance;
	private final boolean _announce;
	private final Map<CombinationItemType, CombinationItemReward> _rewards = new EnumMap<>(CombinationItemType.class);
	
	public CombinationItem(StatSet set)
	{
		_itemOne = set.getInt("one");
		_enchantOne = set.getInt("enchantOne", 0);
		_itemTwo = set.getInt("two");
		_enchantTwo = set.getInt("enchantTwo", 0);
		_commission = set.getLong("commission", 0);
		_chance = set.getFloat("chance", 33);
		_announce = set.getBoolean("announce", false);
	}
	
	public int getItemOne()
	{
		return _itemOne;
	}
	
	public int getEnchantOne()
	{
		return _enchantOne;
	}
	
	public int getItemTwo()
	{
		return _itemTwo;
	}
	
	public int getEnchantTwo()
	{
		return _enchantTwo;
	}
	
	public long getCommission()
	{
		return _commission;
	}
	
	public float getChance()
	{
		return _chance;
	}
	
	public boolean isAnnounce()
	{
		return _announce;
	}
	
	public void addReward(CombinationItemReward item)
	{
		_rewards.put(item.getType(), item);
	}
	
	public CombinationItemReward getReward(CombinationItemType type)
	{
		return _rewards.get(type);
	}
}
