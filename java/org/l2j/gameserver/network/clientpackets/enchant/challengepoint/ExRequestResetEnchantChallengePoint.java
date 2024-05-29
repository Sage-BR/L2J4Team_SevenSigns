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
package org.l2j.gameserver.network.clientpackets.enchant.challengepoint;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.data.xml.EnchantItemData;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.request.EnchantItemRequest;
import org.l2j.gameserver.model.item.enchant.EnchantScroll;
import org.l2j.gameserver.model.item.type.CrystalType;
import org.l2j.gameserver.model.stats.Stat;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.enchant.challengepoint.ExResetEnchantChallengePoint;
import org.l2j.gameserver.network.serverpackets.enchant.single.ExChangedEnchantTargetItemProbList;
import org.l2j.gameserver.network.serverpackets.enchant.single.ExChangedEnchantTargetItemProbList.EnchantProbInfo;

/**
 * @author Serenitty
 */
public class ExRequestResetEnchantChallengePoint implements ClientPacket
{
	@Override
	public void read(ReadablePacket packet)
	{
		packet.readByte();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		player.getChallengeInfo().setChallengePointsPendingRecharge(-1, -1);
		player.sendPacket(new ExResetEnchantChallengePoint(true));
		
		final EnchantItemRequest request = player.getRequest(EnchantItemRequest.class);
		if ((request == null) || request.isProcessing())
		{
			return;
		}
		
		final EnchantScroll scrollTemplate = EnchantItemData.getInstance().getEnchantScroll(request.getEnchantingScroll());
		final double chance = scrollTemplate.getChance(player, request.getEnchantingItem());
		
		final int crystalLevel = request.getEnchantingItem().getTemplate().getCrystalType().getLevel();
		final double enchantRateStat = (crystalLevel > CrystalType.NONE.getLevel()) && (crystalLevel < CrystalType.EVENT.getLevel()) ? player.getStat().getValue(Stat.ENCHANT_RATE) : 0;
		
		player.sendPacket(new ExChangedEnchantTargetItemProbList(new EnchantProbInfo(request.getEnchantingItem().getObjectId(), (int) ((chance + enchantRateStat) * 100), (int) (chance * 100), 0, (int) (enchantRateStat * 100))));
	}
}
