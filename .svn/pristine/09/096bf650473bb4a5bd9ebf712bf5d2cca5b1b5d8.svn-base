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
package instances.ValakasTemple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.l2jmobius.commons.threads.ThreadPool;
import org.l2jmobius.gameserver.instancemanager.InstanceManager;
import org.l2jmobius.gameserver.model.Location;
import org.l2jmobius.gameserver.model.actor.Attackable;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.events.EventType;
import org.l2jmobius.gameserver.model.events.ListenerRegisterType;
import org.l2jmobius.gameserver.model.events.annotations.RegisterEvent;
import org.l2jmobius.gameserver.model.events.annotations.RegisterType;
import org.l2jmobius.gameserver.model.events.impl.creature.OnCreatureTeleported;
import org.l2jmobius.gameserver.model.events.impl.instance.OnInstanceStatusChange;
import org.l2jmobius.gameserver.model.instancezone.Instance;
import org.l2jmobius.gameserver.model.instancezone.InstanceTemplate;
import org.l2jmobius.gameserver.model.spawns.NpcSpawnTemplate;
import org.l2jmobius.gameserver.model.spawns.SpawnGroup;
import org.l2jmobius.gameserver.model.spawns.SpawnTemplate;
import org.l2jmobius.gameserver.network.NpcStringId;
import org.l2jmobius.gameserver.network.serverpackets.ExShowScreenMessage;
import org.l2jmobius.gameserver.network.serverpackets.OnEventTrigger;

import instances.AbstractInstance;

/**
 * @author Index
 */
public class ValakasTemple extends AbstractInstance
{
	private static final int INSTANCE_CREATE = 0;
	private static final int SAVE_HERMIT = 1;
	private static final int KILL_OBSERVATION_DEVICE_CENTER = 2;
	private static final int KILL_MONSTERS_CENTER = 3;
	private static final int KILL_IFRIT_CENTER = 4;
	private static final int KILL_LEFT_OR_RIGHT = 5;
	private static final String KILL_COUNT_VAR = "KillCount";
	private static final int KILL_OBSERVATION_DEVICE_TOMB = 6;
	public static final int KILL_TOMB = 7;
	public static final int GOTO_DUMMY_IFRIT = 8;
	public static final int OPEN_GATE_TIMER = 9;
	private static final int KILL_LAST_IFRIT = 10;
	private static final int FINISH_INSTANCE = 11;
	
	private static final int LEFT_SPECTATOR = 1;
	private static final int RIGHT_SPECTATOR = 2;
	private static final int TOMB_SPECTATOR = 3;
	
	public static final int VALAKAS_TEMPLE_INSTANCE_ID = 230;
	public static final int EVENT_ID_PLAYER_CIRCLE = 24137770;
	private static final int EVENT_ID_BOSS_CIRCLE = 24138880;
	private static final int BOSS_DOOR_ID = 24130002;
	private static final String IS_REMOVED_EVENTS = "IS_REMOVED_EVENTS";
	
	private static final int OBSERVATION_DEVICE = 18730;
	private static final int HUGE_IFRIT = 25964;
	private static final int LAST_IFRIT = 25966;
	private static final int[] MONSTER_IDs =
	{
		22490,
		22491,
		22492,
		22493,
		22494,
	};
	
	private ValakasTemple()
	{
		super(VALAKAS_TEMPLE_INSTANCE_ID);
		setInstanceStatusChangeId(this::onInstanceStatusChange, VALAKAS_TEMPLE_INSTANCE_ID);
		addInstanceCreatedId(VALAKAS_TEMPLE_INSTANCE_ID);
		addKillId(OBSERVATION_DEVICE);
		addKillId(HUGE_IFRIT);
		addKillId(LAST_IFRIT);
		addKillId(MONSTER_IDs);
	}
	
	public void onInstanceStatusChange(OnInstanceStatusChange event)
	{
		final Instance world = event.getWorld();
		final int status = event.getStatus();
		switch (status)
		{
			case INSTANCE_CREATE:
			{
				// onInstanceCreated(instance, null);
				break;
			}
			case KILL_OBSERVATION_DEVICE_CENTER:
			{
				setSecondStatusForInstance(world);
				break;
			}
			case KILL_MONSTERS_CENTER:
			{
				setThirdStatusForInstance(world);
				break;
			}
			case KILL_IFRIT_CENTER:
			{
				setFourthStatusForInstance(world);
				break;
			}
			case KILL_LEFT_OR_RIGHT:
			{
				setFifthStatusForInstance(world);
				break;
			}
			case KILL_OBSERVATION_DEVICE_TOMB:
			{
				setSixthStatusForInstance(world);
				break;
			}
			case KILL_TOMB:
			{
				setSeventhStatusForInstance(world);
				break;
			}
			case GOTO_DUMMY_IFRIT:
			{
				setEightStatusForInstance(world);
				break;
			}
			case OPEN_GATE_TIMER:
			{
				setNineStatusForInstance(world);
				break;
			}
			case KILL_LAST_IFRIT:
			{
				setTenStatusForInstance(world);
				break;
			}
			case FINISH_INSTANCE:
			{
				setElevenStatusForInstance(world);
				break;
			}
		}
	}
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		final Instance world = (killer == null) || (npc == null) ? null : killer.getInstanceWorld();
		if ((npc == null) || (world == null) || (world.getTemplateId() != VALAKAS_TEMPLE_INSTANCE_ID))
		{
			return super.onKill(npc, killer, isSummon);
		}
		
		switch (npc.getId())
		{
			case OBSERVATION_DEVICE:
			{
				if (world.getStatus() == KILL_OBSERVATION_DEVICE_CENTER)
				{
					world.setStatus(KILL_MONSTERS_CENTER);
				}
				else if ((world.getStatus() == KILL_OBSERVATION_DEVICE_TOMB) && (npc.getScriptValue() == TOMB_SPECTATOR))
				{
					world.setStatus(KILL_TOMB);
				}
				else
				{
					spawnMonsterLeftOrRight(world, npc.getScriptValue());
				}
				break;
			}
			case HUGE_IFRIT:
			{
				if (world.getStatus() == KILL_IFRIT_CENTER)
				{
					world.setStatus(KILL_LEFT_OR_RIGHT);
				}
				break;
			}
			case LAST_IFRIT:
			{
				if (world.getStatus() == KILL_LAST_IFRIT)
				{
					world.setStatus(FINISH_INSTANCE);
				}
				break;
			}
			default:
			{
				if ((world.getStatus() == SAVE_HERMIT) && world.getAliveNpcs(MONSTER_IDs).isEmpty())
				{
					world.setStatus(KILL_OBSERVATION_DEVICE_CENTER);
				}
				else if (world.getStatus() == KILL_MONSTERS_CENTER)
				{
					if (npc.getSpawn().getNpcSpawnTemplate().getGroup().getName().equalsIgnoreCase("monsters_from_gate_center"))
					{
						if (world.getParameters().getInt(KILL_COUNT_VAR, 0) == world.getParameters().getInt("NPC_CENTER_MONSTERS", 0))
						{
							world.setStatus(KILL_IFRIT_CENTER);
						}
						else
						{
							world.getParameters().increaseInt(KILL_COUNT_VAR, 0, 1);
						}
					}
				}
				else if (world.getStatus() == KILL_LEFT_OR_RIGHT)
				{
					world.setStatus(KILL_OBSERVATION_DEVICE_TOMB);
				}
				break;
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public void onInstanceCreated(Instance world, Player player)
	{
		final InstanceTemplate template = InstanceManager.getInstance().getInstanceTemplate(VALAKAS_TEMPLE_INSTANCE_ID);
		for (SpawnTemplate spawn : template.getSpawns())
		{
			if (world.getParameters().getInt("NPC_CENTER_MONSTERS", 0) == 0)
			{
				for (NpcSpawnTemplate spawns : spawn.getGroupsByName("monsters_from_gate_center").get(0).getSpawns())
				{
					world.setParameter("NPC_CENTER_MONSTERS", spawns.getCount() + 1);
				}
			}
			
			final List<Location> locations = new ArrayList<>(7);
			for (SpawnGroup group : spawn.getGroupsByName("clones"))
			{
				for (NpcSpawnTemplate clone : group.getSpawns())
				{
					locations.add(clone.getSpawnLocation());
				}
			}
			world.setParameter("TELEPORT_CLONES", locations);
		}
		
		// INSTANCE_CREATE -> SAVE_HERMIT
		world.setStatus(SAVE_HERMIT);
		world.spawnGroup("tomb"); // Make sure no one do not want to speed run it.
		final List<Npc> monstersNearHermit = world.getNpcsOfGroup("hermit_attackers_01");
		final Npc hermitInInstance = world.getNpcOfGroup("hermit_01", Objects::nonNull);
		if ((monstersNearHermit == null) || monstersNearHermit.isEmpty())
		{
			return;
		}
		
		for (Npc monster : monstersNearHermit)
		{
			final Attackable attackable = (Attackable) monster;
			attackable.addDamageHate(hermitInInstance, 0, 1);
			addAttackDesire(monster, hermitInInstance);
		}
		
		ThreadPool.schedule(() -> sendMessageOnScreen(world, NpcStringId.OVER_HERE_PLEASE_HELP_ME), 3000);
		
		super.onInstanceCreated(world, player);
	}
	
	private static void setSecondStatusForInstance(Instance world)
	{
		// KILL_OBSERVATION_DEVICE_CENTER
		world.spawnGroup("gate_of_legion_center");
		world.spawnGroup("spectator_center");
	}
	
	private static void setThirdStatusForInstance(Instance world)
	{
		// KILL_MONSTERS_CENTER
		ThreadPool.schedule(() -> world.spawnGroup("monsters_from_gate_center"), 6_000);
		ThreadPool.schedule(() -> world.spawnGroup("monsters_from_gate_center_02"), 6_000);
	}
	
	private static void setFourthStatusForInstance(Instance world)
	{
		// KILL_IFRIT_CENTER
		world.spawnGroup("raid_boss_center");
		sendMessageOnScreen(world, NpcStringId.WHO_YOU_ARE_HOW_DARE_YOU_INVADE_THE_TEMPLE_OF_THE_GREAT_VALAKAS);
	}
	
	private static void setFifthStatusForInstance(Instance world)
	{
		// KILL_LEFT_OR_RIGHT
		world.spawnGroup("gate_of_legion_left");
		final Npc npcLeft = world.spawnGroup("spectator_left").get(0);
		npcLeft.setScriptValue(LEFT_SPECTATOR);
		world.spawnGroup("gate_of_legion_right");
		final Npc npcRight = world.spawnGroup("spectator_right").get(0);
		npcRight.setScriptValue(RIGHT_SPECTATOR);
	}
	
	private static void setSixthStatusForInstance(Instance world)
	{
		// KILL_OBSERVATION_DEVICE_TOMB
		final Npc tombSpectator = world.spawnGroup("spectator_tomb").get(0);
		tombSpectator.setScriptValue(TOMB_SPECTATOR);
		world.spawnGroup("gate_of_legion_tomb");
	}
	
	private static void setSeventhStatusForInstance(Instance world)
	{
		// KILL_TOMB
		ThreadPool.schedule(() -> world.spawnGroup("monsters_from_gate_tomb"), 6_000);
	}
	
	private static void setEightStatusForInstance(Instance world)
	{
		// GOTO_DUMMY_IFRIT
		final Npc dummyIfrit = world.spawnGroup("dummy_ifrit").get(0);
		dummyIfrit.setImmobilized(true);
		world.spawnGroup("hermit_02");
	}
	
	private static void setNineStatusForInstance(Instance world)
	{
		// OPEN_GATE_TIMER
		world.getPlayers().forEach(player -> player.sendPacket(new OnEventTrigger(ValakasTemple.EVENT_ID_PLAYER_CIRCLE, true)));
		ThreadPool.schedule(() -> world.setStatus(KILL_LAST_IFRIT), 15_000);
	}
	
	private static void setTenStatusForInstance(Instance world)
	{
		// KILL_LAST_IFRIT
		// Enable boss circle.
		ThreadPool.schedule(() -> removeEventsFromInstance(world), 15_000);
		world.getPlayers().forEach(player -> player.sendPacket(new OnEventTrigger(ValakasTemple.EVENT_ID_BOSS_CIRCLE, true)));
		// Make list of players inside.
		final List<Player> players = new ArrayList<>(world.getPlayers());
		// Make list "random" - no matter how players been teleported, but clones will be get random ids.
		Collections.shuffle(players);
		final List<Location> locations = world.getParameters().getList("TELEPORT_CLONES", Location.class);
		for (Player player : players)
		{
			player.teleToLocation(getRandomEntry(locations), false);
		}
		// Spawn and get clones instances.
		final List<Npc> cloneNpcs = world.spawnGroup("clones");
		int counter = 0;
		for (Npc clone : cloneNpcs)
		{
			if ((counter >= players.size()) || (counter > 7))
			{
				break;
			}
			
			clone.setCloneObjId(players.get(counter).getObjectId());
			clone.setName(players.get(counter).getAppearance().getVisibleName());
			clone.broadcastInfo();
			counter += 1;
		}
	}
	
	private static void setElevenStatusForInstance(Instance world)
	{
		// FINISH_INSTANCE
		world.spawnGroup("hermit_03");
		world.finishInstance();
	}
	
	private static void removeEventsFromInstance(Instance world)
	{
		world.despawnGroup("dummy_ifrit");
		runRemoveEventsFromInstance(world);
		ThreadPool.schedule(() -> spawnBoss(world), 15000);
	}
	
	private static void runRemoveEventsFromInstance(Instance world)
	{
		world.getParameters().set(IS_REMOVED_EVENTS, true);
		world.getPlayers().forEach(player -> player.sendPacket(new OnEventTrigger(ValakasTemple.EVENT_ID_PLAYER_CIRCLE, false)));
		world.getPlayers().forEach(player -> player.sendPacket(new OnEventTrigger(ValakasTemple.EVENT_ID_BOSS_CIRCLE, false)));
	}
	
	private static void spawnBoss(Instance world)
	{
		world.openCloseDoor(BOSS_DOOR_ID, true);
		world.spawnGroup("raid_boss_last");
		ThreadPool.schedule(() -> world.spawnGroup("monsters_from_gate_last"), 15000);
	}
	
	private static void spawnMonsterLeftOrRight(Instance world, int side)
	{
		final String spawnGroupName = side == LEFT_SPECTATOR ? "monsters_from_gate_left" : "monsters_from_gate_right";
		ThreadPool.schedule(() -> world.spawnGroup(spawnGroupName), 6000);
	}
	
	private static void sendMessageOnScreen(Instance world, NpcStringId npcId)
	{
		final ExShowScreenMessage screenMessage = new ExShowScreenMessage(npcId, ExShowScreenMessage.TOP_CENTER, 10000, true);
		world.getPlayers().forEach(player -> player.sendPacket(screenMessage));
	}
	
	@RegisterEvent(EventType.ON_CREATURE_TELEPORTED)
	@RegisterType(ListenerRegisterType.GLOBAL_PLAYERS)
	public void onCreatureTeleported(OnCreatureTeleported event)
	{
		if (!event.getCreature().isPlayer())
		{
			return;
		}
		
		final Player player = event.getCreature().getActingPlayer();
		final Instance world = player.getInstanceWorld();
		if ((world == null) || (world.getTemplateId() != VALAKAS_TEMPLE_INSTANCE_ID))
		{
			return;
		}
		
		if ((world.getStatus() == KILL_LAST_IFRIT) && !world.getParameters().getBoolean(IS_REMOVED_EVENTS, false))
		{
			player.sendPacket(new OnEventTrigger(ValakasTemple.EVENT_ID_PLAYER_CIRCLE, true));
			player.sendPacket(new OnEventTrigger(ValakasTemple.EVENT_ID_BOSS_CIRCLE, true));
		}
	}
	
	public static void main(String[] args)
	{
		new ValakasTemple();
	}
}
