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
package org.l2j.gameserver.network.serverpackets;

import org.l2j.gameserver.model.ManufactureItem;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.stats.Stat;
import org.l2j.gameserver.network.ServerPackets;

public class RecipeShopSellList extends ServerPacket
{
	private final Player _buyer;
	private final Player _manufacturer;
	private final double _craftRate;
	private final double _craftCritical;
	
	public RecipeShopSellList(Player buyer, Player manufacturer)
	{
		_buyer = buyer;
		_manufacturer = manufacturer;
		_craftRate = _manufacturer.getStat().getValue(Stat.CRAFT_RATE, 0);
		_craftCritical = _manufacturer.getStat().getValue(Stat.CRAFTING_CRITICAL, 0);
	}
	
	@Override
	public void write()
	{
		ServerPackets.RECIPE_SHOP_SELL_LIST.writeId(this);
		writeInt(_manufacturer.getObjectId());
		writeInt((int) _manufacturer.getCurrentMp()); // Creator's MP
		writeInt(_manufacturer.getMaxMp()); // Creator's MP
		writeLong(_buyer.getAdena()); // Buyer Adena
		if (!_manufacturer.hasManufactureShop())
		{
			writeInt(0);
		}
		else
		{
			writeInt(_manufacturer.getManufactureItems().size());
			for (ManufactureItem item : _manufacturer.getManufactureItems().values())
			{
				writeInt(item.getRecipeId());
				writeInt(0); // CanCreate?
				writeLong(item.getCost());
				writeDouble(Math.min(_craftRate, 100.0));
				writeByte(_craftCritical > 0);
				writeDouble(Math.min(_craftCritical, 100.0));
			}
		}
	}
}
