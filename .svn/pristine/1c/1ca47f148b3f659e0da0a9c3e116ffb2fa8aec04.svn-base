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

import org.l2jmobius.gameserver.enums.AchievementBoxState;
import org.l2jmobius.gameserver.enums.AchievementBoxType;

/**
 * @author Serenitty
 */
public class AchievementBoxHolder
{
	private final int _slotId;
	private AchievementBoxState _boxState;
	private AchievementBoxType _boxType;
	
	public AchievementBoxHolder(int slotId, int boxState, int boxType)
	{
		_slotId = slotId;
		_boxState = AchievementBoxState.values()[boxState];
		_boxType = AchievementBoxType.values()[boxType];
	}
	
	public void setState(AchievementBoxState value)
	{
		_boxState = value;
	}
	
	public AchievementBoxState getState()
	{
		return _boxState;
	}
	
	public AchievementBoxType getType()
	{
		return _boxType;
	}
	
	public void setType(AchievementBoxType value)
	{
		_boxType = value;
	}
	
	public int getSlotId()
	{
		return _slotId;
	}
}
