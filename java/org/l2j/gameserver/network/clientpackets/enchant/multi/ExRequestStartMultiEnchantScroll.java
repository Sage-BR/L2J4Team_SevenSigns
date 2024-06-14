/*
 * This file is part of the L2J 4Team Project.
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
package org.l2j.gameserver.network.clientpackets.enchant.multi;

import org.l2j.gameserver.data.xml.EnchantItemData;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.request.EnchantItemRequest;
import org.l2j.gameserver.model.item.enchant.EnchantScroll;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.enchant.multi.ExResetSelectMultiEnchantScroll;

/**
 * @author Index
 */
public class ExRequestStartMultiEnchantScroll extends ClientPacket
{
	private int _scrollObjectId;
	
	@Override
	protected void readImpl()
	{
		_scrollObjectId = readInt();
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
		
		final Item scroll = player.getInventory().getItemByObjectId(_scrollObjectId);
		final EnchantScroll scrollTemplate = EnchantItemData.getInstance().getEnchantScroll(scroll);
		if ((scrollTemplate == null) || scrollTemplate.isBlessed() || scrollTemplate.isBlessedDown() || scrollTemplate.isSafe() || scrollTemplate.isGiant())
		{
			player.sendPacket(new ExResetSelectMultiEnchantScroll(player, _scrollObjectId, 1));
			return;
		}
		
		request.setEnchantingScroll(_scrollObjectId);
		
		player.sendPacket(new ExResetSelectMultiEnchantScroll(player, _scrollObjectId, 0));
	}
}