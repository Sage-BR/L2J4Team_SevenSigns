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
public class NewHennaPotenSelect extends ServerPacket
{
	private final int _slotId;
	private final int _potenId;
	private final int _activeStep;
	private final boolean _success;
	
	public NewHennaPotenSelect(int slotId, int potenId, int activeStep, boolean success)
	{
		_slotId = slotId;
		_potenId = potenId;
		_activeStep = activeStep;
		_success = success;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_NEW_HENNA_POTEN_SELECT.writeId(this, buffer);
		buffer.writeByte(_slotId);
		buffer.writeInt(_potenId);
		buffer.writeShort(_activeStep);
		buffer.writeByte(_success);
	}
}
