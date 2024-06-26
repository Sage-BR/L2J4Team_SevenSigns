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
package org.l2j.gameserver.model.item.henna;

import org.l2j.gameserver.data.xml.HennaPatternPotentialData;

/**
 * @author Serenitty
 */
public class HennaPoten
{
	private Henna _henna;
	private int _potenId;
	private int _enchantLevel = 1;
	private int _enchantExp;
	private int _slotPosition;
	
	public HennaPoten()
	{
	}
	
	public void setHenna(Henna henna)
	{
		_henna = henna;
	}
	
	public Henna getHenna()
	{
		return _henna;
	}
	
	public void setPotenId(int val)
	{
		_potenId = val;
	}
	
	public int getSlotPosition()
	{
		return _slotPosition;
	}
	
	public void setSlotPosition(int val)
	{
		_slotPosition = val;
	}
	
	public int getPotenId()
	{
		return _potenId;
	}
	
	public void setEnchantLevel(int val)
	{
		_enchantLevel = val;
	}
	
	public int getEnchantLevel()
	{
		return _enchantLevel;
	}
	
	public void setEnchantExp(int val)
	{
		_enchantExp = val;
	}
	
	public int getEnchantExp()
	{
		if (_enchantExp > HennaPatternPotentialData.getInstance().getMaxPotenExp())
		{
			_enchantExp = HennaPatternPotentialData.getInstance().getMaxPotenExp();
			return _enchantExp;
		}
		return _enchantExp;
	}
	
	public boolean isPotentialAvailable()
	{
		return (_henna != null) && (_enchantLevel > 1);
	}
	
	public int getActiveStep()
	{
		if (!isPotentialAvailable())
		{
			return 0;
		}
		
		if (_enchantExp == HennaPatternPotentialData.getInstance().getMaxPotenExp())
		{
			return Math.min(_enchantLevel, _henna.getPatternLevel());
		}
		
		return Math.min(_enchantLevel - 1, _henna.getPatternLevel());
	}
}