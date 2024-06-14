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

import java.util.List;
import java.util.Map;

import org.l2jmobius.gameserver.model.holders.ItemHolder;

/**
 * @author Serenitty
 */
public class DyePotentialFee
{
	private final int _step;
	private final List<ItemHolder> _items;
	private final int _dailyCount;
	private final Map<Integer, Double> _enchantExp;
	
	public DyePotentialFee(int step, List<ItemHolder> items, int dailyCount, Map<Integer, Double> enchantExp)
	{
		_step = step;
		_items = items;
		_dailyCount = dailyCount;
		_enchantExp = enchantExp;
	}
	
	public int getStep()
	{
		return _step;
	}
	
	public List<ItemHolder> getItems()
	{
		return _items;
	}
	
	public int getDailyCount()
	{
		return _dailyCount;
	}
	
	public Map<Integer, Double> getEnchantExp()
	{
		return _enchantExp;
	}
}
