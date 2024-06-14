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
package org.l2j.gameserver.network.clientpackets.equipmentupgrade;

import org.l2j.gameserver.data.xml.EquipmentUpgradeData;
import org.l2j.gameserver.enums.AttributeType;
import org.l2j.gameserver.model.ItemInfo;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.ensoul.EnsoulOption;
import org.l2j.gameserver.model.holders.EquipmentUpgradeHolder;
import org.l2j.gameserver.model.holders.ItemHolder;
import org.l2j.gameserver.model.item.enchant.attribute.AttributeHolder;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.variables.ItemVariables;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.ExItemAnnounce;
import org.l2j.gameserver.network.serverpackets.equipmentupgrade.ExUpgradeSystemResult;
import org.l2j.gameserver.util.Broadcast;

/**
 * @author Mobius
 */
public class RequestUpgradeSystemResult extends ClientPacket
{
	private int _objectId;
	private int _upgradeId;
	
	@Override
	protected void readImpl()
	{
		_objectId = readInt();
		_upgradeId = readInt();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		final Item existingItem = player.getInventory().getItemByObjectId(_objectId);
		if (existingItem == null)
		{
			player.sendPacket(new ExUpgradeSystemResult(0, 0));
			return;
		}
		
		final EquipmentUpgradeHolder upgradeHolder = EquipmentUpgradeData.getInstance().getUpgrade(_upgradeId);
		if (upgradeHolder == null)
		{
			player.sendPacket(new ExUpgradeSystemResult(0, 0));
			return;
		}
		
		for (ItemHolder material : upgradeHolder.getMaterials())
		{
			if (player.getInventory().getInventoryItemCount(material.getId(), -1) < material.getCount())
			{
				player.sendPacket(new ExUpgradeSystemResult(0, 0));
				return;
			}
		}
		
		final long adena = upgradeHolder.getAdena();
		if ((adena > 0) && (player.getAdena() < adena))
		{
			player.sendPacket(new ExUpgradeSystemResult(0, 0));
			return;
		}
		
		if ((existingItem.getTemplate().getId() != upgradeHolder.getRequiredItemId()) || (existingItem.getEnchantLevel() != upgradeHolder.getRequiredItemEnchant()))
		{
			player.sendPacket(new ExUpgradeSystemResult(0, 0));
			return;
		}
		
		// Store old item enchantment info.
		final ItemInfo itemEnchantment = new ItemInfo(existingItem);
		
		// Get materials.
		player.destroyItem("UpgradeEquipment", _objectId, 1, player, true);
		for (ItemHolder material : upgradeHolder.getMaterials())
		{
			player.destroyItemByItemId("UpgradeEquipment", material.getId(), material.getCount(), player, true);
		}
		if (adena > 0)
		{
			player.reduceAdena("UpgradeEquipment", adena, player, true);
		}
		
		// Give item.
		final Item addedItem = player.addItem("UpgradeEquipment", upgradeHolder.getResultItemId(), 1, player, true);
		if (upgradeHolder.isAnnounce())
		{
			Broadcast.toAllOnlinePlayers(new ExItemAnnounce(player, addedItem, ExItemAnnounce.UPGRADE));
		}
		
		// Transfer item enchantments.
		if (addedItem.isEquipable())
		{
			addedItem.setAugmentation(itemEnchantment.getAugmentation(), false);
			if (addedItem.isWeapon() && (addedItem.getTemplate().getAttributes() == null))
			{
				if (itemEnchantment.getAttackElementPower() > 0)
				{
					addedItem.setAttribute(new AttributeHolder(AttributeType.findByClientId(itemEnchantment.getAttackElementType()), itemEnchantment.getAttackElementPower()), false);
				}
			}
			else if (addedItem.getTemplate().getAttributes() == null)
			{
				if (itemEnchantment.getAttributeDefence(AttributeType.FIRE) > 0)
				{
					addedItem.setAttribute(new AttributeHolder(AttributeType.FIRE, itemEnchantment.getAttributeDefence(AttributeType.FIRE)), false);
				}
				if (itemEnchantment.getAttributeDefence(AttributeType.WATER) > 0)
				{
					addedItem.setAttribute(new AttributeHolder(AttributeType.WATER, itemEnchantment.getAttributeDefence(AttributeType.WATER)), false);
				}
				if (itemEnchantment.getAttributeDefence(AttributeType.WIND) > 0)
				{
					addedItem.setAttribute(new AttributeHolder(AttributeType.WIND, itemEnchantment.getAttributeDefence(AttributeType.WIND)), false);
				}
				if (itemEnchantment.getAttributeDefence(AttributeType.EARTH) > 0)
				{
					addedItem.setAttribute(new AttributeHolder(AttributeType.EARTH, itemEnchantment.getAttributeDefence(AttributeType.EARTH)), false);
				}
				if (itemEnchantment.getAttributeDefence(AttributeType.HOLY) > 0)
				{
					addedItem.setAttribute(new AttributeHolder(AttributeType.HOLY, itemEnchantment.getAttributeDefence(AttributeType.HOLY)), false);
				}
				if (itemEnchantment.getAttributeDefence(AttributeType.DARK) > 0)
				{
					addedItem.setAttribute(new AttributeHolder(AttributeType.DARK, itemEnchantment.getAttributeDefence(AttributeType.DARK)), false);
				}
			}
			if (itemEnchantment.getSoulCrystalOptions() != null)
			{
				int pos = -1;
				for (EnsoulOption ensoul : itemEnchantment.getSoulCrystalOptions())
				{
					pos++;
					addedItem.addSpecialAbility(ensoul, pos, 1, false);
				}
			}
			if (itemEnchantment.getSoulCrystalSpecialOptions() != null)
			{
				for (EnsoulOption ensoul : itemEnchantment.getSoulCrystalSpecialOptions())
				{
					addedItem.addSpecialAbility(ensoul, 0, 2, false);
				}
			}
			if (itemEnchantment.getVisualId() > 0)
			{
				final ItemVariables oldVars = existingItem.getVariables();
				final ItemVariables newVars = addedItem.getVariables();
				newVars.set(ItemVariables.VISUAL_ID, oldVars.getInt(ItemVariables.VISUAL_ID, 0));
				newVars.set(ItemVariables.VISUAL_APPEARANCE_STONE_ID, oldVars.getInt(ItemVariables.VISUAL_APPEARANCE_STONE_ID, 0));
				newVars.set(ItemVariables.VISUAL_APPEARANCE_LIFE_TIME, oldVars.getLong(ItemVariables.VISUAL_APPEARANCE_LIFE_TIME, 0));
				newVars.storeMe();
				addedItem.scheduleVisualLifeTime();
			}
		}
		
		// Apply update holder enchant.
		final int enchantLevel = upgradeHolder.getResultItemEnchant();
		if (enchantLevel > 0)
		{
			addedItem.setEnchantLevel(enchantLevel);
		}
		
		// Save item.
		addedItem.updateDatabase(true);
		
		// Send result 
		player.sendPacket(new ExUpgradeSystemResult(addedItem.getObjectId(), 1));
		player.sendItemList();
	}
}
