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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.l2j.gameserver.data.sql.ClanTable;
import org.l2j.gameserver.instancemanager.RankManager;
import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Berezkin Nikolay
 */
public class ExPledgeRankingList extends ServerPacket
{
	private final Player _player;
	private final int _category;
	private final Map<Integer, StatSet> _rankingClanList;
	private final Map<Integer, StatSet> _snapshotClanList;
	
	public ExPledgeRankingList(Player player, int category)
	{
		_player = player;
		_category = category;
		_rankingClanList = RankManager.getInstance().getClanRankList();
		_snapshotClanList = RankManager.getInstance().getSnapshotClanRankList();
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_PLEDGE_RANKING_LIST.writeId(this);
		writeByte(_category);
		if (!_rankingClanList.isEmpty())
		{
			writeScopeData(_category == 0, new ArrayList<>(_rankingClanList.entrySet()), new ArrayList<>(_snapshotClanList.entrySet()));
		}
		else
		{
			writeInt(0);
		}
	}
	
	private void writeScopeData(boolean isTop150, List<Entry<Integer, StatSet>> list, List<Entry<Integer, StatSet>> snapshot)
	{
		Entry<Integer, StatSet> playerData = list.stream().filter(it -> it.getValue().getInt("clan_id", 0) == _player.getClanId()).findFirst().orElse(null);
		final int indexOf = list.indexOf(playerData);
		final List<Entry<Integer, StatSet>> limited = isTop150 ? list.stream().limit(150).collect(Collectors.toList()) : playerData == null ? Collections.emptyList() : list.subList(Math.max(0, indexOf - 10), Math.min(list.size(), indexOf + 10));
		writeInt(limited.size());
		int rank = 1;
		for (Entry<Integer, StatSet> data : limited.stream().sorted(Entry.comparingByKey()).collect(Collectors.toList()))
		{
			int curRank = rank++;
			final StatSet player = data.getValue();
			writeInt(!isTop150 ? data.getKey() : curRank);
			for (Entry<Integer, StatSet> ssData : snapshot.stream().sorted(Entry.comparingByKey()).collect(Collectors.toList()))
			{
				final StatSet snapshotData = ssData.getValue();
				if (player.getInt("clan_id") == snapshotData.getInt("clan_id"))
				{
					writeInt(!isTop150 ? ssData.getKey() : curRank); // server rank snapshot
				}
			}
			writeSizedString(player.getString("clan_name"));
			writeInt(player.getInt("clan_level"));
			writeSizedString(player.getString("char_name"));
			writeInt(player.getInt("level"));
			writeInt(ClanTable.getInstance().getClan(player.getInt("clan_id")) != null ? ClanTable.getInstance().getClan(player.getInt("clan_id")).getMembersCount() : 0);
			writeInt((int) Math.min(Integer.MAX_VALUE, player.getLong("exp")));
		}
	}
}
