/*
 * This file is part of the L2J 4Team project.
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
package org.l2j.gameserver.network.serverpackets;

import org.l2j.gameserver.enums.ClanHallGrade;
import org.l2j.gameserver.model.residences.AbstractResidence;
import org.l2j.gameserver.model.residences.ResidenceFunction;
import org.l2j.gameserver.model.residences.ResidenceFunctionType;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author Steuf
 */
public class AgitDecoInfo extends ServerPacket
{
	private final AbstractResidence _residense;
	
	public AgitDecoInfo(AbstractResidence residense)
	{
		_residense = residense;
	}
	
	@Override
	public void write()
	{
		ServerPackets.AGIT_DECO_INFO.writeId(this);
		writeInt(_residense.getResidenceId());
		// Fireplace
		ResidenceFunction function = _residense.getFunction(ResidenceFunctionType.HP_REGEN);
		if ((function == null) || (function.getLevel() == 0))
		{
			writeByte(0);
		}
		else if (((_residense.getGrade() == ClanHallGrade.GRADE_NONE) && (function.getLevel() < 2)) || ((_residense.getGrade() == ClanHallGrade.GRADE_D) && (function.getLevel() < 3)) || ((_residense.getGrade() == ClanHallGrade.GRADE_C) && (function.getLevel() < 4)) || ((_residense.getGrade() == ClanHallGrade.GRADE_B) && (function.getLevel() < 5)))
		{
			writeByte(1);
		}
		else
		{
			writeByte(2);
		}
		// Carpet - Statue
		function = _residense.getFunction(ResidenceFunctionType.MP_REGEN);
		if ((function == null) || (function.getLevel() == 0))
		{
			writeByte(0);
			writeByte(0);
		}
		else if ((((_residense.getGrade() == ClanHallGrade.GRADE_NONE) || (_residense.getGrade() == ClanHallGrade.GRADE_D)) && (function.getLevel() < 2)) || ((_residense.getGrade() == ClanHallGrade.GRADE_C) && (function.getLevel() < 3)) || ((_residense.getGrade() == ClanHallGrade.GRADE_B) && (function.getLevel() < 4)))
		{
			writeByte(1);
			writeByte(1);
		}
		else
		{
			writeByte(2);
			writeByte(2);
		}
		// Chandelier
		function = _residense.getFunction(ResidenceFunctionType.EXP_RESTORE);
		if ((function == null) || (function.getLevel() == 0))
		{
			writeByte(0);
		}
		else if (function.getLevel() < 2)
		{
			writeByte(1);
		}
		else
		{
			writeByte(2);
		}
		// Mirror
		function = _residense.getFunction(ResidenceFunctionType.TELEPORT);
		if ((function == null) || (function.getLevel() == 0))
		{
			writeByte(0);
		}
		else if (function.getLevel() < 2)
		{
			writeByte(1);
		}
		else
		{
			writeByte(2);
		}
		// Crystal
		writeByte(0);
		// Curtain
		function = _residense.getFunction(ResidenceFunctionType.CURTAIN);
		if ((function == null) || (function.getLevel() == 0))
		{
			writeByte(0);
		}
		else if (function.getLevel() < 2)
		{
			writeByte(1);
		}
		else
		{
			writeByte(2);
		}
		// Magic Curtain
		function = _residense.getFunction(ResidenceFunctionType.ITEM);
		if ((function == null) || (function.getLevel() == 0))
		{
			writeByte(0);
		}
		else if (((_residense.getGrade() == ClanHallGrade.GRADE_NONE) && (function.getLevel() < 2)) || (function.getLevel() < 3))
		{
			writeByte(1);
		}
		else
		{
			writeByte(2);
		}
		// Support
		function = _residense.getFunction(ResidenceFunctionType.BUFF);
		if ((function == null) || (function.getLevel() == 0))
		{
			writeByte(0);
		}
		else if (((_residense.getGrade() == ClanHallGrade.GRADE_NONE) && (function.getLevel() < 2)) || ((_residense.getGrade() == ClanHallGrade.GRADE_D) && (function.getLevel() < 4)) || ((_residense.getGrade() == ClanHallGrade.GRADE_C) && (function.getLevel() < 5)) || ((_residense.getGrade() == ClanHallGrade.GRADE_B) && (function.getLevel() < 8)))
		{
			writeByte(1);
		}
		else
		{
			writeByte(2);
		}
		// Flag
		function = _residense.getFunction(ResidenceFunctionType.OUTERFLAG);
		if ((function == null) || (function.getLevel() == 0))
		{
			writeByte(0);
		}
		else if (function.getLevel() < 2)
		{
			writeByte(1);
		}
		else
		{
			writeByte(2);
		}
		// Front platform
		function = _residense.getFunction(ResidenceFunctionType.PLATFORM);
		if ((function == null) || (function.getLevel() == 0))
		{
			writeByte(0);
		}
		else if (function.getLevel() < 2)
		{
			writeByte(1);
		}
		else
		{
			writeByte(2);
		}
		// Item create?
		function = _residense.getFunction(ResidenceFunctionType.ITEM);
		if ((function == null) || (function.getLevel() == 0))
		{
			writeByte(0);
		}
		else if (((_residense.getGrade() == ClanHallGrade.GRADE_NONE) && (function.getLevel() < 2)) || (function.getLevel() < 3))
		{
			writeByte(1);
		}
		else
		{
			writeByte(2);
		}
	}
}