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
package org.l2j.gameserver.model;

/**
 * @author UnAfraid
 */
public class ActionDataHolder
{
	private final int _id;
	private final String _handler;
	private final int _optionId;
	
	public ActionDataHolder(StatSet set)
	{
		_id = set.getInt("id");
		_handler = set.getString("handler");
		_optionId = set.getInt("option", 0);
	}
	
	public int getId()
	{
		return _id;
	}
	
	public String getHandler()
	{
		return _handler;
	}
	
	public int getOptionId()
	{
		return _optionId;
	}
}
