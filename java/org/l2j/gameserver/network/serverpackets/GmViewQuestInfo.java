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
package org.l2j.gameserver.network.serverpackets;

import java.util.Collection;

import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.quest.Quest;
import org.l2j.gameserver.model.quest.QuestState;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author Tempy
 */
public class GmViewQuestInfo extends ServerPacket
{
	private final Player _player;
	private final Collection<Quest> _questList;
	
	public GmViewQuestInfo(Player player)
	{
		_player = player;
		_questList = player.getAllActiveQuests();
	}
	
	@Override
	public void write()
	{
		ServerPackets.GM_VIEW_QUEST_INFO.writeId(this);
		writeString(_player.getName());
		writeShort(_questList.size()); // quest count
		for (Quest quest : _questList)
		{
			final QuestState qs = _player.getQuestState(quest.getName());
			writeInt(quest.getId());
			writeInt(qs == null ? 0 : qs.getCond());
		}
		writeShort(0); // some size
		// for size; ddQQ
	}
}
