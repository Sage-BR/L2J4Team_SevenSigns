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
package instances.DreamDungeon.VentusTemple;

import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.instancezone.Instance;

import ai.AbstractNpcAI;

/**
 * @author Index
 */
public class VentusMaid extends AbstractNpcAI
{
	public static final int VENTUS_MAID_NPC_ID = 34314;
	
	private VentusMaid()
	{
		addFirstTalkId(VENTUS_MAID_NPC_ID);
		addCreatureSeeId(VENTUS_MAID_NPC_ID);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		final Instance instance = ((player == null) || (npc == null)) ? null : player.getInstanceWorld();
		if ((instance == null) || (instance.getTemplateId() != VentusTemple.INSTANCE_ID))
		{
			return super.onFirstTalk(npc, player);
		}
		
		switch (instance.getStatus())
		{
			case VentusTemple.TALK_WITH_VANTUS_MAID_ON_CREATE:
			{
				instance.setStatus(VentusTemple.KILL_FOUR_DREAM_WATCHERS_INSIDE);
				break;
			}
			case VentusTemple.TALK_WITH_VENTUS_MAID_FOR_RESPAWN_BALLISTA:
			{
				instance.setStatus(VentusTemple.SHOOT_FROM_BALLISTA);
				break;
			}
			case VentusTemple.FINISH_INSTANCE:
			{
				instance.destroy();
				break;
			}
		}
		
		return super.onFirstTalk(npc, player);
	}
	
	@Override
	public String onCreatureSee(Npc npc, Creature creature)
	{
		if (((creature == null) || !creature.isPlayer() || (npc == null)))
		{
			return super.onCreatureSee(npc, creature);
		}
		
		final Instance instance = creature.getInstanceWorld();
		if ((instance == null) || (instance.getTemplateId() != VentusTemple.INSTANCE_ID))
		{
			return super.onCreatureSee(npc, creature);
		}
		
		if (instance.getStatus() == VentusTemple.GO_TO_VENTUS_ROOM)
		{
			npc.setUndying(true);
			instance.setStatus(VentusTemple.SAVE_VENTUS_MAID_IN_VENTUS_ROOM);
		}
		
		return super.onCreatureSee(npc, creature);
	}
	
	public static void main(String[] args)
	{
		new VentusMaid();
	}
}
