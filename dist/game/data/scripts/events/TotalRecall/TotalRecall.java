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
package events.TotalRecall;

import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.SkillHolder;
import org.l2j.gameserver.model.quest.LongTimeEvent;
import org.l2j.gameserver.model.skill.SkillCaster;

/**
 * Total Recall Event
 * @URL https://eu.4gameforum.com/threads/578395/
 * @author QuangNguyen
 */
public class TotalRecall extends LongTimeEvent
{
	// NPC
	private static final int FROG = 9013;
	// Skill
	private static final SkillHolder FROG_KISS = new SkillHolder(55314, 1);
	
	private TotalRecall()
	{
		addStartNpc(FROG);
		addFirstTalkId(FROG);
		addTalkId(FROG);
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		String htmltext = null;
		switch (event)
		{
			case "9013-1.htm":
			case "9013-2.htm":
			case "9013-3.htm":
			{
				htmltext = event;
				break;
			}
			case "frog_buff":
			{
				SkillCaster.triggerCast(npc, player, FROG_KISS.getSkill());
				htmltext = "9013-4.htm";
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		return "9013-1.htm";
	}
	
	public static void main(String[] args)
	{
		new TotalRecall();
	}
}
