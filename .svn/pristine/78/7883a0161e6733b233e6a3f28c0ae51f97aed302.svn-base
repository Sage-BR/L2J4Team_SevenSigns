/*
 * This file is part of the L2J Mobius project.
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
package org.l2jmobius.gameserver.network.serverpackets.ranking;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.data.xml.PetDataTable;
import org.l2jmobius.gameserver.instancemanager.RankManager;
import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Berezkin Nikolay
 */
public class ExPetRankingMyInfo extends ServerPacket
{
	private final int _petId;
	private final Player _player;
	private final Optional<Entry<Integer, StatSet>> _ranking;
	private final Optional<Entry<Integer, StatSet>> _snapshotRanking;
	private final Map<Integer, StatSet> _rankingList;
	private final Map<Integer, StatSet> _snapshotList;
	
	public ExPetRankingMyInfo(Player player, int petId)
	{
		_player = player;
		_petId = petId;
		_ranking = RankManager.getInstance().getPetRankList().entrySet().stream().filter(it -> it.getValue().getInt("controlledItemObjId") == petId).findFirst();
		_snapshotRanking = RankManager.getInstance().getSnapshotPetRankList().entrySet().stream().filter(it -> it.getValue().getInt("controlledItemObjId") == petId).findFirst();
		_rankingList = RankManager.getInstance().getPetRankList();
		_snapshotList = RankManager.getInstance().getSnapshotPetRankList();
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_PET_RANKING_MY_INFO.writeId(this, buffer);
		buffer.writeInt(_petId);
		buffer.writeShort(1);
		buffer.writeInt(-1);
		buffer.writeInt(0);
		buffer.writeInt(_ranking.isPresent() ? _ranking.get().getKey() : 0); // server rank
		buffer.writeInt(_snapshotRanking.isPresent() ? _snapshotRanking.get().getKey() : 0); // snapshot server rank
		if (_petId > 0)
		{
			int typeRank = 1;
			boolean found = false;
			for (StatSet ss : _rankingList.values())
			{
				if (ss.getInt("petType", -1) == PetDataTable.getInstance().getTypeByIndex(_player.getPetEvolve(_petId).getIndex()))
				{
					if (ss.getInt("controlledItemObjId", -1) == _petId)
					{
						found = true;
						buffer.writeInt(typeRank);
						break;
					}
					typeRank++;
				}
			}
			if (!found)
			{
				buffer.writeInt(0);
			}
			int snapshotTypeRank = 1;
			boolean snapshotFound = false;
			for (StatSet ss : _snapshotList.values())
			{
				if (ss.getInt("petType", -1) == PetDataTable.getInstance().getTypeByIndex(_player.getPetEvolve(_petId).getIndex()))
				{
					if (ss.getInt("controlledItemObjId", -1) == _petId)
					{
						snapshotFound = true;
						buffer.writeInt(snapshotTypeRank);
						break;
					}
					snapshotTypeRank++;
				}
			}
			if (!snapshotFound)
			{
				buffer.writeInt(0);
			}
		}
		else
		{
			buffer.writeInt(0);
			buffer.writeInt(0);
		}
	}
}
