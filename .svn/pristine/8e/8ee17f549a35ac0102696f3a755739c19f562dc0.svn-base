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
package org.l2jmobius.gameserver.instancemanager.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.model.holders.ItemChanceHolder;
import org.l2jmobius.gameserver.model.holders.ItemHolder;

/**
 * @author Index
 */
public class LetterCollectorManager
{
	protected static final Logger LOGGER = Logger.getLogger(LetterCollectorManager.class.getName());
	
	private final Map<Integer, LetterCollectorRewardHolder> _rewards = new HashMap<>();
	private final Map<Integer, List<ItemHolder>> _words = new HashMap<>();
	private final Map<String, Integer> _letter = new HashMap<>();
	private final Map<Integer, Boolean> _needToSumAllChance = new HashMap<>();
	
	private int _minLevel = 1;
	private int _maxLevel = Config.PLAYER_MAXIMUM_LEVEL;
	
	protected LetterCollectorManager()
	{
	}
	
	public void init()
	{
		LOGGER.info(getClass().getSimpleName() + ": Loaded " + _rewards.size() + " words.");
		LOGGER.info(getClass().getSimpleName() + ": Loaded " + _letter.size() + " letters.");
	}
	
	public int getMinLevel()
	{
		return _minLevel;
	}
	
	public void setMinLevel(int minLevel)
	{
		_minLevel = minLevel;
	}
	
	public int getMaxLevel()
	{
		return _maxLevel;
	}
	
	public void setMaxLevel(int maxLevel)
	{
		if (maxLevel < 1)
		{
			_maxLevel = Config.PLAYER_MAXIMUM_LEVEL;
		}
		else
		{
			_maxLevel = maxLevel;
		}
	}
	
	public LetterCollectorRewardHolder getRewards(int id)
	{
		return _rewards.get(id);
	}
	
	public List<ItemHolder> getWord(int id)
	{
		return _words.get(id);
	}
	
	public void setRewards(Map<Integer, LetterCollectorRewardHolder> rewards)
	{
		_rewards.putAll(rewards);
	}
	
	public void setWords(Map<Integer, List<ItemHolder>> words)
	{
		_words.putAll(words);
	}
	
	public void addRewards(int id, LetterCollectorRewardHolder rewards)
	{
		_rewards.put(id, rewards);
	}
	
	public void addWords(int id, List<ItemHolder> words)
	{
		_words.put(id, words);
	}
	
	public void resetField()
	{
		_minLevel = 1;
		_rewards.clear();
		_words.clear();
		_needToSumAllChance.clear();
	}
	
	public void setLetters(Map<String, Integer> letters)
	{
		_letter.putAll(letters);
	}
	
	public Map<String, Integer> getLetters()
	{
		return _letter;
	}
	
	public void setNeedToSumAllChance(int id, boolean needToSumAllChance)
	{
		_needToSumAllChance.put(id, needToSumAllChance);
	}
	
	public boolean getNeedToSumAllChance(int id)
	{
		return _needToSumAllChance.get(id);
	}
	
	public static class LetterCollectorRewardHolder
	{
		final List<ItemChanceHolder> _rewards;
		final double _chance;
		
		public LetterCollectorRewardHolder(List<ItemChanceHolder> rewards, double chance)
		{
			_rewards = rewards;
			_chance = chance;
		}
		
		public List<ItemChanceHolder> getRewards()
		{
			return _rewards;
		}
		
		public double getChance()
		{
			return _chance;
		}
	}
	
	public static LetterCollectorManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final LetterCollectorManager INSTANCE = new LetterCollectorManager();
	}
}