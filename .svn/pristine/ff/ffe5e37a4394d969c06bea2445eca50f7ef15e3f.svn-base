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

import java.util.function.Function;

import org.l2jmobius.gameserver.model.ArmorSet;
import org.l2jmobius.gameserver.model.actor.Playable;
import org.l2jmobius.gameserver.model.item.instance.Item;

/**
 * @author UnAfraid
 */
public class ArmorsetSkillHolder extends SkillHolder
{
	private final int _minimumPieces;
	private final int _minEnchant;
	private final int _artifactSlotMask;
	private final int _artifactBookSlot;
	private final boolean _isOptional;
	
	public ArmorsetSkillHolder(int skillId, int skillLevel, int minimumPieces, int minEnchant, boolean isOptional, int artifactSlotMask, int artifactBookSlot)
	{
		super(skillId, skillLevel);
		_minimumPieces = minimumPieces;
		_minEnchant = minEnchant;
		_isOptional = isOptional;
		_artifactSlotMask = artifactSlotMask;
		_artifactBookSlot = artifactBookSlot;
	}
	
	public int getMinimumPieces()
	{
		return _minimumPieces;
	}
	
	public int getMinEnchant()
	{
		return _minEnchant;
	}
	
	public boolean isOptional()
	{
		return _isOptional;
	}
	
	public boolean validateConditions(Playable playable, ArmorSet armorSet, Function<Item, Integer> idProvider)
	{
		// Playable doesn't have full busy (1 of 3) artifact real slot
		if (_artifactSlotMask > armorSet.getArtifactSlotMask(playable, _artifactBookSlot))
		{
			return false;
		}
		
		// Playable doesn't have enough items equipped to use this skill
		if (_minimumPieces > armorSet.getPieceCount(playable, idProvider))
		{
			return false;
		}
		
		// Playable set enchantment isn't enough to use this skill
		if (_minEnchant > armorSet.getSetEnchant(playable))
		{
			return false;
		}
		
		// Playable doesn't have the required item to use this skill
		if (_isOptional && !armorSet.hasOptionalEquipped(playable, idProvider))
		{
			return false;
		}
		
		// Playable already knows that skill
		if (playable.getSkillLevel(getSkillId()) == getSkillLevel())
		{
			return false;
		}
		
		return true;
	}
}
