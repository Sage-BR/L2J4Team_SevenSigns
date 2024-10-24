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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.l2j.gameserver.enums.ClassId;
import org.l2j.gameserver.enums.SkillConditionAffectType;
import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.WorldObject;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.skill.ISkillCondition;
import org.l2j.gameserver.model.skill.Skill;

/**
 * @author UnAfraid, Mobius
 */
public class OpCheckClassListSkillCondition implements ISkillCondition
{
	private final Set<ClassId> _classIds = new HashSet<>();
	private final SkillConditionAffectType _affectType;
	private final boolean _isWithin;
	
	public OpCheckClassListSkillCondition(StatSet params)
	{
		final List<ClassId> classIds = params.getEnumList("classIds", ClassId.class);
		if (classIds != null)
		{
			_classIds.addAll(classIds);
		}
		_affectType = params.getEnum("affectType", SkillConditionAffectType.class);
		_isWithin = params.getBoolean("isWithin");
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		switch (_affectType)
		{
			case CASTER:
			{
				return caster.isPlayer() && (_classIds.contains(caster.getActingPlayer().getClassId()) == _isWithin);
			}
			case TARGET:
			{
				return (target != null) && target.isPlayer() && (_classIds.contains(target.getActingPlayer().getClassId()) == _isWithin);
			}
			default:
			{
				return false;
			}
		}
	}
}
