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

import java.util.Map;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.instancemanager.RankManager;
import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author NviX
 */
public class ExRankingCharInfo extends ServerPacket
{
	private final Player _player;
	private final Map<Integer, StatSet> _playerList;
	private final Map<Integer, StatSet> _snapshotList;
	
	public ExRankingCharInfo(Player player)
	{
		_player = player;
		_playerList = RankManager.getInstance().getRankList();
		_snapshotList = RankManager.getInstance().getSnapshotList();
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_RANKING_CHAR_INFO.writeId(this, buffer);
		if (!_playerList.isEmpty())
		{
			for (Integer id : _playerList.keySet())
			{
				final StatSet player = _playerList.get(id);
				if (player.getInt("charId") == _player.getObjectId())
				{
					buffer.writeInt(id); // server rank
					buffer.writeInt(player.getInt("raceRank")); // race rank
					buffer.writeInt(player.getInt("classRank")); // class rank
					for (Integer id2 : _snapshotList.keySet())
					{
						final StatSet snapshot = _snapshotList.get(id2);
						if (player.getInt("charId") == snapshot.getInt("charId"))
						{
							buffer.writeInt(id2); // server rank snapshot
							buffer.writeInt(snapshot.getInt("classRank")); // class rank snapshot
							buffer.writeInt(player.getInt("classRank")); // class rank snapshot
							buffer.writeInt(0);
							buffer.writeInt(0);
							buffer.writeInt(0);
							return;
						}
					}
				}
			}
			
			buffer.writeInt(0); // server rank
			buffer.writeInt(0); // race rank
			buffer.writeInt(0); // server rank snapshot
			buffer.writeInt(0); // race rank snapshot
			buffer.writeInt(0); // nClassRank
			buffer.writeInt(0); // nClassRank_Snapshot snapshot
		}
		else
		{
			buffer.writeInt(0); // server rank
			buffer.writeInt(0); // race rank
			buffer.writeInt(0); // server rank snapshot
			buffer.writeInt(0); // race rank snapshot
			buffer.writeInt(0); // nClassRank
			buffer.writeInt(0); // nClassRank_Snapshot snapshot
		}
	}
}
