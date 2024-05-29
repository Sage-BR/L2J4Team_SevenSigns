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
package handlers.skillconditionhandlers;

import org.l2j.gameserver.enums.SoulType;
import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.WorldObject;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.skill.ISkillCondition;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.model.stats.Stat;

/**
 * @author UnAfraid
 */
public class OpSoulMaxSkillCondition implements ISkillCondition
{
	private final SoulType _type;
	
	public OpSoulMaxSkillCondition(StatSet params)
	{
		_type = params.getEnum("type", SoulType.class, SoulType.LIGHT);
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		final int maxSouls = (int) caster.getStat().getValue(Stat.MAX_SOULS);
		return caster.isPlayable() && (caster.getActingPlayer().getChargedSouls(_type) < maxSouls);
	}
}