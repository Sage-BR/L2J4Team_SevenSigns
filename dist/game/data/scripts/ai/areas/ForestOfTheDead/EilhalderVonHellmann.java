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
package ai.areas.ForestOfTheDead;

import org.l2j.gameserver.model.Location;
import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.events.EventType;
import org.l2j.gameserver.model.events.ListenerRegisterType;
import org.l2j.gameserver.model.events.annotations.RegisterEvent;
import org.l2j.gameserver.model.events.annotations.RegisterType;
import org.l2j.gameserver.model.events.impl.OnDayNightChange;

import ai.AbstractNpcAI;

/**
 * @author Mobius
 */
public class EilhalderVonHellmann extends AbstractNpcAI
{
	private static final int EILHALDER_VON_HELLMANN = 25328;
	private static final Location SPAWN_LOCATION = new Location(59090, -42188, -3003);
	private static Npc _npcInstance;
	
	private EilhalderVonHellmann()
	{
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		if (npc != null)
		{
			if (npc.isInCombat())
			{
				startQuestTimer("despawn", 30000, _npcInstance, null);
			}
			else
			{
				npc.deleteMe();
				_npcInstance = null;
			}
		}
		return super.onEvent(event, npc, player);
	}
	
	@RegisterEvent(EventType.ON_DAY_NIGHT_CHANGE)
	@RegisterType(ListenerRegisterType.GLOBAL)
	public void onDayNightChange(OnDayNightChange event)
	{
		if (!event.isNight())
		{
			if (_npcInstance != null)
			{
				if (!_npcInstance.isInCombat())
				{
					_npcInstance.deleteMe();
					_npcInstance = null;
				}
				else
				{
					startQuestTimer("despawn", 30000, _npcInstance, null);
				}
			}
		}
		else if (_npcInstance == null)
		{
			_npcInstance = addSpawn(EILHALDER_VON_HELLMANN, SPAWN_LOCATION);
		}
	}
	
	public static void main(String[] args)
	{
		new EilhalderVonHellmann();
	}
}
