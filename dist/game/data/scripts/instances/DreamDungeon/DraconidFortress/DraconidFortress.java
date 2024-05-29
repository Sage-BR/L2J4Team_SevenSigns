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
package instances.DreamDungeon.DraconidFortress;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.l2j.commons.util.Rnd;
import org.l2j.gameserver.instancemanager.InstanceManager;
import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.events.impl.instance.OnInstanceStatusChange;
import org.l2j.gameserver.model.instancezone.Instance;
import org.l2j.gameserver.network.NpcStringId;
import org.l2j.gameserver.network.serverpackets.ExShowScreenMessage;

import instances.AbstractInstance;
import instances.DreamDungeon.CatGuildsLair.CatGuildsLair;

/**
 * @author Index
 */
public class DraconidFortress extends AbstractInstance
{
	/** Instance World IDS */
	public static final int CREATED = 0;
	public static final int KILL_FOUR_DREAM_WATCHERS_INSIDE_FIRST_TIME = 1;
	public static final int TALK_WITH_TRAITOR_FIRST_TIME = 2;
	public static final int TALK_WITH_TRAITOR_SECOND_TIME = 3;
	public static final int KILL_FOUR_DREAM_WATCHERS_INSIDE_SECOND_TIME = 4;
	public static final int KILL_FOLLOWER = 5;
	public static final int BOSS_FIGHT = 6;
	public static final int FINISH_INSTANCE = 7;
	
	public static final int INSTANCE_ID = 224;
	
	private static final int[] MONSTERS =
	{
		22403, // Red Draconid Mage
		22404, // Red Draconid Warrior
		22405, // Red Draconid Hero
		22406, // Red Draconid Lieutenant
	};
	
	private static final int RED_DRACONID_FOLLOWER_MONSTER_ID = 18676;
	
	private static final int TIAD = 18684;
	private static final int BAINT = 18685;
	
	private static final String DREAM_WATCHER_COUNTER = "DREAM_WATCHER_COUNTER";
	
	private static final NpcStringId DREAM_WATCHER = NpcStringId.DREAM_WATCHER;
	private static final NpcStringId STRING_ID_01 = NpcStringId.HE_HE_YOU_WOULDNT_BE_SCARED_BY_A_DEVIL_IT_SEEMS_SINCE_YOU_DARE_TO_SET_YOUR_FOOT_IN_HERE_HE_HE; // guess
	private static final NpcStringId STRING_ID_02 = NpcStringId.WILL_YOU_TURN_BACK_OR_MOVE_FURTHER_HE_HE;
	private static final NpcStringId STRING_ID_03 = NpcStringId.WELL_I_WAS_BORED_BEFORE_BUT_NOW_THE_THINGS_HAVE_TAKEN_A_COMPLETELY_DIFFERENT_TURN_PLEASE_COME_IN_DONT_BE_SHY;
	private static final NpcStringId STRING_ID_04 = NpcStringId.OH_MIGHTY_TIAT_OUR_ENEMIES_ARE_WEAKENED_IT_S_TIME_TO_DEFEAT_THEM_ONCE_AND_FOR_ALL;
	private static final NpcStringId STRING_ID_05 = NpcStringId.OH_THOSE_POWERLESS_ADEN_WARRIORS_LET_S_FINISH_THEM_NOW_AND_MOVE_ON_THE_ADEN_CASTLE;
	private static final NpcStringId STRING_ID_06 = NpcStringId.I_M_GOING_TO_ADEN_COMMANDER_ASSISTANT_FOLLOW_ME;
	private static final NpcStringId STRING_ID_07 = NpcStringId.HE_HE_EXCELLENT_BUT_ARE_YOU_SURE_YOUVE_WON_MAYBE_ALL_OF_THIS_IS_NOTHING_BUT_A_DREAM;
	
	private static final int FIRST_DOOR = 12240030;
	private static final int SECOND_DOOR = 12240031;
	
	private static final int[] CLOSED_DOOR =
	{
		12240028,
		12240029,
	};
	
	private DraconidFortress()
	{
		super(INSTANCE_ID);
		addKillId(MONSTERS);
		addKillId(RED_DRACONID_FOLLOWER_MONSTER_ID);
		addKillId(TIAD, BAINT);
		setInstanceStatusChangeId(this::onInstanceStatusChange, INSTANCE_ID);
	}
	
	public void onInstanceStatusChange(OnInstanceStatusChange event)
	{
		final Instance instance = event.getWorld();
		switch (event.getStatus())
		{
			case KILL_FOUR_DREAM_WATCHERS_INSIDE_FIRST_TIME:
			{
				instance.setReenterTime();
				instance.broadcastPacket(new ExShowScreenMessage(STRING_ID_01, ExShowScreenMessage.TOP_CENTER, 10_000, true));
				instance.spawnGroup("DraconidMonsters");
				instance.despawnGroup("DraconidNPC_1");
				setRandomTitles(instance, true);
				break;
			}
			case TALK_WITH_TRAITOR_FIRST_TIME:
			{
				instance.spawnGroup("DraconidNPC_2");
				removeRandomTag(instance);
				break;
			}
			case TALK_WITH_TRAITOR_SECOND_TIME:
			{
				instance.broadcastPacket(new ExShowScreenMessage(STRING_ID_02, ExShowScreenMessage.TOP_CENTER, 10_000, true));
				instance.openCloseDoor(FIRST_DOOR, true);
				instance.spawnGroup("DraconidMonsters_02");
				instance.spawnGroup("DraconidNPC_3");
				instance.despawnGroup("DraconidNPC_2");
				break;
			}
			case KILL_FOUR_DREAM_WATCHERS_INSIDE_SECOND_TIME:
			{
				instance.broadcastPacket(new ExShowScreenMessage(STRING_ID_03, ExShowScreenMessage.TOP_CENTER, 10_000, true));
				// use the same counter
				instance.getParameters().set(DREAM_WATCHER_COUNTER, 0);
				instance.openCloseDoor(SECOND_DOOR, true);
				instance.spawnGroup("DraconidMonsters_03");
				instance.despawnGroup("DraconidNPC_3");
				setRandomTitles(instance, false);
				break;
			}
			case KILL_FOLLOWER:
			{
				instance.broadcastPacket(new ExShowScreenMessage(STRING_ID_04, ExShowScreenMessage.TOP_CENTER, 10_000, true));
				instance.spawnGroup("DraconidMonsters_04");
				break;
			}
			case BOSS_FIGHT:
			{
				spawnBoss(instance);
				break;
			}
			case FINISH_INSTANCE:
			{
				instance.setDuration(5);
				instance.spawnGroup("DraconidNPC_4");
				instance.broadcastPacket(new ExShowScreenMessage(STRING_ID_07, ExShowScreenMessage.TOP_CENTER, 10_000, true));
				break;
			}
		}
	}
	
	private void spawnBoss(Instance instance)
	{
		final boolean random = Rnd.nextBoolean();
		// show text
		final NpcStringId bossText = random ? STRING_ID_05 : STRING_ID_06;
		instance.broadcastPacket(new ExShowScreenMessage(bossText, ExShowScreenMessage.TOP_CENTER, 10000, true));
		// spawn boss
		final String bossTemplate = random ? "DraconidMonsters_BossTiad" : "DraconidMonsters_BossBaint";
		instance.spawnGroup(bossTemplate).forEach(n -> n.getSpawn().stopRespawn());
	}
	
	private void removeRandomTag(Instance instance)
	{
		final List<Npc> monsterList = new CopyOnWriteArrayList<>(instance.getNpcsOfGroup("DraconidMonsters"));
		for (Npc monster : monsterList)
		{
			if ((monster.getTitleString() != null) && monster.getTitleString().equals(DREAM_WATCHER))
			{
				(monster).doDie(null);
			}
		}
	}
	
	private void setRandomTitles(Instance instance, boolean isFirst)
	{
		final Set<Npc> randomNpcs = new HashSet<>();
		final List<Npc> monsterList = new CopyOnWriteArrayList<>(isFirst ? instance.getNpcsOfGroup("DraconidMonsters") : instance.getNpcsOfGroup("DraconidMonsters_03"));
		if (monsterList.isEmpty())
		{
			for (Player player : instance.getPlayers())
			{
				InstanceManager.getInstance().deleteInstanceTime(player, INSTANCE_ID);
			}
			instance.destroy();
			return;
		}
		
		Collections.shuffle(monsterList);
		for (Npc monster : monsterList)
		{
			if (randomNpcs.size() >= 4)
			{
				break;
			}
			
			randomNpcs.add(monster);
			monsterList.remove(monster);
		}
		
		for (Npc monster : randomNpcs)
		{
			monster.setTitleString(DREAM_WATCHER);
			monster.broadcastInfo();
		}
		monsterList.clear();
		randomNpcs.clear();
	}
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		if (killer == null)
		{
			return super.onKill(npc, killer, isSummon);
		}
		Instance instance = killer.getInstanceWorld();
		if ((instance == null) || (instance.getTemplateId() != INSTANCE_ID))
		{
			return super.onKill(npc, killer, isSummon);
		}
		switch (instance.getStatus())
		{
			case KILL_FOUR_DREAM_WATCHERS_INSIDE_FIRST_TIME:
			case KILL_FOUR_DREAM_WATCHERS_INSIDE_SECOND_TIME:
			{
				final int setStatus = instance.getStatus() == KILL_FOUR_DREAM_WATCHERS_INSIDE_FIRST_TIME ? TALK_WITH_TRAITOR_FIRST_TIME : KILL_FOLLOWER;
				if (instance.getAliveNpcs(MONSTERS).isEmpty())
				{
					instance.setStatus(setStatus);
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
						instance.broadcastPacket(new ExShowScreenMessage(NpcStringId.DREAM_WATCHERS_DEATH_1_4_4, ExShowScreenMessage.BOTTOM_RIGHT, 10000, false));
						break;
					}
					case 2:
					{
						instance.broadcastPacket(new ExShowScreenMessage(NpcStringId.DREAM_WATCHERS_DEATH_2_4_4, ExShowScreenMessage.BOTTOM_RIGHT, 10000, false));
						break;
					}
					case 3:
					{
						instance.broadcastPacket(new ExShowScreenMessage(NpcStringId.DREAM_WATCHERS_DEATH_3_4_4, ExShowScreenMessage.BOTTOM_RIGHT, 10000, false));
						break;
					}
					case 4:
					{
						instance.setStatus(setStatus);
						break;
					}
				}
				break;
			}
			case KILL_FOLLOWER:
			{
				if (npc.getTemplate().getId() == RED_DRACONID_FOLLOWER_MONSTER_ID)
				{
					instance.setStatus(BOSS_FIGHT);
				}
				break;
			}
			case BOSS_FIGHT:
			{
				if ((npc.getTemplate().getId() == TIAD) || (npc.getTemplate().getId() == BAINT))
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
	
	@Override
	public void onInstanceCreated(Instance instance, Player player)
	{
		// make sure all doors will be closed
		final int instanceId = instance.getId();
		closeDoor(FIRST_DOOR, instanceId);
		closeDoor(SECOND_DOOR, instanceId);
		closeDoor(CLOSED_DOOR[0], instanceId);
		closeDoor(CLOSED_DOOR[1], instanceId);
		getDoor(FIRST_DOOR, instanceId).broadcastInfo();
		getDoor(SECOND_DOOR, instanceId).broadcastInfo();
		getDoor(CLOSED_DOOR[0], instanceId).broadcastInfo();
		getDoor(CLOSED_DOOR[1], instanceId).broadcastInfo();
		/*
		 * instance.broadcastPacket(new StaticObject(getDoor(FIRST_DOOR, instanceId), false)); instance.broadcastPacket(new StaticObject(getDoor(SECOND_DOOR, instanceId), false));
		 */
		super.onInstanceCreated(instance, player);
	}
	
	public static void main(String[] args)
	{
		new DraconidFortress();
	}
}
