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
package org.l2jmobius.gameserver.model.holders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.l2jmobius.gameserver.enums.UpgradeDataType;

public class EquipmentUpgradeNormalHolder
{
	private final int _id;
	private final int _type;
	private final long _commission;
	private final double _chance;
	private final ItemEnchantHolder _initialItem;
	private final double _chanceToReceiveBonusItems;
	private final Map<UpgradeDataType, List<ItemEnchantHolder>> _items = new HashMap<>();
	
	/**
	 * @implNote Holder for "UpgradeNormal" equipment system; <list>
	 *           <li>Final Holder will be have getter getItems which get UpgradeDataType;</li>
	 *           <li>Don't forget to check in isHasCategory category type in getItems, for don`t get null or empty collections;</li> </list>
	 * @param id Upgrade ID in DAT file; (yep, duplication);
	 * @param type Upgrade type in DAT file (1 / 2 (used in classic);
	 * @param commission Default Adena count, needed for make "Transformation";
	 * @param chance Success chance of made "Transformation";
	 * @param initialItem Item for upgrade; (cannot be empty)
	 * @param materialItems Materials for upgrade; (can be empty)
	 * @param onSuccessItems Items, which player gets if RND will be < chance (if win);
	 * @param onFailureItems Items, which player gets if RND will be > chance (if lose);
	 * @param chanceToReceiveBonusItems Chance to obtain additional reward on Success (if win);
	 * @param bonusItems Bonus Items;
	 */
	public EquipmentUpgradeNormalHolder(int id, int type, long commission, double chance, ItemEnchantHolder initialItem, List<ItemEnchantHolder> materialItems, List<ItemEnchantHolder> onSuccessItems, List<ItemEnchantHolder> onFailureItems, double chanceToReceiveBonusItems, List<ItemEnchantHolder> bonusItems)
	{
		_id = id;
		_type = type;
		_commission = commission;
		_chance = chance;
		_initialItem = initialItem;
		_chanceToReceiveBonusItems = chanceToReceiveBonusItems;
		if (materialItems != null)
		{
			_items.put(UpgradeDataType.MATERIAL, materialItems);
		}
		_items.put(UpgradeDataType.ON_SUCCESS, onSuccessItems);
		if (onFailureItems != null)
		{
			_items.put(UpgradeDataType.ON_FAILURE, onFailureItems);
		}
		if (bonusItems != null)
		{
			_items.put(UpgradeDataType.BONUS_TYPE, bonusItems);
		}
	}
	
	public int getId()
	{
		return _id;
	}
	
	public int getType()
	{
		return _type;
	}
	
	public long getCommission()
	{
		return _commission;
	}
	
	public double getChance()
	{
		return _chance;
	}
	
	public ItemEnchantHolder getInitialItem()
	{
		return _initialItem;
	}
	
	public double getChanceToReceiveBonusItems()
	{
		return _chanceToReceiveBonusItems;
	}
	
	public List<ItemEnchantHolder> getItems(UpgradeDataType upgradeDataType)
	{
		return _items.get(upgradeDataType);
	}
	
	public boolean isHasCategory(UpgradeDataType upgradeDataType)
	{
		if (_items.isEmpty())
		{
			return false;
		}
		return _items.containsKey(upgradeDataType);
	}
}
