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
package org.l2j.gameserver.network.serverpackets.dualinventory;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Mobius
 */
public class ExDualInventorySwap extends ServerPacket
{
	private final int _slot;
	
	public ExDualInventorySwap(int slot)
	{
		_slot = slot;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_DUAL_INVENTORY_INFO.writeId(this, buffer);
		buffer.writeByte(_slot);
		buffer.writeByte(1); // Success.
		buffer.writeByte(1); // Stable swapping.
		
		// List SlotDBID.
		buffer.writeByte(0);
		buffer.writeInt(0); // Old object id?
		buffer.writeInt(0); // New object id?
		
		// List HennaPotenID.
		buffer.writeByte(0);
		buffer.writeInt(0);
		buffer.writeInt(0);
		buffer.writeByte(0);
		buffer.writeByte(0);
	}
}
