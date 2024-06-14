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

import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.instancezone.Instance;

import ai.AbstractNpcAI;

/**
 * @author Index
 */
public class BrokenBallista extends AbstractNpcAI
{
	private static final int BROKEN_BALLISTA_NPC_ID = 18675;
	
	private BrokenBallista()
	{
		addSpawnId(BROKEN_BALLISTA_NPC_ID);
		addFirstTalkId(BROKEN_BALLISTA_NPC_ID);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		final Instance instance = npc == null ? null : npc.getInstanceWorld();
		if ((instance == null) || (instance.getTemplateId() != VentusTemple.INSTANCE_ID))
		{
			return super.onSpawn(npc);
		}
		
		if ((instance.getStatus() == VentusTemple.SHOOT_FROM_BALLISTA) && (npc != null))
		{
			npc.setRandomAnimation(true);
		}
		
		return super.onSpawn(npc);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		if ((player == null) || (npc == null))
		{
			return super.onFirstTalk(npc, player);
		}
		
		final Instance instance = player.getInstanceWorld();
		if ((instance == null) || (instance.getTemplateId() != VentusTemple.INSTANCE_ID))
		{
			return super.onFirstTalk(npc, player);
		}
		
		if (instance.getStatus() == VentusTemple.SHOOT_FROM_BALLISTA)
		{
			instance.setStatus(VentusTemple.BOSS_FIGHT);
			npc.setTargetable(false);
			player.setTarget(null);
		}
		
		return super.onFirstTalk(npc, player);
	}
	
	public static void main(String[] args)
	{
		new BrokenBallista();
	}
}
