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

import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.network.ServerPackets;

public class GMViewSkillInfo extends ServerPacket
{
	private final Player _player;
	private final Collection<Skill> _skills;
	
	public GMViewSkillInfo(Player player)
	{
		_player = player;
		_skills = _player.getSkillList();
	}
	
	@Override
	public void write()
	{
		ServerPackets.GM_VIEW_SKILL_INFO.writeId(this);
		writeString(_player.getName());
		writeInt(_skills.size());
		final boolean isDisabled = (_player.getClan() != null) && (_player.getClan().getReputationScore() < 0);
		for (Skill skill : _skills)
		{
			writeInt(skill.isPassive());
			writeShort(skill.getDisplayLevel());
			writeShort(skill.getSubLevel());
			writeInt(skill.getDisplayId());
			writeInt(0);
			writeByte(isDisabled && skill.isClanSkill());
			writeByte(skill.isEnchantable());
		}
	}
}