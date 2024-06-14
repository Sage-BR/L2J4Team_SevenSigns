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
package org.l2jmobius.gameserver.network.clientpackets.variation;

import org.l2jmobius.gameserver.data.xml.VariationData;
import org.l2jmobius.gameserver.model.VariationInstance;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.model.options.Variation;
import org.l2jmobius.gameserver.model.options.VariationFee;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.clientpackets.AbstractRefinePacket;
import org.l2jmobius.gameserver.network.serverpackets.ExVariationResult;
import org.l2jmobius.gameserver.network.serverpackets.InventoryUpdate;

/**
 * Format:(ch) ddc
 * @author -Wooden-, Index
 */
public class RequestRefine extends AbstractRefinePacket
{
	private int _targetItemObjId;
	private int _mineralItemObjId;
	
	@Override
	protected void readImpl()
	{
		_targetItemObjId = readInt();
		_mineralItemObjId = readInt();
		readByte(); // is event
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		final Item targetItem = player.getInventory().getItemByObjectId(_targetItemObjId);
		if (targetItem == null)
		{
			return;
		}
		
		final Item mineralItem = player.getInventory().getItemByObjectId(_mineralItemObjId);
		if (mineralItem == null)
		{
			return;
		}
		
		final VariationFee fee = VariationData.getInstance().getFee(targetItem.getId(), mineralItem.getId());
		if (fee == null)
		{
			return;
		}
		
		final Item feeItem = player.getInventory().getItemByItemId(fee.getItemId());
		if ((feeItem == null) && (fee.getItemId() != 0))
		{
			return;
		}
		
		if (!isValid(player, targetItem, mineralItem, feeItem, fee))
		{
			player.sendPacket(ExVariationResult.FAIL);
			player.sendPacket(SystemMessageId.AUGMENTATION_FAILED_DUE_TO_INAPPROPRIATE_CONDITIONS);
			return;
		}
		
		if (fee.getAdenaFee() <= 0)
		{
			player.sendPacket(ExVariationResult.FAIL);
			player.sendPacket(SystemMessageId.AUGMENTATION_FAILED_DUE_TO_INAPPROPRIATE_CONDITIONS);
			return;
		}
		
		final long adenaFee = fee.getAdenaFee();
		if ((adenaFee > 0) && (player.getAdena() < adenaFee))
		{
			player.sendPacket(ExVariationResult.FAIL);
			player.sendPacket(SystemMessageId.AUGMENTATION_FAILED_DUE_TO_INAPPROPRIATE_CONDITIONS);
			return;
		}
		
		final Variation variation = VariationData.getInstance().getVariation(mineralItem.getId(), targetItem);
		if (variation == null)
		{
			player.sendPacket(ExVariationResult.FAIL);
			return;
		}
		
		VariationInstance augment = VariationData.getInstance().generateRandomVariation(variation, targetItem);
		if (augment == null)
		{
			player.sendPacket(ExVariationResult.FAIL);
			return;
		}
		
		// Support for single slot augments.
		final VariationInstance oldAugment = targetItem.getAugmentation();
		final int option1 = augment.getOption1Id();
		final int option2 = augment.getOption2Id();
		if (oldAugment != null)
		{
			if (option1 == -1)
			{
				augment = new VariationInstance(augment.getMineralId(), oldAugment.getOption1Id(), option2);
			}
			else if (option2 == -1)
			{
				augment = new VariationInstance(augment.getMineralId(), option1, oldAugment.getOption2Id());
			}
			else
			{
				augment = new VariationInstance(augment.getMineralId(), option1, option2);
			}
			targetItem.removeAugmentation();
		}
		else
		{
			augment = new VariationInstance(augment.getMineralId(), option1, option2);
		}
		
		// Essence does not support creating a new augment without losing old one.
		targetItem.setAugmentation(augment, true);
		final InventoryUpdate iu = new InventoryUpdate();
		iu.addModifiedItem(targetItem);
		player.sendInventoryUpdate(iu);
		
		player.sendPacket(new ExVariationResult(augment.getOption1Id(), augment.getOption2Id(), true));
		
		// Consume the life stone.
		player.destroyItem("RequestRefine", mineralItem, 1, null, false);
		
		// Consume the gemstones.
		if (feeItem != null)
		{
			player.destroyItem("RequestRefine", feeItem, fee.getItemCount(), null, false);
		}
		
		// Consume Adena.
		if (adenaFee > 0)
		{
			player.reduceAdena("RequestRefine", adenaFee, player, false);
		}
	}
}
