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

/**
 * @author Berezkin Nikolay, Serenitty
 */
public class PurgePlayerHolder
{
	private final int _points;
	private final int _keys;
	private int _remainingKeys;
	
	public PurgePlayerHolder(int points, int keys, int remainingKeys)
	{
		_points = points;
		_keys = keys;
		_remainingKeys = remainingKeys;
	}
	
	public int getPoints()
	{
		if (_remainingKeys == 0)
		{
			return 0;
		}
		return _points;
	}
	
	public int getKeys()
	{
		return _keys;
	}
	
	public int getRemainingKeys()
	{
		if ((_keys == 0) && (_remainingKeys == 0))
		{
			_remainingKeys = 40;
		}
		return _remainingKeys;
	}
}
