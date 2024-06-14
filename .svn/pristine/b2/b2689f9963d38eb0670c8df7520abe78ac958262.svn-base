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
package org.l2jmobius.gameserver.network.serverpackets.newhenna;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Index, Serenitty
 */
public class NewHennaPotenEnchant extends ServerPacket
{
	private final int _slotId;
	private final int _enchantStep;
	private final int _enchantExp;
	private final int _dailyStep;
	private final int _dailyCount;
	private final int _activeStep;
	private final boolean _success;
	
	public NewHennaPotenEnchant(int slotId, int enchantStep, int enchantExp, int dailyStep, int dailyCount, int activeStep, boolean success)
	{
		_slotId = slotId;
		_enchantStep = enchantStep;
		_enchantExp = enchantExp;
		_dailyStep = dailyStep;
		_dailyCount = dailyCount;
		_activeStep = activeStep;
		_success = success;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_NEW_HENNA_POTEN_ENCHANT.writeId(this, buffer);
		buffer.writeByte(_slotId);
		buffer.writeShort(_enchantStep);
		buffer.writeInt(_enchantExp);
		buffer.writeShort(_dailyStep);
		buffer.writeShort(_dailyCount);
		buffer.writeShort(_activeStep);
		buffer.writeByte(_success);
		buffer.writeShort(_dailyStep);
		buffer.writeShort(_dailyCount);
	}
}
