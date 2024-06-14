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
package org.l2j.gameserver.model.holders;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Gustavo Fonseca
 */
public class LimitShopRandomCraftReward
{
	private final int _itemId;
	private final AtomicInteger _count;
	private final int _rewardIndex;
	
	public LimitShopRandomCraftReward(int itemId, int count, int rewardIndex)
	{
		_itemId = itemId;
		_count = new AtomicInteger(count);
		_rewardIndex = rewardIndex;
	}
	
	public int getItemId()
	{
		return _itemId;
	}
	
	public AtomicInteger getCount()
	{
		return _count;
	}
	
	public int getRewardIndex()
	{
		return _rewardIndex;
	}
}