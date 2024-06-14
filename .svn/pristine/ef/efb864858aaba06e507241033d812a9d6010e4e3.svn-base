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
package org.l2jmobius.gameserver.model;

import java.util.HashMap;
import java.util.Map;

import org.l2jmobius.gameserver.model.holders.ItemHolder;

/**
 * @author Index
 */
public class MissionLevelHolder
{
	private int _maxLevel;
	private final int _bonusLevel;
	private final Map<Integer, Integer> _xpForLevel = new HashMap<>();
	private final Map<Integer, ItemHolder> _normalReward = new HashMap<>();
	private final Map<Integer, ItemHolder> _keyReward = new HashMap<>();
	private final ItemHolder _specialReward;
	private final ItemHolder _bonusReward;
	private final boolean _bonusRewardIsAvailable;
	private final boolean _bonusRewardByLevelUp;
	
	public MissionLevelHolder(int maxLevel, int bonusLevel, Map<Integer, Integer> xpForLevel, Map<Integer, ItemHolder> normalReward, Map<Integer, ItemHolder> keyReward, ItemHolder specialReward, ItemHolder bonusReward, boolean bonusRewardByLevelUp, boolean bonusRewardIsAvailable)
	{
		_maxLevel = maxLevel;
		_bonusLevel = bonusLevel;
		_xpForLevel.putAll(xpForLevel);
		_normalReward.putAll(normalReward);
		_keyReward.putAll(keyReward);
		_specialReward = specialReward;
		_bonusReward = bonusReward;
		_bonusRewardByLevelUp = bonusRewardByLevelUp;
		_bonusRewardIsAvailable = bonusRewardIsAvailable;
	}
	
	public int getMaxLevel()
	{
		return _maxLevel;
	}
	
	public void setMaxLevel(int maxLevel)
	{
		_maxLevel = maxLevel;
	}
	
	public int getBonusLevel()
	{
		return _bonusLevel;
	}
	
	public Map<Integer, Integer> getXPForLevel()
	{
		return _xpForLevel;
	}
	
	public int getXPForSpecifiedLevel(int level)
	{
		return _xpForLevel.get(level == 0 ? level + 1 : level);
	}
	
	public Map<Integer, ItemHolder> getNormalRewards()
	{
		return _normalReward;
	}
	
	public Map<Integer, ItemHolder> getKeyRewards()
	{
		return _keyReward;
	}
	
	public ItemHolder getSpecialReward()
	{
		return _specialReward;
	}
	
	public ItemHolder getBonusReward()
	{
		return _bonusReward;
	}
	
	public boolean getBonusRewardByLevelUp()
	{
		return _bonusRewardByLevelUp;
	}
	
	public boolean getBonusRewardIsAvailable()
	{
		return _bonusRewardIsAvailable;
	}
}
