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
package org.l2jmobius.gameserver.model.quest.newquestdata;

import java.util.List;

import org.l2jmobius.gameserver.enums.ClassId;

/**
 * @author Magik
 */
public class NewQuestCondition
{
	private final int _minLevel;
	private final int _maxLevel;
	private final List<Integer> _previousQuestIds;
	private final List<ClassId> _allowedClassIds;
	private final boolean _oneOfPreQuests;
	private final boolean _specificStart;
	
	public NewQuestCondition(int minLevel, int maxLevel, List<Integer> previousQuestIds, List<ClassId> allowedClassIds, boolean oneOfPreQuests, boolean specificStart)
	{
		_minLevel = minLevel;
		_maxLevel = maxLevel;
		_previousQuestIds = previousQuestIds;
		_allowedClassIds = allowedClassIds;
		_oneOfPreQuests = oneOfPreQuests;
		_specificStart = specificStart;
	}
	
	public int getMinLevel()
	{
		return _minLevel;
	}
	
	public int getMaxLevel()
	{
		return _maxLevel;
	}
	
	public List<Integer> getPreviousQuestIds()
	{
		return _previousQuestIds;
	}
	
	public List<ClassId> getAllowedClassIds()
	{
		return _allowedClassIds;
	}
	
	public boolean getOneOfPreQuests()
	{
		return _oneOfPreQuests;
	}
	
	public boolean getSpecificStart()
	{
		return _specificStart;
	}
}
