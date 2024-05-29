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
package org.l2j.gameserver.network.clientpackets.enchant.multi;

import java.util.HashMap;
import java.util.Map;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.request.EnchantItemRequest;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.enchant.multi.ExResultSetMultiEnchantItemList;
import org.l2j.gameserver.network.serverpackets.enchant.single.ChangedEnchantTargetItemProbabilityList;

/**
 * @author Index
 */
public class ExRequestSetMultiEnchantItemList implements ClientPacket
{
	private int _slotId;
	private final Map<Integer, Integer> _itemObjectId = new HashMap<>();
	
	@Override
	public void read(ReadablePacket packet)
	{
		_slotId = packet.readInt();
		for (int i = 1; packet.getRemainingLength() != 0; i++)
		{
			_itemObjectId.put(i, packet.readInt());
		}
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (player.getRequest(EnchantItemRequest.class) == null)
		{
			player.sendPacket(new ExResultSetMultiEnchantItemList(player, 1));
			return;
		}
		
		final EnchantItemRequest request = player.getRequest(EnchantItemRequest.class);
		if (request.getMultiEnchantingItemsBySlot(_slotId) != -1)
		{
			request.clearMultiEnchantingItemsBySlot();
			for (int i = 1; i <= _slotId; i++)
			{
				request.addMultiEnchantingItems(i, _itemObjectId.get(i));
			}
		}
		else
		{
			request.addMultiEnchantingItems(_slotId, _itemObjectId.get(_slotId));
		}
		
		_itemObjectId.clear();
		player.sendPacket(new ExResultSetMultiEnchantItemList(player, 0));
		player.sendPacket(new ChangedEnchantTargetItemProbabilityList(player, true));
	}
}