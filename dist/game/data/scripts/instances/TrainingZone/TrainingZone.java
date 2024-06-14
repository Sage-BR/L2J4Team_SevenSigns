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
package instances.TrainingZone;

import java.util.Arrays;

import org.l2j.gameserver.data.xml.TimedHuntingZoneData;
import org.l2j.gameserver.enums.ChatType;
import org.l2j.gameserver.model.Location;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.SkillHolder;
import org.l2j.gameserver.model.holders.TimedHuntingZoneHolder;
import org.l2j.gameserver.model.instancezone.Instance;
import org.l2j.gameserver.network.NpcStringId;
import org.l2j.gameserver.network.serverpackets.ExSendUIEvent;
import org.l2j.gameserver.network.serverpackets.ExShowScreenMessage;
import org.l2j.gameserver.network.serverpackets.huntingzones.TimedHuntingZoneExit;

import instances.AbstractInstance;

/**
 * @author Serenitty
 * @URL https://www.youtube.com/watch?v=SuRXhj79-rI
 */
public class TrainingZone extends AbstractInstance
{
	// Npcs
	private static final int TOKA = 34305;
	private static final int ERI = 34306;
	private static final int ARBANA = 34309;
	private static final int GROWN = 34307;
	private static final int TIND = 34308;
	
	// Locations
	private static final Location START_LOCATION = new Location(-49375, 17352, -3016);
	private static final Location FARM_COMMON_LOCATION = new Location(-56318, 13566, -3336);
	private static final Location FARM_TOI_LOCATION = new Location(-52432, 5415, -240);
	
	private static final Location FARM_CRUMA_LOCATION = new Location(-43474, 9254, 1136);
	
	private static final int TEMPLATE_ID = 228;
	private static final int HUNTING_ZONE_ID = 108;
	private static final long BOSS_SPAWN_DELAY = 5 * 600_000; // 50 mins
	private static final String SELECTED_ZONE_VAR = "EndTime";
	
	protected static final int[] NO_DELETE_BUFFS =
	{
		48200,
		48233,
		48235,
		48236,
		48483,
	};
	
	private final SkillHolder TRAINING_TIME_BUFF = new SkillHolder(48489, 1); // Sayha Sustentation
	private final SkillHolder BUFFS[] =
	{
		null,
		// Attack Buff
		new SkillHolder(48490, 1),
		new SkillHolder(48491, 1),
		new SkillHolder(48492, 1),
		new SkillHolder(48493, 1),
		new SkillHolder(48494, 1),
		new SkillHolder(48495, 1),
		new SkillHolder(48496, 1),
		new SkillHolder(48497, 1),
		new SkillHolder(48498, 1),
		new SkillHolder(48499, 1),
		new SkillHolder(48500, 1),
		new SkillHolder(48501, 1),
		// Def Buff
		new SkillHolder(48502, 1),
		new SkillHolder(48503, 1),
		new SkillHolder(48504, 1),
		new SkillHolder(48505, 1),
		new SkillHolder(48506, 1),
		new SkillHolder(48507, 1),
		new SkillHolder(48508, 1),
		new SkillHolder(48509, 1),
		new SkillHolder(48510, 1),
	};
	
	private final NpcStringId BOSS_SPAWN_ANNOUNCEMENT[] =
	{
		null, // 0
		NpcStringId.TANTA_LIZARDMAN_CHIEF_BOOJUDU_APPEARS, // 1
		NpcStringId.SEL_MAHUM_CHIEF_PETRON_APPEARS, // 2
		NpcStringId.MIGHTY_TUREK_ORC_WARRIOR_KERION_APPEARS, // 3
		NpcStringId.MIGHTY_TUREK_ORC_WARRIOR_FURIOUS_TUKHAK_APPEARS, // 4
		NpcStringId.INSOLENCE_S_HORROR_RILVA_APPEARS, // 5
		NpcStringId.INSOLENCE_S_HORROR_RILVA_APPEARS, // 6
		NpcStringId.INSOLENCE_S_HORROR_RILVA_APPEARS, // 7
		NpcStringId.INSOLENCE_S_HORROR_RILVA_APPEARS, // 8
		NpcStringId.INSOLENCE_S_ABYSS_RYUN_APPEARS, // 9
		NpcStringId.INSOLENCE_S_ABYSS_RYUN_APPEARS, // 10
		NpcStringId.INSOLENCE_S_GUARDIAN_CHEL_APPEARS, // 11
		NpcStringId.INSOLENCE_S_GUARDIAN_CHEL_APPEARS, // 12
		NpcStringId.INSOLENCE_S_GUARDIAN_CHEL_APPEARS, // 13
		NpcStringId.INSOLENCE_S_GUARDIAN_CHEL_APPEARS, // 14
		NpcStringId.INSOLENCE_S_GUARDIAN_CHEL_APPEARS, // 15
		NpcStringId.INSOLENCE_S_GUARDIAN_CHEL_APPEARS, // 16
		NpcStringId.CRUMA_GUARDIAN_SUSCEPTOR_PRIME_HAS_APPEARED, // 17
		NpcStringId.CRUMA_MANAGER_SUSCEPTOR_PRIMO_HAS_APPEARED, // 18
		NpcStringId.MUTATED_WATCHMAN_LAKARTA_HAS_APPEARED, // 19
		NpcStringId.EVOLUTION_MANAGER_BYKANT_HAS_APPEARED, // 20
		NpcStringId.ANCIENT_GIANT_QUEEN_HACEPSUTE_HAS_APPEARED, // 21
	};
	
	public TrainingZone()
	{
		super(TEMPLATE_ID);
		addInstanceLeaveId(TEMPLATE_ID);
		addFirstTalkId(TOKA, ERI, GROWN, TIND, ARBANA);
		addTalkId(TOKA, ERI, GROWN, TIND, ARBANA);
		addCreatureSeeId(GROWN);
		
		addInstanceCreatedId(TEMPLATE_ID);
		addInstanceEnterId(TEMPLATE_ID);
		addInstanceDestroyId(TEMPLATE_ID);
		addInstanceLeaveId(TEMPLATE_ID);
		
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		if (event.startsWith("ENTER"))
		{
			final int zoneId = Integer.parseInt(event.split(" ")[1]);
			final TimedHuntingZoneHolder huntingZone = TimedHuntingZoneData.getInstance().getHuntingZone(zoneId);
			if (huntingZone == null)
			{
				return null;
			}
			
			enterInstance(player, npc, huntingZone.getInstanceId());
			
		}
		if (event.startsWith("npc_talk"))
		{
			final Instance world = npc.getInstanceWorld();
			if (world != null)
			{
				npc.broadcastSay(ChatType.NPC_SHOUT, "I can summon mobs for the training!");
				startQuestTimer(event, 11000, npc, player);
			}
			
		}
		if (npc != null)
		{
			final Instance instance = npc.getInstanceWorld();
			if (npc.getId() == TOKA)
			{
				if (event.equals("34305-1.html"))
				{
					return event;
				}
				else if (event.startsWith("get_buff"))
				{
					final int index = Integer.parseInt(event.split(" ")[1]);
					if ((index < 1) || (index > 12))
					{
						return null;
					}
					
					BUFFS[index].getSkill().applyEffects(npc, player);
				}
			}
			else if (npc.getId() == ERI)
			{
				if (event.equals("34306-1.html"))
				{
					return event;
				}
				else if (event.startsWith("get_buff"))
				{
					final int index = Integer.parseInt(event.split(" ")[1]);
					if ((index < 13) || (index > 21))
					{
						return null;
					}
					
					BUFFS[index].getSkill().applyEffects(npc, player);
				}
			}
			else if (npc.getId() == GROWN)
			{
				if (event.equals("34307-tower_of_insolence.html") || event.equals("34307-giant_cave.html"))
				{
					return event;
				}
				else if (event.startsWith("choose_zone"))
				{
					final int index = Integer.parseInt(event.split(" ")[1]);
					if ((index < 1) || (index > 21))
					{
						return null;
					}
					
					instance.getParameters().set(SELECTED_ZONE_VAR, index);
					for (int i = 1; i < 21; i++)
					{
						instance.despawnGroup("Mobs_" + i);
					}
					instance.spawnGroup("Mobs_" + index);
					
					return "34307-1.html";
				}
			}
			else if (npc.getId() == TIND)
			{
				if (event.equals("teleport_to_farm_zone"))
				{
					final int index = instance.getParameters().getInt(SELECTED_ZONE_VAR);
					if (index < 0)
					{
						return null;
					}
					
					if ((index >= 5) && (index <= 16))
					{
						player.teleToLocation(FARM_TOI_LOCATION);
					}
					else if ((index > 16) && (index <= 21))
					{
						player.teleToLocation(FARM_CRUMA_LOCATION);
					}
					else
					{
						player.teleToLocation(FARM_COMMON_LOCATION);
					}
				}
				else if (event.equals("teleport_to_start_zone"))
				{
					player.teleToLocation(START_LOCATION);
				}
			}
		}
		else
		{
			if (event.equals("SPAWN_BOSS"))
			{
				if ((player == null) || (player.getInstanceWorld() == null) || !player.isOnline())
				{
					return null;
				}
				
				final Instance instance = player.getInstanceWorld();
				final int index = instance.getParameters().getInt(SELECTED_ZONE_VAR);
				if ((index < 1) || (index > 21))
				{
					return null;
				}
				
				instance.spawnGroup("Raid_" + index).forEach(m -> m.getSpawn().stopRespawn());
				instance.broadcastPacket(new ExShowScreenMessage(BOSS_SPAWN_ANNOUNCEMENT[index], 2, 10000, true));
			}
		}
		return null;
	}
	
	@Override
	public void onInstanceCreated(Instance instance, Player player)
	{
		startQuestTimer("SPAWN_BOSS", BOSS_SPAWN_DELAY, null, player, false);
	}
	
	@Override
	public void onInstanceEnter(Player player, Instance instance)
	{
		TRAINING_TIME_BUFF.getSkill().applyEffects(player, player);
		player.sendPacket(new ExSendUIEvent(player, false, false, (int) (instance.getRemainingTime() / 1000), 0, NpcStringId.TIME_LEFT));
	}
	
	@Override
	public void onInstanceDestroy(Instance instance)
	{
		cancelQuestTimer("SPAWN_BOSS", null, null);
	}
	
	@Override
	public void onInstanceLeave(Player player, Instance instance)
	{
		player.sendPacket(new ExSendUIEvent(player, true, false, (int) (instance.getRemainingTime() / 1000), 0, NpcStringId.TIME_LEFT));
		removeBuffs(player);
		
		player.sendPacket(new TimedHuntingZoneExit(HUNTING_ZONE_ID));
	}
	
	@Override
	public String onCreatureSee(Npc npc, Creature creature)
	{
		if (creature.isPlayer())
		{
			final Instance world = creature.getInstanceWorld();
			final Npc grown = world.getNpc(GROWN);
			startQuestTimer("npc_talk", 2000, grown, null);
		}
		return super.onCreatureSee(npc, creature);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		final Instance instance = npc.getInstanceWorld();
		switch (npc.getId())
		{
			case TIND:
			{
				if (instance.getParameters().getInt(SELECTED_ZONE_VAR, -1) < 0)
				{
					return "34308-no_zone.html";
				}
				if (npc.calculateDistance3D(START_LOCATION) < 500)
				{
					return "34308-outside.html";
				}
				return "34308-inside.html";
			}
		}
		return npc.getId() + ".html";
	}
	
	private void removeBuffs(Creature ch)
	{
		ch.getEffectList().stopEffects(info -> (info != null) && !info.getSkill().isStayAfterDeath() && (Arrays.binarySearch(NO_DELETE_BUFFS, info.getSkill().getId()) < 0), true, true);
	}
	
	public static void main(String[] args)
	{
		new TrainingZone();
	}
}
