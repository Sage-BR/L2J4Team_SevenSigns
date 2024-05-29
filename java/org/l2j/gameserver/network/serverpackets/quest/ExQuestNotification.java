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

import org.l2j.gameserver.model.quest.QuestState;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Magik
 */
public class ExQuestNotification extends ServerPacket
{
	private final QuestState _questState;
	
	public ExQuestNotification(QuestState questState)
	{
		_questState = questState;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_QUEST_NOTIFICATION.writeId(this);
		writeInt(_questState.getQuest().getId());
		writeInt(_questState.getCount());
		writeByte(_questState.getState());
	}
}
