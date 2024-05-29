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
package org.l2j.gameserver.network.clientpackets.enchant;

import org.l2j.Config;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.data.xml.EnchantChallengePointData;
import org.l2j.gameserver.data.xml.EnchantChallengePointData.EnchantChallengePointsItemInfo;
import org.l2j.gameserver.data.xml.EnchantChallengePointData.EnchantChallengePointsOptionInfo;
import org.l2j.gameserver.data.xml.EnchantItemData;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.request.EnchantItemRequest;
import org.l2j.gameserver.model.item.enchant.EnchantScroll;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.item.type.CrystalType;
import org.l2j.gameserver.model.stats.Stat;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.PacketLogger;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.enchant.EnchantResult;
import org.l2j.gameserver.network.serverpackets.enchant.ExPutEnchantScrollItemResult;
import org.l2j.gameserver.network.serverpackets.enchant.ExPutEnchantTargetItemResult;
import org.l2j.gameserver.network.serverpackets.enchant.single.ChangedEnchantTargetItemProbabilityList;
import org.l2j.gameserver.network.serverpackets.enchant.single.ExChangedEnchantTargetItemProbList;
import org.l2j.gameserver.network.serverpackets.enchant.single.ExChangedEnchantTargetItemProbList.EnchantProbInfo;
import org.l2j.gameserver.util.Util;

/**
 * @author KenM
 */
public class RequestExTryToPutEnchantTargetItem implements ClientPacket
{
	private int _objectId;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_objectId = packet.readInt();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final EnchantItemRequest request = player.getRequest(EnchantItemRequest.class);
		if ((request == null) || request.isProcessing())
		{
			return;
		}
		
		final Item scroll = request.getEnchantingScroll();
		if (scroll == null)
		{
			return;
		}
		
		final Item item = player.getInventory().getItemByObjectId(_objectId);
		if (item == null)
		{
			Util.handleIllegalPlayerAction(player, "RequestExTryToPutEnchantTargetItem: " + player + " tried to cheat using a packet manipulation tool! Ban this player!", Config.DEFAULT_PUNISH);
			return;
		}
		
		final EnchantScroll scrollTemplate = EnchantItemData.getInstance().getEnchantScroll(scroll);
		if (!item.isEnchantable() || (scrollTemplate == null) || !scrollTemplate.isValid(item, null) || (item.getEnchantLevel() >= scrollTemplate.getMaxEnchantLevel()))
		{
			player.sendPacket(SystemMessageId.DOES_NOT_FIT_STRENGTHENING_CONDITIONS_OF_THE_SCROLL);
			request.setEnchantingItem(0);
			player.sendPacket(new ExPutEnchantTargetItemResult(0));
			player.sendPacket(new EnchantResult(EnchantResult.ERROR, null, null, 0));
			player.sendPacket(new ExPutEnchantScrollItemResult(1));
			if (scrollTemplate == null)
			{
				PacketLogger.warning("RequestExTryToPutEnchantTargetItem: " + player + " has used undefined scroll with id " + scroll.getId());
			}
			return;
		}
		
		request.setEnchantingItem(_objectId);
		request.setEnchantLevel(item.getEnchantLevel());
		
		request.setTimestamp(System.currentTimeMillis());
		player.sendPacket(new ExPutEnchantTargetItemResult(_objectId));
		player.sendPacket(new ChangedEnchantTargetItemProbabilityList(player, false));
		
		final double chance = scrollTemplate.getChance(player, item);
		if (chance > 0)
		{
			double challengePointsChance = 0;
			final EnchantChallengePointsItemInfo info = EnchantChallengePointData.getInstance().getInfoByItemId(item.getId());
			if (info != null)
			{
				final int groupId = info.groupId();
				final int pendingGroupId = player.getChallengeInfo().getChallengePointsPendingRecharge()[0];
				final int pendingOptionIndex = player.getChallengeInfo().getChallengePointsPendingRecharge()[1];
				if ((pendingGroupId == groupId) && ((pendingOptionIndex == EnchantChallengePointData.OPTION_PROB_INC1) || (pendingOptionIndex == EnchantChallengePointData.OPTION_PROB_INC2)))
				{
					final EnchantChallengePointsOptionInfo optionInfo = EnchantChallengePointData.getInstance().getOptionInfo(pendingGroupId, pendingOptionIndex);
					if ((optionInfo != null) && (item.getEnchantLevel() >= optionInfo.minEnchant()) && (item.getEnchantLevel() <= optionInfo.maxEnchant()))
					{
						challengePointsChance = optionInfo.chance();
						player.getChallengeInfo().setChallengePointsPendingRecharge(-1, -1);
					}
				}
			}
			
			final int crystalLevel = item.getTemplate().getCrystalType().getLevel();
			final double enchantRateStat = (crystalLevel > CrystalType.NONE.getLevel()) && (crystalLevel < CrystalType.EVENT.getLevel()) ? player.getStat().getValue(Stat.ENCHANT_RATE) : 0;
			player.sendPacket(new ExChangedEnchantTargetItemProbList(new EnchantProbInfo(item.getObjectId(), (int) ((chance + challengePointsChance + enchantRateStat) * 100), (int) (chance * 100), (int) (challengePointsChance * 100), (int) (enchantRateStat * 100))));
		}
	}
}
