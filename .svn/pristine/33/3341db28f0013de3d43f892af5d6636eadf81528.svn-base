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
package org.l2jmobius.gameserver.enums;

/**
 * @author Index
 */
public enum WorldExchangeItemSubType
{
	WEAPON(0),
	ARMOR(1),
	ACCESSORY(2),
	ETC(3),
	ARTIFACT_B1(4),
	ARTIFACT_C1(5),
	ARTIFACT_D1(6),
	ARTIFACT_A1(7),
	ENCHANT_SCROLL(8),
	BLESS_ENCHANT_SCROLL(9),
	MULTI_ENCHANT_SCROLL(10),
	ANCIENT_ENCHANT_SCROLL(11),
	SPIRITSHOT(12),
	SOULSHOT(13),
	BUFF(14),
	VARIATION_STONE(15),
	DYE(16),
	SOUL_CRYSTAL(17),
	SKILLBOOK(18),
	ETC_ENCHANT(19),
	POTION_AND_ETC_SCROLL(20),
	TICKET(21),
	CRAFT(22),
	INC_ENCHANT_PROP(23),
	ADENA(24),
	ETC_SUB_TYPE(25);
	
	private final int _id;
	
	private WorldExchangeItemSubType(int id)
	{
		_id = id;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public static WorldExchangeItemSubType getWorldExchangeItemSubType(int id)
	{
		for (WorldExchangeItemSubType type : values())
		{
			if (type.getId() == id)
			{
				return type;
			}
		}
		return null;
	}
}
