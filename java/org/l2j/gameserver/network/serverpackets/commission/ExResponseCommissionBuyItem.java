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
package org.l2j.gameserver.network.serverpackets.commission;

import org.l2j.gameserver.model.ItemInfo;
import org.l2j.gameserver.model.commission.CommissionItem;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author NosBit
 */
public class ExResponseCommissionBuyItem extends ServerPacket
{
	public static final ExResponseCommissionBuyItem FAILED = new ExResponseCommissionBuyItem(null);
	
	private final CommissionItem _commissionItem;
	
	public ExResponseCommissionBuyItem(CommissionItem commissionItem)
	{
		_commissionItem = commissionItem;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_RESPONSE_COMMISSION_BUY_ITEM.writeId(this);
		writeInt(_commissionItem != null);
		if (_commissionItem != null)
		{
			final ItemInfo itemInfo = _commissionItem.getItemInfo();
			writeInt(itemInfo.getEnchantLevel());
			writeInt(itemInfo.getItem().getId());
			writeLong(itemInfo.getCount());
		}
	}
}
