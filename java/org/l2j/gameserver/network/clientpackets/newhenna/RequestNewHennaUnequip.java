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
package org.l2j.gameserver.network.clientpackets.newhenna;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.item.henna.Henna;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.PacketLogger;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.ActionFailed;
import org.l2j.gameserver.network.serverpackets.UserInfo;
import org.l2j.gameserver.network.serverpackets.newhenna.NewHennaUnequip;

/**
 * @author Index, Serenitty
 */
public class RequestNewHennaUnequip implements ClientPacket
{
	private int _slotId;
	private int _itemId;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_slotId = packet.readByte();
		_itemId = packet.readInt(); // CostItemId
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (!client.getFloodProtectors().canPerformTransaction())
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			player.sendPacket(new NewHennaUnequip(_slotId, 0));
			return;
		}
		
		if (_slotId > player.getHennaPotenList().length)
		{
			return;
		}
		
		final Henna henna = player.getHenna(_slotId);
		if (henna == null)
		{
			PacketLogger.warning(getClass().getSimpleName() + ": " + player + " requested Henna Draw remove without any henna.");
			player.sendPacket(ActionFailed.STATIC_PACKET);
			player.sendPacket(new NewHennaUnequip(_slotId, 0));
			return;
		}
		
		int feeType = 0;
		
		if (_itemId == 57)
		{
			feeType = henna.getCancelFee();
		}
		else if (_itemId == 91663)
		{
			feeType = henna.getCancelL2CoinFee();
		}
		
		if (player.destroyItemByItemId("FeeType", _itemId, feeType, player, false))
		{
			player.removeHenna(_slotId);
			player.getStat().recalculateStats(true);
			player.sendPacket(new NewHennaUnequip(_slotId, 1));
			player.sendPacket(new UserInfo(player));
		}
		else
		{
			if (_itemId == 57)
			{
				player.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_ENOUGH_ADENA_TO_REGISTER_THE_ITEM);
			}
			else if (_itemId == 91663)
			{
				player.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_ENOUGH_L2_COINS_ADD_MORE_L2_COINS_AND_TRY_AGAIN);
			}
			player.sendPacket(ActionFailed.STATIC_PACKET);
			player.sendPacket(new NewHennaUnequip(_slotId, 0));
		}
	}
}