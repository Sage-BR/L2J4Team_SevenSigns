/*
 * This file is part of the L2J 4Team project.
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
package ai.areas.BeeHive;

import java.util.HashSet;
import java.util.Set;

import org.l2j.gameserver.model.World;
import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.instance.Monster;
import org.l2j.gameserver.model.actor.instance.Pet;
import org.l2j.gameserver.model.holders.SkillHolder;

import ai.AbstractNpcAI;

/**
 * @author Index
 */
public class BeeHive extends AbstractNpcAI
{
	// NPCs
	private static final int PET_70_MONSTER = 22297; // Tag [Slayer] - BUFF
	private static final int PLAYER_70_MONSTER = 22303; // Rusty
	private static final int PET_80_MONSTER = 22302; // Rude Tag [Slayer] - BUFF
	private static final int PLAYER_80_MONSTER = 22304; // Giant Rusty
	private static final Set<Integer> LV_70_MONSTERS = new HashSet<>();
	static
	{
		LV_70_MONSTERS.add(22293);
		LV_70_MONSTERS.add(22294);
		LV_70_MONSTERS.add(22295);
		LV_70_MONSTERS.add(22296);
	}
	private static final Set<Integer> LV_80_MONSTERS = new HashSet<>();
	static
	{
		LV_80_MONSTERS.add(22298);
		LV_80_MONSTERS.add(22299);
		LV_80_MONSTERS.add(22300);
		LV_80_MONSTERS.add(22301);
	}
	// Skills
	private static final SkillHolder[] SKILLS =
	{
		new SkillHolder(48197, 1), // (Lv. 1) Pet Growth Effect
		new SkillHolder(48198, 1) // (Lv. 1) Improved Pet Skills
	};
	// Items
	private static final int TAG_PET_BOX = 94634;
	private static final int LOW_PET_XP_CRYSTAL = 94635;
	// Misc
	private static final long DESPAWN_TIME = 2 * 60 * 1000; // 2 minutes
	
	private BeeHive()
	{
		addKillId(LV_70_MONSTERS);
		addKillId(LV_80_MONSTERS);
		addKillId(PET_70_MONSTER, PET_80_MONSTER);
		addAttackId(PET_70_MONSTER, PET_80_MONSTER);
	}
	
	@Override
	public String onAttack(Npc npc, Player attacker, int damage, boolean isSummon)
	{
		if (!isSummon)
		{
			return super.onAttack(npc, attacker, damage, isSummon);
		}
		
		final Pet pet = attacker.getPet();
		if ((pet == null) || (pet.getCurrentFed() == 0) || pet.isDead() || pet.isAffectedBySkill(SKILLS[0]) || pet.isAffectedBySkill(SKILLS[1]))
		{
			return super.onAttack(npc, attacker, damage, isSummon);
		}
		
		if ((npc.getId() == PET_70_MONSTER) || (npc.getId() == PET_80_MONSTER))
		{
			pet.doCast(getRandomEntry(SKILLS).getSkill());
		}
		
		return super.onAttack(npc, attacker, damage, isSummon);
	}
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		if (killer.hasPet() && ((npc.getId() == PET_70_MONSTER) || (npc.getId() == PET_80_MONSTER)))
		{
			if (getRandom(1000) < 1)
			{
				killer.addItem("Bee hive special monster", LOW_PET_XP_CRYSTAL, 1, killer, true);
			}
			else if (getRandom(100) < 1)
			{
				killer.addItem("Bee hive special monster", TAG_PET_BOX, 1, killer, true);
			}
		}
		else if (getRandomBoolean())
		{
			// Check if already spawned.
			for (Monster monster : World.getInstance().getVisibleObjects(killer, Monster.class))
			{
				if (monster.getScriptValue() == killer.getObjectId())
				{
					return super.onKill(npc, killer, isSummon);
				}
			}
			
			final boolean isLow = LV_70_MONSTERS.contains(npc.getId());
			if (isLow || LV_80_MONSTERS.contains(npc.getId()))
			{
				final Npc spawn;
				if (killer.hasPet())
				{
					spawn = addSpawn(isLow ? PET_70_MONSTER : PET_80_MONSTER, npc.getLocation(), false, DESPAWN_TIME);
				}
				else
				{
					spawn = addSpawn(isLow ? PLAYER_70_MONSTER : PLAYER_80_MONSTER, npc.getLocation(), false, DESPAWN_TIME);
				}
				spawn.setScriptValue(killer.getObjectId());
				spawn.setShowSummonAnimation(true);
				addAttackPlayerDesire(spawn, killer.hasPet() ? killer.getPet() : killer);
			}
		}
		
		return super.onKill(npc, killer, isSummon);
	}
	
	public static void main(String[] args)
	{
		new BeeHive();
	}
}