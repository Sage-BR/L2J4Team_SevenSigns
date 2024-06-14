/*
 * This file is part of the L2J Mobius project.
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
package ai.areas.Hellbound.CitadelTeleport;

import org.l2jmobius.gameserver.model.Location;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;

import ai.AbstractNpcAI;

/**
 * @author MacuK
 */
public class CitadelTeleport extends AbstractNpcAI
{
	// NPCs
	private static final int TELEPORT = 34244;
	private static final int CAPTURE_DEVICE = 34245;
	// Locations
	private static final Location[] LOCATIONS =
	{
		new Location(-10717, 282642, -13824),
		new Location(-12071, 272965, -9032),
		new Location(-12454, 284135, -15872),
		new Location(-12714, 272944, -12104),
		new Location(-12828, 284197, -9728),
		new Location(-14219, 282404, -11776),
		new Location(-21343, 282630, -15872),
		new Location(-21368, 282680, -9728),
		new Location(-21376, 282658, -11776),
		new Location(-21405, 282671, -13824),
		new Location(16679, 244326, 10496),
	};
	
	public CitadelTeleport()
	{
		addStartNpc(TELEPORT, CAPTURE_DEVICE);
		addTalkId(TELEPORT, CAPTURE_DEVICE);
		addFirstTalkId(TELEPORT, CAPTURE_DEVICE);
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		if (event.equals("teleport"))
		{
			player.teleToLocation(getRandomEntry(LOCATIONS));
			return null;
		}
		
		return super.onEvent(event, npc, player);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		return npc.getId() + ".htm";
	}
	
	public static void main(String[] args)
	{
		new CitadelTeleport();
	}
}
