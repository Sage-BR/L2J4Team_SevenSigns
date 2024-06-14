/*
 * This file is part of the L2J 4Team Project.
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
package org.l2j.gameserver.model.holders;

/**
 * @author Liamxroy
 */
public class TriggerSkillInfoHolder
{
	private final SkillHolder _skill;
	private final int _min;
	private final int _max;
	
	public TriggerSkillInfoHolder(SkillHolder skill, int min, int max)
	{
		_skill = skill;
		_min = min;
		_max = max;
	}
	
	public int getMin()
	{
		return _min;
	}
	
	public int getMax()
	{
		return _max;
	}
	
	public SkillHolder getSkillHolder()
	{
		return _skill;
	}
}
