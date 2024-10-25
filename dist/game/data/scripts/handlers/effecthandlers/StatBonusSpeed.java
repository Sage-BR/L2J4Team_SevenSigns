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

import java.util.List;

import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.conditions.Condition;
import org.l2j.gameserver.model.conditions.ConditionUsingItemType;
import org.l2j.gameserver.model.effects.AbstractEffect;
import org.l2j.gameserver.model.item.type.ArmorType;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.model.stats.BaseStat;
import org.l2j.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class StatBonusSpeed extends AbstractEffect
{
	private final Double _stat;
	private final Condition _armorTypeCondition;
	
	public StatBonusSpeed(StatSet params)
	{
		_stat = Double.valueOf(params.getEnum("stat", BaseStat.class, BaseStat.DEX).ordinal());
		
		int armorTypesMask = 0;
		final List<String> armorTypes = params.getList("armorType", String.class);
		if (armorTypes != null)
		{
			for (String armorType : armorTypes)
			{
				try
				{
					armorTypesMask |= ArmorType.valueOf(armorType).mask();
				}
				catch (IllegalArgumentException e)
				{
					final IllegalArgumentException exception = new IllegalArgumentException("armorTypes should contain ArmorType enum value but found " + armorType);
					exception.addSuppressed(e);
					throw exception;
				}
			}
		}
		_armorTypeCondition = armorTypesMask != 0 ? new ConditionUsingItemType(armorTypesMask) : null;
	}
	
	@Override
	public void pump(Creature effected, Skill skill)
	{
		if ((_armorTypeCondition == null) || _armorTypeCondition.test(effected, effected, skill))
		{
			effected.getStat().mergeAdd(Stat.STAT_BONUS_SPEED, _stat);
		}
	}
}
