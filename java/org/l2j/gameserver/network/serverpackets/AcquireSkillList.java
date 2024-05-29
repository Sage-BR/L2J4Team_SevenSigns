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
import java.util.LinkedList;
import java.util.List;

import org.l2j.gameserver.data.xml.SkillTreeData;
import org.l2j.gameserver.model.SkillLearn;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.ItemHolder;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author Sdw, Mobius
 */
public class AcquireSkillList extends ServerPacket
{
	private Player _player;
	private Collection<SkillLearn> _learnable;
	
	public AcquireSkillList(Player player)
	{
		super(512);
		
		if (!player.isSubclassLocked()) // Changing class.
		{
			_player = player;
			_learnable = SkillTreeData.getInstance().getAvailableSkills(player, player.getClassId(), false, false);
			_learnable.addAll(SkillTreeData.getInstance().getNextAvailableSkills(player, player.getClassId(), false, false));
		}
	}
	
	@Override
	public void write()
	{
		if (_player == null)
		{
			return;
		}
		
		ServerPackets.ACQUIRE_SKILL_LIST.writeId(this);
		writeShort(_learnable.size());
		for (SkillLearn skill : _learnable)
		{
			final int skillId = _player.getReplacementSkill(skill.getSkillId());
			writeInt(skillId);
			
			writeInt(skill.getSkillLevel()); // 414 both Main and Essence writeInt.
			writeLong(skill.getLevelUpSp());
			writeByte(skill.getGetLevel());
			writeByte(0); // Skill dual class level.
			
			writeByte(_player.getKnownSkill(skillId) == null);
			
			writeByte(skill.getRequiredItems().size());
			for (List<ItemHolder> item : skill.getRequiredItems())
			{
				writeInt(item.get(0).getId());
				writeLong(item.get(0).getCount());
			}
			
			final List<Skill> removeSkills = new LinkedList<>();
			for (int id : skill.getRemoveSkills())
			{
				final Skill removeSkill = _player.getKnownSkill(id);
				if (removeSkill != null)
				{
					removeSkills.add(removeSkill);
				}
			}
			
			writeByte(removeSkills.size());
			for (Skill removed : removeSkills)
			{
				writeInt(removed.getId());
				writeInt(removed.getLevel()); // 414 both Main and Essence writeInt.
			}
		}
	}
}
