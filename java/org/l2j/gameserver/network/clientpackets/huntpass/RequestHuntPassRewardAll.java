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
package org.l2j.gameserver.network.clientpackets.huntpass;

import org.l2j.commons.threads.ThreadPool;
import org.l2j.gameserver.data.xml.HuntPassData;
import org.l2j.gameserver.data.xml.ItemData;
import org.l2j.gameserver.model.HuntPass;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.request.RewardRequest;
import org.l2j.gameserver.model.holders.ItemHolder;
import org.l2j.gameserver.model.item.ItemTemplate;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.SystemMessage;
import org.l2j.gameserver.network.serverpackets.huntpass.HuntPassInfo;
import org.l2j.gameserver.network.serverpackets.huntpass.HuntPassSayhasSupportInfo;
import org.l2j.gameserver.network.serverpackets.huntpass.HuntPassSimpleInfo;

/**
 * @author Serenitty, Mobius, Fakee
 */
public class RequestHuntPassRewardAll extends ClientPacket
{
	private int _huntPassType;
	
	@Override
	protected void readImpl()
	{
		_huntPassType = readByte();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (player.hasRequest(RewardRequest.class))
		{
			return;
		}
		player.addRequest(new RewardRequest(player));
		
		int rewardIndex;
		int premiumRewardIndex;
		boolean inventoryLimitReached = false;
		final HuntPass huntPass = player.getHuntPass();
		REWARD_LOOP: while (true)
		{
			rewardIndex = huntPass.getRewardStep();
			premiumRewardIndex = huntPass.getPremiumRewardStep();
			if ((rewardIndex >= HuntPassData.getInstance().getRewardsCount()) && (premiumRewardIndex >= HuntPassData.getInstance().getPremiumRewardsCount()))
			{
				break REWARD_LOOP;
			}
			
			ItemHolder reward = null;
			if (!huntPass.isPremium())
			{
				if (rewardIndex >= huntPass.getCurrentStep())
				{
					break REWARD_LOOP;
				}
				
				reward = HuntPassData.getInstance().getRewards().get(rewardIndex);
			}
			else
			{
				if (premiumRewardIndex >= huntPass.getCurrentStep())
				{
					break REWARD_LOOP;
				}
				
				if (rewardIndex < HuntPassData.getInstance().getRewardsCount())
				{
					reward = HuntPassData.getInstance().getRewards().get(rewardIndex);
				}
				else if (premiumRewardIndex < HuntPassData.getInstance().getPremiumRewardsCount())
				{
					reward = HuntPassData.getInstance().getPremiumRewards().get(premiumRewardIndex);
				}
			}
			if (reward == null)
			{
				break REWARD_LOOP;
			}
			
			final ItemTemplate itemTemplate = ItemData.getInstance().getTemplate(reward.getId());
			final long weight = itemTemplate.getWeight() * reward.getCount();
			final long slots = itemTemplate.isStackable() ? 1 : reward.getCount();
			if (!player.getInventory().validateWeight(weight) || !player.getInventory().validateCapacity(slots))
			{
				player.sendPacket(SystemMessageId.YOUR_INVENTORY_S_WEIGHT_SLOT_LIMIT_HAS_BEEN_EXCEEDED_SO_YOU_CAN_T_RECEIVE_THE_REWARD_PLEASE_FREE_UP_SOME_SPACE_AND_TRY_AGAIN);
				inventoryLimitReached = true;
				break REWARD_LOOP;
			}
			
			normalReward(player);
			premiumReward(player);
			huntPass.setRewardStep(rewardIndex + 1);
		}
		
		if (!inventoryLimitReached)
		{
			huntPass.setRewardAlert(false);
		}
		
		player.sendPacket(new HuntPassInfo(player, _huntPassType));
		player.sendPacket(new HuntPassSayhasSupportInfo(player));
		player.sendPacket(new HuntPassSimpleInfo(player));
		
		ThreadPool.schedule(() -> player.removeRequest(RewardRequest.class), 300);
	}
	
	private void rewardItem(Player player, ItemHolder reward)
	{
		if (reward.getId() == 72286) // Sayha's Grace Sustention Points
		{
			final int count = (int) reward.getCount();
			player.getHuntPass().addSayhaTime(count);
			
			final SystemMessage msg = new SystemMessage(SystemMessageId.YOU_VE_GOT_S1_SAYHA_S_GRACE_SUSTENTION_POINT_S);
			msg.addInt(count);
			player.sendPacket(msg);
		}
		else
		{
			player.addItem("HuntPassReward", reward, player, true);
		}
	}
	
	private void premiumReward(Player player)
	{
		final HuntPass huntPass = player.getHuntPass();
		final int premiumRewardIndex = huntPass.getPremiumRewardStep();
		if (premiumRewardIndex >= HuntPassData.getInstance().getPremiumRewardsCount())
		{
			return;
		}
		
		if (!huntPass.isPremium())
		{
			return;
		}
		
		rewardItem(player, HuntPassData.getInstance().getPremiumRewards().get(premiumRewardIndex));
		huntPass.setPremiumRewardStep(premiumRewardIndex + 1);
	}
	
	private void normalReward(Player player)
	{
		final HuntPass huntPass = player.getHuntPass();
		final int rewardIndex = huntPass.getRewardStep();
		if (rewardIndex >= HuntPassData.getInstance().getRewardsCount())
		{
			return;
		}
		
		if (huntPass.isPremium() && ((huntPass.getPremiumRewardStep() < rewardIndex) || (huntPass.getPremiumRewardStep() >= HuntPassData.getInstance().getPremiumRewardsCount())))
		{
			return;
		}
		
		rewardItem(player, HuntPassData.getInstance().getRewards().get(rewardIndex));
	}
}
