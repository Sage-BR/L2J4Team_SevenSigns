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
package org.l2j.gameserver.model.conditions;

import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.item.ItemTemplate;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.model.zone.ZoneId;
import org.l2j.gameserver.network.SystemMessageId;

/**
 * Player Call Pc condition implementation.
 * @author Adry_85
 */
public class ConditionPlayerCallPc extends Condition
{
	private final boolean _value;
	
	public ConditionPlayerCallPc(boolean value)
	{
		_value = value;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, Skill skill, ItemTemplate item)
	{
		boolean canCallPlayer = true;
		final Player player = effector.getActingPlayer();
		if (player == null)
		{
			canCallPlayer = false;
		}
		else if (player.isInOlympiadMode())
		{
			player.sendPacket(SystemMessageId.CANNOT_BE_SUMMONED_IN_THIS_LOCATION);
			canCallPlayer = false;
		}
		else if (player.inObserverMode())
		{
			canCallPlayer = false;
		}
		else if (player.isInsideZone(ZoneId.NO_SUMMON_FRIEND) || player.isInsideZone(ZoneId.JAIL) || player.isFlyingMounted())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_USE_SUMMONING_OR_TELEPORTING_IN_THIS_AREA);
			canCallPlayer = false;
		}
		return (_value == canCallPlayer);
	}
}
