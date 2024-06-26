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

import org.l2j.gameserver.model.interfaces.IIdentifiable;

public enum ExBrProductReplyType implements IIdentifiable
{
	SUCCESS(1),
	LACK_OF_POINT(-1),
	INVALID_PRODUCT(-2),
	USER_CANCEL(-3),
	INVENTORY_OVERFLOW(-4),
	CLOSED_PRODUCT(-5),
	SERVER_ERROR(-6),
	BEFORE_SALE_DATE(-7),
	AFTER_SALE_DATE(-8),
	INVALID_USER(-9),
	INVALID_ITEM(-10),
	INVALID_USER_STATE(-11),
	NOT_DAY_OF_WEEK(-12),
	NOT_TIME_OF_DAY(-13),
	SOLD_OUT(-14);
	
	private final int _id;
	
	ExBrProductReplyType(int id)
	{
		_id = id;
	}
	
	@Override
	public int getId()
	{
		return _id;
	}
}