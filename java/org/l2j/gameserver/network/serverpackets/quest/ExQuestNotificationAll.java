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
package org.l2j.gameserver.network.serverpackets.quest;

import java.util.Collection;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.quest.Quest;
import org.l2j.gameserver.model.quest.QuestState;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Magik
 */
public class ExQuestNotificationAll extends ServerPacket
{
	private final Player _activeChar;
	
	public ExQuestNotificationAll(Player activeChar)
	{
		_activeChar = activeChar;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_QUEST_NOTIFICATION_ALL.writeId(this, buffer);
		
		final Collection<Quest> quests = _activeChar.getAllActiveQuests();
		
		buffer.writeInt(quests.size());
		for (Quest quest : quests)
		{
			final QuestState questState = quest.getQuestState(_activeChar, false);
			buffer.writeInt(quest.getId());
			buffer.writeInt(questState.getCount());
		}
	}
}
