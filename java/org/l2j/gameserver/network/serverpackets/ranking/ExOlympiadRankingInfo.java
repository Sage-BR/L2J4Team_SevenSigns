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

import org.l2j.Config;

import org.l2j.gameserver.enums.ClassId;
import org.l2j.gameserver.enums.RankingOlympiadCategory;
import org.l2j.gameserver.enums.RankingOlympiadScope;
import org.l2j.gameserver.instancemanager.RankManager;
import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Berezkin Nikolay
 */
public class ExOlympiadRankingInfo extends ServerPacket
{
	private final Player _player;
	private final int _tabId;
	private final int _rankingType;
	private final int _unk;
	private final int _classId;
	private final int _serverId;
	private final Map<Integer, StatSet> _playerList;
	private final Map<Integer, StatSet> _snapshotList;
	
	public ExOlympiadRankingInfo(Player player, int tabId, int rankingType, int unk, int classId, int serverId)
	{
		_player = player;
		_tabId = tabId;
		_rankingType = rankingType;
		_unk = unk;
		_classId = classId;
		_serverId = serverId;
		_playerList = RankManager.getInstance().getOlyRankList();
		_snapshotList = RankManager.getInstance().getSnapshotOlyList();
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_OLYMPIAD_RANKING_INFO.writeId(this);
		writeByte(_tabId); // Tab id
		writeByte(_rankingType); // ranking type
		writeByte(_unk); // unk, shows 1 all time
		writeInt(_classId); // class id (default 148) or caller class id for personal rank
		writeInt(_serverId); // 0 - all servers, server id - for caller server
		writeInt(933); // unk, 933 all time
		if (!_playerList.isEmpty())
		{
			final RankingOlympiadCategory category = RankingOlympiadCategory.values()[_tabId];
			writeFilteredRankingData(category, category.getScopeByGroup(_rankingType), ClassId.getClassId(_classId));
		}
	}
	
	private void writeFilteredRankingData(RankingOlympiadCategory category, RankingOlympiadScope scope, ClassId classId)
	{
		switch (category)
		{
			case SERVER:
			{
				writeScopeData(scope, new ArrayList<>(_playerList.entrySet()), new ArrayList<>(_snapshotList.entrySet()));
				break;
			}
			case CLASS:
			{
				writeScopeData(scope, _playerList.entrySet().stream().filter(it -> it.getValue().getInt("classId") == classId.getId()).collect(Collectors.toList()), _snapshotList.entrySet().stream().filter(it -> it.getValue().getInt("classId") == classId.getId()).collect(Collectors.toList()));
				break;
			}
		}
	}
	
	private void writeScopeData(RankingOlympiadScope scope, List<Entry<Integer, StatSet>> list, List<Entry<Integer, StatSet>> snapshot)
	{
		Entry<Integer, StatSet> playerData = list.stream().filter(it -> it.getValue().getInt("charId", 0) == _player.getObjectId()).findFirst().orElse(null);
		final int indexOf = list.indexOf(playerData);
		final List<Entry<Integer, StatSet>> limited;
		switch (scope)
		{
			case TOP_100:
			{
				limited = list.stream().limit(100).collect(Collectors.toList());
				break;
			}
			case ALL:
			{
				limited = list;
				break;
			}
			case TOP_50:
			{
				limited = list.stream().limit(50).collect(Collectors.toList());
				break;
			}
			case SELF:
			{
				limited = playerData == null ? Collections.emptyList() : list.subList(Math.max(0, indexOf - 10), Math.min(list.size(), indexOf + 10));
				break;
			}
			default:
			{
				limited = Collections.emptyList();
			}
		}
		writeInt(limited.size());
		int rank = 1;
		for (Entry<Integer, StatSet> data : limited.stream().sorted(Entry.comparingByKey()).collect(Collectors.toList()))
		{
			int curRank = rank++;
			final StatSet player = data.getValue();
			writeSizedString(player.getString("name")); // name
			writeSizedString(player.getString("clanName")); // clan name
			writeInt(scope == RankingOlympiadScope.SELF ? data.getKey() : curRank); // rank
			if (!snapshot.isEmpty())
			{
				int snapshotRank = 1;
				for (Entry<Integer, StatSet> ssData : snapshot.stream().sorted(Entry.comparingByKey()).collect(Collectors.toList()))
				{
					final StatSet snapshotData = ssData.getValue();
					if (player.getInt("charId") == snapshotData.getInt("charId"))
					{
						writeInt(scope == RankingOlympiadScope.SELF ? ssData.getKey() : snapshotRank++); // previous rank
					}
				}
			}
			else
			{
				writeInt(scope == RankingOlympiadScope.SELF ? data.getKey() : curRank);
			}
			writeInt(Config.SERVER_ID); // server id
			writeInt(player.getInt("level")); // level
			writeInt(player.getInt("classId")); // class id
			writeInt(player.getInt("clanLevel")); // clan level
			writeInt(player.getInt("competitions_won")); // win count
			writeInt(player.getInt("competitions_lost")); // lose count
			writeInt(player.getInt("olympiad_points")); // points
			writeInt(player.getInt("legend_count")); // legend count
			writeInt(player.getInt("count")); // hero count
		}
	}
}
