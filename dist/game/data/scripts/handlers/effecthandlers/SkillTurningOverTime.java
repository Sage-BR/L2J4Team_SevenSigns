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

import org.l2j.commons.util.Rnd;
import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.effects.AbstractEffect;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.model.stats.Formulas;
import org.l2j.gameserver.network.SystemMessageId;

/**
 * Skill Turning effect implementation.
 */
public class SkillTurningOverTime extends AbstractEffect
{
	private final int _chance;
	private final boolean _staticChance;
	
	public SkillTurningOverTime(StatSet params)
	{
		_chance = params.getInt("chance", 100);
		_staticChance = params.getBoolean("staticChance", false);
		setTicks(params.getInt("ticks"));
	}
	
	@Override
	public boolean onActionTime(Creature effector, Creature effected, Skill skill, Item item)
	{
		if ((effected == null) || (effected == effector) || effected.isRaid())
		{
			return false;
		}
		
		final boolean skillSuccess = _staticChance ? Formulas.calcProbability(_chance, effector, effected, skill) : (Rnd.get(100) < _chance);
		if (skillSuccess && effected.isCastingNow())
		{
			effected.abortAllSkillCasters();
			effected.sendPacket(SystemMessageId.YOUR_CASTING_HAS_BEEN_INTERRUPTED);
		}
		
		return super.onActionTime(effector, effected, skill, item);
	}
}