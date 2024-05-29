/*
 * This file is part of the L2J 4Team project.
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
package org.l2j.gameserver.network.serverpackets.quest;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.l2j.gameserver.data.xml.NewQuestData;
import org.l2j.gameserver.instancemanager.QuestManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.quest.Quest;
import org.l2j.gameserver.model.quest.QuestState;
import org.l2j.gameserver.model.quest.newquestdata.NewQuest;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Magik
 */
public class ExQuestAcceptableList extends ServerPacket
{
	public final Player _player;
	
	public ExQuestAcceptableList(Player player)
	{
		_player = player;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_QUEST_ACCEPTABLE_LIST.writeId(this);
		
		final List<Quest> availableQuests = new LinkedList<>();
		final Collection<NewQuest> newQuests = NewQuestData.getInstance().getQuests();
		final QuestManager questManager = QuestManager.getInstance();
		
		for (NewQuest newQuest : newQuests)
		{
			final Quest quest = questManager.getQuest(newQuest.getId());
			if ((quest != null) && quest.canStartQuest(_player))
			{
				final QuestState questState = _player.getQuestState(quest.getName());
				if (questState == null)
				{
					availableQuests.add(quest);
				}
			}
		}
		
		writeInt(availableQuests.size());
		availableQuests.forEach(quest -> writeInt(quest.getId()));
	}
}
