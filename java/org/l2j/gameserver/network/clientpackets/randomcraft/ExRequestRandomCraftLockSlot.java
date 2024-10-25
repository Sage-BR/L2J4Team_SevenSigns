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
package org.l2j.gameserver.network.clientpackets.randomcraft;

import org.l2j.Config;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.itemcontainer.Inventory;
import org.l2j.gameserver.model.itemcontainer.PlayerRandomCraft;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.randomcraft.ExCraftRandomInfo;
import org.l2j.gameserver.network.serverpackets.randomcraft.ExCraftRandomLockSlot;

/**
 * @author Mode
 */
public class ExRequestRandomCraftLockSlot extends ClientPacket
{
	private static final int[] LOCK_PRICE =
	{
		100,
		500,
		1000
	};
	
	private int _id;
	
	@Override
	protected void readImpl()
	{
		_id = readInt();
	}
	
	@Override
	protected void runImpl()
	{
		if (!Config.ENABLE_RANDOM_CRAFT)
		{
			return;
		}
		
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		if ((_id >= 0) && (_id < 5))
		{
			final PlayerRandomCraft rc = player.getRandomCraft();
			int lockedItemCount = rc.getLockedSlotCount();
			if (((rc.getRewards().size() - 1) >= _id) && (lockedItemCount < 3))
			{
				int price = LOCK_PRICE[Math.min(lockedItemCount, 2)];
				Item lcoin = player.getInventory().getItemByItemId(Inventory.LCOIN_ID);
				if ((lcoin != null) && (lcoin.getCount() >= price))
				{
					player.destroyItem("RandomCraft Lock Slot", lcoin, price, player, true);
					rc.getRewards().get(_id).lock();
					player.sendPacket(new ExCraftRandomLockSlot());
					player.sendPacket(new ExCraftRandomInfo(player));
				}
			}
		}
	}
}
