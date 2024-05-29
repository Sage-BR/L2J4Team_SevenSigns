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
package org.l2j.gameserver.network.serverpackets.mentoring;

import java.util.ArrayList;
import java.util.List;

import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author UnAfraid
 */
public class ListMenteeWaiting extends ServerPacket
{
	private static final int PLAYERS_PER_PAGE = 64;
	
	private final List<Player> _possibleCandiates = new ArrayList<>();
	private final int _page;
	
	public ListMenteeWaiting(int page, int minLevel, int maxLevel)
	{
		_page = page;
		// for (Player player : World.getInstance().getPlayers())
		// {
		// if ((player.getLevel() >= minLevel) && (player.getLevel() <= maxLevel) && !player.isMentee() && !player.isMentor() && !player.isInCategory(CategoryType.SIXTH_CLASS_GROUP))
		// {
		// _possibleCandiates.add(player);
		// }
		// }
	}
	
	@Override
	public void write()
	{
		ServerPackets.LIST_MENTEE_WAITING.writeId(this);
		writeInt(1); // always 1 in retail
		if (_possibleCandiates.isEmpty())
		{
			writeInt(0);
			writeInt(0);
			return;
		}
		
		writeInt(_possibleCandiates.size());
		writeInt(_possibleCandiates.size() % PLAYERS_PER_PAGE);
		for (Player player : _possibleCandiates)
		{
			if ((1 <= (PLAYERS_PER_PAGE * _page)) && (1 > (PLAYERS_PER_PAGE * (_page - 1))))
			{
				writeString(player.getName());
				writeInt(player.getActiveClass());
				writeInt(player.getLevel());
			}
		}
	}
}
