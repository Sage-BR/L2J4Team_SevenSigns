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
package instances.DreamDungeon.CatGuildsLair;

import java.util.ArrayList;
import java.util.List;

import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.instancezone.Instance;

import ai.AbstractNpcAI;

/**
 * @author Index
 */
public class TeleporterCat extends AbstractNpcAI
{
	private static final List<Integer> INSTANCE_IDS = new ArrayList<>(List.of(221, 222, 223, 224));
	public static final int TELEPORTER_CAT_NPC_ID = 34316;
	
	private TeleporterCat()
	{
		addFirstTalkId(TELEPORTER_CAT_NPC_ID);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		final Instance instance = ((player == null) || (npc == null)) ? null : player.getInstanceWorld();
		if ((instance == null) || !INSTANCE_IDS.contains(instance.getTemplateId()))
		{
			return super.onFirstTalk(npc, player);
		}
		
		switch (instance.getStatus())
		{
			case CatGuildsLair.CREATED:
			{
				instance.setStatus(CatGuildsLair.TALK_WITH_TELEPORTER_CAT);
				break;
			}
			case CatGuildsLair.TALK_WITH_TELEPORTER_CAT:
			{
				instance.setStatus(CatGuildsLair.KILL_FOUR_DREAM_WATCHERS_INSIDE);
				break;
			}
			case CatGuildsLair.FINISH_INSTANCE:
			{
				instance.destroy();
				break;
			}
		}
		
		return super.onFirstTalk(npc, player);
	}
	
	public static void main(String[] args)
	{
		new TeleporterCat();
	}
}
