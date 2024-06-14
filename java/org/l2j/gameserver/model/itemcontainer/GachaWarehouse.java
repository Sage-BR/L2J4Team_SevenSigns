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
package org.l2j.gameserver.model.itemcontainer;

import org.l2j.gameserver.enums.ItemLocation;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.actor.Player;

public class GachaWarehouse extends Warehouse
{
	public static final int MAXIMUM_TEMPORARY_WAREHOUSE_COUNT = 1_100;
	
	private final Creature _owner;
	
	public GachaWarehouse(Player player)
	{
		_owner = player;
	}
	
	@Override
	protected Creature getOwner()
	{
		return _owner;
	}
	
	@Override
	protected ItemLocation getBaseLocation()
	{
		return ItemLocation.GACHA;
	}
}
