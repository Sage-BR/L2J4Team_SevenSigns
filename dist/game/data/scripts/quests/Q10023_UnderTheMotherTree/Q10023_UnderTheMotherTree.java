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
package quests.Q10023_UnderTheMotherTree;

import org.l2j.Config;
import org.l2j.gameserver.data.xml.TeleportListData;
import org.l2j.gameserver.instancemanager.QuestManager;
import org.l2j.gameserver.model.Location;
import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.events.EventType;
import org.l2j.gameserver.model.events.ListenerRegisterType;
import org.l2j.gameserver.model.events.annotations.RegisterEvent;
import org.l2j.gameserver.model.events.annotations.RegisterType;
import org.l2j.gameserver.model.events.impl.creature.player.OnPlayerLogin;
import org.l2j.gameserver.model.quest.Quest;
import org.l2j.gameserver.model.quest.QuestDialogType;
import org.l2j.gameserver.model.quest.QuestState;
import org.l2j.gameserver.model.quest.newquestdata.NewQuestLocation;
import org.l2j.gameserver.model.quest.newquestdata.QuestCondType;
import org.l2j.gameserver.network.serverpackets.PlaySound;
import org.l2j.gameserver.network.serverpackets.quest.ExQuestDialog;

import quests.Q10024_NewLifesLessons.Q10024_NewLifesLessons;
import quests.Q10025_NewLifesLessons.Q10025_NewLifesLessons;

/**
 * @author Magik, Mobius
 */
public class Q10023_UnderTheMotherTree extends Quest
{
	private static final int QUEST_ID = 10023;
	
	public Q10023_UnderTheMotherTree()
	{
		super(QUEST_ID);
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		switch (event)
		{
			case "ACCEPT":
			{
				if (!canStartQuest(player))
				{
					break;
				}
				
				final QuestState questState = getQuestState(player, true);
				if (!questState.isStarted() && !questState.isCompleted())
				{
					questState.startQuest();
				}
				break;
			}
			case "TELEPORT":
			{
				QuestState questState = getQuestState(player, false);
				if (questState == null)
				{
					if (!canStartQuest(player))
					{
						break;
					}
					
					questState = getQuestState(player, true);
					
					final NewQuestLocation questLocation = getQuestData().getLocation();
					if (questLocation.getStartLocationId() > 0)
					{
						final Location location = TeleportListData.getInstance().getTeleport(questLocation.getStartLocationId()).getLocation();
						if (teleportToQuestLocation(player, location))
						{
							questState.setCond(QuestCondType.ACT);
							sendAcceptDialog(player);
						}
					}
					break;
				}
				
				final NewQuestLocation questLocation = getQuestData().getLocation();
				if (questState.isCond(QuestCondType.STARTED))
				{
					if (questLocation.getQuestLocationId() > 0)
					{
						final Location location = TeleportListData.getInstance().getTeleport(questLocation.getQuestLocationId()).getLocation();
						if (teleportToQuestLocation(player, location) && (questLocation.getQuestLocationId() == questLocation.getEndLocationId()))
						{
							questState.setCond(QuestCondType.DONE);
							sendEndDialog(player);
						}
					}
				}
				else if (questState.isCond(QuestCondType.DONE) && !questState.isCompleted())
				{
					if (questLocation.getEndLocationId() > 0)
					{
						final Location location = TeleportListData.getInstance().getTeleport(questLocation.getEndLocationId()).getLocation();
						if (teleportToQuestLocation(player, location))
						{
							sendEndDialog(player);
						}
					}
				}
				break;
			}
			case "COMPLETE":
			{
				final QuestState questState = getQuestState(player, false);
				if (questState == null)
				{
					break;
				}
				
				if (questState.isCond(QuestCondType.DONE) && !questState.isCompleted())
				{
					questState.exitQuest(false, true);
					
					Quest quest = QuestManager.getInstance().getQuest(10024);
					if (quest.canStartQuest(player))
					{
						final QuestState nextQuestState = player.getQuestState(Q10024_NewLifesLessons.class.getSimpleName());
						if (nextQuestState == null)
						{
							player.sendPacket(new ExQuestDialog(10024, QuestDialogType.ACCEPT));
						}
					}
					else
					{
						quest = QuestManager.getInstance().getQuest(10025);
						if (quest.canStartQuest(player))
						{
							final QuestState nextQuestState = player.getQuestState(Q10025_NewLifesLessons.class.getSimpleName());
							if (nextQuestState == null)
							{
								player.sendPacket(new ExQuestDialog(10025, QuestDialogType.ACCEPT));
							}
						}
					}
				}
				break;
			}
		}
		
		return null;
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		final QuestState questState = getQuestState(player, false);
		if ((questState != null) && !questState.isCompleted())
		{
			if (questState.isCond(QuestCondType.STARTED))
			{
				if (questState.isStarted())
				{
					questState.setCount(getQuestData().getGoal().getCount());
					questState.setCond(QuestCondType.DONE);
					player.sendPacket(new ExQuestDialog(questState.getQuest().getId(), QuestDialogType.END));
				}
			}
			else if (questState.isCond(QuestCondType.DONE))
			{
				player.sendPacket(new ExQuestDialog(questState.getQuest().getId(), QuestDialogType.END));
			}
		}
		
		npc.showChatWindow(player);
		return null;
	}
	
	@RegisterEvent(EventType.ON_PLAYER_LOGIN)
	@RegisterType(ListenerRegisterType.GLOBAL_PLAYERS)
	public void onPlayerLogin(OnPlayerLogin event)
	{
		if (Config.DISABLE_TUTORIAL)
		{
			return;
		}
		
		final Player player = event.getPlayer();
		if (!canStartQuest(player))
		{
			return;
		}
		
		final QuestState questState = getQuestState(player, false);
		if ((questState == null) || !questState.isCompleted())
		{
			player.sendPacket(new PlaySound(2, player.isMageClass() ? "tutorial_voice_001d_A" : "tutorial_voice_001c_A", 0, 0, player.getX(), player.getY(), player.getZ()));
		}
	}
}
