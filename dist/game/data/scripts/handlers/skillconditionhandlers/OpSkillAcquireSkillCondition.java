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
package handlers.skillconditionhandlers;

import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.WorldObject;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.skill.ISkillCondition;
import org.l2j.gameserver.model.skill.Skill;

/**
 * @author Sdw
 */
public class OpSkillAcquireSkillCondition implements ISkillCondition
{
	private final int _skillId;
	private final boolean _hasLearned;
	
	public OpSkillAcquireSkillCondition(StatSet params)
	{
		_skillId = params.getInt("skillId");
		_hasLearned = params.getBoolean("hasLearned");
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		if ((target == null) || !target.isCreature())
		{
			return false;
		}
		
		final int skillLevel = ((Creature) target).getSkillLevel(_skillId);
		return _hasLearned ? skillLevel != 0 : skillLevel == 0;
	}
}
