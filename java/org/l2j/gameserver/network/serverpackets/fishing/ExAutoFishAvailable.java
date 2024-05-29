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
package org.l2j.gameserver.network.serverpackets.fishing;

import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author bit
 */
public class ExAutoFishAvailable extends ServerPacket
{
	public static final ExAutoFishAvailable YES = new ExAutoFishAvailable(true);
	public static final ExAutoFishAvailable NO = new ExAutoFishAvailable(false);
	
	private final boolean _available;
	
	private ExAutoFishAvailable(boolean available)
	{
		_available = available;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_AUTO_FISH_AVAILABLE.writeId(this);
		writeByte(_available);
	}
}
