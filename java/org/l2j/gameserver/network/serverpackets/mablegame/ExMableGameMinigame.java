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

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

public class ExMableGameMinigame extends ServerPacket
{
	private final int _bossType;
	private final int _luckyNumber;
	private final int _dice;
	private final int _bossDice;
	private final int _result;
	private final boolean _isLuckyNumber;
	private final int _rewardItemId;
	private final long _rewardItemCount;
	
	public ExMableGameMinigame(int bossType, int luckyNumber, int dice, int bossDice, int result, boolean isLuckyNumber, int rewardItemId, long rewardItemCount)
	{
		_bossType = bossType;
		_luckyNumber = luckyNumber;
		_dice = dice;
		_bossDice = bossDice;
		_result = result;
		_isLuckyNumber = isLuckyNumber;
		_rewardItemId = rewardItemId;
		_rewardItemCount = rewardItemCount;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_MABLE_GAME_MINIGAME.writeId(this, buffer);
		buffer.writeInt(_bossType);
		buffer.writeInt(_luckyNumber);
		buffer.writeInt(_dice);
		buffer.writeInt(_bossDice);
		buffer.writeByte(_result);
		buffer.writeByte(_isLuckyNumber);
		buffer.writeInt(_rewardItemId);
		buffer.writeLong(_rewardItemCount);
	}
}
