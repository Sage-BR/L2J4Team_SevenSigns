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
package org.l2jmobius.gameserver.network.serverpackets.equipmentupgradenormal;

import java.util.Collections;
import java.util.List;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.model.holders.UniqueItemEnchantHolder;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.AbstractItemPacket;

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
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_UPGRADE_SYSTEM_NORMAL_RESULT.writeId(this, buffer);
		buffer.writeShort(_result); // Result ID
		buffer.writeInt(_upgradeId); // Upgrade ID
		buffer.writeByte(_success); // Success
		buffer.writeInt(_resultItems.size()); // Array of result items (success/failure) start.
		for (UniqueItemEnchantHolder item : _resultItems)
		{
			buffer.writeInt(item.getObjectId());
			buffer.writeInt(item.getId());
			buffer.writeInt(item.getEnchantLevel());
			buffer.writeInt(Math.toIntExact(item.getCount()));
		}
		buffer.writeByte(0); // Is bonus? Do not see any effect.
		buffer.writeInt(_bonusItems.size()); // Array of bonus items start.
		for (UniqueItemEnchantHolder bonus : _bonusItems)
		{
			buffer.writeInt(bonus.getObjectId());
			buffer.writeInt(bonus.getId());
			buffer.writeInt(bonus.getEnchantLevel());
			buffer.writeInt(Math.toIntExact(bonus.getCount()));
		}
	}
}
