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
package org.l2j.gameserver.network.serverpackets.ranking;

import java.util.Map;
import java.util.Optional;

import org.l2j.gameserver.instancemanager.RankManager;
import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Berezkin Nikolay
 */
public class ExPvpRankingMyInfo extends ServerPacket
{
	private final Player _player;
	private final Map<Integer, StatSet> _playerList;
	private final Map<Integer, StatSet> _snapshotList;
	
	public ExPvpRankingMyInfo(Player player)
	{
		_player = player;
		_playerList = RankManager.getInstance().getPvpRankList();
		_snapshotList = RankManager.getInstance().getSnapshotPvpRankList();
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_PVP_RANKING_MY_INFO.writeId(this);
		if (!_playerList.isEmpty())
		{
			boolean found = false;
			for (Integer id : _playerList.keySet())
			{
				final StatSet ss = _playerList.get(id);
				if (ss.getInt("charId") == _player.getObjectId())
				{
					final Optional<Map.Entry<Integer, StatSet>> snapshotValue = _snapshotList.entrySet().stream().filter(it -> it.getValue().getInt("charId") == _player.getObjectId()).findFirst();
					found = true;
					writeLong(ss.getInt("points")); // pvp points
					writeInt(id); // current rank
					writeInt(snapshotValue.isPresent() ? snapshotValue.get().getKey() : id); // ingame shown change in rank as this value - current rank value.
					writeInt(ss.getInt("kills")); // kills
					writeInt(ss.getInt("deaths")); // deaths
				}
			}
			if (!found)
			{
				writeLong(0); // pvp points
				writeInt(0); // current rank
				writeInt(0); // ingame shown change in rank as this value - current rank value.
				writeInt(0); // kills
				writeInt(0); // deaths
			}
		}
		else
		{
			writeLong(0); // pvp points
			writeInt(0); // current rank
			writeInt(0); // ingame shown change in rank as this value - current rank value.
			writeInt(0); // kills
			writeInt(0); // deaths
		}
	}
}
