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
package org.l2j.gameserver.network.clientpackets;

import java.util.List;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.enums.PlayerCondOverride;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.item.EtcItem;
import org.l2j.gameserver.model.item.ItemTemplate;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.serverpackets.InventoryUpdate;
import org.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Zoey76
 */
public class RequestUnEquipItem implements ClientPacket
{
	private int _slot;
	
	/**
	 * Packet type id 0x16 format: cd
	 */
	@Override
	public void read(ReadablePacket packet)
	{
		_slot = packet.readInt();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final Item item = player.getInventory().getPaperdollItemByItemId(_slot);
		// Wear-items are not to be unequipped.
		if (item == null)
		{
			return;
		}
		
		// The English system message say weapon, but it's applied to any equipped item.
		if (player.isAttackingNow() || player.isCastingNow())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_CHANGE_WEAPONS_DURING_AN_ATTACK);
			return;
		}
		
		// Arrows and bolts.
		if ((_slot == ItemTemplate.SLOT_L_HAND) && (item.getTemplate() instanceof EtcItem))
		{
			return;
		}
		
		// Prevent of unequipping a cursed weapon.
		if ((_slot == ItemTemplate.SLOT_LR_HAND) && (player.isCursedWeaponEquipped() || player.isCombatFlagEquipped()))
		{
			return;
		}
		
		// Prevent player from unequipping items in special conditions.
		if (player.hasBlockActions() || player.isAlikeDead())
		{
			return;
		}
		
		if (!player.getInventory().canManipulateWithItemId(item.getId()) || (item.isWeapon() && item.getWeaponItem().isForceEquip() && !player.canOverrideCond(PlayerCondOverride.ITEM_CONDITIONS)))
		{
			player.sendPacket(SystemMessageId.THAT_ITEM_CANNOT_BE_TAKEN_OFF);
			return;
		}
		
		final List<Item> unequipped = player.getInventory().unEquipItemInBodySlotAndRecord(_slot);
		player.broadcastUserInfo();
		
		// This can be 0 if the user pressed the right mouse button twice very fast.
		if (!unequipped.isEmpty())
		{
			SystemMessage sm = null;
			final Item unequippedItem = unequipped.get(0);
			if (unequippedItem.getEnchantLevel() > 0)
			{
				sm = new SystemMessage(SystemMessageId.S1_S2_UNEQUIPPED);
				sm.addInt(unequippedItem.getEnchantLevel());
			}
			else
			{
				sm = new SystemMessage(SystemMessageId.S1_UNEQUIPPED);
			}
			sm.addItemName(unequippedItem);
			player.sendPacket(sm);
			
			final InventoryUpdate iu = new InventoryUpdate();
			iu.addItems(unequipped);
			player.sendInventoryUpdate(iu);
		}
	}
}
