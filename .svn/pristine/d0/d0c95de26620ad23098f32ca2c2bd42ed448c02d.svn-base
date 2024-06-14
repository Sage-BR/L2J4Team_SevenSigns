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

import org.l2jmobius.gameserver.model.StatSet;

/**
 * @author Serenitty
 */
public class EnchantStarHolder
{
	private final int _level;
	private final int _expMax;
	private final int _expOnFail;
	private final long _feeAdena;
	
	public EnchantStarHolder(StatSet set)
	{
		_level = set.getInt("level");
		_expMax = set.getInt("expMax");
		_expOnFail = set.getInt("expOnFail");
		_feeAdena = set.getLong("feeAdena");
	}
	
	public int getLevel()
	{
		return _level;
	}
	
	public int getExpMax()
	{
		return _expMax;
	}
	
	public int getExpOnFail()
	{
		return _expOnFail;
	}
	
	public long getFeeAdena()
	{
		return _feeAdena;
	}
}
