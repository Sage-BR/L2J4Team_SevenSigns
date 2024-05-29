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

import java.util.Map;
import java.util.Map.Entry;

import org.l2j.gameserver.network.ServerPackets;

/**
 * @author -Wooden-
 * @author UnAfraid, mrTJO
 */
public class PackageToList extends ServerPacket
{
	private final Map<Integer, String> _players;
	
	public PackageToList(Map<Integer, String> chars)
	{
		_players = chars;
	}
	
	@Override
	public void write()
	{
		ServerPackets.PACKAGE_TO_LIST.writeId(this);
		writeInt(_players.size());
		for (Entry<Integer, String> entry : _players.entrySet())
		{
			writeInt(entry.getKey());
			writeString(entry.getValue());
		}
	}
}
