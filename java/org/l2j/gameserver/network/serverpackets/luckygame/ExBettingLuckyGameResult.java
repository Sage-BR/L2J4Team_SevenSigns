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
package org.l2j.gameserver.network.serverpackets.luckygame;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.l2j.gameserver.enums.LuckyGameItemType;
import org.l2j.gameserver.enums.LuckyGameResultType;
import org.l2j.gameserver.enums.LuckyGameType;
import org.l2j.gameserver.model.holders.ItemHolder;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Sdw
 */
public class ExBettingLuckyGameResult extends ServerPacket
{
	public static final ExBettingLuckyGameResult NORMAL_INVALID_ITEM_COUNT = new ExBettingLuckyGameResult(LuckyGameResultType.INVALID_ITEM_COUNT, LuckyGameType.NORMAL);
	public static final ExBettingLuckyGameResult LUXURY_INVALID_ITEM_COUNT = new ExBettingLuckyGameResult(LuckyGameResultType.INVALID_ITEM_COUNT, LuckyGameType.LUXURY);
	public static final ExBettingLuckyGameResult NORMAL_INVALID_CAPACITY = new ExBettingLuckyGameResult(LuckyGameResultType.INVALID_CAPACITY, LuckyGameType.NORMAL);
	public static final ExBettingLuckyGameResult LUXURY_INVALID_CAPACITY = new ExBettingLuckyGameResult(LuckyGameResultType.INVALID_CAPACITY, LuckyGameType.LUXURY);
	
	private final LuckyGameResultType _result;
	private final LuckyGameType _type;
	private final Map<LuckyGameItemType, List<ItemHolder>> _rewards;
	private final int _ticketCount;
	private final int _size;
	
	public ExBettingLuckyGameResult(LuckyGameResultType result, LuckyGameType type)
	{
		_result = result;
		_type = type;
		_rewards = new EnumMap<>(LuckyGameItemType.class);
		_ticketCount = 0;
		_size = 0;
	}
	
	public ExBettingLuckyGameResult(LuckyGameResultType result, LuckyGameType type, Map<LuckyGameItemType, List<ItemHolder>> rewards, int ticketCount)
	{
		_result = result;
		_type = type;
		_rewards = rewards;
		_ticketCount = ticketCount;
		_size = (int) rewards.values().stream().mapToLong(i -> i.stream().count()).sum();
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_BETTING_LUCKY_GAME_RESULT.writeId(this);
		writeInt(_result.getClientId());
		writeInt(_type.ordinal());
		writeInt(_ticketCount);
		writeInt(_size);
		for (Entry<LuckyGameItemType, List<ItemHolder>> reward : _rewards.entrySet())
		{
			for (ItemHolder item : reward.getValue())
			{
				writeInt(reward.getKey().getClientId());
				writeInt(item.getId());
				writeInt((int) item.getCount());
			}
		}
	}
}
