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
package ai.areas.WarTornPlains;

import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.Playable;
import org.l2j.gameserver.model.actor.Player;

import ai.AbstractNpcAI;

/**
 * Cave Maiden, Keeper AI.
 * @author proGenitor
 */
public class WarTornPlains extends AbstractNpcAI
{
	// Monsters
	private static final int GRAVEYARD_WANDERER = 20659;
	private static final int ARCHER_OF_GREED = 20660;
	private static final int HATAR_RATMAN_THIEF = 20661;
	private static final int HATAR_RATMAN_BOSS = 20662;
	private static final int HATAR_HANISHEE = 20663;
	private static final int DEMONIC_EYE = 20664;
	private static final int TAIK_ORC_ELDER = 20665;
	private static final int FARCRAN = 20667;
	// Guard
	private static final int FIERCE_GUARD = 22103;
	
	private WarTornPlains()
	{
		addKillId(GRAVEYARD_WANDERER, ARCHER_OF_GREED, HATAR_RATMAN_THIEF, HATAR_RATMAN_BOSS, HATAR_HANISHEE, DEMONIC_EYE, TAIK_ORC_ELDER, FARCRAN);
	}
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		if (getRandom(100) < 10)
		{
			final Npc spawnBanshee = addSpawn(FIERCE_GUARD, npc, false, 300000);
			final Playable attacker = isSummon ? killer.getServitors().values().stream().findFirst().orElse(killer.getPet()) : killer;
			addAttackPlayerDesire(spawnBanshee, attacker);
			npc.deleteMe();
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	public static void main(String[] args)
	{
		new WarTornPlains();
	}
}