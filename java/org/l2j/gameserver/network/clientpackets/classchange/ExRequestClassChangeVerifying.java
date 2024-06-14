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
package org.l2j.gameserver.network.clientpackets.classchange;

import org.l2j.gameserver.enums.CategoryType;
import org.l2j.gameserver.enums.ClassId;
import org.l2j.gameserver.instancemanager.QuestManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.quest.Quest;
import org.l2j.gameserver.model.quest.QuestState;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.classchange.ExClassChangeSetAlarm;

/**
 * @author Mobius
 */
public class ExRequestClassChangeVerifying extends ClientPacket
{
	private int _classId;
	
	@Override
	protected void readImpl()
	{
		_classId = readInt();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (_classId != player.getClassId().getId())
		{
			return;
		}
		
		if (player.isInCategory(CategoryType.FOURTH_CLASS_GROUP))
		{
			return;
		}
		
		if (player.isInCategory(CategoryType.THIRD_CLASS_GROUP))
		{
			if (!thirdClassCheck(player))
			{
				return;
			}
		}
		else if (player.isInCategory(CategoryType.SECOND_CLASS_GROUP))
		{
			if (!secondClassCheck(player))
			{
				return;
			}
		}
		else if (player.isInCategory(CategoryType.FIRST_CLASS_GROUP))
		{
			if (!firstClassCheck(player))
			{
				return;
			}
		}
		
		player.sendPacket(ExClassChangeSetAlarm.STATIC_PACKET);
	}
	
	private boolean firstClassCheck(Player player)
	{
		QuestState qs = null;
		if (player.isDeathKnight())
		{
			final Quest quest = QuestManager.getInstance().getQuest(10101);
			qs = player.getQuestState(quest.getName());
		}
		else if (player.isAssassin())
		{
			final Quest quest = QuestManager.getInstance().getQuest(10123);
			qs = player.getQuestState(quest.getName());
		}
		else
		{
			switch (player.getRace())
			{
				case HUMAN:
				{
					if (player.getClassId() == ClassId.FIGHTER)
					{
						final Quest quest = QuestManager.getInstance().getQuest(10009);
						qs = player.getQuestState(quest.getName());
					}
					else
					{
						final Quest quest = QuestManager.getInstance().getQuest(10020);
						qs = player.getQuestState(quest.getName());
					}
					break;
				}
				case ELF:
				{
					final Quest quest = QuestManager.getInstance().getQuest(10033);
					qs = player.getQuestState(quest.getName());
					break;
				}
				case DARK_ELF:
				{
					final Quest quest = QuestManager.getInstance().getQuest(10046);
					qs = player.getQuestState(quest.getName());
					break;
				}
				case ORC:
				{
					final Quest quest = QuestManager.getInstance().getQuest(10057);
					qs = player.getQuestState(quest.getName());
					break;
				}
				case DWARF:
				{
					final Quest quest = QuestManager.getInstance().getQuest(10079);
					qs = player.getQuestState(quest.getName());
					break;
				}
				case KAMAEL:
				{
					final Quest quest = QuestManager.getInstance().getQuest(10090);
					qs = player.getQuestState(quest.getName());
					break;
				}
				case SYLPH:
				{
					final Quest quest = QuestManager.getInstance().getQuest(10112);
					qs = player.getQuestState(quest.getName());
					break;
				}
			}
		}
		return (qs != null) && qs.isCompleted();
	}
	
	private boolean secondClassCheck(Player player)
	{
		// SecondClassChange.java has only level check.
		return player.getLevel() >= 40;
	}
	
	private boolean thirdClassCheck(Player player)
	{
		final Quest quest = QuestManager.getInstance().getQuest(19900);
		final QuestState qs = player.getQuestState(quest.getName());
		return (qs != null) && qs.isCompleted();
	}
}