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
package org.l2j.gameserver.network.clientpackets.randomcraft;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.l2j.Config;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.data.xml.RandomCraftData;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.request.RandomCraftRequest;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.randomcraft.ExCraftExtract;
import org.l2j.gameserver.network.serverpackets.randomcraft.ExCraftInfo;

/**
 * @author Mode
 */
public class ExRequestRandomCraftExtract implements ClientPacket
{
	private final Map<Integer, Long> _items = new HashMap<>();
	
	@Override
	public void read(ReadablePacket packet)
	{
		final int size = packet.readInt();
		for (int i = 0; i < size; i++)
		{
			final int objId = packet.readInt();
			final long count = packet.readLong();
			if (count > 0)
			{
				_items.put(objId, count);
			}
		}
	}
	
	@Override
	public void run(GameClient client)
	{
		if (!Config.ENABLE_RANDOM_CRAFT)
		{
			return;
		}
		
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (player.hasItemRequest() || player.hasRequest(RandomCraftRequest.class))
		{
			return;
		}
		player.addRequest(new RandomCraftRequest(player));
		
		int points = 0;
		int fee = 0;
		Map<Integer, Long> toDestroy = new HashMap<>();
		for (Entry<Integer, Long> e : _items.entrySet())
		{
			final int objId = e.getKey();
			final long count = e.getValue();
			if (count < 1)
			{
				player.removeRequest(RandomCraftRequest.class);
				return;
			}
			
			final Item item = player.getInventory().getItemByObjectId(objId);
			if (item != null)
			{
				if (count > item.getCount())
				{
					continue;
				}
				
				toDestroy.put(objId, count);
				points += RandomCraftData.getInstance().getPoints(item.getId()) * count;
				fee += RandomCraftData.getInstance().getFee(item.getId()) * count;
			}
			else
			{
				player.sendPacket(new ExCraftExtract());
			}
		}
		
		if ((points < 1) || (fee < 0))
		{
			player.removeRequest(RandomCraftRequest.class);
			return;
		}
		
		if (player.reduceAdena("RandomCraft Extract", fee, player, true))
		{
			for (Entry<Integer, Long> e : toDestroy.entrySet())
			{
				player.destroyItem("RandomCraft Extract", e.getKey(), e.getValue(), player, true);
			}
			player.getRandomCraft().addCraftPoints(points);
		}
		
		player.sendPacket(new ExCraftInfo(player));
		player.sendPacket(new ExCraftExtract());
		player.removeRequest(RandomCraftRequest.class);
	}
}
