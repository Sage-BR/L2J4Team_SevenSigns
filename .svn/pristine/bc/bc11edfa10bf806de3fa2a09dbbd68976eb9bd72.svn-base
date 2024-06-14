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
package instances.ValakasTemple.TombStone;

import java.util.HashMap;
import java.util.Map;

import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.instancezone.Instance;

import ai.AbstractNpcAI;
import instances.ValakasTemple.ValakasTemple;

/**
 * @author Index
 */
public class TombStone extends AbstractNpcAI
{
	// Monsters
	public static final int SEALED_TOMB_STONE = 18727;
	public static final int DUMMY_TOMB_STORE = 18728;
	// Misc
	private static final Map<Integer, Npc> DUMMY_STONE_NPC = new HashMap<>();
	private static final String DUMMY_TOMB_STATE = "DUMMY_TOMB_STATE";
	private static final int TOMB_100 = 0; // NPC state on HP percent
	private static final int TOMB_066 = 1; // 1 chain break
	private static final int TOMB_033 = 2; // 2 chain break
	private static final int TOMB_000 = 3; // all tomb break
	
	private TombStone()
	{
		addAttackId(SEALED_TOMB_STONE);
		addKillId(SEALED_TOMB_STONE);
		addSpawnId(DUMMY_TOMB_STORE);
		addSpawnId(DUMMY_TOMB_STORE);
		addInstanceDestroyId(ValakasTemple.VALAKAS_TEMPLE_INSTANCE_ID);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		final Instance world = npc.getInstanceWorld();
		if ((world == null) || (world.getTemplateId() != ValakasTemple.VALAKAS_TEMPLE_INSTANCE_ID))
		{
			return super.onSpawn(npc);
		}
		
		npc.setDisplayEffect(TOMB_100);
		npc.setUndying(true);
		npc.setTargetable(false);
		npc.setImmobilized(true);
		DUMMY_STONE_NPC.put(world.getId(), npc);
		
		return super.onSpawn(npc);
	}
	
	@Override
	public String onAttack(Npc npc, Player attacker, int damage, boolean isSummon)
	{
		final Instance world = npc.getInstanceWorld();
		if ((world == null) || (world.getTemplateId() != ValakasTemple.VALAKAS_TEMPLE_INSTANCE_ID))
		{
			return super.onSpawn(npc);
		}
		
		if (world.getStatus() != ValakasTemple.KILL_TOMB)
		{
			npc.setImmobilized(true);
			return null;
		}
		
		final Npc dummyStone = DUMMY_STONE_NPC.get(world.getId());
		switch (npc.getCurrentHpPercent())
		{
			case 66:
			{
				dummyStone.setDisplayEffect(TOMB_066);
				world.getParameters().set(DUMMY_TOMB_STATE, TOMB_066);
				break;
			}
			case 50:
			{
				world.spawnGroup("raid_boss_tomb");
				break; // was fallthrough
			}
			case 33:
			{
				dummyStone.setDisplayEffect(TOMB_033);
				world.getParameters().set(DUMMY_TOMB_STATE, TOMB_033);
				break;
			}
		}
		
		return super.onAttack(npc, attacker, damage, isSummon);
	}
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		final Instance world = npc.getInstanceWorld();
		if ((world == null) || (world.getTemplateId() != ValakasTemple.VALAKAS_TEMPLE_INSTANCE_ID))
		{
			return super.onSpawn(npc);
		}
		
		final Npc dummyStone = DUMMY_STONE_NPC.get(world.getId());
		DUMMY_STONE_NPC.remove(world.getId());
		dummyStone.setDisplayEffect(TOMB_000);
		dummyStone.deleteMe();
		world.getParameters().set(DUMMY_TOMB_STATE, TOMB_000);
		world.setStatus(ValakasTemple.GOTO_DUMMY_IFRIT);
		
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public void onInstanceDestroy(Instance instance)
	{
		DUMMY_STONE_NPC.remove(instance.getId());
		super.onInstanceDestroy(instance);
	}
	
	public static void main(String[] args)
	{
		new TombStone();
	}
}
