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
package org.l2j.gameserver.network.serverpackets.olympiad;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.enums.OlympiadMode;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author godson
 */
public class ExOlympiadMode extends ServerPacket
{
	private final OlympiadMode _mode;
	
	/**
	 * @param mode (0 = return, 3 = spectate)
	 */
	public ExOlympiadMode(OlympiadMode mode)
	{
		_mode = mode;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_OLYMPIAD_MODE.writeId(this, buffer);
		buffer.writeByte(_mode.ordinal());
	}
}

