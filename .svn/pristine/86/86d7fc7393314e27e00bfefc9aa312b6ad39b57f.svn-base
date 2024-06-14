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
package org.l2jmobius.gameserver.model.holders;

import org.l2jmobius.gameserver.model.Location;

/**
 * @author NasSeKa
 */
public class SharedTeleportHolder
{
	private final int _id;
	private final String _name;
	private int _count;
	private final Location _location;
	
	public SharedTeleportHolder(int id, String name, int count, int x, int y, int z)
	{
		_id = id;
		_name = name;
		_count = count;
		_location = new Location(x, y, z);
	}
	
	public int getId()
	{
		return _id;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public int getCount()
	{
		return Math.max(0, _count);
	}
	
	public void decrementCount()
	{
		_count -= 1;
	}
	
	public Location getLocation()
	{
		return _location;
	}
}