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
import org.l2jmobius.gameserver.model.skill.Skill;

import ai.AbstractNpcAI;

/**
 * @author petryxa
 */
public class BurningBuff extends AbstractNpcAI
{
	// NPC
	private static final int TYRANNOSAURUS = 21978;
	// Skill
	private static final SkillHolder BURNING = new SkillHolder(48054, 1);
	
	private BurningBuff()
	{
		addAttackId(TYRANNOSAURUS);
	}
	
	@Override
	public String onAttack(Npc npc, Player attacker, int damage, boolean isSummon, Skill skill)
	{
		if (!attacker.isAffectedBySkill(BURNING))
		{
			BURNING.getSkill().applyEffects(attacker, attacker);
		}
		return super.onAttack(npc, attacker, damage, isSummon, skill);
	}
	
	public static void main(String[] args)
	{
		new BurningBuff();
	}
}
