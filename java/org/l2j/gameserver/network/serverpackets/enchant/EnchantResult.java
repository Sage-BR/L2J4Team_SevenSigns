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
package org.l2j.gameserver.network.serverpackets.enchant;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.model.holders.ItemHolder;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Index
 */
public class EnchantResult extends ServerPacket
{
	public static final int SUCCESS = 0;
	/**
	 * if (Type == ITEME_ENCHT_AG || Type == ITEME_BLESS_ENCHT_AG || Type == ITEME_MULTI_ENCHT_AG || Type == ITEME_ANCIENT_CRYSTAL_ENCHANT_AG) The growth failed. The agathion disappeared. else S1_CRYSTALLIZED calculate challenge points
	 */
	public static final int FAIL = 1;
	/**
	 * remove scrolls
	 */
	public static final int ERROR = 2;
	/**
	 * Deprecated
	 */
	public static final int BLESSED_FAIL = 3;
	/**
	 * if (Type == ITEME_ENCHT_AG || Type == ITEME_BLESS_ENCHT_AG || Type == ITEME_MULTI_ENCHT_AG || Type == ITEME_ANCIENT_CRYSTAL_ENCHANT_AG) The growth failed. The agathion disappeared. else CRYSTALLIZED
	 */
	public static final int NO_CRYSTAL = 4;
	/**
	 * Deprecated
	 */
	public static final int SAFE_FAIL = 5;
	/**
	 * FAILURE - Enchantment failed. You have obtained the listed items.
	 */
	public static final int CHALLENGE_ENCHANT_SAFE = 6;
	/**
	 * DECREASE *will show item*
	 */
	public static final int DECREASE = 7; // -> set enchant to 0 in UI
	/**
	 * if(isAutoEnchanting || isAutoEnchantingStop) will reuse scroll if (Type == ITEME_ENCHT_AG || Type == ITEME_BLESS_ENCHT_AG || Type == ITEME_MULTI_ENCHT_AG || Type == ITEME_ANCIENT_CRYSTAL_ENCHANT_AG) IN_CASE_OF_FAILURE_THE_AGATHION_S_GROWTH_LEVEL_WILL_REMAIN_THE_SAME else Enchant failed. The
	 * enchant skill for the corresponding item will be exactly retained. *will show item*
	 */
	public static final int REMAIN = 8; // -> remaining the same with sys string
	/**
	 * if(Type == ITEME_ENCHT_AG || Type == ITEME_BLESS_ENCHT_AG || Type == ITEME_MULTI_ENCHT_AG || Type == ITEME_ANCIENT_CRYSTAL_ENCHANT_AG) The growth failed. The agathion disappeared. else Enchantment failed. You have obtained the listed items. *will show item*
	 */
	public static final int FAILED_WITH_OPTIONS_NO_AND_NO_POINTS = 9;
	/**
	 * if (Type == ITEME_ENCHT_AG || Type == ITEME_BLESS_ENCHT_AG || Type == ITEME_MULTI_ENCHT_AG || Type == ITEME_ANCIENT_CRYSTAL_ENCHANT_AG) The growth failed. The agathion's growth level is reset. else Enchantment failed. You have obtained the listed items. if (isAutoEnchanting ||
	 * isAutoEnchantingStop && SelectItemInfo.Enchanted == EnchantValue) will reuse scroll in blue auto enchant *will show item*
	 */
	public static final int SAFE_FAIL_02 = 10;
	
	private final int _result;
	private final ItemHolder _crystal;
	private final ItemHolder _additional;
	private final int _enchantLevel;
	
	public EnchantResult(int result, ItemHolder crystal, ItemHolder additionalItem, int enchantLevel)
	{
		_result = result;
		_crystal = crystal == null ? new ItemHolder(0, 0) : crystal;
		_additional = additionalItem == null ? new ItemHolder(0, 0) : additionalItem;
		_enchantLevel = enchantLevel;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.ENCHANT_RESULT.writeId(this, buffer);
		buffer.writeInt(_result);
		buffer.writeInt(_crystal.getId());
		buffer.writeLong(_crystal.getCount());
		buffer.writeInt(_additional.getId());
		buffer.writeLong(_additional.getCount());
		buffer.writeInt(_enchantLevel);
	}
}
