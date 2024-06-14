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
package org.l2j.gameserver.network.clientpackets.variation;

import org.l2j.gameserver.model.VariationInstance;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.request.VariationRequest;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.variation.ApplyVariationOption;

/**
 * @author Index
 */
public class ExApplyVariationOption extends ClientPacket
{
	private int _enchantedObjectId;
	private int _option1;
	private int _option2;
	
	@Override
	protected void readImpl()
	{
		_enchantedObjectId = readInt();
		_option1 = readInt();
		_option2 = readInt();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		final VariationRequest request = player.getRequest(VariationRequest.class);
		final Item targetItem = request.getAugmentedItem();
		final VariationInstance augment = request.getAugment();
		final int option1Id = augment.getOption1Id();
		final int option2Id = augment.getOption2Id();
		
		if ((targetItem.getObjectId() != _enchantedObjectId) || (_option1 != option1Id) || (_option2 != option2Id))
		{
			player.sendPacket(new ApplyVariationOption(0, 0, 0, 0));
			return;
		}
		
		targetItem.setAugmentation(augment, true);
		
		player.sendPacket(new ApplyVariationOption(1, _enchantedObjectId, _option1, _option2));
		
		// Apply new augment.
		if (targetItem.isEquipped())
		{
			targetItem.getAugmentation().applyBonus(player);
		}
		
		// Recalculate all stats.
		player.getStat().recalculateStats(true);
		
		player.sendItemList();
		player.removeRequest(VariationRequest.class);
	}
}