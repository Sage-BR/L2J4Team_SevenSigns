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
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.effects.AbstractEffect;
import org.l2j.gameserver.model.skill.EffectScope;
import org.l2j.gameserver.model.skill.ISkillCondition;
import org.l2j.gameserver.model.skill.Skill;

import handlers.effecthandlers.SummonCubic;

/**
 * @author UnAfraid
 */
public class CanSummonCubicSkillCondition implements ISkillCondition
{
	public CanSummonCubicSkillCondition(StatSet params)
	{
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		if (!caster.isPlayer() || caster.isAlikeDead() || caster.getActingPlayer().inObserverMode())
		{
			return false;
		}
		
		final Player player = caster.getActingPlayer();
		if (player.getAutoUseSettings().isAutoSkill(skill.getId()))
		{
			for (AbstractEffect effect : skill.getEffects(EffectScope.GENERAL))
			{
				if ((effect instanceof SummonCubic) && (player.getCubicById(((SummonCubic) effect).getCubicId()) != null))
				{
					return false;
				}
			}
		}
		
		return !player.inObserverMode() && !player.isMounted() && !player.isSpawnProtected() && !player.isTeleportProtected();
	}
}
