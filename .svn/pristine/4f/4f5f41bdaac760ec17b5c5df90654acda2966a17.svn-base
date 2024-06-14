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
package instances.DreamDungeon.DraconidFortress;

import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.instancezone.Instance;

import ai.AbstractNpcAI;

/**
 * @author Index
 */
public class RedDraconidTraitor extends AbstractNpcAI
{
	public static final int RED_DRACONIT_TRAITOR_NPC_ID = 34315;
	
	private RedDraconidTraitor()
	{
		addFirstTalkId(RED_DRACONIT_TRAITOR_NPC_ID);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		final Instance instance = ((player == null) || (npc == null)) ? null : player.getInstanceWorld();
		if ((instance == null) || (instance.getTemplateId() != DraconidFortress.INSTANCE_ID))
		{
			return super.onFirstTalk(npc, player);
		}
		
		switch (instance.getStatus())
		{
			case DraconidFortress.CREATED:
			{
				instance.setStatus(DraconidFortress.KILL_FOUR_DREAM_WATCHERS_INSIDE_FIRST_TIME);
				break;
			}
			case DraconidFortress.TALK_WITH_TRAITOR_FIRST_TIME:
			{
				instance.setStatus(DraconidFortress.TALK_WITH_TRAITOR_SECOND_TIME);
				break;
			}
			case DraconidFortress.TALK_WITH_TRAITOR_SECOND_TIME:
			{
				instance.setStatus(DraconidFortress.KILL_FOUR_DREAM_WATCHERS_INSIDE_SECOND_TIME);
				break;
			}
			case DraconidFortress.FINISH_INSTANCE:
			{
				instance.destroy();
				break;
			}
		}
		
		return super.onFirstTalk(npc, player);
	}
	
	public static void main(String[] args)
	{
		new RedDraconidTraitor();
	}
}
