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
package events.BattleWithKeber;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReference;

import org.l2jmobius.gameserver.data.xml.SpawnData;
import org.l2jmobius.gameserver.model.Location;
import org.l2jmobius.gameserver.model.World;
import org.l2jmobius.gameserver.model.WorldObject;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.ItemChanceHolder;
import org.l2jmobius.gameserver.model.quest.LongTimeEvent;
import org.l2jmobius.gameserver.model.spawns.SpawnGroup;
import org.l2jmobius.gameserver.model.spawns.SpawnTemplate;
import org.l2jmobius.gameserver.network.NpcStringId;
import org.l2jmobius.gameserver.network.serverpackets.ExShowScreenMessage;
import org.l2jmobius.gameserver.util.Broadcast;

/**
 * @author Index
 */
public class BattleWithKeber extends LongTimeEvent
{
	// NPCs
	private static final int BONNIE = 34216;
	private static final int KEBER_SEAL_STONE = 18551;
	// Locations from Bonnie teleport
	private static final Location PLAINS_OF_GLORY_TELEPORT = new Location(133994, 7136, -4352);
	private static final Location WAR_TORN_PLAINS_TELEPORT = new Location(161658, 21299, -3672);
	private static final Location SILENT_VALLEY_TELEPORT = new Location(181645, 52823, -6112);
	// Spawn locations for monsters
	private static final Location PLAINS_OF_GLORY_BOSS = new Location(132573, 7865, -4300);
	private static final Location WAR_TORN_PLAINS_BOSS = new Location(161205, 21680, -3680);
	private static final Location SILENT_VALLEY_BOSS = new Location(180640, 52824, -6096);
	// Monsters
	private static final int KEBER_LOW = 18537; // 63
	private static final int KEBER_MIDDLE = 18552; // 69
	private static final int KEBER_HIGH = 18553; // 75
	private static final int KISHAN = 18538; // 63
	private static final int NOMA = 18542; // 69
	private static final int SPALL = 18546; // 75
	private static final Set<Integer> BOSSES = new HashSet<>();
	static
	{
		BOSSES.add(KEBER_LOW);
		BOSSES.add(KEBER_MIDDLE);
		BOSSES.add(KEBER_HIGH);
		BOSSES.add(KISHAN);
		BOSSES.add(NOMA);
		BOSSES.add(SPALL);
	}
	private static final List<Integer> NORMAL_MINIONS = new ArrayList<>();
	static
	{
		NORMAL_MINIONS.add(18539);
		NORMAL_MINIONS.add(18543);
		NORMAL_MINIONS.add(18547);
	}
	private static final List<Integer> ELITE_MINIONS = new ArrayList<>();
	static
	{
		ELITE_MINIONS.add(18540);
		ELITE_MINIONS.add(18544);
		ELITE_MINIONS.add(18548);
	}
	private static final List<Integer> BRAINWASHED_MINIONS = new ArrayList<>();
	static
	{
		BRAINWASHED_MINIONS.add(18541);
		BRAINWASHED_MINIONS.add(18545);
		BRAINWASHED_MINIONS.add(18549);
	}
	// Drop
	private static final Set<ItemChanceHolder> KEBER_LOW_DROP = new HashSet<>();
	static
	{
		KEBER_LOW_DROP.add(new ItemChanceHolder(57, 100, 525000)); // Adena x525000
		KEBER_LOW_DROP.add(new ItemChanceHolder(91767, 100, 1)); // Enchant Kit: Talisman of Aden x2
		KEBER_LOW_DROP.add(new ItemChanceHolder(94481, 100, 150)); // Clan XP x150
	}
	private static final Set<ItemChanceHolder> KEBER_MIDDLE_DROP = new HashSet<>();
	static
	{
		KEBER_MIDDLE_DROP.add(new ItemChanceHolder(57, 100, 725000)); // Adena 725000
		KEBER_MIDDLE_DROP.add(new ItemChanceHolder(91767, 100, 1)); // Enchant Kit: Talisman of Aden x2
		KEBER_MIDDLE_DROP.add(new ItemChanceHolder(94481, 100, 150)); // Clan XP x150
	}
	private static final Set<ItemChanceHolder> KEBER_HIGH_DROP = new HashSet<>();
	static
	{
		KEBER_HIGH_DROP.add(new ItemChanceHolder(57, 100, 925000)); // Adena x925000
		KEBER_HIGH_DROP.add(new ItemChanceHolder(91767, 100, 1)); // Enchant Kit: Talisman of Aden x2
		KEBER_HIGH_DROP.add(new ItemChanceHolder(94481, 100, 150)); // Clan XP x150
	}
	private static final Set<ItemChanceHolder> KISHAN_DROP = new HashSet<>();
	static
	{
		KISHAN_DROP.add(new ItemChanceHolder(57, 100, 45000)); // Adena x45000
		KISHAN_DROP.add(new ItemChanceHolder(91767, 100, 1)); // Enchant Kit: Talisman of Aden x1
		KISHAN_DROP.add(new ItemChanceHolder(94481, 100, 50)); // Clan XP x50
	}
	private static final Set<ItemChanceHolder> NOMA_DROP = new HashSet<>();
	static
	{
		NOMA_DROP.add(new ItemChanceHolder(57, 100, 55000)); // Adena x55000
		NOMA_DROP.add(new ItemChanceHolder(91767, 100, 1)); // Enchant Kit: Talisman of Aden x1
		NOMA_DROP.add(new ItemChanceHolder(94481, 100, 50)); // Clan XP x50
	}
	private static final Set<ItemChanceHolder> SPALL_DROP = new HashSet<>();
	static
	{
		SPALL_DROP.add(new ItemChanceHolder(57, 100, 65000)); // Adena x55000
		SPALL_DROP.add(new ItemChanceHolder(91767, 100, 1)); // Enchant Kit: Talisman of Aden x1
		SPALL_DROP.add(new ItemChanceHolder(94481, 100, 50)); // Clan XP x50
	}
	private static final Set<ItemChanceHolder> MINION_DROP = new HashSet<>();
	static
	{
		MINION_DROP.add(new ItemChanceHolder(91767, 10, 1)); // Enchant Kit: Talisman of Aden x1
	}
	private static final Map<Integer, Set<ItemChanceHolder>> NPC_DROP = new HashMap<>();
	static
	{
		NPC_DROP.put(KEBER_LOW, KEBER_LOW_DROP);
		NPC_DROP.put(KEBER_MIDDLE, KEBER_MIDDLE_DROP);
		NPC_DROP.put(KEBER_HIGH, KEBER_HIGH_DROP);
		NPC_DROP.put(KISHAN, KISHAN_DROP);
		NPC_DROP.put(NOMA, NOMA_DROP);
		NPC_DROP.put(SPALL, SPALL_DROP);
	}
	// Misc
	private static final int EVENT_MESSAGE_START = -1;
	private static final int EVENT_MESSAGE_KILL = -2;
	private static final int EVENT_MESSAGE_TO_KILLER = -3;
	private static final int EVENT_MESSAGE_WEAK = -4;
	private static final int ELITE_MONSTER_COUNT = 200;
	private static final int BRAINWASHED_MONSTER_COUNT = 100;
	private static final int PLAINS_OF_GLORY_TELEPORT_COST = 2000;
	private static final int WAR_TORN_PLAINS_TELEPORT_COST = 2500;
	private static final int SILENT_VALLEY_TELEPORT_COST = 3000;
	private static final Map<Integer, NpcStringId> BROADCAST_MESSAGES = new HashMap<>();
	static
	{
		BROADCAST_MESSAGES.put(KEBER_LOW, NpcStringId.AHEM_YOU_DARE_TO_BOTHER_KEBER_YOU_WILL_SEE_NO_MERCY);
		BROADCAST_MESSAGES.put(KEBER_MIDDLE, NpcStringId.AHEM_YOU_DARE_TO_BOTHER_KEBER_YOU_WILL_SEE_NO_MERCY);
		BROADCAST_MESSAGES.put(KEBER_HIGH, NpcStringId.AHEM_YOU_DARE_TO_BOTHER_KEBER_YOU_WILL_SEE_NO_MERCY);
		BROADCAST_MESSAGES.put(KISHAN, NpcStringId.KISHAN_MY_SERVANT_ATTACK);
		BROADCAST_MESSAGES.put(NOMA, NpcStringId.NOMA_MY_SERVANT_ATTACK);
		BROADCAST_MESSAGES.put(SPALL, NpcStringId.SPALL_MY_SERVANT_ATTACK);
		BROADCAST_MESSAGES.put(EVENT_MESSAGE_START, NpcStringId.MAKE_THE_SACRED_BLOOD_SACRIFICE_TO_KEBER_ATTACK);
		BROADCAST_MESSAGES.put(EVENT_MESSAGE_KILL, NpcStringId.AH_YOU_ARE_INDEED_STRONG_S1_I_HAVE_TO_RETREAT_THIS_TIME);
		BROADCAST_MESSAGES.put(EVENT_MESSAGE_TO_KILLER, NpcStringId.YOU_WILL_NOT_BE_FORGIVEN_I_WILL_BE_BACK_AND_YOU_LL_REGRET_IT);
		BROADCAST_MESSAGES.put(EVENT_MESSAGE_WEAK, NpcStringId.LOOKS_LIKE_YOU_RE_WAY_TOO_WEAK_TO_BEAT_ME);
	}
	private static final AtomicIntegerArray MINION_KILL_COUNTER = new AtomicIntegerArray(3);
	private static final AtomicReference<SpawnTemplate> SPAWN_TEMPLATE = new AtomicReference<>();
	private static final AtomicIntegerArray MINION_MAX_COUNT = new AtomicIntegerArray(3);
	
	private BattleWithKeber()
	{
		if (isEventPeriod())
		{
			addFirstTalkId(BONNIE);
			addTalkId(BONNIE);
			addKillId(KEBER_LOW, KEBER_MIDDLE, KEBER_HIGH, KISHAN, NOMA, SPALL);
			addKillId(NORMAL_MINIONS);
			addKillId(ELITE_MINIONS);
			addKillId(BRAINWASHED_MINIONS);
			addSpawnId(KEBER_LOW, KEBER_MIDDLE, KEBER_HIGH, KISHAN, NOMA, SPALL);
			addSpawnId(NORMAL_MINIONS);
			addSpawnId(ELITE_MINIONS);
			startQuestTimer("KEBER_EVENT_START", getMsTimeForEventStage(1), null, null);
			SPAWN_TEMPLATE.set(SpawnData.getInstance().getSpawnByName("KEBER_MINIONS"));
			if (SPAWN_TEMPLATE.get() != null)
			{
				MINION_MAX_COUNT.set(0, SpawnData.getInstance().getSpawnGroupByName("KISHAN").getSpawns().stream().findFirst().get().getCount());
				MINION_MAX_COUNT.set(1, SpawnData.getInstance().getSpawnGroupByName("NOMA").getSpawns().stream().findFirst().get().getCount());
				MINION_MAX_COUNT.set(2, SpawnData.getInstance().getSpawnGroupByName("SPALL").getSpawns().stream().findFirst().get().getCount());
			}
		}
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		switch (event)
		{
			case "34216-02.html":
			{
				String html = getHtm(player, "34216-02.html");
				html = html.replace("%n1%", String.valueOf(PLAINS_OF_GLORY_TELEPORT_COST));
				html = html.replace("%n2%", String.valueOf(WAR_TORN_PLAINS_TELEPORT_COST));
				html = html.replace("%n3%", String.valueOf(SILENT_VALLEY_TELEPORT_COST));
				return html;
			}
			case "TELEPORT_01":
			case "TELEPORT_02":
			case "TELEPORT_03":
			{
				final byte requestLocation = Byte.parseByte(event.replace("TELEPORT_0", ""));
				if (player.getAdena() < (requestLocation == 1 ? PLAINS_OF_GLORY_TELEPORT_COST : requestLocation == 2 ? WAR_TORN_PLAINS_TELEPORT_COST : SILENT_VALLEY_TELEPORT_COST))
				{
					return "34216-04.html";
				}
				player.teleToLocation((requestLocation == 1 ? PLAINS_OF_GLORY_TELEPORT : requestLocation == 2 ? WAR_TORN_PLAINS_TELEPORT : SILENT_VALLEY_TELEPORT));
				return "34216-03.html";
			}
			case "KEBER_EVENT_START":
			{
				for (Npc wo : World.getInstance().getVisibleObjects(player, Npc.class))
				{
					if (wo.getId() == KEBER_SEAL_STONE)
					{
						wo.setDisplayEffect(1);
						Broadcast.toKnownPlayers((wo), new ExShowScreenMessage(BROADCAST_MESSAGES.get(EVENT_MESSAGE_START), ExShowScreenMessage.TOP_CENTER, 5000));
					}
				}
				MINION_KILL_COUNTER.set(0, 0);
				MINION_KILL_COUNTER.set(1, 0);
				MINION_KILL_COUNTER.set(2, 0);
				SPAWN_TEMPLATE.get().getGroups().forEach(SpawnGroup::spawnAll);
				cancelQuestTimer("KEBER_EVENT_START", null, null);
				startQuestTimer("KEBER_EVENT_BOSS_STAGE", getMsTimeForEventStage(2), null, null);
				break;
			}
			case "KEBER_EVENT_BOSS_STAGE":
			{
				changeNpcSpawn(3, false, false);
				for (int fieldType = 0; fieldType < 3; fieldType++)
				{
					final int summonedBossId = (getRandomBoolean() ? (fieldType == 0 ? KEBER_LOW : fieldType == 1 ? KEBER_MIDDLE : KEBER_HIGH) : (fieldType == 0 ? KISHAN : fieldType == 1 ? NOMA : SPALL));
					final Location spawnLoc = (fieldType == 0 ? PLAINS_OF_GLORY_BOSS : fieldType == 1 ? WAR_TORN_PLAINS_BOSS : SILENT_VALLEY_BOSS);
					addSpawn(summonedBossId, spawnLoc, false, 5 * 60 * 1000); // 5 minutes
				}
				cancelQuestTimer("KEBER_EVENT_BOSS_STAGE", null, null);
				startQuestTimer("KEBER_EVENT_END", getMsTimeForEventStage(3), null, null);
				break;
			}
			case "KEBER_EVENT_END":
			{
				for (Npc wo : World.getInstance().getVisibleObjects(player, Npc.class))
				{
					if (wo.getId() == KEBER_SEAL_STONE)
					{
						wo.setDisplayEffect(2); // remove effect
						for (Npc boss : World.getInstance().getVisibleObjects((wo), Npc.class))
						{
							if (BOSSES.contains(boss.getId()) && !boss.isAlikeDead())
							{
								Broadcast.toKnownPlayers((wo), new ExShowScreenMessage(BROADCAST_MESSAGES.get(EVENT_MESSAGE_WEAK), ExShowScreenMessage.TOP_CENTER, 5000));
								break;
							}
						}
					}
				}
				cancelQuestTimer("KEBER_EVENT_END", null, null);
				startQuestTimer("KEBER_EVENT_START", getMsTimeForEventStage(1), null, null);
				changeNpcSpawn(3, true, true);
				break;
			}
		}
		return null;
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		return "34216-01.html";
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		final int type = getFieldType(npc.getId());
		final boolean eventIsPending = (getQuestTimer("KEBER_EVENT_START", null, null) != null) && getQuestTimer("KEBER_EVENT_START", null, null).isActive();
		final boolean eventOnBossStage = (getQuestTimer("KEBER_EVENT_END", null, null) != null) && getQuestTimer("KEBER_EVENT_END", null, null).isActive();
		if ((type != -1) && (npc.getId() == NORMAL_MINIONS.get(type)))
		{
			if (eventIsPending || eventOnBossStage)
			{
				npc.getSpawn().stopRespawn();
				npc.deleteMe();
				return super.onSpawn(npc);
			}
			
			final int eliteMonsterId = ELITE_MINIONS.get(type);
			final int brainWashedMonsterId = BRAINWASHED_MINIONS.get(type);
			final int nearbyMonstersCount = World.getInstance().getVisibleObjects(npc, Npc.class).stream().filter(nearby -> ((nearby.getId() == NORMAL_MINIONS.get(type)) || (nearby.getId() == eliteMonsterId) || (nearby.getId() == brainWashedMonsterId))).toList().size();
			final int maxSpawnMonsters = MINION_MAX_COUNT.length() == 0 ? 0 : MINION_MAX_COUNT.get(type);
			if (nearbyMonstersCount >= maxSpawnMonsters)
			{
				npc.deleteMe();
				return super.onSpawn(npc);
			}
			final int killedCount = MINION_KILL_COUNTER.get(type);
			if (killedCount >= (ELITE_MONSTER_COUNT + BRAINWASHED_MONSTER_COUNT))
			{
				addSpawn(brainWashedMonsterId, npc.getLocation());
				npc.deleteMe();
			}
			else if (killedCount >= ELITE_MONSTER_COUNT)
			{
				addSpawn(eliteMonsterId, npc.getLocation());
				npc.deleteMe();
			}
		}
		else if (BOSSES.contains(npc.getId()))
		{
			// Broadcast to nearby players string which says "monster respawn"
			Broadcast.toKnownPlayers(npc, new ExShowScreenMessage(BROADCAST_MESSAGES.get(npc.getId()), ExShowScreenMessage.TOP_CENTER, 5000));
			World.getInstance().getVisibleObjects(npc, Player.class).stream().findAny().ifPresent(player -> addAttackPlayerDesire(npc, player));
		}
		return super.onSpawn(npc);
	}
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		if (NORMAL_MINIONS.contains(npc.getId()) || ELITE_MINIONS.contains(npc.getId()))
		{
			final int type = getFieldType(npc.getId());
			int count = MINION_KILL_COUNTER.get(type);
			if ((count >= ELITE_MONSTER_COUNT) && ELITE_MINIONS.contains(npc.getId()))
			{
				MINION_KILL_COUNTER.addAndGet(type, 1);
			}
			else if (count < ELITE_MONSTER_COUNT)
			{
				MINION_KILL_COUNTER.addAndGet(type, 1);
			}
		}
		else
		{
			final Set<ItemChanceHolder> monsterDrop = ((npc.getTemplate().getLevel() > (killer.getLevel() - 15)) && (npc.getTemplate().getLevel() < (killer.getLevel() + 15))) ? NPC_DROP.getOrDefault(npc.getId(), MINION_DROP) : new HashSet<>();
			for (ItemChanceHolder drop : monsterDrop)
			{
				if (getRandom(100) < drop.getChance())
				{
					if ((drop.getId() == 94481) && (killer.getClan() != null)) // Avoid to get clan points as item.
					{
						killer.getClan().addExp(killer.getObjectId(), (int) drop.getCount());
					}
					else
					{
						killer.addItem(_eventName, drop.getId(), drop.getCount(), null, true);
					}
				}
			}
			if (BOSSES.contains(npc.getId()))
			{
				changeNpcSpawn(getFieldType(npc.getId()), true, false);
				Broadcast.toKnownPlayers(killer, new ExShowScreenMessage(BROADCAST_MESSAGES.get(EVENT_MESSAGE_KILL), ExShowScreenMessage.TOP_CENTER, 10000, killer.getName()));
				killer.sendPacket(new ExShowScreenMessage(BROADCAST_MESSAGES.get(EVENT_MESSAGE_TO_KILLER), ExShowScreenMessage.TOP_CENTER, 5000));
				for (Npc crystal : World.getInstance().getVisibleObjects(npc, Npc.class))
				{
					if (crystal.getId() == KEBER_SEAL_STONE)
					{
						crystal.setDisplayEffect(2);
						break;
					}
				}
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	private long getMsTimeForEventStage(int type)
	{
		final Calendar returnTime = Calendar.getInstance();
		final long currentTime = System.currentTimeMillis();
		returnTime.set(Calendar.MILLISECOND, 0);
		returnTime.set(Calendar.SECOND, 0);
		if (type == 1)
		{
			returnTime.set(Calendar.MINUTE, 0);
			returnTime.add(Calendar.HOUR_OF_DAY, 1);
		}
		else if (type == 2)
		{
			returnTime.add(Calendar.MINUTE, 10);
		}
		else if (type == 3)
		{
			returnTime.add(Calendar.MINUTE, 5);
		}
		if (returnTime.getTimeInMillis() < currentTime)
		{
			returnTime.add(Calendar.DAY_OF_YEAR, 1);
		}
		return returnTime.getTimeInMillis() - currentTime;
	}
	
	/**
	 * Because value is set and we know all ids, it can be used to get fast access to any field monster.
	 * @param npcId
	 * @return 0 - Plains, 1 - War-Torn, 2 - Silent Valley
	 */
	private int getFieldType(int npcId)
	{
		if ((npcId == KEBER_LOW) || (npcId == KISHAN) || (npcId == NORMAL_MINIONS.get(0)) || (npcId == ELITE_MINIONS.get(0)) || (npcId == BRAINWASHED_MINIONS.get(0)))
		{
			return 0;
		}
		else if ((npcId == KEBER_MIDDLE) || (npcId == NOMA) || (npcId == NORMAL_MINIONS.get(1)) || (npcId == ELITE_MINIONS.get(1)) || (npcId == BRAINWASHED_MINIONS.get(1)))
		{
			return 1;
		}
		else if ((npcId == KEBER_HIGH) || (npcId == SPALL) || (npcId == NORMAL_MINIONS.get(2)) || (npcId == ELITE_MINIONS.get(2)) || (npcId == BRAINWASHED_MINIONS.get(2)))
		{
			return 2;
		}
		return -1;
	}
	
	private void changeNpcSpawn(int type, boolean delete, boolean removeAll)
	{
		if (removeAll)
		{
			SPAWN_TEMPLATE.get().getGroups().forEach(SpawnGroup::despawnAll);
		}
		
		for (WorldObject wo : World.getInstance().getVisibleObjects())
		{
			if (!wo.isMonster())
			{
				continue;
			}
			
			if (((type == 0) || (type == 3)) //
				&& ((!removeAll && (NORMAL_MINIONS.get(0) == wo.getId())) //
					|| (ELITE_MINIONS.get(0) == wo.getId()) //
					|| (BRAINWASHED_MINIONS.get(0) == wo.getId()) //
					|| (removeAll && ((KISHAN == wo.getId()) || (KEBER_LOW == wo.getId())))))
			{
				removeSpawn(wo, delete);
			}
			if (((type == 1) || (type == 3)) //
				&& ((!removeAll && (NORMAL_MINIONS.get(1) == wo.getId())) //
					|| (ELITE_MINIONS.get(1) == wo.getId()) //
					|| (BRAINWASHED_MINIONS.get(1) == wo.getId()) //
					|| (removeAll && ((NOMA == wo.getId()) || (KEBER_MIDDLE == wo.getId())))))
			{
				removeSpawn(wo, delete);
			}
			if (((type == 2) || (type == 3)) //
				&& ((!removeAll && (NORMAL_MINIONS.get(2) == wo.getId())) //
					|| (ELITE_MINIONS.get(2) == wo.getId()) //
					|| (BRAINWASHED_MINIONS.get(2) == wo.getId()) //
					|| (removeAll && ((SPALL == wo.getId()) || (KEBER_HIGH == wo.getId())))))
			{
				removeSpawn(wo, delete);
			}
		}
	}
	
	private void removeSpawn(WorldObject wo, boolean delete)
	{
		final Npc npc = ((Npc) wo);
		npc.getSpawn().stopRespawn();
		if (delete && (!BOSSES.contains(npc.getTemplate().getId()) || !npc.isAlikeDead()))
		{
			npc.deleteMe();
		}
	}
	
	public static void main(String[] args)
	{
		new BattleWithKeber();
	}
}