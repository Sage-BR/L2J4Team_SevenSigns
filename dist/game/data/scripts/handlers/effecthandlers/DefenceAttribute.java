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

import java.util.EnumSet;
import java.util.Set;

import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.effects.AbstractEffect;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.model.stats.Stat;

/**
 * @author Mobius
 */
public class DefenceAttribute extends AbstractEffect
{
	private final double _amount;
	private final Stat _singleStat;
	private final Set<Stat> _multipleStats;
	
	public DefenceAttribute(StatSet params)
	{
		_amount = params.getDouble("amount", 0);
		final String attributes = params.getString("attribute", "FIRE");
		if (attributes.contains(","))
		{
			_singleStat = null;
			_multipleStats = EnumSet.noneOf(Stat.class);
			for (String attribute : attributes.split(","))
			{
				_multipleStats.add(Stat.valueOf(attribute + "_RES"));
			}
		}
		else
		{
			_singleStat = Stat.valueOf(attributes + "_RES");
			_multipleStats = null;
		}
	}
	
	@Override
	public void pump(Creature effected, Skill skill)
	{
		if (_singleStat != null)
		{
			effected.getStat().mergeAdd(_singleStat, _amount);
			return;
		}
		
		for (Stat stat : _multipleStats)
		{
			effected.getStat().mergeAdd(stat, _amount);
		}
	}
}
