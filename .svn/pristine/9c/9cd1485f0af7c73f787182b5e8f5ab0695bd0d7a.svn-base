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
package handlers.itemhandlers;

import org.l2jmobius.gameserver.data.xml.EnchantChallengePointData;
import org.l2jmobius.gameserver.handler.IItemHandler;
import org.l2jmobius.gameserver.model.actor.Playable;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.serverpackets.enchant.challengepoint.ExEnchantChallengePointInfo;

/**
 * @author Serenitty
 */
public class ChallengePointsCoupon implements IItemHandler
{
	@Override
	public boolean useItem(Playable playable, Item item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.YOUR_PET_CANNOT_CARRY_THIS_ITEM);
			return false;
		}
		
		final int pointsToGive;
		final int categoryId;
		switch (item.getId())
		{
			case 97125: // Rare Accessory Challenge Points +50
			{
				pointsToGive = 50;
				categoryId = 1;
				break;
			}
			case 97126: // Talisman Challenge Points +50
			{
				pointsToGive = 50;
				categoryId = 2;
				break;
			}
			case 97127: // Special Equipment Challenge Points +50
			{
				pointsToGive = 50;
				categoryId = 3;
				break;
			}
			case 97276: // Rare Accessory Challenge Points +20
			{
				pointsToGive = 20;
				categoryId = 1;
				break;
			}
			case 97277: // Talisman Challenge Points +20
			{
				pointsToGive = 20;
				categoryId = 2;
				break;
			}
			case 97278: // Special Equipment Challenge Points +20
			{
				pointsToGive = 20;
				categoryId = 3;
				break;
			}
			default:
			{
				return false;
			}
		}
		
		final Player player = playable.getActingPlayer();
		if (player.getChallengeInfo().canAddPoints(categoryId, pointsToGive))
		{
			player.destroyItem("Challenge Coupon", item.getObjectId(), 1, null, false);
			player.getChallengeInfo().getChallengePoints().compute(categoryId, (k, v) -> v == null ? Math.min(EnchantChallengePointData.getInstance().getMaxPoints(), pointsToGive) : Math.min(EnchantChallengePointData.getInstance().getMaxPoints(), v + pointsToGive));
			player.sendPacket(new ExEnchantChallengePointInfo(player));
		}
		else
		{
			player.sendMessage("The points of this coupon exceed the limit.");
		}
		
		return true;
	}
}
