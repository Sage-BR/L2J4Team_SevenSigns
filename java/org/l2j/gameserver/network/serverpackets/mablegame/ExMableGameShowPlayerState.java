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
package org.l2j.gameserver.network.serverpackets.mablegame;

import java.util.List;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.data.xml.MableGameData;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.ItemHolder;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

public class ExMableGameShowPlayerState extends ServerPacket
{
	private final int _commonDiceLimit;
	private final int _dailyAvailableRounds;
	private final int _highestCellId;
	private final ItemHolder _finishReward;
	private final List<ItemHolder> _resetItems;
	private final MableGameData.MableGamePlayerState _playerState;
	
	public ExMableGameShowPlayerState(Player player)
	{
		final MableGameData data = MableGameData.getInstance();
		_commonDiceLimit = data.getCommonDiceLimit();
		_dailyAvailableRounds = data.getDailyAvailableRounds();
		_highestCellId = data.getHighestCellId();
		_finishReward = data.getRoundReward();
		_resetItems = data.getResetItems();
		_playerState = data.getPlayerState(player.getAccountName());
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_MABLE_GAME_SHOW_PLAYER_STATE.writeId(this, buffer);
		buffer.writeInt(_playerState.getRound());
		buffer.writeInt(_playerState.getCurrentCellId());
		buffer.writeInt(_playerState.getRemainCommonDice());
		buffer.writeInt(_commonDiceLimit);
		buffer.writeByte(_playerState.getCurrentCellId() == _highestCellId ? (_dailyAvailableRounds == _playerState.getRound() ? 6 : 5) : 0); // (cCurrentState // 0-3 unk / 4 = just finished / buttoncCurrentState / 5 = reset / 6 = all rounds completed)
		buffer.writeInt(_dailyAvailableRounds); // FinishRewards
		for (int i = 1; i <= _dailyAvailableRounds; i++)
		{
			buffer.writeInt(i); // PlayCount
			buffer.writeInt(_finishReward.getId());
			buffer.writeLong(_finishReward.getCount());
		}
		buffer.writeInt(_resetItems.size()); // ResetItems
		for (ItemHolder item : _resetItems)
		{
			buffer.writeInt(item.getId());
			buffer.writeLong(item.getCount());
		}
	}
}
