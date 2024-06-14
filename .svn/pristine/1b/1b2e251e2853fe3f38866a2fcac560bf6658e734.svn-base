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
package ai.bosses.Glakias;

import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.SkillHolder;
import org.l2jmobius.gameserver.model.skill.Skill;
import org.l2jmobius.gameserver.model.skill.SkillCaster;

import ai.AbstractNpcAI;

/**
 * @author Serenitty
 */
public class GlakiasEasy extends AbstractNpcAI
{
	// NPCs
	private static final int GLAKIAS = 29136;
	private static final int GLAKIAS2 = 29137;
	// Skills
	private static final SkillHolder GLAKIAS_ENCHANCEMENT_LV_1 = new SkillHolder(48372, 1);
	// Skills Glakias (29136)
	private static final SkillHolder EMPEROR_THUNDER_LV_1 = new SkillHolder(48378, 1);
	private static final SkillHolder EMPEROR_THUNDER_LV_2 = new SkillHolder(48378, 2);
	private static final SkillHolder EMPEROR_SMASH_LV_1 = new SkillHolder(48377, 1);
	private static final SkillHolder EMPEROR_SMASH_LV_2 = new SkillHolder(48377, 2);
	// Skills Glakias (29137)
	private static final SkillHolder EMPEROR_TORNADO_LV_1 = new SkillHolder(48382, 1);
	private static final SkillHolder EMPEROR_TORNADO_LV_2 = new SkillHolder(48382, 2);
	private static final SkillHolder EMPEROR_CRYSTAL_LV_1 = new SkillHolder(48383, 1);
	private static final SkillHolder EMPEROR_CRYSTAL_LV_2 = new SkillHolder(48383, 2);
	
	public GlakiasEasy()
	{
		addKillId(GLAKIAS);
		addAttackId(GLAKIAS);
	}
	
	@Override
	public String onAttack(Npc npc, Player attacker, int damage, boolean isSummon, Skill skill)
	{
		if (npc.isAttackable())
		{
			if (npc.getId() == GLAKIAS)
			{
				manageGlakiasSkills(npc, attacker);
			}
			else if (npc.getId() == GLAKIAS2)
			{
				manageTwoGlakiasSkills(npc, attacker);
			}
		}
		
		return super.onAttack(npc, attacker, damage, isSummon, skill);
	}
	
	private void manageGlakiasSkills(Npc npc, Player player)
	{
		if (npc.isCastingNow(SkillCaster::isAnyNormalType) || npc.isCoreAIDisabled() || !npc.isInCombat())
		{
			return;
		}
		
		SkillHolder skillToCast = null;
		final double currentHpPercentage = npc.getCurrentHp() / npc.getMaxHp();
		if (currentHpPercentage > 45)
		{
			if (getRandom(100) < 40)
			{
				final int randomSkill = getRandom(4);
				switch (randomSkill)
				{
					case 1:
					{
						skillToCast = EMPEROR_THUNDER_LV_1;
						break;
					}
					case 2:
					{
						skillToCast = EMPEROR_SMASH_LV_1;
						break;
					}
				}
			}
		}
		else if ((currentHpPercentage < 45) && (getRandom(100) < 40))
		{
			final int randomSkill = getRandom(4);
			switch (randomSkill)
			{
				case 1:
				{
					skillToCast = EMPEROR_THUNDER_LV_2;
					break;
				}
				case 2:
				{
					skillToCast = EMPEROR_SMASH_LV_2;
					break;
				}
				case 3:
				{
					skillToCast = GLAKIAS_ENCHANCEMENT_LV_1;
					break;
				}
			}
		}
		
		if ((skillToCast != null) && SkillCaster.checkUseConditions(npc, skillToCast.getSkill()))
		{
			npc.setTarget(player);
			npc.doCast(skillToCast.getSkill());
		}
	}
	
	private void manageTwoGlakiasSkills(Npc npc, Player player)
	{
		if (npc.isCastingNow(SkillCaster::isAnyNormalType) || npc.isCoreAIDisabled() || !npc.isInCombat())
		{
			return;
		}
		
		SkillHolder skillToCast = null;
		final double currentHpPercentage = npc.getCurrentHp() / npc.getMaxHp();
		if (currentHpPercentage > 45)
		{
			if (getRandom(100) < 40)
			{
				final int randomSkill = getRandom(4);
				switch (randomSkill)
				{
					case 1:
					{
						skillToCast = EMPEROR_TORNADO_LV_1;
						break;
					}
					case 2:
					{
						skillToCast = EMPEROR_CRYSTAL_LV_1;
						break;
					}
				}
			}
		}
		else if ((currentHpPercentage < 45) && (getRandom(100) < 40))
		{
			final int randomSkill = getRandom(4);
			switch (randomSkill)
			{
				case 1:
				{
					skillToCast = EMPEROR_CRYSTAL_LV_2;
					break;
				}
				case 2:
				{
					skillToCast = EMPEROR_TORNADO_LV_2;
					break;
				}
				case 3:
				{
					skillToCast = GLAKIAS_ENCHANCEMENT_LV_1;
					break;
				}
			}
		}
		
		if ((skillToCast != null) && SkillCaster.checkUseConditions(npc, skillToCast.getSkill()))
		{
			npc.setTarget(player);
			npc.doCast(skillToCast.getSkill());
		}
	}
	
	public static void main(String[] args)
	{
		new GlakiasEasy();
	}
}
