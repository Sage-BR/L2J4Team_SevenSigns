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
package ai.areas.PrimevalIsle;

import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.SkillHolder;

import ai.AbstractNpcAI;

/**
 * @author petryxa
 */
public class SayhaBuff extends AbstractNpcAI
{
	// NPCs
	private static final int[] MONSTERS =
	{
		21962, // Wild Strider
		21963, // Elroki
		21964, // Pachycephalosaurus
		21966, // Ornithomimus
		21968, // Ornithomimus
		21969, // Deinonychus
		21971, // Velociraptor
		21974, // Ornithomimus
		21976, // Deinonychus
		21978, // Tyrannosaurus
		22056, // Pachycephalosaurus
		22057, // Strider
		22058, // Ornithomimus
		22059, // Pterosaur
	};
	// Skill
	private static final SkillHolder BENEFACTION_OF_BLUE_HAWK = new SkillHolder(50123, 1);
	
	private SayhaBuff()
	{
		addKillId(MONSTERS);
	}
	
	@Override
	public String onKill(Npc npc, Player attacker, boolean isSummon)
	{
		if (!attacker.isAffectedBySkill(BENEFACTION_OF_BLUE_HAWK))
		{
			BENEFACTION_OF_BLUE_HAWK.getSkill().applyEffects(attacker, attacker);
		}
		return super.onKill(npc, attacker, isSummon);
	}
	
	public static void main(String[] args)
	{
		new SayhaBuff();
	}
}