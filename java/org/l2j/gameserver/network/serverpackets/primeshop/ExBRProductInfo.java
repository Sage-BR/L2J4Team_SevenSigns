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

import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.primeshop.PrimeShopGroup;
import org.l2j.gameserver.model.primeshop.PrimeShopItem;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Gnacik
 */
public class ExBRProductInfo extends ServerPacket
{
	private final PrimeShopGroup _item;
	private final int _charPoints;
	private final long _charAdena;
	private final long _charCoins;
	
	public ExBRProductInfo(PrimeShopGroup item, Player player)
	{
		_item = item;
		_charPoints = player.getPrimePoints();
		_charAdena = player.getAdena();
		_charCoins = player.getInventory().getInventoryItemCount(23805, -1);
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_BR_PRODUCT_INFO.writeId(this);
		writeInt(_item.getBrId());
		writeInt(_item.getPrice());
		writeInt(_item.getItems().size());
		for (PrimeShopItem item : _item.getItems())
		{
			writeInt(item.getId());
			writeInt((int) item.getCount());
			writeInt(item.getWeight());
			writeInt(item.isTradable());
		}
		writeLong(_charAdena);
		writeLong(_charPoints);
		writeLong(_charCoins); // Hero coins
	}
}
