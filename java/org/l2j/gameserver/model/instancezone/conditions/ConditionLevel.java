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
package org.l2j.gameserver.model.instancezone.conditions;

import org.l2j.Config;
import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.instancezone.InstanceTemplate;
import org.l2j.gameserver.network.SystemMessageId;

/**
 * Instance level condition
 * @author malyelfik
 */
public class ConditionLevel extends Condition
{
	private final int _min;
	private final int _max;
	
	public ConditionLevel(InstanceTemplate template, StatSet parameters, boolean onlyLeader, boolean showMessageAndHtml)
	{
		super(template, parameters, onlyLeader, showMessageAndHtml);
		// Load params
		_min = Math.min(Config.PLAYER_MAXIMUM_LEVEL, parameters.getInt("min", 1));
		_max = Math.min(Config.PLAYER_MAXIMUM_LEVEL, parameters.getInt("max", Integer.MAX_VALUE));
		// Set message
		setSystemMessage(SystemMessageId.C1_DOES_NOT_MEET_LEVEL_REQUIREMENTS_AND_CANNOT_ENTER, (msg, player) -> msg.addString(player.getName()));
	}
	
	@Override
	protected boolean test(Player player, Npc npc)
	{
		return (player.getLevel() >= _min) && (player.getLevel() <= _max);
	}
}
