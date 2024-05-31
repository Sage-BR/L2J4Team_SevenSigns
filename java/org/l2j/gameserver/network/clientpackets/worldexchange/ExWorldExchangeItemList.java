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
package org.l2j.gameserver.network.clientpackets.worldexchange;

import java.util.ArrayList;
import java.util.List;

import org.l2j.Config;
import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.enums.WorldExchangeItemSubType;
import org.l2j.gameserver.enums.WorldExchangeSortType;
import org.l2j.gameserver.instancemanager.WorldExchangeManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.WorldExchangeHolder;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.worldexchange.WorldExchangeItemList;

/**
 * @author Index
 */
public class ExWorldExchangeItemList implements ClientPacket
{
	private int _category;
	private int _sortType;
	private final List<Integer> _itemIdList = new ArrayList<>();
	
	@Override
	public void read(ReadablePacket packet)
	{
		_category = packet.readShort();
		_sortType = packet.readByte();
		packet.readInt(); // page
		int size = packet.readInt();
		for (int i = 0; i < size; i++)
		{
			_itemIdList.add(packet.readInt());
		}
	}
	
	@Override
	public void run(GameClient client)
	{
		if (!Config.ENABLE_WORLD_EXCHANGE)
		{
			return;
		}
		
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final String lang = Config.MULTILANG_ENABLE ? player.getLang() != null ? player.getLang() : Config.WORLD_EXCHANGE_DEFAULT_LANG : Config.WORLD_EXCHANGE_DEFAULT_LANG;
		if (_itemIdList.isEmpty())
		{
			final List<WorldExchangeHolder> holders = WorldExchangeManager.getInstance().getItemBids(player.getObjectId(), WorldExchangeItemSubType.getWorldExchangeItemSubType(_category), WorldExchangeSortType.getWorldExchangeSortType(_sortType), lang);
			player.sendPacket(new WorldExchangeItemList(holders, WorldExchangeItemSubType.getWorldExchangeItemSubType(_category)));
		}
		else
		{
			WorldExchangeManager.getInstance().addCategoryType(_itemIdList, _category);
			final List<WorldExchangeHolder> holders = WorldExchangeManager.getInstance().getItemBids(_itemIdList, WorldExchangeSortType.getWorldExchangeSortType(_sortType), lang);
			player.sendPacket(new WorldExchangeItemList(holders, WorldExchangeItemSubType.getWorldExchangeItemSubType(_category)));
		}
	}
}
