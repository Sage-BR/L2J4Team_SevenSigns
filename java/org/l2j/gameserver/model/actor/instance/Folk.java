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
package org.l2j.gameserver.model.actor.instance;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.l2j.gameserver.data.xml.SkillTreeData;
import org.l2j.gameserver.enums.AcquireSkillType;
import org.l2j.gameserver.enums.ClassId;
import org.l2j.gameserver.enums.InstanceType;
import org.l2j.gameserver.model.SkillLearn;
import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.status.FolkStatus;
import org.l2j.gameserver.model.actor.templates.NpcTemplate;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.serverpackets.ExAcquirableSkillListByClass;
import org.l2j.gameserver.network.serverpackets.SystemMessage;

public class Folk extends Npc
{
	public Folk(NpcTemplate template)
	{
		super(template);
		setInstanceType(InstanceType.Folk);
		setInvul(false);
	}
	
	@Override
	public FolkStatus getStatus()
	{
		return (FolkStatus) super.getStatus();
	}
	
	@Override
	public void initCharStatus()
	{
		setStatus(new FolkStatus(this));
	}
	
	/**
	 * Displays Skill Tree for a given player, npc and class Id.
	 * @param player the active character.
	 * @param npc the last folk.
	 * @param classId player's active class id.
	 */
	public static void showSkillList(Player player, Npc npc, ClassId classId)
	{
		final int npcId = npc.getTemplate().getId();
		if (npcId == 32611) // Tolonis (Officer)
		{
			final List<SkillLearn> skills = SkillTreeData.getInstance().getAvailableCollectSkills(player);
			if (skills.isEmpty()) // No more skills to learn, come back when you level.
			{
				final int minLevel = SkillTreeData.getInstance().getMinLevelForNewSkill(player, SkillTreeData.getInstance().getCollectSkillTree());
				if (minLevel > 0)
				{
					final SystemMessage sm = new SystemMessage(SystemMessageId.YOU_DO_NOT_HAVE_ANY_FURTHER_SKILLS_TO_LEARN_COME_BACK_WHEN_YOU_HAVE_REACHED_LEVEL_S1);
					sm.addInt(minLevel);
					player.sendPacket(sm);
				}
				else
				{
					player.sendPacket(SystemMessageId.THERE_ARE_NO_OTHER_SKILLS_TO_LEARN);
				}
			}
			else
			{
				player.sendPacket(new ExAcquirableSkillListByClass(skills, AcquireSkillType.COLLECT));
			}
			return;
		}
		
		// Normal skills, No LearnedByFS, no AutoGet skills.
		final Collection<SkillLearn> skills = SkillTreeData.getInstance().getAvailableSkills(player, classId, false, false);
		if (skills.isEmpty())
		{
			final Map<Long, SkillLearn> skillTree = SkillTreeData.getInstance().getCompleteClassSkillTree(classId);
			final int minLevel = SkillTreeData.getInstance().getMinLevelForNewSkill(player, skillTree);
			if (minLevel > 0)
			{
				final SystemMessage sm = new SystemMessage(SystemMessageId.YOU_DO_NOT_HAVE_ANY_FURTHER_SKILLS_TO_LEARN_COME_BACK_WHEN_YOU_HAVE_REACHED_LEVEL_S1);
				sm.addInt(minLevel);
				player.sendPacket(sm);
			}
			else if (player.getClassId().level() == 1)
			{
				final SystemMessage sm = new SystemMessage(SystemMessageId.THERE_ARE_NO_OTHER_SKILLS_TO_LEARN_PLEASE_COME_BACK_AFTER_S1ND_CLASS_CHANGE);
				sm.addInt(2);
				player.sendPacket(sm);
			}
			else
			{
				player.sendPacket(SystemMessageId.THERE_ARE_NO_OTHER_SKILLS_TO_LEARN);
			}
		}
		else
		{
			player.sendPacket(new ExAcquirableSkillListByClass(skills, AcquireSkillType.CLASS));
		}
	}
}
