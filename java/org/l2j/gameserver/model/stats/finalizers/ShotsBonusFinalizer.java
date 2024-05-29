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
package org.l2j.gameserver.model.stats.finalizers;

import java.util.OptionalDouble;

import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.stats.IStatFunction;
import org.l2j.gameserver.model.stats.Stat;

/**
 * @author UnAfraid
 */
public class ShotsBonusFinalizer implements IStatFunction
{
	@Override
	public double calc(Creature creature, OptionalDouble base, Stat stat)
	{
		throwIfPresent(base);
		
		double baseValue = 1;
		final Player player = creature.getActingPlayer();
		if (player != null)
		{
			final Item weapon = player.getActiveWeaponInstance();
			if ((weapon != null) && weapon.isEnchanted())
			{
				switch (weapon.getWeaponItem().getItemGrade())
				{
					case D:
					case C:
					{
						baseValue += (weapon.getEnchantLevel() * 0.4) / 100;
						break;
					}
					case B:
					{
						baseValue += (weapon.getEnchantLevel() * 0.7) / 100;
						break;
					}
					case A:
					{
						baseValue += (weapon.getEnchantLevel() * 1.4) / 100;
						break;
					}
					case S:
					{
						baseValue += (weapon.getEnchantLevel() * 1.6) / 100;
						break;
					}
				}
			}
			if (player.getActiveRubyJewel() != null)
			{
				baseValue += player.getActiveRubyJewel().getBonus();
			}
		}
		return Stat.defaultValue(creature, stat, baseValue);
	}
}
