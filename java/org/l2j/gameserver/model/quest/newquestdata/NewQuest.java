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
package org.l2j.gameserver.model.quest.newquestdata;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.l2j.gameserver.data.xml.ExperienceData;
import org.l2j.gameserver.data.xml.ItemData;
import org.l2j.gameserver.enums.ClassId;
import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.holders.ItemHolder;
import org.l2j.gameserver.model.item.ItemTemplate;

/**
 * @author Magik
 */
public class NewQuest
{
	private static final Logger LOGGER = Logger.getLogger(NewQuest.class.getName());
	
	private final int _id;
	private final int _questType;
	private final String _name;
	private final int _startNpcId;
	private final int _endNpcId;
	private final int _startItemId;
	private final NewQuestLocation _location;
	private final NewQuestCondition _conditions;
	private final NewQuestGoal _goal;
	private final NewQuestReward _rewards;
	
	public NewQuest(StatSet set)
	{
		_id = set.getInt("id", -1);
		_questType = set.getInt("type", -1);
		_name = set.getString("name", "");
		_startNpcId = set.getInt("startNpcId", -1);
		_endNpcId = set.getInt("endNpcId", -1);
		_startItemId = set.getInt("startItemId", -1);
		_location = new NewQuestLocation(set.getInt("startLocationId", 0), set.getInt("endLocationId", 0));
		
		final String classIds = set.getString("classIds", "");
		final List<ClassId> classRestriction = classIds.isEmpty() ? Collections.emptyList() : Arrays.stream(classIds.split(";")).map(it -> ClassId.getClassId(Integer.parseInt(it))).collect(Collectors.toList());
		final String preQuestId = set.getString("preQuestId", "");
		final List<Integer> preQuestIds = preQuestId.isEmpty() ? Collections.emptyList() : Arrays.stream(preQuestId.split(";")).map(it -> Integer.parseInt(it)).collect(Collectors.toList());
		_conditions = new NewQuestCondition(set.getInt("minLevel", -1), set.getInt("maxLevel", ExperienceData.getInstance().getMaxLevel()), preQuestIds, classRestriction, set.getBoolean("oneOfPreQuests", false), set.getBoolean("specificStart", false));
		
		final int goalItemId = set.getInt("goalItemId", -1);
		final int goalCount = set.getInt("goalCount", -1);
		if (goalItemId > 0)
		{
			final ItemTemplate template = ItemData.getInstance().getTemplate(goalItemId);
			if (template == null)
			{
				LOGGER.warning(getClass().getSimpleName() + _id + ": Could not find goal item template with id " + goalItemId);
			}
			else
			{
				if (!template.isStackable())
				{
					LOGGER.warning(getClass().getSimpleName() + _id + ": Item template with id " + goalItemId + " should be stackable.");
				}
				if (!template.isQuestItem())
				{
					LOGGER.warning(getClass().getSimpleName() + _id + ": Item template with id " + goalItemId + " should be quest item.");
				}
			}
		}
		_goal = new NewQuestGoal(goalItemId, goalCount, set.getString("goalString", ""));
		
		_rewards = new NewQuestReward(set.getLong("rewardExp", -1), set.getLong("rewardSp", -1), set.getInt("rewardLevel", -1), set.getList("rewardItems", ItemHolder.class));
	}
	
	public int getId()
	{
		return _id;
	}
	
	public int getQuestType()
	{
		return _questType;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public int getStartNpcId()
	{
		return _startNpcId;
	}
	
	public int getEndNpcId()
	{
		return _endNpcId;
	}
	
	public int getStartItemId()
	{
		return _startItemId;
	}
	
	public NewQuestLocation getLocation()
	{
		return _location;
	}
	
	public NewQuestCondition getConditions()
	{
		return _conditions;
	}
	
	public NewQuestGoal getGoal()
	{
		return _goal;
	}
	
	public NewQuestReward getRewards()
	{
		return _rewards;
	}
}
