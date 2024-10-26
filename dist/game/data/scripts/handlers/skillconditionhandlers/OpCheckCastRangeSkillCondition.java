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

import org.l2j.gameserver.geoengine.GeoEngine;
import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.WorldObject;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.skill.ISkillCondition;
import org.l2j.gameserver.model.skill.Skill;

/**
 * @author Mobius
 */
public class OpCheckCastRangeSkillCondition implements ISkillCondition
{
	private final int _distance;
	
	public OpCheckCastRangeSkillCondition(StatSet params)
	{
		_distance = params.getInt("distance");
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		return (target != null) //
			&& (caster.calculateDistance3D(target) >= _distance) //
			&& GeoEngine.getInstance().canSeeTarget(caster, target);
	}
}
