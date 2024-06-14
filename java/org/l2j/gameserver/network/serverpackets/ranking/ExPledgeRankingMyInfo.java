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

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.instancemanager.RankManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Berezkin Nikolay
 */
public class ExPledgeRankingMyInfo extends ServerPacket
{
	private final Player _player;
	
	public ExPledgeRankingMyInfo(Player player)
	{
		_player = player;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_PLEDGE_RANKING_MY_INFO.writeId(this, buffer);
		buffer.writeInt(_player.getClan() != null ? RankManager.getInstance().getClanRankList().entrySet().stream().anyMatch(it -> it.getValue().getInt("clan_id") == _player.getClanId()) ? RankManager.getInstance().getClanRankList().entrySet().stream().filter(it -> it.getValue().getInt("clan_id") == _player.getClanId()).findFirst().orElse(null).getKey() : 0 : 0); // rank
		buffer.writeInt(_player.getClan() != null ? RankManager.getInstance().getSnapshotClanRankList().entrySet().stream().anyMatch(it -> it.getValue().getInt("clan_id") == _player.getClanId()) ? RankManager.getInstance().getSnapshotClanRankList().entrySet().stream().filter(it -> it.getValue().getInt("clan_id") == _player.getClanId()).findFirst().orElse(null).getKey() : 0 : 0); // snapshot
		buffer.writeInt(_player.getClan() != null ? _player.getClan().getExp() : 0); // exp
	}
}
