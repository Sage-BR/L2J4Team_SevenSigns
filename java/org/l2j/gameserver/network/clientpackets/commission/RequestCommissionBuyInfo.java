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
package org.l2j.gameserver.network.clientpackets.commission;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.instancemanager.ItemCommissionManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.commission.CommissionItem;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.commission.ExCloseCommission;
import org.l2j.gameserver.network.serverpackets.commission.ExResponseCommissionBuyInfo;

/**
 * @author NosBit
 */
public class RequestCommissionBuyInfo implements ClientPacket
{
	private long _commissionId;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_commissionId = packet.readLong();
		// packet.readInt(); // CommissionItemType
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (!ItemCommissionManager.isPlayerAllowedToInteract(player))
		{
			player.sendPacket(ExCloseCommission.STATIC_PACKET);
			return;
		}
		
		if (!player.isInventoryUnder80(false) || (player.getWeightPenalty() >= 3))
		{
			player.sendPacket(SystemMessageId.TO_BUY_CANCEL_YOU_NEED_TO_FREE_20_OF_WEIGHT_AND_10_OF_SLOTS_IN_YOUR_INVENTORY);
			player.sendPacket(ExResponseCommissionBuyInfo.FAILED);
			return;
		}
		
		final CommissionItem commissionItem = ItemCommissionManager.getInstance().getCommissionItem(_commissionId);
		if (commissionItem != null)
		{
			player.sendPacket(new ExResponseCommissionBuyInfo(commissionItem));
		}
		else
		{
			player.sendPacket(SystemMessageId.ITEM_PURCHASE_IS_NOT_AVAILABLE_BECAUSE_THE_CORRESPONDING_ITEM_DOES_NOT_EXIST);
			player.sendPacket(ExResponseCommissionBuyInfo.FAILED);
		}
	}
}
