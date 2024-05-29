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
package org.l2j.gameserver.network.clientpackets;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.instancemanager.ItemAuctionManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.itemauction.ItemAuction;
import org.l2j.gameserver.model.itemauction.ItemAuctionInstance;
import org.l2j.gameserver.model.itemcontainer.Inventory;
import org.l2j.gameserver.network.GameClient;

/**
 * @author Forsaiken
 */
public class RequestBidItemAuction implements ClientPacket
{
	private int _instanceId;
	private long _bid;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_instanceId = packet.readInt();
		_bid = packet.readLong();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		// can't use auction fp here
		if (!client.getFloodProtectors().canPerformTransaction())
		{
			player.sendMessage("You are bidding too fast.");
			return;
		}
		
		if ((_bid < 0) || (_bid > Inventory.MAX_ADENA))
		{
			return;
		}
		
		final ItemAuctionInstance instance = ItemAuctionManager.getInstance().getManagerInstance(_instanceId);
		if (instance != null)
		{
			final ItemAuction auction = instance.getCurrentAuction();
			if (auction != null)
			{
				auction.registerBid(player, _bid);
			}
		}
	}
}