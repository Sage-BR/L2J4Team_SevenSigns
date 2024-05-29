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

import org.l2j.gameserver.instancemanager.GraciaSeedsManager;
import org.l2j.gameserver.network.ServerPackets;

public class ExShowSeedMapInfo extends ServerPacket
{
	public static final ExShowSeedMapInfo STATIC_PACKET = new ExShowSeedMapInfo();
	
	private ExShowSeedMapInfo()
	{
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_SHOW_SEED_MAP_INFO.writeId(this);
		writeInt(2); // seed count
		// Seed of Destruction
		writeInt(1); // id 1? Grand Crusade
		writeInt(2770 + GraciaSeedsManager.getInstance().getSoDState()); // sys msg id
		// Seed of Infinity
		writeInt(2); // id 2? Grand Crusade
		// Manager not implemented yet
		writeInt(2766); // sys msg id
	}
}
