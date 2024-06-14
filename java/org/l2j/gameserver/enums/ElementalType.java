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
package org.l2j.gameserver.enums;

import org.l2j.gameserver.model.stats.Stat;

/**
 * @author JoeAlisson, Mobius
 */
public enum ElementalType
{
	NONE,
	FIRE,
	WATER,
	WIND,
	EARTH;
	
	public byte getId()
	{
		return (byte) (ordinal());
	}
	
	public static ElementalType of(byte elementId)
	{
		return values()[elementId];
	}
	
	public boolean isSuperior(ElementalType targetType)
	{
		return this == superior(targetType);
	}
	
	public boolean isInferior(ElementalType targetType)
	{
		return targetType == superior(this);
	}
	
	public ElementalType getSuperior()
	{
		return superior(this);
	}
	
	public static ElementalType superior(ElementalType elementalType)
	{
		switch (elementalType)
		{
			case FIRE:
			{
				return WATER;
			}
			case WATER:
			{
				return WIND;
			}
			case WIND:
			{
				return EARTH;
			}
			case EARTH:
			{
				return FIRE;
			}
			default:
			{
				return NONE;
			}
		}
	}
	
	public Stat getAttackStat()
	{
		switch (this)
		{
			case EARTH:
			{
				return Stat.ELEMENTAL_SPIRIT_EARTH_ATTACK;
			}
			case WIND:
			{
				return Stat.ELEMENTAL_SPIRIT_WIND_ATTACK;
			}
			case FIRE:
			{
				return Stat.ELEMENTAL_SPIRIT_FIRE_ATTACK;
			}
			case WATER:
			{
				return Stat.ELEMENTAL_SPIRIT_WATER_ATTACK;
			}
			default:
			{
				return null;
			}
		}
	}
	
	public Stat getDefenseStat()
	{
		switch (this)
		{
			case EARTH:
			{
				return Stat.ELEMENTAL_SPIRIT_EARTH_DEFENSE;
			}
			case WIND:
			{
				return Stat.ELEMENTAL_SPIRIT_WIND_DEFENSE;
			}
			case FIRE:
			{
				return Stat.ELEMENTAL_SPIRIT_FIRE_DEFENSE;
			}
			case WATER:
			{
				return Stat.ELEMENTAL_SPIRIT_WATER_DEFENSE;
			}
			default:
			{
				return null;
			}
		}
	}
	
	public String getName()
	{
		switch (this)
		{
			case FIRE:
			{
				return "Fire";
			}
			case WATER:
			{
				return "Water";
			}
			case WIND:
			{
				return "Wind";
			}
			case EARTH:
			{
				return "Earth";
			}
			default:
			{
				return "None";
			}
		}
	}
}
