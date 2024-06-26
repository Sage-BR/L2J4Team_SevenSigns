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
package org.l2j.gameserver.model.beautyshop;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sdw
 */
public class BeautyData
{
	private final Map<Integer, BeautyItem> _hairList = new HashMap<>();
	private final Map<Integer, BeautyItem> _faceList = new HashMap<>();
	
	public void addHair(BeautyItem hair)
	{
		_hairList.put(hair.getId(), hair);
	}
	
	public void addFace(BeautyItem face)
	{
		_faceList.put(face.getId(), face);
	}
	
	public Map<Integer, BeautyItem> getHairList()
	{
		return _hairList;
	}
	
	public Map<Integer, BeautyItem> getFaceList()
	{
		return _faceList;
	}
}
