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
package org.l2j.gameserver.model.actor.request;

import org.l2j.gameserver.model.VariationInstance;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.item.instance.Item;

/**
 * @author Index
 */
public class VariationRequest extends AbstractRequest
{
	private Item _augmented;
	private Item _mineral;
	private VariationInstance _augmentation;
	
	public VariationRequest(Player player)
	{
		super(player);
	}
	
	public synchronized void setAugmentedItem(int objectId)
	{
		_augmented = getPlayer().getInventory().getItemByObjectId(objectId);
	}
	
	public synchronized Item getAugmentedItem()
	{
		return _augmented;
	}
	
	public synchronized void setMineralItem(int objectId)
	{
		_mineral = getPlayer().getInventory().getItemByObjectId(objectId);
	}
	
	public synchronized Item getMineralItem()
	{
		return _mineral;
	}
	
	public synchronized void setAugment(VariationInstance augment)
	{
		_augmentation = augment;
	}
	
	public synchronized VariationInstance getAugment()
	{
		return _augmentation;
	}
	
	@Override
	public boolean isUsing(int objectId)
	{
		return false;
	}
}
