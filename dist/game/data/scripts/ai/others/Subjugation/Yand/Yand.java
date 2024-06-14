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
package ai.others.Subjugation.Yand;

import org.l2j.gameserver.data.xml.MultisellData;
import org.l2j.gameserver.model.Location;
import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.Player;

import ai.AbstractNpcAI;

/**
 * @author Serenitty
 */
public class Yand extends AbstractNpcAI
{
	// NPC
	private static final int YAND = 34327;
	// Item
	private static final int MORGOS_MILITARY_SCROLL_MS = 90318605;
	// Location
	private static final Location TELEPORT_LOC = new Location(146915, -82589, -5128);
	
	private Yand()
	{
		addFirstTalkId(YAND);
		addTalkId(YAND);
		addSpawnId(YAND);
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		switch (event)
		{
			case "GoToInsideMorgos":
			{
				final int military = player.getVariables().getInt("MORGOS_MILITARY_FREE", 1);
				if (military == 0)
				{
					return "34327-01.html";
				}
				player.teleToLocation(TELEPORT_LOC);
				player.getVariables().set("MORGOS_MILITARY_FREE", 0);
				break;
			}
			case "BuyScrollMorgos":
			{
				MultisellData.getInstance().separateAndSend(MORGOS_MILITARY_SCROLL_MS, player, null, false);
				break;
			}
		}
		return null;
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		return "34327.html";
	}
	
	public static void main(String[] args)
	{
		new Yand();
	}
}
