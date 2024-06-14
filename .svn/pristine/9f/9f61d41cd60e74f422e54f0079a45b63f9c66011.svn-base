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
import org.l2jmobius.gameserver.network.NpcStringId;
import org.l2jmobius.gameserver.network.serverpackets.ExShowScreenMessage;

import ai.AbstractNpcAI;

/**
 * @author Index
 */
public class Rascal extends AbstractNpcAI
{
	private static final int RASCAL_NPC_ID = 34312;
	
	// NPC STRINGS
	private static final NpcStringId STRING_ID_01 = NpcStringId.MASTER_GUSTAV_TOOK_MY_HORSIE;
	private static final NpcStringId STRING_ID_02 = NpcStringId.THE_HORSIE_IS_IN_THE_MANOR_I_WILL_OPEN_THE_DOORS_AND_WE_LL_GO_IN_AND_PLAY;
	
	private Rascal()
	{
		addFirstTalkId(RASCAL_NPC_ID);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		final Instance instance = ((player == null) || (npc == null)) ? null : player.getInstanceWorld();
		if ((instance == null) || (instance.getTemplateId() != GustavsManor.INSTANCE_ID))
		{
			return super.onFirstTalk(npc, player);
		}
		
		switch (instance.getStatus())
		{
			case GustavsManor.CREATED:
			{
				instance.setStatus(GustavsManor.GO_TO_GATES_AND_KILL_GIRL);
				// Master Gustav took my horsie.
				instance.broadcastPacket(new ExShowScreenMessage(STRING_ID_01, ExShowScreenMessage.TOP_CENTER, 10000, true));
				break;
			}
			case GustavsManor.GO_TO_GATES_AND_KILL_GIRL:
			{
				return super.onFirstTalk(npc, player);
			}
			case GustavsManor.TALK_WITH_RASCAL:
			{
				instance.setStatus(GustavsManor.KILL_FOUR_DREAM_WATCHERS_INSIDE);
				// The horsie is in the manor. I will open the doors and we'll go in and play.
				instance.broadcastPacket(new ExShowScreenMessage(STRING_ID_02, ExShowScreenMessage.TOP_CENTER, 10000, true));
				break;
			}
			case GustavsManor.FINISH_INSTANCE:
			{
				// instance.getPlayers().forEach(p -> p.sendPacket(new TimedHuntingZoneExit(0)));
				instance.destroy();
				break;
			}
		}
		
		return super.onFirstTalk(npc, player);
	}
	
	public static void main(String[] args)
	{
		new Rascal();
	}
}
