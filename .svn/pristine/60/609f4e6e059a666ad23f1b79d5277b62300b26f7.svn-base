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
package ai.areas.SelMahumBase;

import java.util.HashSet;
import java.util.Set;

import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;

import ai.AbstractNpcAI;

/**
 * @author petryxa
 */
public class SelMahumThief extends AbstractNpcAI
{
	// NPCs
	private static final int THIEF = 22241; // Sel Mahum Thief
	private static final Set<Integer> MONSTERS = new HashSet<>();
	static
	{
		MONSTERS.add(22239);
		MONSTERS.add(22238);
		MONSTERS.add(22240);
		MONSTERS.add(22237);
		MONSTERS.add(22254);
	}
	// Misc
	private static final long DESPAWN_TIME = 5 * 60 * 1000; // 5 min
	
	private SelMahumThief()
	{
		addKillId(MONSTERS);
	}
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		if (getRandom(100) < 20)
		{
			final Npc spawn = addSpawn(THIEF, npc.getLocation(), false, DESPAWN_TIME);
			addAttackPlayerDesire(spawn, killer);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	public static void main(String[] args)
	{
		new SelMahumThief();
	}
}
