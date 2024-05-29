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
package ai.areas.Hellbound.HellboundMessenger;

import org.l2j.gameserver.model.Location;
import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.Player;

import ai.AbstractNpcAI;

/**
 * @author Mobius
 */
public class HellboundMessenger extends AbstractNpcAI
{
	// NPC
	private static final int MESSENGER = 34196;
	// Location
	private static final Location IVORY_TOWER = new Location(86722, 15389, -3515);
	// Misc
	private static final int MINIMUM_LEVEL = 85;
	
	private HellboundMessenger()
	{
		addStartNpc(MESSENGER);
		addTalkId(MESSENGER);
		addFirstTalkId(MESSENGER);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		if ((npc.getId() == MESSENGER) && event.equals("teleport"))
		{
			if (player.getLevel() < MINIMUM_LEVEL)
			{
				return "34196-02.htm";
			}
			
			player.teleToLocation(IVORY_TOWER);
			return null;
		}
		
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		return "34196-01.htm";
	}
	
	public static void main(String[] args)
	{
		new HellboundMessenger();
	}
}
