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
package org.l2jmobius.gameserver.model.item.henna;

import org.l2jmobius.gameserver.model.holders.ItemHolder;
import org.l2jmobius.gameserver.model.item.combination.CombinationItemType;

/**
 * @author Index
 */
public class CombinationHennaReward extends ItemHolder
{
	private final int _hennaId;
	private final CombinationItemType _type;
	
	public CombinationHennaReward(int id, int count, CombinationItemType type)
	{
		super(id, count);
		_hennaId = 0;
		_type = type;
	}
	
	public CombinationHennaReward(int hennaId, int id, int count, CombinationItemType type)
	{
		super(id, count);
		_hennaId = hennaId;
		_type = type;
	}
	
	public int getHennaId()
	{
		return _hennaId;
	}
	
	public CombinationItemType getType()
	{
		return _type;
	}
}
