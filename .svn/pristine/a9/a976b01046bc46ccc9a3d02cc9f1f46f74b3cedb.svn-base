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
package instances.DreamDungeon.VentusTemple;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

import org.l2jmobius.commons.threads.ThreadPool;
import org.l2jmobius.commons.util.Rnd;
import org.l2jmobius.gameserver.instancemanager.InstanceManager;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.actor.instance.Monster;
import org.l2jmobius.gameserver.model.events.impl.instance.OnInstanceStatusChange;
import org.l2jmobius.gameserver.model.instancezone.Instance;
import org.l2jmobius.gameserver.network.NpcStringId;
import org.l2jmobius.gameserver.network.serverpackets.ExShowScreenMessage;

import instances.AbstractInstance;
import instances.DreamDungeon.CatGuildsLair.CatGuildsLair;

/**
 * @author Index
 */
public class VentusTemple extends AbstractInstance
{
	/** Instance World IDS */
	public static final int CREATED = 0;
	public static final int TALK_WITH_VANTUS_MAID_ON_CREATE = 1;
	public static final int KILL_FOUR_DREAM_WATCHERS_INSIDE = 2;
	public static final int GO_TO_VENTUS_ROOM = 3;
	public static final int SAVE_VENTUS_MAID_IN_VENTUS_ROOM = 4;
	public static final int TALK_WITH_VENTUS_MAID_FOR_RESPAWN_BALLISTA = 5;
	public static final int SHOOT_FROM_BALLISTA = 6;
	public static final int BOSS_FIGHT = 7;
	public static final int FINISH_INSTANCE = 8;
	
	public static final int INSTANCE_ID = 223;
	
	private static final int[] MONSTERS =
	{
		22399, // Ancient Swordsman
		22400, // Ancient Thrower
		22401, // Ancient Guard
		22402, // Ancient Guard Captain
	};
	
	private static final int VENTUS = 18682;
	private static final int REKARIO = 18683;
	
	private static final String FIRST_STRING_TIMER = "FIRST_STRING_TIMER";
	private static final String DREAM_WATCHER_COUNTER = "DREAM_WATCHER_COUNTER";
	private static final String MONSTERS_ATTACK_MAID_COUNTER = "MONSTERS_ATTACK_MAID_COUNTER";
	
	private static final NpcStringId DREAM_WATCHER = NpcStringId.DREAM_WATCHER;
	private static final NpcStringId STRING_ID_01 = NpcStringId.I_HATE_THAT_CURSED_ARROGANT_VENTUS_YOU_GO_AHEAD_AND_GET_TO_THE_TEMPLE_OF_VENTUS;
	private static final NpcStringId STRING_ID_02 = NpcStringId.FIRST_DEAL_WITH_THOSE_ENSLAVED_BY_VENTUS;
	private static final NpcStringId STRING_ID_03 = NpcStringId.THIS_SCOUNDREL_IS_GOING_TO_SUBJUGATE_THE_OTHERS_WILL_USING_DARK_MAGIC_TRICKS;
	private static final NpcStringId STRING_ID_04 = NpcStringId.USE_THE_BALLISTA_TO_DESTROY_VENTUS_STATUE;
	private static final NpcStringId STRING_ID_05 = NpcStringId.I_VE_BEEN_HAVING_SUCH_A_NICE_DREAM_BUT_SOMEONE_HAS_INTERRUPTED_IT_DO_YOU_KNOW_WHAT_PRICE_YOU_LL_HAVE_TO_PAY_FOR_WAKING_ME_UP;
	private static final NpcStringId STRING_ID_06 = NpcStringId.MASTER_VENTUS_DON_T_YOU_WORRY_I_LL_DEAL_WITH_IT_MYSELF;
	private static final NpcStringId STRING_ID_07 = NpcStringId.OH_VENTUS_HAUGHTY_VENTUS_I_WISH_IT_WERE_NOT_A_DREAM;
	
	private VentusTemple()
	{
		super(INSTANCE_ID);
		addKillId(MONSTERS);
		addKillId(VENTUS, REKARIO);
		addAttackId(MONSTERS);
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
				instance.setStatus(TALK_WITH_VANTUS_MAID_ON_CREATE);
				// send screen message if player will not talk with Holy Grail's Raider
				// I hate that cursed arrogant Ventus! You go ahead and get to the Temple of Ventus.
				final ScheduledFuture<?> showFirstStringOnScreen = ThreadPool.schedule(() -> instance.broadcastPacket(new ExShowScreenMessage(STRING_ID_01, ExShowScreenMessage.TOP_CENTER, 10000, true)), 10000);
				instance.getParameters().set(FIRST_STRING_TIMER, showFirstStringOnScreen);
				break;
			}
			// case TALK_WITH_VANTUS_MAID_ON_CREATE:
			case KILL_FOUR_DREAM_WATCHERS_INSIDE:
			{
				getScheduleMessageAndMakeAction(instance);
				instance.setReenterTime();
				instance.despawnGroup("VentusNPC_1");
				instance.spawnGroup("VentusMonsters").forEach(npc -> npc.getSpawn().stopRespawn());
				instance.spawnGroup("VentusDreamMonsters_01").forEach(npc -> npc.getSpawn().stopRespawn());
				ThreadPool.schedule(() -> setRandomTitles(instance), 500);
				break;
			}
			case GO_TO_VENTUS_ROOM:
			{
				instance.spawnGroup("VentusNPC_2").forEach(npc -> npc.getSpawn().stopRespawn());
				instance.spawnGroup("VentusMonsters_2").forEach(npc -> npc.getSpawn().stopRespawn()); // for avoid spawn by CreatureSee event
				instance.broadcastPacket(new ExShowScreenMessage(STRING_ID_03, ExShowScreenMessage.TOP_CENTER, 10000, true));
				break;
			}
			case SAVE_VENTUS_MAID_IN_VENTUS_ROOM:
			{
				// spawn monsters who will be attack maid
				final List<Npc> maidAttackers = instance.getNpcsOfGroup("VentusMonsters_2");
				maidAttackers.forEach(npc -> npc.getSpawn().stopRespawn());
				// start attack maid
				addDesireToAttack(instance, instance.getNpc(VentusMaid.VENTUS_MAID_NPC_ID));
				// add count of monsters who attack maid
				instance.getParameters().set(MONSTERS_ATTACK_MAID_COUNTER, -(maidAttackers.size()));
				break;
			}
			case SHOOT_FROM_BALLISTA:
			{
				instance.despawnGroup("VentusNPC_2");
				instance.spawnGroup("VentusNPC_3").forEach(npc -> npc.getSpawn().stopRespawn());
				instance.broadcastPacket(new ExShowScreenMessage(STRING_ID_04, ExShowScreenMessage.TOP_CENTER, 10000, true));
				break;
			}
			case BOSS_FIGHT:
			{
				ThreadPool.schedule(() ->
				{
					instance.despawnGroup("VentusNPC_3");
					spawnBoss(instance);
				}, 2500);
				break;
			}
			case FINISH_INSTANCE:
			{
				instance.setDuration(5);
				instance.spawnGroup("VentusNPC_4");
				instance.broadcastPacket(new ExShowScreenMessage(STRING_ID_07, ExShowScreenMessage.TOP_CENTER, 10000, true));
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
		final String bossTemplate = random ? "VentusBoss_Ventus" : "VentusBoss_Rekario";
		instance.spawnGroup(bossTemplate).forEach(n -> n.getSpawn().stopRespawn());
	}
	
	private void setRandomTitles(Instance instance)
	{
		final int maxDreamerMonsters = 6;
		final Set<Npc> randomNpcs = new HashSet<>();
		final List<Npc> monsters = new CopyOnWriteArrayList<>(instance.getNpcs(22402)); // get all captains
		if (monsters.isEmpty())
		{
			for (Player player : instance.getPlayers())
			{
				InstanceManager.getInstance().deleteInstanceTime(player, INSTANCE_ID);
			}
			instance.destroy();
			return;
		}
		
		Collections.shuffle(monsters);
		for (Npc monster : monsters)
		{
			if (randomNpcs.size() >= maxDreamerMonsters)
			{
				break;
			}
			
			if (monsters.size() <= maxDreamerMonsters)
			{
				if (randomNpcs.isEmpty())
				{
					randomNpcs.addAll(monsters);
				}
				if (randomNpcs.size() < 4) // min instance count
				{
					while (randomNpcs.size() <= maxDreamerMonsters)
					{
						final Npc finalMonster = monsters.get(Rnd.get(1, monsters.size()) - 1);
						randomNpcs.add(finalMonster);
						monsters.remove(finalMonster);
					}
				}
				break;
			}
			
			if (Rnd.get(100) < 35)
			{
				randomNpcs.add(monster);
			}
			else if (isMonsterIsOneOfSpawnGroup(monster, true))
			{
				monster.deleteMe();
			}
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
	
	@Override
	public String onAttack(Npc npc, Player attacker, int damage, boolean isSummon)
	{
		if (attacker == null)
		{
			return super.onAttack(npc, attacker, damage, isSummon);
		}
		
		final Instance instance = attacker.getInstanceWorld();
		if ((instance == null) || (instance.getTemplateId() == 0))
		{
			return super.onAttack(npc, attacker, damage, isSummon);
		}
		
		if (isMonsterIsOneOfSpawnGroup(npc, false))
		{
			if ((npc.getTarget() != null) && npc.getTarget().isPlayer())
			{
				return super.onAttack(npc, attacker, damage, isSummon);
			}
			addDesireToAttack(instance, attacker);
		}
		
		return super.onAttack(npc, attacker, damage, isSummon);
	}
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		if (killer == null)
		{
			return super.onKill(npc, killer, isSummon);
		}
		
		final Instance instance = killer.getInstanceWorld();
		if ((instance == null) || (instance.getTemplateId() == 0))
		{
			return super.onKill(npc, killer, isSummon);
		}
		
		switch (instance.getStatus())
		{
			case KILL_FOUR_DREAM_WATCHERS_INSIDE:
			{
				if (instance.getAliveNpcs(MONSTERS).isEmpty())
				{
					instance.setStatus(GO_TO_VENTUS_ROOM);
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
						instance.setStatus(GO_TO_VENTUS_ROOM);
						break;
					}
				}
				break;
			}
			case SAVE_VENTUS_MAID_IN_VENTUS_ROOM:
			{
				if (isMonsterIsOneOfSpawnGroup(npc, false) && (instance.getParameters().increaseInt(MONSTERS_ATTACK_MAID_COUNTER, 0, 1) >= 0))
				{
					instance.setStatus(TALK_WITH_VENTUS_MAID_FOR_RESPAWN_BALLISTA);
					break;
				}
				break;
			}
			case BOSS_FIGHT:
			{
				if ((npc.getTemplate().getId() == VENTUS) || (npc.getTemplate().getId() == REKARIO))
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
	
	private static boolean isMonsterIsOneOfSpawnGroup(Npc npc, boolean isDream)
	{
		final String group = isDream ? "VentusDreamMonsters_01" : "VentusMonsters_2";
		return (npc != null) && (npc.getSpawn() != null) && (npc.getSpawn().getNpcSpawnTemplate() != null) && (npc.getSpawn().getNpcSpawnTemplate().getGroup() != null) && (npc.getSpawn().getNpcSpawnTemplate().getGroup().getName() != null) && npc.getSpawn().getNpcSpawnTemplate().getGroup().getName().equalsIgnoreCase(group);
	}
	
	private void addDesireToAttack(Instance instance, Creature target)
	{
		final List<Npc> monsterAttackers = instance.getNpcsOfGroup("VentusMonsters_2");
		for (Npc monster : monsterAttackers)
		{
			((Monster) monster).addDamageHate(target, 0, target.isNpc() ? 1 : 2);
			addAttackDesire(monster, target);
		}
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
			// First deal with those enslaved by Ventus. // guess
			instance.broadcastPacket(new ExShowScreenMessage(STRING_ID_02, ExShowScreenMessage.TOP_CENTER, 10000, true));
		}
		if ((showFirstStringOnScreen != null) && showFirstStringOnScreen.isDone())
		{
			showFirstStringOnScreen.cancel(true);
			showFirstStringOnScreen = null;
		}
		instance.getParameters().remove(FIRST_STRING_TIMER);
	}
	
	public static void main(String[] args)
	{
		new VentusTemple();
	}
}
