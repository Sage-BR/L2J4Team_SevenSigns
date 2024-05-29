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

import java.util.LinkedList;
import java.util.List;

import org.l2j.gameserver.enums.SkillEnchantType;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.network.ServerPackets;

public class ExEnchantSkillList extends ServerPacket
{
	private final SkillEnchantType _type;
	private final List<Skill> _skills = new LinkedList<>();
	
	public ExEnchantSkillList(SkillEnchantType type)
	{
		_type = type;
	}
	
	public void addSkill(Skill skill)
	{
		_skills.add(skill);
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_ENCHANT_SKILL_LIST.writeId(this);
		writeInt(_type.ordinal());
		writeInt(_skills.size());
		for (Skill skill : _skills)
		{
			writeInt(skill.getId());
			writeShort(skill.getLevel());
			writeShort(skill.getSubLevel());
		}
	}
}