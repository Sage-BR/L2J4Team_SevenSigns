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
package instances.DreamDungeon.BenedictsMonastery;

import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.instancezone.Instance;

import ai.AbstractNpcAI;

/**
 * @author Index
 */
public class HolyGrailsRaider extends AbstractNpcAI
{
	private static final int HOLY_GRAILS_RIDER = 34313;
	
	private HolyGrailsRaider()
	{
		addFirstTalkId(HOLY_GRAILS_RIDER);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		final Instance instance = ((player == null) || (npc == null)) ? null : player.getInstanceWorld();
		if ((instance == null) || (instance.getTemplateId() != BenedictsMonastery.INSTANCE_ID))
		{
			return super.onFirstTalk(npc, player);
		}
		
		switch (instance.getStatus())
		{
			case BenedictsMonastery.TALK_WITH_HOLY_GRAILS_RAIDER_NPC_ON_START:
			{
				instance.setStatus(BenedictsMonastery.KILL_FOUR_DREAM_WATCHERS_INSIDE);
				break;
			}
			case BenedictsMonastery.TALK_WITH_HOLY_GRAILS_RAIDER_NPC_TO_OPEN_DOORS:
			{
				instance.setStatus(BenedictsMonastery.TALK_WITH_HOLY_GRAIL_BEFORE_BOSS);
				break;
			}
			case BenedictsMonastery.FINISH_INSTANCE:
			{
				instance.destroy();
				break;
			}
		}
		
		return super.onFirstTalk(npc, player);
	}
	
	public static void main(String[] args)
	{
		new HolyGrailsRaider();
	}
}
