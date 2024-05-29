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
package org.l2j.gameserver.network.serverpackets;

import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.taskmanager.GameTimeTaskManager;

public class ClientSetTime extends ServerPacket
{
	public static final ClientSetTime STATIC_PACKET = new ClientSetTime();
	
	private ClientSetTime()
	{
	}
	
	@Override
	public void write()
	{
		ServerPackets.CLIENT_SET_TIME.writeId(this);
		writeInt(GameTimeTaskManager.getInstance().getGameTime()); // Time in client minutes.
		writeInt(GameTimeTaskManager.IG_DAYS_PER_DAY); // Constant to match the server time. This determines the speed of the client clock.
	}
}