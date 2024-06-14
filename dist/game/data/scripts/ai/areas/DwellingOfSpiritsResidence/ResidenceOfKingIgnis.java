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
package ai.areas.DwellingOfSpiritsResidence;

import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.SkillHolder;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.model.skill.SkillCaster;

import ai.AbstractNpcAI;

/**
 * @author RobikBobik
 */
public class ResidenceOfKingIgnis extends AbstractNpcAI
{
	// NPCs
	private static final int IGNIS = 29105;
	// Skills
	private static final SkillHolder FIRE_RAG_1 = new SkillHolder(50050, 1);
	private static final SkillHolder FIRE_RAG_2 = new SkillHolder(50050, 2);
	private static final SkillHolder FIRE_RAG_3 = new SkillHolder(50050, 3);
	private static final SkillHolder FIRE_RAG_4 = new SkillHolder(50050, 4);
	private static final SkillHolder FIRE_RAG_5 = new SkillHolder(50050, 5);
	private static final SkillHolder FIRE_RAG_6 = new SkillHolder(50050, 6);
	private static final SkillHolder FIRE_RAG_7 = new SkillHolder(50050, 7);
	private static final SkillHolder FIRE_RAG_8 = new SkillHolder(50050, 8);
	private static final SkillHolder FIRE_RAG_9 = new SkillHolder(50050, 9);
	private static final SkillHolder FIRE_RAG_10 = new SkillHolder(50050, 10);
	
	public ResidenceOfKingIgnis()
	{
		addAttackId(IGNIS);
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		switch (event)
		{
			case "CAST_FIRE_RAGE_1":
			{
				if (SkillCaster.checkUseConditions(npc, FIRE_RAG_1.getSkill()))
				{
					npc.doCast(FIRE_RAG_1.getSkill());
				}
				break;
			}
			case "CAST_FIRE_RAGE_2":
			{
				if (SkillCaster.checkUseConditions(npc, FIRE_RAG_2.getSkill()))
				{
					npc.doCast(FIRE_RAG_2.getSkill());
				}
				break;
			}
			case "CAST_FIRE_RAGE_3":
			{
				if (SkillCaster.checkUseConditions(npc, FIRE_RAG_3.getSkill()))
				{
					npc.doCast(FIRE_RAG_3.getSkill());
				}
				break;
			}
			case "CAST_FIRE_RAGE_4":
			{
				if (SkillCaster.checkUseConditions(npc, FIRE_RAG_4.getSkill()))
				{
					npc.doCast(FIRE_RAG_4.getSkill());
				}
				break;
			}
			case "CAST_FIRE_RAGE_5":
			{
				if (SkillCaster.checkUseConditions(npc, FIRE_RAG_5.getSkill()))
				{
					npc.doCast(FIRE_RAG_5.getSkill());
				}
				break;
			}
			case "CAST_FIRE_RAGE_6":
			{
				if (SkillCaster.checkUseConditions(npc, FIRE_RAG_6.getSkill()))
				{
					npc.doCast(FIRE_RAG_6.getSkill());
				}
				break;
			}
			case "CAST_FIRE_RAGE_7":
			{
				if (SkillCaster.checkUseConditions(npc, FIRE_RAG_7.getSkill()))
				{
					npc.doCast(FIRE_RAG_7.getSkill());
				}
				break;
			}
			case "CAST_FIRE_RAGE_8":
			{
				if (SkillCaster.checkUseConditions(npc, FIRE_RAG_8.getSkill()))
				{
					npc.doCast(FIRE_RAG_8.getSkill());
				}
				break;
			}
			case "CAST_FIRE_RAGE_9":
			{
				if (SkillCaster.checkUseConditions(npc, FIRE_RAG_9.getSkill()))
				{
					npc.doCast(FIRE_RAG_9.getSkill());
				}
				break;
			}
			case "CAST_FIRE_RAGE_10":
			{
				if (SkillCaster.checkUseConditions(npc, FIRE_RAG_10.getSkill()))
				{
					npc.doCast(FIRE_RAG_10.getSkill());
				}
				break;
			}
		}
		return null;
	}
	
	@Override
	public String onAttack(Npc npc, Player attacker, int damage, boolean isSummon, Skill skill)
	{
		if ((npc.getCurrentHp() < (npc.getMaxHp() * 0.99)) && (npc.getCurrentHp() > (npc.getMaxHp() * 0.70)))
		{
			startQuestTimer("CAST_FIRE_RAGE_1", 1000, npc, null);
		}
		else if ((npc.getCurrentHp() < (npc.getMaxHp() * 0.70)) && (npc.getCurrentHp() > (npc.getMaxHp() * 0.50)))
		{
			startQuestTimer("CAST_FIRE_RAGE_2", 1000, npc, null);
		}
		else if ((npc.getCurrentHp() < (npc.getMaxHp() * 0.50)) && (npc.getCurrentHp() > (npc.getMaxHp() * 0.40)))
		{
			startQuestTimer("CAST_FIRE_RAGE_3", 1000, npc, null);
		}
		else if ((npc.getCurrentHp() < (npc.getMaxHp() * 0.40)) && (npc.getCurrentHp() > (npc.getMaxHp() * 0.25)))
		{
			startQuestTimer("CAST_FIRE_RAGE_4", 1000, npc, null);
		}
		else if ((npc.getCurrentHp() < (npc.getMaxHp() * 0.25)) && (npc.getCurrentHp() > (npc.getMaxHp() * 0.15)))
		{
			startQuestTimer("CAST_FIRE_RAGE_5", 1000, npc, null);
		}
		else if ((npc.getCurrentHp() < (npc.getMaxHp() * 0.15)) && (npc.getCurrentHp() > (npc.getMaxHp() * 0.10)))
		{
			startQuestTimer("CAST_FIRE_RAGE_6", 1000, npc, null);
		}
		else if ((npc.getCurrentHp() < (npc.getMaxHp() * 0.10)) && (npc.getCurrentHp() > (npc.getMaxHp() * 0.7)))
		{
			startQuestTimer("CAST_FIRE_RAGE_7", 1000, npc, null);
		}
		else if ((npc.getCurrentHp() < (npc.getMaxHp() * 0.7)) && (npc.getCurrentHp() > (npc.getMaxHp() * 0.5)))
		{
			startQuestTimer("CAST_FIRE_RAGE_8", 1000, npc, null);
		}
		else if ((npc.getCurrentHp() < (npc.getMaxHp() * 0.5)) && (npc.getCurrentHp() > (npc.getMaxHp() * 0.3)))
		{
			startQuestTimer("CAST_FIRE_RAGE_9", 1000, npc, null);
		}
		else if (npc.getCurrentHp() < (npc.getMaxHp() * 0.3))
		{
			startQuestTimer("CAST_FIRE_RAGE_10", 1000, npc, null);
		}
		return super.onAttack(npc, attacker, damage, isSummon, skill);
	}
	
	public static void main(String[] args)
	{
		new ResidenceOfKingIgnis();
	}
}
