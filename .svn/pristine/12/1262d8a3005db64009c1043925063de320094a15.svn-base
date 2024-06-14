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
package ai.areas.Cemetery;

import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Playable;
import org.l2jmobius.gameserver.model.actor.Player;

import ai.AbstractNpcAI;

/**
 * Cave Maiden, Keeper AI.
 * @author proGenitor
 */
public class Cemetery extends AbstractNpcAI
{
	// Monsters
	private static final int SOUL_OF_RUINS = 21000;
	private static final int ROVING_SOUL = 20999;
	private static final int CRUEL_PUNISHER = 20998;
	private static final int SOLDIER_OF_GRIEF = 20997;
	private static final int SPITEFUL_GHOST_OF_RUINS = 20996;
	private static final int TORTURED_UNDEAD = 20678;
	private static final int TAIRIM = 20675;
	private static final int TAIK_ORC_SUPPLY_OFFICER = 20669;
	private static final int GRAVE_GUARD = 20668;
	private static final int TAIK_ORC_WATCHMAN = 20666;
	// Guard
	private static final int GRAVE_WARDEN = 22128;
	
	private Cemetery()
	{
		addKillId(SOUL_OF_RUINS, ROVING_SOUL, CRUEL_PUNISHER, SOLDIER_OF_GRIEF, SPITEFUL_GHOST_OF_RUINS, TORTURED_UNDEAD, TAIRIM, TAIK_ORC_SUPPLY_OFFICER, GRAVE_GUARD, TAIK_ORC_WATCHMAN);
	}
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		if (getRandom(100) < 10)
		{
			final Npc spawnBanshee = addSpawn(GRAVE_WARDEN, npc, false, 300000);
			final Playable attacker = isSummon ? killer.getServitors().values().stream().findFirst().orElse(killer.getPet()) : killer;
			addAttackPlayerDesire(spawnBanshee, attacker);
			npc.deleteMe();
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	public static void main(String[] args)
	{
		new Cemetery();
	}
}
