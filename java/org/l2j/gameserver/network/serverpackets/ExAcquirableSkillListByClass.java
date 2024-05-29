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

import java.util.Collection;

import org.l2j.gameserver.enums.AcquireSkillType;
import org.l2j.gameserver.model.SkillLearn;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author UnAfraid
 */
public class ExAcquirableSkillListByClass extends ServerPacket
{
	private final Collection<SkillLearn> _learnable;
	private final AcquireSkillType _type;
	
	public ExAcquirableSkillListByClass(Collection<SkillLearn> learnable, AcquireSkillType type)
	{
		_learnable = learnable;
		_type = type;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_ACQUIRABLE_SKILL_LIST_BY_CLASS.writeId(this);
		writeShort(_type.getId());
		writeShort(_learnable.size());
		for (SkillLearn skill : _learnable)
		{
			writeInt(skill.getSkillId());
			writeShort(skill.getSkillLevel());
			writeShort(skill.getSkillLevel());
			writeByte(skill.getGetLevel());
			writeLong(skill.getLevelUpSp());
			writeByte(skill.getRequiredItems().size());
			if (_type == AcquireSkillType.SUBPLEDGE)
			{
				writeShort(0);
			}
		}
	}
}
