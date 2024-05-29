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

import java.util.ArrayList;
import java.util.List;

import org.l2j.gameserver.data.xml.SkillData;
import org.l2j.gameserver.data.xml.SkillTreeData;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.skill.CommonSkill;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author UnAfraid
 */
public class ExAlchemySkillList extends ServerPacket
{
	private final List<Skill> _skills = new ArrayList<>();
	
	public ExAlchemySkillList(Player player)
	{
		for (Skill s : player.getAllSkills())
		{
			if (SkillTreeData.getInstance().isAlchemySkill(s.getId(), s.getLevel()))
			{
				_skills.add(s);
			}
		}
		_skills.add(SkillData.getInstance().getSkill(CommonSkill.ALCHEMY_CUBE.getId(), 1));
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_ALCHEMY_SKILL_LIST.writeId(this);
		writeInt(_skills.size());
		for (Skill skill : _skills)
		{
			writeInt(skill.getId());
			writeInt(skill.getLevel());
			writeLong(0); // Always 0 on Naia, SP I guess?
			writeByte(skill.getId() != CommonSkill.ALCHEMY_CUBE.getId()); // This is type in flash, visible or not
		}
	}
}
