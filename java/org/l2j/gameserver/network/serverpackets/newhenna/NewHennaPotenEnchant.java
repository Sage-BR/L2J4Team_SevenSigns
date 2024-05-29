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
package org.l2j.gameserver.network.serverpackets.newhenna;

import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

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
	public void write()
	{
		ServerPackets.EX_NEW_HENNA_POTEN_ENCHANT.writeId(this);
		writeByte(_slotId);
		writeShort(_enchantStep);
		writeInt(_enchantExp);
		writeShort(_dailyStep);
		writeShort(_dailyCount);
		writeShort(_activeStep);
		writeByte(_success);
		writeShort(_dailyStep);
		writeShort(_dailyCount);
	}
}
