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
package org.l2jmobius.gameserver.model.item.henna;

import java.util.EnumMap;
import java.util.Map;

import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.item.combination.CombinationItemType;

/**
 * @author Index
 */
public class CombinationHenna
{
	private final int _henna;
	private final int _itemOne;
	private final long _countOne;
	private final int _itemTwo;
	private final long _countTwo;
	private final long _commission;
	private final float _chance;
	private final Map<CombinationItemType, CombinationHennaReward> _rewards = new EnumMap<>(CombinationItemType.class);
	
	public CombinationHenna(StatSet set)
	{
		_henna = set.getInt("dyeId");
		_itemOne = set.getInt("itemOne", -1);
		_countOne = set.getLong("countOne", 1);
		_itemTwo = set.getInt("itemTwo", -1);
		_countTwo = set.getLong("countTwo", 1);
		_commission = set.getLong("commission", 0);
		_chance = set.getFloat("chance", 33);
	}
	
	public int getHenna()
	{
		return _henna;
	}
	
	public int getItemOne()
	{
		return _itemOne;
	}
	
	public long getCountOne()
	{
		return _countOne;
	}
	
	public int getItemTwo()
	{
		return _itemTwo;
	}
	
	public long getCountTwo()
	{
		return _countTwo;
	}
	
	public long getCommission()
	{
		return _commission;
	}
	
	public float getChance()
	{
		return _chance;
	}
	
	public void addReward(CombinationHennaReward item)
	{
		_rewards.put(item.getType(), item);
	}
	
	public CombinationHennaReward getReward(CombinationItemType type)
	{
		return _rewards.get(type);
	}
}
