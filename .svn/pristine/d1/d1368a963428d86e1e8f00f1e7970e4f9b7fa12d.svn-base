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
package org.l2jmobius.gameserver.network.clientpackets.limitshop;

import java.util.List;

import org.l2jmobius.gameserver.data.xml.LimitShopClanData;
import org.l2jmobius.gameserver.data.xml.LimitShopCraftData;
import org.l2jmobius.gameserver.data.xml.LimitShopData;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.LimitShopProductHolder;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;
import org.l2jmobius.gameserver.network.serverpackets.limitshop.ExPurchaseLimitShopItemListNew;

/**
 * @author Mobius
 */
public class RequestPurchaseLimitShopItemList extends ClientPacket
{
	private static final int MAX_PAGE_SIZE = 350;
	
	private int _shopType;
	
	@Override
	protected void readImpl()
	{
		_shopType = readByte();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		final List<LimitShopProductHolder> products;
		switch (_shopType)
		{
			case 3: // Normal Lcoin Shop
			{
				products = LimitShopData.getInstance().getProducts();
				break;
			}
			case 4: // Lcoin Special Craft
			{
				products = LimitShopCraftData.getInstance().getProducts();
				break;
			}
			case 100: // Clan Shop
			{
				products = LimitShopClanData.getInstance().getProducts();
				break;
			}
			default:
			{
				return;
			}
		}
		
		// Calculate the number of pages.
		final int totalPages = (products.size() / MAX_PAGE_SIZE) + ((products.size() % MAX_PAGE_SIZE) == 0 ? 0 : 1);
		
		// Iterate over pages.
		for (int page = 0; page < totalPages; page++)
		{
			// Calculate start and end indices for each page.
			final int start = page * MAX_PAGE_SIZE;
			final int end = Math.min(start + MAX_PAGE_SIZE, products.size());
			
			// Get the subList for current page.
			final List<LimitShopProductHolder> productList = products.subList(start, end);
			
			// Send the 
			player.sendPacket(new ExPurchaseLimitShopItemListNew(player, _shopType, page + 1, totalPages, productList));
		}
	}
}
