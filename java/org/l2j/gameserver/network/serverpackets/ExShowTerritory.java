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

import java.util.ArrayList;
import java.util.List;

import org.l2j.gameserver.model.interfaces.ILocational;
import org.l2j.gameserver.network.ServerPackets;

/**
 * Note: <b>There is known issue with this packet, it cannot be removed unless game client is restarted!</b>
 * @author UnAfraid
 */
public class ExShowTerritory extends ServerPacket
{
	private final int _minZ;
	private final int _maxZ;
	private final List<ILocational> _vertices = new ArrayList<>();
	
	public ExShowTerritory(int minZ, int maxZ)
	{
		_minZ = minZ;
		_maxZ = maxZ;
	}
	
	public void addVertice(ILocational loc)
	{
		_vertices.add(loc);
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_SHOW_TERRITORY.writeId(this);
		writeInt(_vertices.size());
		writeInt(_minZ);
		writeInt(_maxZ);
		for (ILocational loc : _vertices)
		{
			writeInt(loc.getX());
			writeInt(loc.getY());
		}
	}
}
