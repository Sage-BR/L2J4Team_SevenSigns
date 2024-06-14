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
package org.l2jmobius.gameserver.network.clientpackets.worldexchange;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.instancemanager.WorldExchangeManager;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;

/**
 * @author Index
 */
public class ExWorldExchangeRegisterItem extends ClientPacket
{
	private long _price;
	private int _itemId;
	private long _amount;
	
	@Override
	protected void readImpl()
	{
		_price = readLong();
		_itemId = readInt();
		_amount = readLong();
	}
	
	@Override
	protected void runImpl()
	{
		if (!Config.ENABLE_WORLD_EXCHANGE)
		{
			return;
		}
		
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		WorldExchangeManager.getInstance().registerItemBid(player, _itemId, _amount, _price);
	}
}
