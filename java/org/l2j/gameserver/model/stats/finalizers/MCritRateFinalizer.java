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
import org.l2j.gameserver.model.item.ItemTemplate;
import org.l2j.gameserver.model.stats.BaseStat;
import org.l2j.gameserver.model.stats.IStatFunction;
import org.l2j.gameserver.model.stats.Stat;

/**
 * @author UnAfraid
 */
public class MCritRateFinalizer implements IStatFunction
{
	@Override
	public double calc(Creature creature, OptionalDouble base, Stat stat)
	{
		throwIfPresent(base);
		
		double baseValue = calcWeaponPlusBaseValue(creature, stat);
		if (creature.isPlayer())
		{
			// Enchanted legs bonus
			baseValue += calcEnchantBodyPart(creature, ItemTemplate.SLOT_LEGS);
		}
		
		final double physicalBonus = (creature.getStat().getMul(Stat.MAGIC_CRITICAL_RATE_BY_CRITICAL_RATE, 1) - 1) * creature.getStat().getCriticalHit();
		final double witBonus = creature.getWIT() > 0 ? BaseStat.WIT.calcBonus(creature) : 1;
		
		final double maxMagicalCritRate;
		if (creature.isPlayable())
		{
			maxMagicalCritRate = Config.MAX_MCRIT_RATE + creature.getStat().getValue(Stat.ADD_MAX_MAGIC_CRITICAL_RATE, 0);
		}
		else
		{
			maxMagicalCritRate = Double.MAX_VALUE;
		}
		
		return validateValue(creature, Stat.defaultValue(creature, stat, (baseValue * witBonus * 10) + physicalBonus), 0, maxMagicalCritRate);
	}
	
	@Override
	public double calcEnchantBodyPartBonus(int enchantLevel, boolean isBlessed)
	{
		if (isBlessed)
		{
			return (0.5 * Math.max(enchantLevel - 3, 0)) + (0.5 * Math.max(enchantLevel - 6, 0));
		}
		return (0.34 * Math.max(enchantLevel - 3, 0)) + (0.34 * Math.max(enchantLevel - 6, 0));
	}
}
