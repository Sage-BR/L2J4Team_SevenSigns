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
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.effects.AbstractEffect;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.skill.Skill;

/**
 * Give XP effect implementation.
 * @author Mobius
 */
public class GiveXp extends AbstractEffect
{
	private final long _xp;
	private final int _level;
	private final double _percentage;
	
	public GiveXp(StatSet params)
	{
		_xp = params.getLong("xp", 0);
		_level = params.getInt("level", 0);
		_percentage = params.getDouble("percentage", 0);
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, Item item)
	{
		if (!effector.isPlayer() || !effected.isPlayer() || effected.isAlikeDead())
		{
			return;
		}
		
		final Player player = effector.getActingPlayer();
		final double amount;
		if (player.getLevel() < _level)
		{
			amount = (_xp / 100.0) * _percentage;
		}
		else
		{
			amount = _xp;
		}
		
		player.addExpAndSp(amount, 0);
	}
}