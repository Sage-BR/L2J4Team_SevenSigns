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
package ai.bosses.TowerOfInsolence;

import org.l2j.commons.time.SchedulingPattern;
import org.l2j.gameserver.model.Location;
import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.Player;

import ai.AbstractNpcAI;

/**
 * @author gugaf
 */
public class Anais extends AbstractNpcAI
{
	// NPC
	private static final int ANAIS = 29207;
	// Location
	private static final Location ANAIS_LOC = new Location(115213, 16623, 10080);
	// Misc
	private static final String ANAIS_RESPAWN_PATTERN = "30 22 * * 1";
	private SchedulingPattern _respawnPattern = null;
	
	public Anais()
	{
		addKillId(ANAIS);
		
		_respawnPattern = new SchedulingPattern(ANAIS_RESPAWN_PATTERN);
		
		final long nextRespawnTime = getNextRespawnTime();
		if (nextRespawnTime > 0)
		{
			startQuestTimer("respawn_anais", nextRespawnTime, null, null);
		}
		else
		{
			addSpawn(ANAIS, ANAIS_LOC, false, getDespawnTime());
		}
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		if (event.equalsIgnoreCase("respawn_anais"))
		{
			addSpawn(ANAIS, ANAIS_LOC, false, getDespawnTime());
		}
		return super.onEvent(event, npc, player);
	}
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		final long nextRespawnTime = getNextRespawnTime();
		if (nextRespawnTime > 0)
		{
			startQuestTimer("respawn_anais", nextRespawnTime, null, null);
		}
		else
		{
			addSpawn(ANAIS, ANAIS_LOC, false, getDespawnTime());
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	private long getDespawnTime()
	{
		final long currentTime = System.currentTimeMillis();
		return _respawnPattern.next(currentTime) - currentTime - 60000 /* 1 minute less */;
	}
	
	private long getNextRespawnTime()
	{
		final long currentTime = System.currentTimeMillis();
		return _respawnPattern.next(currentTime) - currentTime;
	}
	
	public static void main(String[] args)
	{
		new Anais();
	}
}
