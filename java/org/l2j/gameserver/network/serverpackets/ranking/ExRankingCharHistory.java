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
package org.l2j.gameserver.network.serverpackets.ranking;

import java.util.Collection;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.RankingHistoryDataHolder;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Serenitty, Brado, Enryu
 */
public class ExRankingCharHistory extends ServerPacket
{
	private final Player _player;
	private final Collection<RankingHistoryDataHolder> _history;
	
	public ExRankingCharHistory(Player player)
	{
		_player = player;
		_history = _player.getRankingHistoryData();
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_RANKING_CHAR_HISTORY.writeId(this, buffer);
		buffer.writeInt(_history.size());
		if (_history.isEmpty())
		{
			buffer.writeByte(0);
			buffer.writeByte(0);
			buffer.writeByte(0);
		}
		else
		{
			for (RankingHistoryDataHolder rankingData : _history)
			{
				buffer.writeInt((int) (rankingData.getDay()));
				buffer.writeInt(rankingData.getRank());
				buffer.writeLong(rankingData.getExp());
			}
		}
	}
}
