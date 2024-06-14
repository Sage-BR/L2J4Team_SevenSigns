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
package org.l2j.gameserver.network.serverpackets.enchant;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.data.xml.EnchantItemData;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.request.EnchantItemRequest;
import org.l2j.gameserver.model.holders.ItemHolder;
import org.l2j.gameserver.model.item.enchant.EnchantScroll;
import org.l2j.gameserver.model.item.enchant.EnchantSupportItem;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Index
 */
public class ResetEnchantItemFailRewardInfo extends ServerPacket
{
	private final Player _player;
	
	public ResetEnchantItemFailRewardInfo(Player player)
	{
		_player = player;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		if (_player.getRequest(EnchantItemRequest.class) == null)
		{
			return;
		}
		
		final EnchantItemRequest request = _player.getRequest(EnchantItemRequest.class);
		if ((request.getEnchantingItem() == null) || request.isProcessing() || (request.getEnchantingScroll() == null))
		{
			return;
		}
		
		final EnchantScroll enchantScroll = EnchantItemData.getInstance().getEnchantScroll(request.getEnchantingScroll());
		final Item enchantItem = request.getEnchantingItem();
		Item addedItem = new Item(enchantItem.getId());
		addedItem.setOwnerId(_player.getObjectId());
		addedItem.setEnchantLevel(request.getEnchantingItem().getEnchantLevel());
		EnchantSupportItem enchantSupportItem = null;
		ItemHolder result = null;
		if (request.getSupportItem() != null)
		{
			enchantSupportItem = EnchantItemData.getInstance().getSupportItem(request.getSupportItem());
		}
		if (enchantScroll.isBlessed() || ((request.getSupportItem() != null) && (enchantSupportItem != null) && enchantSupportItem.isBlessed()))
		{
			addedItem.setEnchantLevel(0);
		}
		else if (enchantScroll.isBlessedDown() || enchantScroll.isCursed() /* || ((request.getSupportItem() != null) && (enchantSupportItem != null) && enchantSupportItem.isDown()) */)
		{
			addedItem.setEnchantLevel(enchantItem.getEnchantLevel() - 1);
		}
		else if (enchantScroll.isSafe())
		{
			addedItem.setEnchantLevel(enchantItem.getEnchantLevel());
		}
		else
		{
			addedItem = null;
			if (enchantItem.getTemplate().isCrystallizable())
			{
				result = new ItemHolder(enchantItem.getTemplate().getCrystalItemId(), Math.max(0, enchantItem.getCrystalCount() - ((enchantItem.getTemplate().getCrystalCount() + 1) / 2)));
			}
		}
		
		ServerPackets.EX_RES_ENCHANT_ITEM_FAIL_REWARD_INFO.writeId(this, buffer);
		
		buffer.writeInt(enchantItem.getObjectId());
		
		int challengeGroup = _player.getChallengeInfo().getNowGroup();
		int challengePoint = _player.getChallengeInfo().getNowPoint();
		buffer.writeInt(challengeGroup);
		buffer.writeInt(challengePoint);
		
		if (result != null)
		{
			buffer.writeInt(1); // Loop count.
			buffer.writeInt(result.getId());
			buffer.writeInt((int) result.getCount());
		}
		else if (addedItem != null)
		{
			buffer.writeInt(1); // Loop count.
			buffer.writeInt(enchantItem.getId());
			buffer.writeInt(1);
		}
		else
		{
			buffer.writeInt(0); // Loop count.
		}
	}
}
