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
package org.l2j.gameserver.network.clientpackets;

import java.util.List;

import org.l2j.commons.util.Rnd;
import org.l2j.gameserver.instancemanager.events.LetterCollectorManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.ItemChanceHolder;
import org.l2j.gameserver.model.holders.ItemHolder;
import org.l2j.gameserver.model.itemcontainer.PlayerInventory;
import org.l2j.gameserver.network.SystemMessageId;

/**
 * @author Index, Mobius
 */
public class ExLetterCollectorTakeReward extends ClientPacket
{
	private int _wordId;
	
	@Override
	protected void readImpl()
	{
		_wordId = readInt();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		final PlayerInventory inventory = player.getInventory();
		if (inventory == null)
		{
			return;
		}
		
		final LetterCollectorManager.LetterCollectorRewardHolder lcrh = LetterCollectorManager.getInstance().getRewards(_wordId);
		if (lcrh == null)
		{
			return;
		}
		
		for (ItemHolder needLetter : LetterCollectorManager.getInstance().getWord(_wordId))
		{
			if (inventory.getInventoryItemCount(needLetter.getId(), -1) < needLetter.getCount())
			{
				return;
			}
		}
		for (ItemHolder destroyLetter : LetterCollectorManager.getInstance().getWord(_wordId))
		{
			if (!player.destroyItemByItemId("LetterCollector", destroyLetter.getId(), destroyLetter.getCount(), player, true))
			{
				return;
			}
		}
		
		final ItemChanceHolder rewardItem = getRandomReward(lcrh.getRewards(), lcrh.getChance());
		if (rewardItem == null)
		{
			player.sendPacket(SystemMessageId.NOTHING_HAPPENED);
			return;
		}
		
		player.addItem("LetterCollector", rewardItem.getId(), rewardItem.getCount(), rewardItem.getEnchantmentLevel(), player, true);
	}
	
	private ItemChanceHolder getRandomReward(List<ItemChanceHolder> rewards, double holderChance)
	{
		final double chance = Rnd.get(holderChance);
		double itemChance = 0;
		for (ItemChanceHolder rewardItem : rewards)
		{
			itemChance += rewardItem.getChance();
			if (chance <= itemChance)
			{
				if (rewardItem.getId() == -1)
				{
					return null;
				}
				return rewardItem;
			}
		}
		return null;
	}
}