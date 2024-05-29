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
package org.l2j.gameserver.network.serverpackets.primeshop;

import java.util.Collection;

import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.primeshop.PrimeShopGroup;
import org.l2j.gameserver.model.primeshop.PrimeShopItem;
import org.l2j.gameserver.model.variables.AccountVariables;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author UnAfraid
 */
public class ExBRProductList extends ServerPacket
{
	private final Player _player;
	private final int _type;
	private final Collection<PrimeShopGroup> _primeList;
	
	public ExBRProductList(Player player, int type, Collection<PrimeShopGroup> items)
	{
		_player = player;
		_type = type;
		_primeList = items;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_BR_PRODUCT_LIST.writeId(this);
		writeLong(_player.getAdena()); // Adena
		writeLong(0); // Hero coins
		writeByte(_type); // Type 0 - Home, 1 - History, 2 - Favorites
		writeInt(_primeList.size());
		for (PrimeShopGroup brItem : _primeList)
		{
			writeInt(brItem.getBrId());
			writeByte(brItem.getCat());
			writeByte(brItem.getPaymentType()); // Payment Type: 0 - Prime Points, 1 - Adena, 2 - Hero Coins
			writeInt(brItem.getPrice());
			writeByte(brItem.getPanelType()); // Item Panel Type: 0 - None, 1 - Event, 2 - Sale, 3 - New, 4 - Best
			writeInt(brItem.getRecommended()); // Recommended: (bit flags) 1 - Top, 2 - Left, 4 - Right
			writeInt(brItem.getStartSale());
			writeInt(brItem.getEndSale());
			writeByte(brItem.getDaysOfWeek());
			writeByte(brItem.getStartHour());
			writeByte(brItem.getStartMinute());
			writeByte(brItem.getStopHour());
			writeByte(brItem.getStopMinute());
			
			// Daily account limit.
			if ((brItem.getAccountDailyLimit() > 0) && (_player.getAccountVariables().getInt(AccountVariables.PRIME_SHOP_PRODUCT_DAILY_COUNT + brItem.getBrId(), 0) >= brItem.getAccountDailyLimit()))
			{
				writeInt(brItem.getAccountDailyLimit());
				writeInt(brItem.getAccountDailyLimit());
			}
			// General account limit.
			else if ((brItem.getAccountBuyLimit() > 0) && (_player.getAccountVariables().getInt(AccountVariables.PRIME_SHOP_PRODUCT_COUNT + brItem.getBrId(), 0) >= brItem.getAccountBuyLimit()))
			{
				writeInt(brItem.getAccountBuyLimit());
				writeInt(brItem.getAccountBuyLimit());
			}
			else
			{
				writeInt(brItem.getStock());
				writeInt(brItem.getTotal());
			}
			
			writeByte(brItem.getSalePercent());
			writeByte(brItem.getMinLevel());
			writeByte(brItem.getMaxLevel());
			writeInt(brItem.getMinBirthday());
			writeInt(brItem.getMaxBirthday());
			
			// Daily account limit.
			if (brItem.getAccountDailyLimit() > 0)
			{
				writeInt(1); // Days
				writeInt(brItem.getAccountDailyLimit()); // Amount
			}
			// General account limit.
			else if (brItem.getAccountBuyLimit() > 0)
			{
				writeInt(-1); // Days
				writeInt(brItem.getAccountBuyLimit()); // Amount
			}
			else
			{
				writeInt(0); // Days
				writeInt(0); // Amount
			}
			
			writeByte(brItem.getItems().size());
			for (PrimeShopItem item : brItem.getItems())
			{
				writeInt(item.getId());
				writeInt((int) item.getCount());
				writeInt(item.getWeight());
				writeInt(item.isTradable());
			}
		}
	}
}