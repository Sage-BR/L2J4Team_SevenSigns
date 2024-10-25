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
package org.l2j.gameserver.model.conditions;

import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.item.ItemTemplate;
import org.l2j.gameserver.model.skill.Skill;

/**
 * The Class ConditionPlayerSex.
 */
public class ConditionPlayerSex extends Condition
{
	// male 0 female 1
	private final int _sex;
	
	/**
	 * Instantiates a new condition player sex.
	 * @param sex the sex
	 */
	public ConditionPlayerSex(int sex)
	{
		_sex = sex;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, Skill skill, ItemTemplate item)
	{
		if (effector.getActingPlayer() == null)
		{
			return false;
		}
		return (effector.getActingPlayer().getAppearance().isFemale() ? 1 : 0) == _sex;
	}
}
