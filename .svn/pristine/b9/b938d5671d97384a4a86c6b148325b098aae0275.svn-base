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
package org.l2jmobius.gameserver.network.clientpackets.huntpass;

import java.util.Calendar;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;
import org.l2jmobius.gameserver.network.serverpackets.huntpass.HuntPassInfo;
import org.l2jmobius.gameserver.network.serverpackets.huntpass.HuntPassSayhasSupportInfo;

/**
 * @author Serenitty
 */
public class RequestHuntPassBuyPremium extends ClientPacket
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
		
		final Calendar calendar = Calendar.getInstance();
		if ((calendar.get(Calendar.DAY_OF_MONTH) == Config.HUNT_PASS_PERIOD) && (calendar.get(Calendar.HOUR_OF_DAY) == 6) && (calendar.get(Calendar.MINUTE) < 30))
		{
			player.sendPacket(SystemMessageId.CURRENTLY_UNAVAILABLE_FOR_PURCHASE_YOU_CAN_BUY_THE_SEASON_PASS_ADDITIONAL_REWARDS_ONLY_UNTIL_6_30_A_M_OF_THE_SEASON_S_LAST_DAY);
			return;
		}
		
		if (!player.destroyItemByItemId("RequestHuntPassBuyPremium", Config.HUNT_PASS_PREMIUM_ITEM_ID, Config.HUNT_PASS_PREMIUM_ITEM_COUNT, player, true))
		{
			player.sendPacket(SystemMessageId.NOT_ENOUGH_MONEY_TO_USE_THE_FUNCTION);
			return;
		}
		
		player.getHuntPass().setPremium(true);
		player.sendPacket(new HuntPassSayhasSupportInfo(player));
		player.sendPacket(new HuntPassInfo(player, _huntPassType));
	}
}