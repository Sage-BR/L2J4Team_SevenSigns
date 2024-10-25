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
package org.l2j.gameserver.network.clientpackets.enchant.challengepoint;

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
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.enchant.challengepoint.ExEnchantChallengePointInfo;
import org.l2j.gameserver.network.serverpackets.enchant.challengepoint.ExSetEnchantChallengePoint;
import org.l2j.gameserver.network.serverpackets.enchant.single.ExChangedEnchantTargetItemProbList;
import org.l2j.gameserver.network.serverpackets.enchant.single.ExChangedEnchantTargetItemProbList.EnchantProbInfo;

/**
 * @author Serenitty
 */
public class ExRequestSetEnchantChallengePoint extends ClientPacket
{
	private int _useType;
	private boolean _useTicket;
	
	@Override
	protected void readImpl()
	{
		_useType = readInt();
		_useTicket = readBoolean();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		final EnchantItemRequest request = player.getRequest(EnchantItemRequest.class);
		if ((request == null) || request.isProcessing())
		{
			player.sendPacket(new ExSetEnchantChallengePoint(false));
			return;
		}
		
		final Item item = request.getEnchantingItem();
		if (item == null)
		{
			player.sendPacket(new ExSetEnchantChallengePoint(false));
			return;
		}
		
		final EnchantChallengePointsItemInfo info = EnchantChallengePointData.getInstance().getInfoByItemId(item.getId());
		if (info == null)
		{
			player.sendPacket(new ExSetEnchantChallengePoint(false));
			return;
		}
		
		final int groupId = info.groupId();
		if (_useTicket)
		{
			final int remainingRecharges = player.getChallengeInfo().getChallengePointsRecharges(groupId, _useType);
			if (remainingRecharges > 0)
			{
				player.getChallengeInfo().setChallengePointsPendingRecharge(groupId, _useType);
				player.sendPacket(new ExSetEnchantChallengePoint(true));
				player.sendPacket(new ExEnchantChallengePointInfo(player));
			}
			else
			{
				player.sendPacket(new ExSetEnchantChallengePoint(false));
				return;
			}
		}
		else
		{
			final int remainingRecharges = player.getChallengeInfo().getChallengePointsRecharges(groupId, _useType);
			if (remainingRecharges < EnchantChallengePointData.getInstance().getMaxTicketCharge())
			{
				int remainingPoints = player.getChallengeInfo().getChallengePoints().getOrDefault(groupId, 0);
				final int fee = EnchantChallengePointData.getInstance().getFeeForOptionIndex(_useType);
				if (remainingPoints >= fee)
				{
					remainingPoints -= fee;
					player.getChallengeInfo().getChallengePoints().put(groupId, remainingPoints);
					player.getChallengeInfo().addChallengePointsRecharge(groupId, _useType, 1);
					player.getChallengeInfo().setChallengePointsPendingRecharge(groupId, _useType);
					player.sendPacket(new ExSetEnchantChallengePoint(true));
					player.sendPacket(new ExEnchantChallengePointInfo(player));
				}
				else
				{
					player.sendPacket(new ExSetEnchantChallengePoint(false));
					return;
				}
			}
		}
		
		final EnchantScroll scrollTemplate = EnchantItemData.getInstance().getEnchantScroll(request.getEnchantingScroll());
		final double chance = scrollTemplate.getChance(player, item);
		
		double challengePointsChance = 0;
		final int pendingGroupId = player.getChallengeInfo().getChallengePointsPendingRecharge()[0];
		final int pendingOptionIndex = player.getChallengeInfo().getChallengePointsPendingRecharge()[1];
		if ((pendingGroupId == groupId) && ((pendingOptionIndex == EnchantChallengePointData.OPTION_PROB_INC1) || (pendingOptionIndex == EnchantChallengePointData.OPTION_PROB_INC2)))
		{
			final EnchantChallengePointsOptionInfo optionInfo = EnchantChallengePointData.getInstance().getOptionInfo(pendingGroupId, pendingOptionIndex);
			if ((optionInfo != null) && (item.getEnchantLevel() >= optionInfo.minEnchant()) && (item.getEnchantLevel() <= optionInfo.maxEnchant()))
			{
				challengePointsChance = optionInfo.chance();
			}
		}
		
		final int crystalLevel = item.getTemplate().getCrystalType().getLevel();
		final double enchantRateStat = (crystalLevel > CrystalType.NONE.getLevel()) && (crystalLevel < CrystalType.EVENT.getLevel()) ? player.getStat().getValue(Stat.ENCHANT_RATE) : 0;
		
		player.sendPacket(new ExChangedEnchantTargetItemProbList(new EnchantProbInfo(item.getObjectId(), (int) ((chance + challengePointsChance + enchantRateStat) * 100), (int) (chance * 100), (int) (challengePointsChance * 100), (int) (enchantRateStat * 100))));
	}
}
