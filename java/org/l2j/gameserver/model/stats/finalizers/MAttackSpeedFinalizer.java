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
package org.l2j.gameserver.model.stats.finalizers;

import java.util.OptionalDouble;

import org.l2j.Config;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.stats.BaseStat;
import org.l2j.gameserver.model.stats.IStatFunction;
import org.l2j.gameserver.model.stats.Stat;

/**
 * @author UnAfraid
 */
public class MAttackSpeedFinalizer implements IStatFunction
{
	@Override
	public double calc(Creature creature, OptionalDouble base, Stat stat)
	{
		throwIfPresent(base);
		
		double baseValue = calcWeaponBaseValue(creature, stat);
		if (Config.CHAMPION_ENABLE && creature.isChampion())
		{
			baseValue *= Config.CHAMPION_SPD_ATK;
		}
		
		final double witBonus = creature.getWIT() > 0 ? BaseStat.WIT.calcBonus(creature) : 1;
		baseValue *= witBonus;
		return validateValue(creature, defaultValue(creature, stat, baseValue), 1, creature.isPlayable() ? Config.MAX_MATK_SPEED : Double.MAX_VALUE);
	}
	
	private double defaultValue(Creature creature, Stat stat, double baseValue)
	{
		final double mul = Math.max(creature.getStat().getMul(stat), 0.7);
		final double add = creature.getStat().getAdd(stat);
		return (baseValue * mul) + add + creature.getStat().getMoveTypeValue(stat, creature.getMoveType());
	}
}
