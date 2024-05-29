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
package org.l2j.gameserver.network.serverpackets.enchant.multi;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.request.EnchantItemRequest;
import org.l2j.gameserver.model.holders.ItemHolder;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Index
 */
public class ExResultMultiEnchantItemList extends ServerPacket
{
	public static final int SUCCESS = 0;
	public static final int FAIL = 1;
	public static final int ERROR = 2;
	private final Player _player;
	private boolean _error;
	private boolean _isResult;
	private Map<Integer, int[]> _successEnchant = new HashMap<>();
	private Map<Integer, Integer> _failureEnchant = new HashMap<>();
	private Map<Integer, ItemHolder> _failureReward = new HashMap<>();
	private final Map<Integer, Integer> _failChallengePointInfoList;
	
	public ExResultMultiEnchantItemList(Player player, boolean error)
	{
		_player = player;
		_error = error;
		_failChallengePointInfoList = new HashMap<>();
	}
	
	public ExResultMultiEnchantItemList(Player player, Map<Integer, ItemHolder> failureReward)
	{
		_player = player;
		_failureReward = failureReward;
		_failChallengePointInfoList = new HashMap<>();
	}
	
	public ExResultMultiEnchantItemList(Player player, Map<Integer, int[]> successEnchant, Map<Integer, Integer> failureEnchant)
	{
		_player = player;
		_successEnchant = successEnchant;
		_failureEnchant = failureEnchant;
		_failChallengePointInfoList = new HashMap<>();
	}
	
	public ExResultMultiEnchantItemList(Player player, Map<Integer, int[]> successEnchant, Map<Integer, Integer> failureEnchant, boolean isResult)
	{
		_player = player;
		_successEnchant = successEnchant;
		_failureEnchant = failureEnchant;
		_isResult = isResult;
		_failChallengePointInfoList = new HashMap<>();
	}
	
	public ExResultMultiEnchantItemList(Player player, Map<Integer, int[]> successEnchant, Map<Integer, Integer> failureEnchant, Map<Integer, Integer> failChallengePointInfoList, boolean isResult)
	{
		_player = player;
		_successEnchant = successEnchant;
		_failureEnchant = failureEnchant;
		_isResult = isResult;
		_failChallengePointInfoList = failChallengePointInfoList;
	}
	
	@Override
	public void write()
	{
		if (_player.getRequest(EnchantItemRequest.class) == null)
		{
			return;
		}
		final EnchantItemRequest request = _player.getRequest(EnchantItemRequest.class);
		
		ServerPackets.EX_RES_MULTI_ENCHANT_ITEM_LIST.writeId(this);
		
		if (_error)
		{
			writeByte(0);
			return;
		}
		
		writeByte(1);
		
		/* EnchantSuccessItem */
		if (_failureReward.size() == 0)
		{
			writeInt(_successEnchant.size());
			if (_successEnchant.size() != 0)
			{
				for (int[] success : _successEnchant.values())
				{
					writeInt(success[0]);
					writeInt(success[1]);
				}
			}
		}
		else
		{
			writeInt(0);
		}
		
		/* EnchantFailItem */
		writeInt(_failureEnchant.size());
		if (_failureEnchant.size() != 0)
		{
			for (int failure : _failureEnchant.values())
			{
				writeInt(failure);
				writeInt(0);
			}
		}
		else
		{
			writeInt(0);
		}
		
		/* EnchantFailRewardItem */
		if (((_successEnchant.size() == 0) && (request.getMultiFailItemsCount() != 0)) || (_isResult && (request.getMultiFailItemsCount() != 0)))
		{
			writeInt(request.getMultiFailItemsCount());
			_failureReward = request.getMultiEnchantFailItems();
			for (ItemHolder failure : _failureReward.values())
			{
				writeInt(failure.getId());
				writeInt((int) failure.getCount());
			}
			if (_isResult)
			{
				request.clearMultiSuccessEnchantList();
				request.clearMultiFailureEnchantList();
			}
			request.clearMultiFailReward();
		}
		else
		{
			writeInt(0);
		}
		
		/* EnchantFailChallengePointInfo */
		
		writeInt(_failChallengePointInfoList.size());
		for (Entry<Integer, Integer> item : _failChallengePointInfoList.entrySet())
		{
			writeInt(item.getKey());
			writeInt(item.getValue());
		}
	}
}
