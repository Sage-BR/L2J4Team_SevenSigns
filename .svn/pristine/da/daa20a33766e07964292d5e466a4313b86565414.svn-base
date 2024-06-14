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
package ai.areas.SilentValley;

import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Playable;
import org.l2jmobius.gameserver.model.actor.Player;

import ai.AbstractNpcAI;

public class SilentValley extends AbstractNpcAI
{
	// Monsters
	private static final int CREATURE_OF_THE_PAST = 20967;
	private static final int FORGOTTEN_FACE = 20968;
	private static final int GIANT_SHADOW = 20969;
	private static final int WARRIOR_OF_ANCIENT_TIMES = 20970;
	private static final int SHAMAN_OF_ANCIENT_TIMES = 20971;
	private static final int FORGOTTEN_ANCIENT_CREATURE = 20972;
	// Guard
	private static final int ANCIENT_GUARDIAN = 22106;
	
	private SilentValley()
	{
		addKillId(CREATURE_OF_THE_PAST, FORGOTTEN_FACE, GIANT_SHADOW, WARRIOR_OF_ANCIENT_TIMES, SHAMAN_OF_ANCIENT_TIMES, FORGOTTEN_ANCIENT_CREATURE);
	}
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		if (getRandom(100) < 10)
		{
			final Npc spawnBanshee = addSpawn(ANCIENT_GUARDIAN, npc, false, 300000);
			final Playable attacker = isSummon ? killer.getServitors().values().stream().findFirst().orElse(killer.getPet()) : killer;
			addAttackPlayerDesire(spawnBanshee, attacker);
			npc.deleteMe();
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	public static void main(String[] args)
	{
		new SilentValley();
	}
}