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
package org.l2j.gameserver.network.clientpackets.quest;

import org.l2j.gameserver.enums.QuestType;
import org.l2j.gameserver.instancemanager.QuestManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.events.Containers;
import org.l2j.gameserver.model.events.EventDispatcher;
import org.l2j.gameserver.model.events.EventType;
import org.l2j.gameserver.model.events.impl.creature.player.OnPlayerQuestAbort;
import org.l2j.gameserver.model.quest.Quest;
import org.l2j.gameserver.model.quest.QuestState;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.quest.ExQuestNotificationAll;
import org.l2j.gameserver.network.serverpackets.quest.ExQuestUI;

/**
 * @author Magik, Mobius
 */
public class RequestExQuestCancel extends ClientPacket
{
	private int _questId;
	
	@Override
	protected void readImpl()
	{
		_questId = readInt();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		final Quest quest = QuestManager.getInstance().getQuest(_questId);
		final QuestState qs = quest.getQuestState(player, false);
		if ((qs != null) && !qs.isCompleted())
		{
			qs.setSimulated(false);
			qs.exitQuest(QuestType.REPEATABLE);
			player.sendPacket(new ExQuestUI(player));
			player.sendPacket(new ExQuestNotificationAll(player));
			
			if (EventDispatcher.getInstance().hasListener(EventType.ON_PLAYER_QUEST_ABORT, player, Containers.Players()))
			{
				EventDispatcher.getInstance().notifyEventAsync(new OnPlayerQuestAbort(player, _questId), player, Containers.Players());
			}
			
			quest.onQuestAborted(player);
		}
	}
}
