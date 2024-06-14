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
package instances.DreamDungeon.CatGuildsLair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.l2jmobius.commons.util.Rnd;
import org.l2jmobius.gameserver.instancemanager.InstanceManager;
import org.l2jmobius.gameserver.model.Location;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.events.impl.instance.OnInstanceStatusChange;
import org.l2jmobius.gameserver.model.instancezone.Instance;
import org.l2jmobius.gameserver.model.instancezone.InstanceTemplate;
import org.l2jmobius.gameserver.model.spawns.SpawnGroup;
import org.l2jmobius.gameserver.model.spawns.SpawnTemplate;
import org.l2jmobius.gameserver.network.NpcStringId;
import org.l2jmobius.gameserver.network.serverpackets.ExShowScreenMessage;

import instances.AbstractInstance;
import instances.DreamDungeon.BenedictsMonastery.BenedictsMonastery;
import instances.DreamDungeon.DraconidFortress.DraconidFortress;
import instances.DreamDungeon.GustavsManor.GustavsManor;
import instances.DreamDungeon.VentusTemple.VentusTemple;

/**
 * @author Index
 */
public class CatGuildsLair extends AbstractInstance
{
	public static final int CREATED = 1000;
	public static final int TALK_WITH_TELEPORTER_CAT = 1001;
	public static final int KILL_FOUR_DREAM_WATCHERS_INSIDE = 1002;
	public static final int TALK_WITH_ELVEN_ELDER_CAT = 1003;
	public static final int BOSS_FIGHT = 1004;
	public static final int FINISH_INSTANCE = 1005;
	
	private static final int[] INSTANCE_IDs =
	{
		GustavsManor.INSTANCE_ID,
		BenedictsMonastery.INSTANCE_ID,
		VentusTemple.INSTANCE_ID,
		DraconidFortress.INSTANCE_ID,
	};
	
	private static final int[] MONSTERS =
	{
		22407, // Cat Fighter
		22408, // Cat Mage
		22409, // Cat Warrior
		22410, // Cat Warlock
	};
	
	private static final int FELINE_KING = 18686;
	private static final int FELINE_QUEEN = 18687;
	
	private static final String DREAM_WATCHER_COUNTER = "DREAM_WATCHER_COUNTER";
	
	private static final NpcStringId DREAM_WATCHER = NpcStringId.DREAM_WATCHER;
	private static final NpcStringId STRING_ID_01 = NpcStringId.THE_CAT_GUILD_S_KING_IS_IN_TROUBLE_PLEASE_HELP_HIM;
	private static final NpcStringId STRING_ID_02 = NpcStringId.HELP_OUR_GUILD_PLEASE_I_WILL_TELL_YOU_WHAT_S_GOING_ON;
	private static final NpcStringId STRING_ID_03 = NpcStringId.WHAT_S_THE_FUSS_THOSE_WILLING_TO_ADDRESS_THE_KING_COME_TO_ME;
	private static final NpcStringId STRING_ID_04 = NpcStringId.WHAT_YOU_ARE_GOING_TO_GIVE_ADVICES_TO_OUR_KING_HE_LL_BE_SO_MAD;
	private static final NpcStringId STRING_ID_05 = NpcStringId.IS_SOMETHING_WRONG_WITH_ME_THEN_ADVISE_ME;
	private static final NpcStringId STRING_ID_06 = NpcStringId.WHEW_AS_I_THOUGHT_THE_FALL_HAS_STOPPED;
	
	private static final Map<String, List<SpawnGroup>> CAT_LAIR_SPAWN_GROUPS = new HashMap<>();
	
	private static InstanceTemplate CAT_LAIR_INSTANCE;
	private static Location START_LOCATION;
	
	public CatGuildsLair()
	{
		super(INSTANCE_IDs);
		addKillId(MONSTERS);
		addKillId(FELINE_KING, FELINE_QUEEN);
		addInstanceEnterId(INSTANCE_IDs);
		setInstanceStatusChangeId(this::onInstanceStatusChange, INSTANCE_IDs);
		init();
	}
	
	public void onInstanceStatusChange(OnInstanceStatusChange event)
	{
		final Instance instance = event.getWorld();
		final int status = event.getStatus();
		if ((instance == null) || (status < CREATED))
		{
			return;
		}
		
		switch (status)
		{
			case CREATED:
			{
				instance.setDuration(60); // update time for destroy instance
				sendScreenMessage(instance, STRING_ID_01);
				break;
			}
			case TALK_WITH_TELEPORTER_CAT:
			{
				instance.getPlayers().forEach(player -> player.teleToLocation(START_LOCATION, false));
				instance.getParameters().set(DREAM_WATCHER_COUNTER, 0);
				spawnNpcFromGroup(instance, "CatLair_Npc_01");
				break;
			}
			case KILL_FOUR_DREAM_WATCHERS_INSIDE:
			{
				spawnNpcFromGroup(instance, "CatLair_Monsters_01");
				spawnNpcFromGroup(instance, "CatLair_Monsters_02");
				spawnNpcFromGroup(instance, "CatLair_Monsters_04");
				setRandomTitles(instance, false);
				break;
			}
			case TALK_WITH_ELVEN_ELDER_CAT:
			{
				spawnNpcFromGroup(instance, "CatLair_Npc_02");
				sendScreenMessage(instance, STRING_ID_03);
				final Npc teleporterCat = instance.getNpc(instances.DreamDungeon.CatGuildsLair.TeleporterCat.TELEPORTER_CAT_NPC_ID);
				startQuestTimer("ADDITIONAL_WAVE", 10_000, teleporterCat, null, false);
				break;
			}
			case BOSS_FIGHT:
			{
				instance.despawnGroup("CatLair_Npc_02");
				spawnBoss(instance);
				break;
			}
			case FINISH_INSTANCE:
			{
				instance.setDuration(5);
				sendScreenMessage(instance, STRING_ID_06);
				spawnNpcFromGroup(instance, "CatLair_Npc_03");
				break;
			}
		}
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		final Instance instance = npc.getInstanceWorld();
		if ((instance == null) || (instance.getStatus() < CREATED))
		{
			return null;
		}
		
		if (event.equalsIgnoreCase("ADDITIONAL_WAVE"))
		{
			spawnNpcFromGroup(instance, "CatLair_Monsters_03");
			setRandomTitles(instance, true);
		}
		
		return super.onEvent(event, npc, player);
	}
	
	private void spawnBoss(Instance instance)
	{
		final boolean random = Rnd.nextBoolean();
		// show text
		final NpcStringId bossText = random ? STRING_ID_04 : STRING_ID_05;
		sendScreenMessage(instance, bossText);
		// spawn boss
		final String bossTemplate = random ? "catLair_Boss_FelineKing" : "catLair_Boss_FelineQueen";
		spawnNpcFromGroup(instance, bossTemplate);
	}
	
	private void setRandomTitles(Instance instance, boolean isAdditionalWave)
	{
		final List<Npc> selectedMonsters = new CopyOnWriteArrayList<>();
		final List<Npc> instanceNpc = new CopyOnWriteArrayList<>(instance.getNpcs(MONSTERS));
		if (isAdditionalWave)
		{
			final String npc3 = "CatLair_Monsters_03";
			selectedMonsters.addAll(getNpcListByGroupName(new CopyOnWriteArrayList<>(instanceNpc), npc3));
		}
		else
		{
			Collections.shuffle(instanceNpc);
			final String npc1 = "CatLair_Monsters_01";
			final String npc2 = "CatLair_Monsters_02";
			final String npc4 = "CatLair_Monsters_04";
			final List<Npc> monsterNpc1 = new CopyOnWriteArrayList<>(getNpcListByGroupName(new CopyOnWriteArrayList<>(instanceNpc), npc1));
			final List<Npc> monsterNpc2 = new CopyOnWriteArrayList<>(getNpcListByGroupName(new CopyOnWriteArrayList<>(instanceNpc), npc2));
			final List<Npc> monsterNpc4 = new CopyOnWriteArrayList<>(getNpcListByGroupName(new CopyOnWriteArrayList<>(instanceNpc), npc4));
			for (int index = 0; index < 2; index++)
			{
				final Npc monster = monsterNpc1.stream().findAny().orElse(null);
				selectedMonsters.add(monster);
				monsterNpc1.remove(monster);
			}
			for (int index = 0; index < 2; index++)
			{
				final Npc monster = monsterNpc2.stream().findAny().orElse(null);
				selectedMonsters.add(monster);
				monsterNpc2.remove(monster);
			}
			for (int index = 0; index < 2; index++)
			{
				final Npc monster = monsterNpc4.stream().findAny().orElse(null);
				selectedMonsters.add(monster);
				monsterNpc4.remove(monster);
			}
		}
		for (Npc monster : selectedMonsters)
		{
			monster.setTitleString(DREAM_WATCHER);
			monster.broadcastInfo();
		}
	}
	
	private List<Npc> getNpcListByGroupName(CopyOnWriteArrayList<Npc> npcList, String groupName)
	{
		final List<Npc> newList = new ArrayList<>();
		for (Npc monster : npcList)
		{
			if (isMonsterIsOneOfSpawnGroup(monster, groupName))
			{
				newList.add(monster);
			}
		}
		return newList;
	}
	
	private static boolean isMonsterIsOneOfSpawnGroup(Npc npc, String group)
	{
		return (npc != null) && (npc.getSpawn() != null) && (npc.getSpawn().getNpcSpawnTemplate() != null) && (npc.getSpawn().getNpcSpawnTemplate().getGroup() != null) && (npc.getSpawn().getNpcSpawnTemplate().getGroup().getName() != null) && npc.getSpawn().getNpcSpawnTemplate().getGroup().getName().equalsIgnoreCase(group);
	}
	
	private void spawnNpcFromGroup(Instance instance, String group)
	{
		for (SpawnGroup spawnGroup : CAT_LAIR_SPAWN_GROUPS.get(group))
		{
			spawnGroup.spawnAll(instance);
		}
	}
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		if (killer == null)
		{
			return super.onKill(npc, killer, isSummon);
		}
		
		final Instance instance = killer.getInstanceWorld();
		if ((instance == null) || (instance.getStatus() < CREATED))
		{
			return super.onKill(npc, killer, isSummon);
		}
		
		switch (instance.getStatus())
		{
			case KILL_FOUR_DREAM_WATCHERS_INSIDE:
			{
				if (instance.getAliveNpcs(MONSTERS).isEmpty())
				{
					instance.setStatus(TALK_WITH_ELVEN_ELDER_CAT);
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
						instance.broadcastPacket(new ExShowScreenMessage(NpcStringId.DREAM_WATCHER_S_DEATH_1_4, ExShowScreenMessage.BOTTOM_RIGHT, 10000, false));
						break;
					}
					case 2:
					{
						instance.broadcastPacket(new ExShowScreenMessage(NpcStringId.DREAM_WATCHER_S_DEATH_2_4, ExShowScreenMessage.BOTTOM_RIGHT, 10000, false));
						break;
					}
					case 3:
					{
						instance.broadcastPacket(new ExShowScreenMessage(NpcStringId.DREAM_WATCHER_S_DEATH_3_4, ExShowScreenMessage.BOTTOM_RIGHT, 10000, false));
						break;
					}
					case 4:
					{
						instance.setStatus(TALK_WITH_ELVEN_ELDER_CAT);
						break;
					}
				}
				break;
			}
			case BOSS_FIGHT:
			{
				if ((npc.getTemplate().getId() == FELINE_KING) || (npc.getTemplate().getId() == FELINE_QUEEN))
				{
					instance.setStatus(FINISH_INSTANCE);
				}
				break;
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public void onInstanceEnter(Player player, Instance instance)
	{
		final int status = instance.getStatus();
		if (status < CREATED)
		{
			return;
		}
		
		player.teleToLocation(START_LOCATION, false);
		
		super.onInstanceEnter(player, instance);
	}
	
	public static void sendScreenMessage(Instance instance, NpcStringId stringId)
	{
		instance.broadcastPacket(new ExShowScreenMessage(stringId, ExShowScreenMessage.TOP_CENTER, 10000, true));
	}
	
	public static boolean calculateChanceForCatLair(Instance instance)
	{
		if (Rnd.get(100) < 25)
		{
			instance.setStatus(CREATED);
			instance.spawnGroup("CatLair_TeleporterCat");
			sendScreenMessage(instance, Rnd.nextBoolean() ? STRING_ID_01 : STRING_ID_02);
			return true;
		}
		
		return false;
	}
	
	public static void startCatLairInstance(Player player)
	{
		final Instance instance = player.getInstanceWorld();
		instance.setStatus(TALK_WITH_TELEPORTER_CAT);
		player.teleToLocation(START_LOCATION, false);
	}
	
	private void init()
	{
		CAT_LAIR_INSTANCE = InstanceManager.getInstance().getInstanceTemplate(999999);
		START_LOCATION = CAT_LAIR_INSTANCE.getEnterLocation();
		for (SpawnTemplate grp : CAT_LAIR_INSTANCE.getSpawns())
		{
			for (SpawnGroup spawnGroup : grp.getGroups())
			{
				CAT_LAIR_SPAWN_GROUPS.computeIfAbsent(spawnGroup.getName(), v -> new ArrayList<>()).add(spawnGroup);
			}
		}
	}
	
	public static void main(String[] args)
	{
		new CatGuildsLair();
	}
}
