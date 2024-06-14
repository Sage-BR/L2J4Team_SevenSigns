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
package instances.DreamDungeon.GustavsManor;

import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.instancezone.Instance;

import ai.AbstractNpcAI;

/**
 * @author Index
 */
public class WoodHorse extends AbstractNpcAI
{
	private static final int WOOD_HORSE_NPC_ID = 18673;
	
	private WoodHorse()
	{
		addFirstTalkId(WOOD_HORSE_NPC_ID);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		final Instance instance = ((player == null) || (npc == null)) ? null : player.getInstanceWorld();
		if ((instance == null) || (instance.getTemplateId() != GustavsManor.INSTANCE_ID))
		{
			return super.onFirstTalk(npc, player);
		}
		
		if (instance.getStatus() == GustavsManor.TALK_WITH_HORSIE_IN_MANOR)
		{
			instance.setStatus(GustavsManor.BOSS_FIGHT);
		}
		
		return super.onFirstTalk(npc, player);
	}
	
	public static void main(String[] args)
	{
		new WoodHorse();
	}
}
