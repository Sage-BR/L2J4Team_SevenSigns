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

import org.l2j.gameserver.enums.RankingCategory;
import org.l2j.gameserver.enums.RankingScope;
import org.l2j.gameserver.instancemanager.RankManager;
import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Mobius
 */
public class ExPetRankingList extends ServerPacket
{
	private final Player _player;
	private final int _season;
	private final int _tabId;
	private final int _type;
	private final int _petItemObjectId;
	private final Map<Integer, StatSet> _playerList;
	private final Map<Integer, StatSet> _snapshotList;
	
	public ExPetRankingList(Player player, int season, int tabId, int type, int petItemObjectId)
	{
		_player = player;
		_season = season;
		_tabId = tabId;
		_type = type;
		_petItemObjectId = petItemObjectId;
		_playerList = RankManager.getInstance().getPetRankList();
		_snapshotList = RankManager.getInstance().getSnapshotPetRankList();
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_PET_RANKING_LIST.writeId(this);
		writeByte(_season);
		writeByte(_tabId);
		writeShort(_type);
		writeInt(_petItemObjectId);
		if (!_playerList.isEmpty())
		{
			final RankingCategory category = RankingCategory.values()[_tabId];
			writeFilteredRankingData(category, category.getScopeByGroup(_season));
		}
		else
		{
			writeInt(0);
		}
	}
	
	private void writeFilteredRankingData(RankingCategory category, RankingScope scope)
	{
		switch (category)
		{
			case SERVER:
			{
				writeScopeData(scope, new ArrayList<>(_playerList.entrySet()), new ArrayList<>(_snapshotList.entrySet()));
				break;
			}
			case RACE:
			{
				writeScopeData(scope, _playerList.entrySet().stream().filter(it -> it.getValue().getInt("petType") == _type).collect(Collectors.toList()), _snapshotList.entrySet().stream().filter(it -> it.getValue().getInt("petType") == _type).collect(Collectors.toList()));
				break;
			}
			case CLAN:
			{
				writeScopeData(scope, _player.getClan() == null ? Collections.emptyList() : _playerList.entrySet().stream().filter(it -> it.getValue().getString("clanName").equals(_player.getClan().getName())).collect(Collectors.toList()), _player.getClan() == null ? Collections.emptyList() : _snapshotList.entrySet().stream().filter(it -> it.getValue().getString("clanName").equals(_player.getClan().getName())).collect(Collectors.toList()));
				break;
			}
			case FRIEND:
			{
				writeScopeData(scope, _playerList.entrySet().stream().filter(it -> _player.getFriendList().contains(it.getValue().getInt("charId"))).collect(Collectors.toList()), _snapshotList.entrySet().stream().filter(it -> _player.getFriendList().contains(it.getValue().getInt("charId"))).collect(Collectors.toList()));
				break;
			}
		}
	}
	
	private void writeScopeData(RankingScope scope, List<Entry<Integer, StatSet>> list, List<Entry<Integer, StatSet>> snapshot)
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
			case TOP_150:
			{
				limited = list.stream().limit(150).collect(Collectors.toList());
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
			final StatSet pet = data.getValue();
			writeSizedString(pet.getString("name"));
			writeSizedString(pet.getString("owner_name"));
			writeSizedString(pet.getString("clanName"));
			writeInt(1000000 + pet.getInt("npcId"));
			writeShort(pet.getInt("petType"));
			writeShort(pet.getInt("level"));
			writeShort(pet.getInt("owner_race"));
			writeShort(pet.getInt("owner_level"));
			writeInt(scope == RankingScope.SELF ? data.getKey() : curRank); // server rank
			if (!snapshot.isEmpty())
			{
				for (Entry<Integer, StatSet> ssData : snapshot.stream().sorted(Entry.comparingByKey()).collect(Collectors.toList()))
				{
					final StatSet snapshotData = ssData.getValue();
					if (pet.getInt("controlledItemObjId") == snapshotData.getInt("controlledItemObjId"))
					{
						writeInt(scope == RankingScope.SELF ? ssData.getKey() : curRank); // server rank snapshot
					}
				}
			}
			else
			{
				writeInt(scope == RankingScope.SELF ? data.getKey() : curRank); // server rank
			}
		}
	}
}
