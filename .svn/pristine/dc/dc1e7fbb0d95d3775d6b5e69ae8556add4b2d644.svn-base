/*
 * This file is part of the L2J Mobius project.
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
package instances.DreamDungeon.BenedictsMonastery;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

import org.l2jmobius.commons.threads.ThreadPool;
import org.l2jmobius.commons.util.Rnd;
import org.l2jmobius.gameserver.instancemanager.InstanceManager;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.events.impl.instance.OnInstanceStatusChange;
import org.l2jmobius.gameserver.model.instancezone.Instance;
import org.l2jmobius.gameserver.network.NpcStringId;
import org.l2jmobius.gameserver.network.serverpackets.ExShowScreenMessage;

import instances.AbstractInstance;
import instances.DreamDungeon.CatGuildsLair.CatGuildsLair;

/**
 * @author Index
 */
public class BenedictsMonastery extends AbstractInstance
{
	/** Instance World IDS */
	public static final int CREATED = 0;
	public static final int TALK_WITH_HOLY_GRAILS_RAIDER_NPC_ON_START = 1;
	public static final int KILL_FOUR_DREAM_WATCHERS_INSIDE = 2;
	public static final int TALK_WITH_HOLY_GRAILS_RAIDER_NPC_TO_OPEN_DOORS = 3;
	public static final int TALK_WITH_HOLY_GRAIL_BEFORE_BOSS = 4;
	public static final int BOSS_FIGHT = 5;
	public static final int FINISH_INSTANCE = 6;
	
	public static final int INSTANCE_ID = 222;
	
	private static final int[] MONSTERS =
	{
		22395, // Guardian Angel of the Holy Grail - Bow
		22396, // Guardian Angel of the Holy Grail - Blunt
		22397, // Guardian Angel of the Holy Grail - Spire
		22398, // Guardian Angel of the Holy Grail - Dagger
	};
	
	private static final int[] MAIN_DOOR_IDS =
	{
		23150003,
		23150004,
	};
	
	private static final int GABRIELLE = 18680;
	private static final int GABRIELLE_MINION = 18681;
	
	private static final String FIRST_STRING_TIMER = "FIRST_STRING_TIMER";
	private static final String DREAM_WATCHER_COUNTER = "DREAM_WATCHER_COUNTER";
	
	private static final NpcStringId DREAM_WATCHER = NpcStringId.DREAM_WATCHER;
	private static final NpcStringId STRING_ID_01 = NpcStringId.HUMAN_FIND_THE_HOLY_GRAIL_IT_HAS_ANSWERS_TO_YOUR_QUESTIONS;
	private static final NpcStringId STRING_ID_02 = NpcStringId.GO_TO_THE_LAST_ROOM_THE_HOLY_GRAIL_IS_INSIDE;
	private static final NpcStringId STRING_ID_03 = NpcStringId.ARE_YOU_BRAVE_ENOUGH_TO_CROSS_THIS_BROKEN_BRIDGE;
	private static final NpcStringId STRING_ID_04 = NpcStringId.HOW_DARE_YOU_HUMAN_GABRIELLE_WILL_KILL_YOU_FOR_THAT;
	private static final NpcStringId STRING_ID_05 = NpcStringId.GABRIELLE_IS_NOT_NEEDED_I_LL_DEAL_WITH_IT_MYSELF;
	private static final NpcStringId STRING_ID_06 = NpcStringId.HEHEHE_GABRIELLE_ISN_T_THIS_STUPID_BUT_WHERE_IS_THE_HOLY_GRAIL;
	
	private BenedictsMonastery()
	{
		super(INSTANCE_ID);
		addKillId(MONSTERS);
		addKillId(GABRIELLE, GABRIELLE_MINION);
		setInstanceStatusChangeId(this::onInstanceStatusChange, INSTANCE_ID);
	}
	
	public void onInstanceStatusChange(OnInstanceStatusChange event)
	{
		final Instance instance = event.getWorld();
		final int status = event.getStatus();
		switch (status)
		{
			case CREATED:
			{
				instance.setStatus(TALK_WITH_HOLY_GRAILS_RAIDER_NPC_ON_START);
				// send screen message if player will not talk with Holy Grail's Raider
				// human, found a holy grail, it has answers to your questions
				ScheduledFuture<?> showFirstStringOnScreen = ThreadPool.schedule(() -> instance.broadcastPacket(new ExShowScreenMessage(STRING_ID_01, ExShowScreenMessage.TOP_CENTER, 10000, true)), 30000);
				instance.getParameters().set(FIRST_STRING_TIMER, showFirstStringOnScreen);
				break;
			}
			// case TALK_WITH_HOLY_GRAILS_RAIDER_NPC_ON_START:
			case KILL_FOUR_DREAM_WATCHERS_INSIDE:
			{
				getScheduleMessageAndMakeAction(instance);
				instance.setReenterTime();
				instance.despawnGroup("BenedictNPC_1");
				instance.spawnGroup("BenedictMonsters").forEach(npc -> npc.getSpawn().stopRespawn());
				setRandomTitles(instance);
				break;
			}
			case TALK_WITH_HOLY_GRAILS_RAIDER_NPC_TO_OPEN_DOORS:
			{
				// Go to last room for holy grail
				instance.broadcastPacket(new ExShowScreenMessage(STRING_ID_02, ExShowScreenMessage.TOP_CENTER, 10000, true));
				instance.spawnGroup("BenedictNPC_2").forEach(npc -> npc.getSpawn().stopRespawn());
				break;
			}
			case TALK_WITH_HOLY_GRAIL_BEFORE_BOSS:
			{
				// Are you brave enough to cross this broken bridge?
				instance.broadcastPacket(new ExShowScreenMessage(STRING_ID_03, ExShowScreenMessage.TOP_CENTER, 10000, true));
				instance.spawnGroup("BenedictNPC_3").forEach(npc -> npc.getSpawn().stopRespawn());
				for (int doorId : MAIN_DOOR_IDS)
				{
					instance.openCloseDoor(doorId, true);
				}
				break;
			}
			case BOSS_FIGHT:
			{
				instance.despawnGroup("BenedictNPC_3");
				spawnBoss(instance);
				break;
			}
			case FINISH_INSTANCE:
			{
				instance.setDuration(5);
				instance.broadcastPacket(new ExShowScreenMessage(STRING_ID_06, ExShowScreenMessage.TOP_CENTER, 10000, true));
				break;
			}
		}
	}
	
	private void spawnBoss(Instance instance)
	{
		final boolean random = Rnd.nextBoolean();
		// show text
		final NpcStringId bossText = random ? STRING_ID_04 : STRING_ID_05;
		instance.broadcastPacket(new ExShowScreenMessage(bossText, ExShowScreenMessage.TOP_CENTER, 10000, true));
		// spawn boss
		final String bossTemplate = random ? "BenedictMonsters_Boss_Gabriel" : "BenedictMonsters_Boss_GabrielMinion";
		instance.spawnGroup(bossTemplate).forEach(n -> n.getSpawn().stopRespawn());
	}
	
	private void setRandomTitles(Instance instance)
	{
		final List<Npc> monsters = instance.getNpcsOfGroup("BenedictMonsters");
		if (monsters.isEmpty())
		{
			for (Player player : instance.getPlayers())
			{
				InstanceManager.getInstance().deleteInstanceTime(player, INSTANCE_ID);
			}
			instance.destroy();
			return;
		}
		
		final Set<Npc> randomNpcs = new HashSet<>();
		while (randomNpcs.size() < 7)
		{
			final Npc monster = monsters.get(Rnd.get(1, monsters.size()) - 1);
			randomNpcs.add(monster);
			monsters.remove(monster);
		}
		for (Npc monster : randomNpcs)
		{
			monster.setTitleString(DREAM_WATCHER);
			monster.broadcastInfo();
		}
		monsters.clear();
		randomNpcs.clear();
	}
	
	private static void getScheduleMessageAndMakeAction(Instance instance)
	{
		// make sure message is shown (if will not - send) and trying remove it from memory
		ScheduledFuture<?> showFirstStringOnScreen = instance.getParameters().getObject(FIRST_STRING_TIMER, ScheduledFuture.class, null);
		if ((showFirstStringOnScreen == null) || !showFirstStringOnScreen.isDone())
		{
			if (showFirstStringOnScreen != null)
			{
				showFirstStringOnScreen.cancel(true);
				showFirstStringOnScreen = null;
			}
			// I hate that cursed arrogant Ventus! You go ahead and get to the Temple of Ventus.
			instance.broadcastPacket(new ExShowScreenMessage(STRING_ID_01, ExShowScreenMessage.TOP_CENTER, 10000, true));
		}
		if ((showFirstStringOnScreen != null) && showFirstStringOnScreen.isDone())
		{
			showFirstStringOnScreen.cancel(true);
			showFirstStringOnScreen = null;
		}
		instance.getParameters().remove(FIRST_STRING_TIMER);
	}
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		if (killer == null)
		{
			return super.onKill(npc, killer, isSummon);
		}
		
		final Instance instance = killer.getInstanceWorld();
		if ((instance == null) || (instance.getTemplateId() != INSTANCE_ID))
		{
			return super.onKill(npc, killer, isSummon);
		}
		
		switch (instance.getStatus())
		{
			case KILL_FOUR_DREAM_WATCHERS_INSIDE:
			{
				if (instance.getAliveNpcs(MONSTERS).isEmpty())
				{
					instance.setStatus(TALK_WITH_HOLY_GRAILS_RAIDER_NPC_TO_OPEN_DOORS);
					break;
				}
				
				if ((npc.getTitleString() == null) || !npc.getTitleString().equals(DREAM_WATCHER))
				{
					return super.onKill(npc, killer, isSummon);
				}
				
				final int dreamWatcherCount = instance.getParameters().increaseInt(DREAM_WATCHER_COUNTER, 0, 1);
				switch (dreamWatcherCount)
				{
					case 1:
					{
						instance.broadcastPacket(new ExShowScreenMessage(NpcStringId.DREAM_WATCHER_S_DEATH_1_4_2, ExShowScreenMessage.BOTTOM_RIGHT, 10000, false));
						break;
					}
					case 2:
					{
						instance.broadcastPacket(new ExShowScreenMessage(NpcStringId.DREAM_WATCHER_S_DEATH_2_4_2, ExShowScreenMessage.BOTTOM_RIGHT, 10000, false));
						break;
					}
					case 3:
					{
						instance.broadcastPacket(new ExShowScreenMessage(NpcStringId.DREAM_WATCHER_S_DEATH_3_4_2, ExShowScreenMessage.BOTTOM_RIGHT, 10000, false));
						break;
					}
					case 4:
					{
						instance.setStatus(TALK_WITH_HOLY_GRAILS_RAIDER_NPC_TO_OPEN_DOORS);
						break;
					}
				}
				break;
			}
			case BOSS_FIGHT:
			{
				if ((npc.getTemplate().getId() == GABRIELLE) || (npc.getTemplate().getId() == GABRIELLE_MINION))
				{
					if (!CatGuildsLair.calculateChanceForCatLair(instance))
					{
						instance.setStatus(FINISH_INSTANCE);
					}
				}
				break;
			}
		}
		
		return super.onKill(npc, killer, isSummon);
	}
	
	public static void main(String[] args)
	{
		new BenedictsMonastery();
	}
}
