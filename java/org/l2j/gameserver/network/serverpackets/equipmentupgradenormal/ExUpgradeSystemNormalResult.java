/*
 * This file is part of the L2J 4Team project.
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
package org.l2j.gameserver.network.serverpackets.equipmentupgradenormal;

import java.util.Collections;
import java.util.List;

import org.l2j.gameserver.model.holders.UniqueItemEnchantHolder;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.AbstractItemPacket;

/**
 * @author Index
 */
public class ExUpgradeSystemNormalResult extends AbstractItemPacket
{
	public static final ExUpgradeSystemNormalResult FAIL = new ExUpgradeSystemNormalResult(0, 0, false, Collections.emptyList(), Collections.emptyList());
	
	private final int _result;
	private final int _upgradeId;
	private final boolean _success;
	private final List<UniqueItemEnchantHolder> _resultItems;
	private final List<UniqueItemEnchantHolder> _bonusItems;
	
	public ExUpgradeSystemNormalResult(int result, int upgradeId, boolean success, List<UniqueItemEnchantHolder> resultItems, List<UniqueItemEnchantHolder> bonusItems)
	{
		_result = result;
		_upgradeId = upgradeId;
		_success = success;
		_resultItems = resultItems;
		_bonusItems = bonusItems;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_UPGRADE_SYSTEM_NORMAL_RESULT.writeId(this);
		writeShort(_result); // Result ID
		writeInt(_upgradeId); // Upgrade ID
		writeByte(_success); // Success
		writeInt(_resultItems.size()); // Array of result items (success/failure) start.
		for (UniqueItemEnchantHolder item : _resultItems)
		{
			writeInt(item.getObjectId());
			writeInt(item.getId());
			writeInt(item.getEnchantLevel());
			writeInt(Math.toIntExact(item.getCount()));
		}
		writeByte(0); // Is bonus? Do not see any effect.
		writeInt(_bonusItems.size()); // Array of bonus items start.
		for (UniqueItemEnchantHolder bonus : _bonusItems)
		{
			writeInt(bonus.getObjectId());
			writeInt(bonus.getId());
			writeInt(bonus.getEnchantLevel());
			writeInt(Math.toIntExact(bonus.getCount()));
		}
	}
}
