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
package org.l2j.gameserver.model.stats.functions;

import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.conditions.Condition;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.model.stats.Stat;

/**
 * Returns the initial value plus the function value, if the condition are met.
 * @author Zoey76
 */
public class FuncAdd extends AbstractFunction
{
	public FuncAdd(Stat stat, int order, Object owner, double value, Condition applyCond)
	{
		super(stat, order, owner, value, applyCond);
	}
	
	@Override
	public double calc(Creature effector, Creature effected, Skill skill, double initVal)
	{
		if ((getApplyCond() == null) || getApplyCond().test(effector, effected, skill))
		{
			return initVal + getValue();
		}
		return initVal;
	}
}
