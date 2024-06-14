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
package ai.others.OrcFortress;

import java.util.HashSet;
import java.util.Set;

import org.l2jmobius.gameserver.ai.CtrlIntention;
import org.l2jmobius.gameserver.model.World;
import org.l2jmobius.gameserver.model.actor.Attackable;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;

import ai.AbstractNpcAI;

/**
 * @author Serenitty
 */
public class RunnersOrcs extends AbstractNpcAI
{
	// NPCs
	private static final Set<Integer> ORC_GUARDS = new HashSet<>();
	static
	{
		ORC_GUARDS.add(22175);
		ORC_GUARDS.add(22176);
		ORC_GUARDS.add(22177);
		ORC_GUARDS.add(22178);
		ORC_GUARDS.add(22179);
		ORC_GUARDS.add(22180);
		ORC_GUARDS.add(22181);
		
	}
	private static final Set<Integer> ORC_GUARDS_ALL = new HashSet<>();
	static
	{
		ORC_GUARDS_ALL.add(22164);
		ORC_GUARDS_ALL.add(22165);
		ORC_GUARDS_ALL.add(22166);
		ORC_GUARDS_ALL.add(22167);
		ORC_GUARDS_ALL.add(22168);
		ORC_GUARDS_ALL.add(22169);
		ORC_GUARDS_ALL.add(22170);
		ORC_GUARDS_ALL.add(22171);
		ORC_GUARDS_ALL.add(22172);
		ORC_GUARDS_ALL.add(22173);
		ORC_GUARDS_ALL.add(22174);
		ORC_GUARDS_ALL.add(22175);
		ORC_GUARDS_ALL.add(22176);
		ORC_GUARDS_ALL.add(22177);
		ORC_GUARDS_ALL.add(22178);
		ORC_GUARDS_ALL.add(22179);
		ORC_GUARDS_ALL.add(22180);
		ORC_GUARDS_ALL.add(22181);
		
	}
	private static final int ORC_FORTRESS_FLAG = 18397;
	
	public RunnersOrcs()
	{
		addSpawnId(ORC_GUARDS);
		addCreatureSeeId(ORC_GUARDS_ALL);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		World.getInstance().forEachVisibleObject(npc, Npc.class, flag ->
		{
			if ((flag.getId() == ORC_FORTRESS_FLAG))
			{
				npc.setRunning();
				((Attackable) npc).addDamageHate(flag, 0, 999999);
				npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, flag);
			}
		});
		return super.onSpawn(npc);
	}
	
	@Override
	public String onCreatureSee(Npc npc, Creature creature)
	{
		if (creature.isPlayer())
		{
			World.getInstance().forEachVisibleObjectInRange(npc, Player.class, 800, players ->
			{
				npc.setRunning();
				((Attackable) npc).addDamageHate(players, 0, 999999);
				npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, players);
			});
		}
		return super.onCreatureSee(npc, creature);
	}
	
	public static void main(String[] args)
	{
		new RunnersOrcs();
	}
}
