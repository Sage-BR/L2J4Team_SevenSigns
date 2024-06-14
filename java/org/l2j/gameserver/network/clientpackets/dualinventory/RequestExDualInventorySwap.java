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
package org.l2j.gameserver.network.clientpackets.dualinventory;

import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.clientpackets.ClientPacket;

/**
 * @author Mobius
 */
public class RequestExDualInventorySwap extends ClientPacket
{
	private int _slot;
	
	@Override
	protected void readImpl()
	{
		_slot = readByte() == 0 ? 0 : 1; // 0 = A, 1 = B
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		player.setDualInventorySlot(_slot);
	}
}
