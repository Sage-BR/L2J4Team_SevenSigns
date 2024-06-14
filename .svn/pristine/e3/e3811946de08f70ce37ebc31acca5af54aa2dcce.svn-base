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
package org.l2jmobius.gameserver.network.clientpackets.enchant;

import org.l2jmobius.gameserver.data.xml.EnchantItemData;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.actor.request.EnchantItemRequest;
import org.l2jmobius.gameserver.model.item.enchant.EnchantScroll;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;
import org.l2jmobius.gameserver.network.serverpackets.enchant.ExPutEnchantScrollItemResult;

/**
 * @author Sdw
 */
public class RequestExAddEnchantScrollItem extends ClientPacket
{
	private int _scrollObjectId;
	
	@Override
	protected void readImpl()
	{
		_scrollObjectId = readInt();
		readInt(); // enchantObjectId?
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (player.getRequest(EnchantItemRequest.class) == null)
		{
			player.addRequest(new EnchantItemRequest(player, _scrollObjectId));
		}
		final EnchantItemRequest request = player.getRequest(EnchantItemRequest.class);
		request.setEnchantingScroll(_scrollObjectId);
		
		final Item scroll = request.getEnchantingScroll();
		if ((scroll == null))
		{
			// Message may be custom.
			player.sendPacket(SystemMessageId.AUGMENTATION_REQUIREMENTS_ARE_NOT_FULFILLED);
			player.sendPacket(new ExPutEnchantScrollItemResult(0));
			request.setEnchantingItem(Player.ID_NONE);
			request.setEnchantingScroll(Player.ID_NONE);
			return;
		}
		
		final EnchantScroll scrollTemplate = EnchantItemData.getInstance().getEnchantScroll(scroll);
		if ((scrollTemplate == null))
		{
			// Message may be custom.
			player.sendPacket(SystemMessageId.AUGMENTATION_REQUIREMENTS_ARE_NOT_FULFILLED);
			player.sendPacket(new ExPutEnchantScrollItemResult(0));
			request.setEnchantingScroll(Player.ID_NONE);
			return;
		}
		
		request.setTimestamp(System.currentTimeMillis());
		player.sendPacket(new ExPutEnchantScrollItemResult(_scrollObjectId));
	}
}
