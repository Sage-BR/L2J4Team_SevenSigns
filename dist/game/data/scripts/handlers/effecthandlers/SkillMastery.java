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
package handlers.effecthandlers;

import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.effects.AbstractEffect;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.model.stats.BaseStat;
import org.l2j.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class SkillMastery extends AbstractEffect
{
	private final Double _stat;
	
	public SkillMastery(StatSet params)
	{
		_stat = Double.valueOf(params.getEnum("stat", BaseStat.class, BaseStat.STR).ordinal());
	}
	
	@Override
	public void pump(Creature effected, Skill skill)
	{
		effected.getStat().mergeAdd(Stat.SKILL_MASTERY, _stat);
	}
}
