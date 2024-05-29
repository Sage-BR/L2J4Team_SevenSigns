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
import java.util.List;

import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author -Wooden-
 */
public class PledgeSkillList extends ServerPacket
{
	private final Collection<Skill> _skills;
	private final List<SubPledgeSkill> _subSkills;
	
	public static class SubPledgeSkill
	{
		int _subType;
		int _skillId;
		int _skillLevel;
		
		public SubPledgeSkill(int subType, int skillId, int skillLevel)
		{
			_subType = subType;
			_skillId = skillId;
			_skillLevel = skillLevel;
		}
	}
	
	public PledgeSkillList(Clan clan)
	{
		_skills = clan.getAllSkills();
		_subSkills = clan.getAllSubSkills();
	}
	
	@Override
	public void write()
	{
		ServerPackets.PLEDGE_SKILL_LIST.writeId(this);
		writeInt(_skills.size());
		writeInt(_subSkills.size()); // Squad skill length
		for (Skill sk : _skills)
		{
			writeInt(sk.getDisplayId());
			writeShort(sk.getDisplayLevel());
			writeShort(0); // Sub level
		}
		for (SubPledgeSkill sk : _subSkills)
		{
			writeInt(sk._subType); // Clan Sub-unit types
			writeInt(sk._skillId);
			writeShort(sk._skillLevel);
			writeShort(0); // Sub level
		}
	}
}
