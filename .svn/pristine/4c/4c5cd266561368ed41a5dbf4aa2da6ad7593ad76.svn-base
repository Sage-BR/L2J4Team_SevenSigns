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
package org.l2jmobius.gameserver.network.clientpackets.subjugation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.l2jmobius.commons.util.Rnd;
import org.l2jmobius.gameserver.data.xml.SubjugationGacha;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.PurgePlayerHolder;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;
import org.l2jmobius.gameserver.network.serverpackets.subjugation.ExSubjugationGacha;
import org.l2jmobius.gameserver.network.serverpackets.subjugation.ExSubjugationGachaUI;

/**
 * @author Berezkin Nikolay
 */
public class RequestSubjugationGacha extends ClientPacket
{
	private int _category;
	private int _amount;
	
	@Override
	protected void readImpl()
	{
		_category = readInt();
		_amount = readInt();
	}
	
	@Override
	protected void runImpl()
	{
		if ((_amount < 1) || ((_amount * 20000L) < 1))
		{
			return;
		}
		
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		final PurgePlayerHolder playerKeys = player.getPurgePoints().get(_category);
		final Map<Integer, Double> subjugationData = SubjugationGacha.getInstance().getSubjugation(_category);
		if ((playerKeys != null) && (playerKeys.getKeys() >= _amount) && (player.getInventory().getAdena() > (20000L * _amount)))
		{
			player.getInventory().reduceAdena("Purge Gacha", 20000L * _amount, player, null);
			final int curKeys = playerKeys.getKeys() - _amount;
			player.getPurgePoints().put(_category, new PurgePlayerHolder(playerKeys.getPoints(), curKeys, 0));
			Map<Integer, Integer> rewards = new HashMap<>();
			for (int i = 0; i < _amount; i++)
			{
				double rate = 0;
				for (int index = 0; index < subjugationData.size(); index++)
				{
					final double[] chances = subjugationData.values().stream().mapToDouble(it -> it).toArray();
					final double maxBound = Arrays.stream(chances).sum();
					final double itemChance = chances[index];
					if (Rnd.get(maxBound - rate) < itemChance)
					{
						final int itemId = subjugationData.keySet().stream().mapToInt(it -> it).toArray()[index];
						rewards.put(itemId, rewards.getOrDefault(itemId, 0) + 1);
						player.addItem("Purge Gacha", itemId, 1, player, true);
						break;
					}
					rate += itemChance;
				}
			}
			player.sendPacket(new ExSubjugationGachaUI(curKeys));
			player.sendPacket(new ExSubjugationGacha(rewards));
		}
	}
}
