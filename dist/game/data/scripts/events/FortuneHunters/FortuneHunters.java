/*
 * This file is part of the L2J 4Team Project.
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
package events.FortuneHunters;

import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.SkillHolder;
import org.l2j.gameserver.model.quest.LongTimeEvent;
import org.l2j.gameserver.model.skill.SkillCaster;

/**
 * @author Mobius
 */
public class FortuneHunters extends LongTimeEvent
{
	// NPC
	private static final int VERLINS_MESSENGER = 9034;
	// Skill
	private static final SkillHolder VERLINS_PROTECTION = new SkillHolder(59853, 1);
	// Misc
	private static final String VERLIN_REWARD_VAR = "VERLIN_REWARD_VAR";
	private static final long REWARD_DELAY = 86400000; // 1 day
	
	private FortuneHunters()
	{
		addFirstTalkId(VERLINS_MESSENGER);
		addTalkId(VERLINS_MESSENGER);
		addSpawnId(VERLINS_MESSENGER);
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		if (event.equals("buff"))
		{
			final long currentTime = System.currentTimeMillis();
			if ((player.getVariables().getLong(VERLIN_REWARD_VAR, 0) - currentTime) > 0)
			{
				return "9034-02.html";
			}
			
			player.getVariables().set(VERLIN_REWARD_VAR, currentTime + REWARD_DELAY);
			SkillCaster.triggerCast(npc, player, VERLINS_PROTECTION.getSkill());
		}
		
		return null;
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		return "9034-01.html";
	}
	
	public static void main(String[] args)
	{
		new FortuneHunters();
	}
}
