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
 * @author Geremy
 */
public class PetExtractionHolder
{
	private final int _petId;
	private final int _petLevel;
	private final long _extractExp;
	private final int _extractItem;
	private final ItemHolder _defaultCost;
	private final ItemHolder _extractCost;
	
	public PetExtractionHolder(int petId, int petLevel, long extractExp, int extractItem, ItemHolder defaultCost, ItemHolder extractCost)
	{
		_petId = petId;
		_petLevel = petLevel;
		_extractExp = extractExp;
		_extractItem = extractItem;
		_defaultCost = defaultCost;
		_extractCost = extractCost;
	}
	
	public int getPetId()
	{
		return _petId;
	}
	
	public int getPetLevel()
	{
		return _petLevel;
	}
	
	public long getExtractExp()
	{
		return _extractExp;
	}
	
	public int getExtractItem()
	{
		return _extractItem;
	}
	
	public ItemHolder getDefaultCost()
	{
		return _defaultCost;
	}
	
	public ItemHolder getExtractCost()
	{
		return _extractCost;
	}
}
