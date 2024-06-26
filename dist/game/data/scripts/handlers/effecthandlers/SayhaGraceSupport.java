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
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.effects.AbstractEffect;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.skill.Skill;

/**
 * @author Mode
 */
public class SayhaGraceSupport extends AbstractEffect
{
	public SayhaGraceSupport(StatSet params)
	{
	}
	
	@Override
	public boolean canStart(Creature effector, Creature effected, Skill skill)
	{
		return (effected != null) && effected.isPlayer();
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, Item item)
	{
		final Player player = effected.getActingPlayer();
		final double rnd = Rnd.nextDouble() * 100;
		if (rnd <= 0.1) // 4h
		{
			player.setSayhaGraceSupportEndTime(System.currentTimeMillis() + (3600000 * 4));
		}
		else if (rnd <= 0.3) // 3h
		{
			player.setSayhaGraceSupportEndTime(System.currentTimeMillis() + (3600000 * 3));
		}
		else if (rnd <= 0.6) // 2h
		{
			player.setSayhaGraceSupportEndTime(System.currentTimeMillis() + (3600000 * 2));
		}
		else if (rnd <= 1.1) // 1h
		{
			player.setSayhaGraceSupportEndTime(System.currentTimeMillis() + (3600000 * 1));
		}
	}
}
