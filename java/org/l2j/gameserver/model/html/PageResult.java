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
package org.l2j.gameserver.model.html;

/**
 * @author UnAfraid
 */
public class PageResult
{
	private final int _pages;
	private final StringBuilder _pagerTemplate;
	private final StringBuilder _bodyTemplate;
	
	public PageResult(int pages, StringBuilder pagerTemplate, StringBuilder bodyTemplate)
	{
		_pages = pages;
		_pagerTemplate = pagerTemplate;
		_bodyTemplate = bodyTemplate;
	}
	
	public int getPages()
	{
		return _pages;
	}
	
	public StringBuilder getPagerTemplate()
	{
		return _pagerTemplate;
	}
	
	public StringBuilder getBodyTemplate()
	{
		return _bodyTemplate;
	}
}