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
package org.l2jmobius.gameserver.data.xml;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import org.w3c.dom.Document;

import org.l2jmobius.commons.util.IXmlReader;
import org.l2jmobius.gameserver.model.MissionLevelHolder;
import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.holders.ItemHolder;

/**
 * @author Index
 */
public class MissionLevel implements IXmlReader
{
	private static final Logger LOGGER = Logger.getLogger(MissionLevel.class.getName());
	
	private final Map<Integer, MissionLevelHolder> _template = new HashMap<>();
	private int _currentSeason;
	
	protected MissionLevel()
	{
		load();
	}
	
	@Override
	public void load()
	{
		_template.clear();
		parseDatapackFile("data/MissionLevel.xml");
		if (_currentSeason > 0)
		{
			LOGGER.info(getClass().getSimpleName() + ": Loaded " + _template.size() + " seasons.");
		}
		else
		{
			_template.clear();
		}
	}
	
	@Override
	public void parseDocument(Document doc, File f)
	{
		forEach(doc, "list", listNode ->
		{
			forEach(listNode, "current", current -> _currentSeason = parseInteger(current.getAttributes(), "season"));
			forEach(listNode, "missionLevel", missionNode ->
			{
				final StatSet missionSet = new StatSet(parseAttributes(missionNode));
				final AtomicInteger season = new AtomicInteger(missionSet.getInt("season"));
				final AtomicInteger maxLevel = new AtomicInteger(missionSet.getInt("maxLevel"));
				final AtomicBoolean bonusRewardIsAvailable = new AtomicBoolean(missionSet.getBoolean("bonusRewardIsAvailable"));
				final AtomicBoolean bonusRewardByLevelUp = new AtomicBoolean(missionSet.getBoolean("bonusRewardByLevelUP"));
				final AtomicReference<Map<Integer, ItemHolder>> keyReward = new AtomicReference<>(new HashMap<>());
				final AtomicReference<Map<Integer, ItemHolder>> normalReward = new AtomicReference<>(new HashMap<>());
				final AtomicReference<Map<Integer, Integer>> xpForLevel = new AtomicReference<>(new HashMap<>());
				final AtomicReference<ItemHolder> specialReward = new AtomicReference<>();
				final AtomicReference<ItemHolder> bonusReward = new AtomicReference<>();
				forEach(missionNode, "expTable", expListNode -> forEach(expListNode, "exp", expNode ->
				{
					final StatSet expSet = new StatSet(parseAttributes(expNode));
					xpForLevel.get().put(expSet.getInt("level"), expSet.getInt("amount"));
				}));
				forEach(missionNode, "baseRewards", baseRewardsNode -> forEach(baseRewardsNode, "baseReward", rewards ->
				{
					final StatSet rewardsSet = new StatSet(parseAttributes(rewards));
					normalReward.get().put(rewardsSet.getInt("level"), new ItemHolder(rewardsSet.getInt("itemId"), rewardsSet.getLong("itemCount")));
				}));
				forEach(missionNode, "keyRewards", keyRewardsNode -> forEach(keyRewardsNode, "keyReward", rewards ->
				{
					final StatSet rewardsSet = new StatSet(parseAttributes(rewards));
					keyReward.get().put(rewardsSet.getInt("level"), new ItemHolder(rewardsSet.getInt("itemId"), rewardsSet.getLong("itemCount")));
				}));
				forEach(missionNode, "specialReward", specialRewardNode ->
				{
					final StatSet specialRewardSet = new StatSet(parseAttributes(specialRewardNode));
					specialReward.set(new ItemHolder(specialRewardSet.getInt("itemId"), specialRewardSet.getLong("itemCount")));
				});
				forEach(missionNode, "bonusReward", bonusRewardNode ->
				{
					final StatSet bonusRewardSet = new StatSet(parseAttributes(bonusRewardNode));
					bonusReward.set(new ItemHolder(bonusRewardSet.getInt("itemId"), bonusRewardSet.getLong("itemCount")));
				});
				int bonusLevel = normalReward.get().keySet().stream().max(Integer::compare).orElse(maxLevel.get());
				if (bonusLevel == maxLevel.get())
				{
					bonusLevel = bonusLevel - 1;
				}
				_template.put(season.get(), new MissionLevelHolder(maxLevel.get(), bonusLevel + 1, xpForLevel.get(), normalReward.get(), keyReward.get(), specialReward.get(), bonusReward.get(), bonusRewardByLevelUp.get(), bonusRewardIsAvailable.get()));
			});
		});
	}
	
	public int getCurrentSeason()
	{
		return _currentSeason;
	}
	
	public MissionLevelHolder getMissionBySeason(int season)
	{
		return _template.getOrDefault(season, null);
	}
	
	public static MissionLevel getInstance()
	{
		return MissionLevel.SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final MissionLevel INSTANCE = new MissionLevel();
	}
}
