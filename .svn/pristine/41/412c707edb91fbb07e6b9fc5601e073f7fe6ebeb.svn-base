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
package events.HolidayOutOfSchedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.l2jmobius.commons.time.SchedulingPattern;
import org.l2jmobius.gameserver.model.Location;
import org.l2jmobius.gameserver.model.World;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.ItemHolder;
import org.l2jmobius.gameserver.model.holders.SkillHolder;
import org.l2jmobius.gameserver.model.quest.LongTimeEvent;
import org.l2jmobius.gameserver.model.skill.SkillCaster;
import org.l2jmobius.gameserver.model.skill.SkillCastingType;
import org.l2jmobius.gameserver.network.NpcStringId;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.serverpackets.ExShowScreenMessage;
import org.l2jmobius.gameserver.network.serverpackets.MagicSkillLaunched;
import org.l2jmobius.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Index
 */
public class HolidayOutOfSchedule extends LongTimeEvent
{
	// NPCs
	private static final int RUDOLPH_HUMANIZED_NPC_ID = 34338;
	private static final int LARGE_CHRISTMAS_TREE_NPC_ID = 34278;
	private static final int SANTA_CLAUS_NPC_ID = 34339;
	// Skill
	private static final SkillHolder TREE_SKILL = new SkillHolder(48596, 1);
	// Items
	private static final Set<ItemHolder> SANTA_GIFT = new HashSet<>();
	static
	{
		SANTA_GIFT.add(new ItemHolder(97147, 1)); // Dye Powder - Sealed
		SANTA_GIFT.add(new ItemHolder(71856, 1)); // L-Coin Pouch
		SANTA_GIFT.add(new ItemHolder(96731, 1)); // Giant's Shining Relic Pouch (Time-limited)
		SANTA_GIFT.add(new ItemHolder(72359, 1)); // New Year Gift Box (Time-limited)
		SANTA_GIFT.add(new ItemHolder(94269, 10)); // Scroll: Boost Attack - Sealed
		SANTA_GIFT.add(new ItemHolder(94271, 10)); // Scroll: Boost Defense - Sealed
	}
	// Locations
	private static final Map<String, Location[]> SANTA_LOCATION = new HashMap<>();
	static
	{
		SANTA_LOCATION.put("CRUMA", new Location[]
		{
			new Location(17294, 114772, -3440, 27293), // Santa Claus - Gift Manager
			new Location(17144, 114731, -3440, 0), // 2015 Christmas Tree, Large
			new Location(17146, 114176, -3440, 0), // Player Teleport
		});
		SANTA_LOCATION.put("SILENT_VALLEY", new Location[]
		{
			new Location(183910, 46823, -5952, 9784), // Santa Claus - Gift Manager
			new Location(184009, 46713, -5952, 0), // 2015 Christmas Tree, Large
			new Location(184222, 46666, -5936, 0), // Player Teleport
		});
		SANTA_LOCATION.put("LIZARDMAN_PLAINS", new Location[]
		{
			new Location(79446, 74089, -3192, 3355), // Santa Claus - Gift Manager
			new Location(79276, 74018, -3192, 0), // 2015 Christmas Tree, Large
			new Location(81160, 75068, -3592, 0), // Player Teleport
		});
		SANTA_LOCATION.put("DRAGON_VALLEY_WEST", new Location[]
		{
			new Location(118854, 116910, -3720, 45575), // Santa Claus - Gift Manager
			new Location(118924, 117063, -3720, 0), // 2015 Christmas Tree, Large
			new Location(119029, 116799, -3728, 0), // Player Teleport
		});
		SANTA_LOCATION.put("DRAGON_VALLEY_EAST", new Location[]
		{
			new Location(84847, 109373, -3200, 45344), // Santa Claus - Gift Manager
			new Location(84938, 109535, -3200, 0), // 2015 Christmas Tree, Large
			new Location(85021, 109046, -3200, 0), // Player Teleport
		});
		SANTA_LOCATION.put("TOI", new Location[]
		{
			new Location(112980, 10122, -5144, 9574), // Santa Claus - Gift Manager
			new Location(112830, 10009, -5128, 0), // 2015 Christmas Tree, Large
			new Location(114649, 11115, -5120, 0), // Player Teleport
		});
		SANTA_LOCATION.put("ORC_BARRAKS", new Location[]
		{
			new Location(-91975, 106440, -3680, 8528), // Santa Claus - Gift Manager
			new Location(-92132, 106468, -3672, 0), // 2015 Christmas Tree, Large
			new Location(-91509, 107149, -3688, 0), // Player Teleport
		});
		SANTA_LOCATION.put("RAKSHAS", new Location[]
		{
			new Location(-48112, 139994, -2920, 34492), // Santa Claus - Gift Manager
			new Location(-48073, 139840, -2904, 0), // 2015 Christmas Tree, Large
			new Location(-48363, 140230, -2944, 0), // Player Teleport
		});
		SANTA_LOCATION.put("GIANT_CAVE", new Location[]
		{
			new Location(177391, 50459, -3984, 17216), // Santa Claus - Gift Manager
			new Location(177391, 50337, -3984, 0), // 2015 Christmas Tree, Large
			new Location(178024, 52164, -3984, 0), // Player Teleport
		});
		SANTA_LOCATION.put("HOT_SPIRITS", new Location[]
		{
			new Location(149537, -112910, -2064, 18106), // Santa Claus - Gift Manager
			new Location(149466, -113060, -2064, 0), // 2015 Christmas Tree, Large
			new Location(149370, -112869, -2064, 0), // Player Teleport
		});
		SANTA_LOCATION.put("GORDE_CANYON", new Location[]
		{
			new Location(164359, -49245, -3536, 22516), // Santa Claus - Gift Manager
			new Location(164443, -49394, -3536, 0), // 2015 Christmas Tree, Large
			new Location(164389, -47956, -3528, 0), // Player Teleport
		});
		SANTA_LOCATION.put("XILENOS_FORTRESS", new Location[]
		{
			new Location(128157, -40225, -3504, 60759), // Santa Claus - Gift Manager
			new Location(128026, -40054, -3520, 0), // 2015 Christmas Tree, Large
			new Location(127226, -40970, -3584, 0), // Player Teleport
		});
		SANTA_LOCATION.put("MORGOD_MILITARY_BASE", new Location[]
		{
			new Location(145493, -68714, -3656, 2058), // Santa Claus - Gift Manager
			new Location(145356, -68603, -3656, 0), // 2015 Christmas Tree, Large
			new Location(146782, -68944, -3656, 0), // Player Teleport
		});
	}
	// Misc
	private static final Set<Npc> TREE_NPCs = new HashSet<>();
	private static final Npc[] SCHEDULE_NPCS = new Npc[2];
	private static boolean _santaActive = false;
	private static String _santaLocation = null;
	
	private HolidayOutOfSchedule()
	{
		if (!isEventPeriod())
		{
			return;
		}
		
		addFirstTalkId(RUDOLPH_HUMANIZED_NPC_ID, SANTA_CLAUS_NPC_ID);
		addTalkId(RUDOLPH_HUMANIZED_NPC_ID, SANTA_CLAUS_NPC_ID);
		addSpawnId(RUDOLPH_HUMANIZED_NPC_ID, LARGE_CHRISTMAS_TREE_NPC_ID, SANTA_CLAUS_NPC_ID);
		startQuestTimer("BUFF_AROUND_PLAYERS", 7_000, null, null, true);
		despawnSanta();
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		if (npc != null)
		{
			switch (npc.getId())
			{
				case RUDOLPH_HUMANIZED_NPC_ID:
				{
					switch (event)
					{
						case "34338-01.htm":
						{
							return event;
						}
						case "BACK":
						{
							return getRudolphDialogue();
						}
						case "GOTO_SANTA":
						{
							if (_santaActive)
							{
								player.teleToLocation(SANTA_LOCATION.get(_santaLocation)[2], false);
							}
							return RUDOLPH_HUMANIZED_NPC_ID + "-0" + (_santaActive ? "2" : "3") + ".htm";
						}
					}
					break;
				}
				case SANTA_CLAUS_NPC_ID:
				{
					if (event.equalsIgnoreCase("GET_SANTA_GIFT"))
					{
						final int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
						if (player.getAccountVariables().getInt(getClass().getSimpleName() + "_SANTA_GIFT", 0) != currentDay)
						{
							SANTA_GIFT.forEach(g -> player.addItem("Santa Gift", g, npc, true));
							player.getAccountVariables().set(getClass().getSimpleName() + "_SANTA_GIFT", currentDay);
						}
						else
						{
							player.sendPacket(new SystemMessage(SystemMessageId.THIS_ACCOUNT_HAS_ALREADY_RECEIVED_A_GIFT_THE_GIFT_CAN_ONLY_BE_GIVEN_ONCE_PER_ACCOUNT));
						}
						return SANTA_CLAUS_NPC_ID + "-01.htm";
					}
				}
			}
		}
		else
		{
			if (event.equalsIgnoreCase("BUFF_AROUND_PLAYERS"))
			{
				for (Npc tree : TREE_NPCs)
				{
					final List<Player> aroundPlayers = World.getInstance().getVisibleObjectsInRange(tree, Player.class, 300);
					if (aroundPlayers.isEmpty())
					{
						continue;
					}
					aroundPlayers.forEach(p ->
					{
						SkillCaster.triggerCast(tree, p, TREE_SKILL.getSkill());
						p.sendPacket(new MagicSkillLaunched(tree, TREE_SKILL.getSkillId(), TREE_SKILL.getSkillLevel(), SkillCastingType.NORMAL, new HashSet<>(aroundPlayers)));
					});
				}
			}
			else if (event.equalsIgnoreCase("SANTA_SPAWN"))
			{
				activateSanta();
			}
			else if (event.equalsIgnoreCase("SANTA_DESPAWN"))
			{
				despawnSanta();
			}
		}
		return super.onEvent(event, npc, player);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		return npc.getId() == RUDOLPH_HUMANIZED_NPC_ID ? getRudolphDialogue() : npc.getId() + "-00.htm";
	}
	
	private String getRudolphDialogue()
	{
		return RUDOLPH_HUMANIZED_NPC_ID + "-00_" + (_santaActive ? "b" : "a") + ".htm";
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		if (npc.getId() == LARGE_CHRISTMAS_TREE_NPC_ID)
		{
			TREE_NPCs.add(npc);
		}
		return super.onSpawn(npc);
	}
	
	private void activateSanta()
	{
		final long currentTime = System.currentTimeMillis();
		final SchedulingPattern endTimer = new SchedulingPattern("0 12,22 * * *");
		if (_santaActive)
		{
			final SchedulingPattern respawnTimer = new SchedulingPattern("0 11,21 * * *");
			if (endTimer.next(currentTime) > respawnTimer.next(currentTime))
			{
				despawnSanta();
			}
			startQuestTimer("SANTA_DESPAWN", endTimer.next(currentTime) - currentTime, null, null);
			return;
		}
		
		_santaActive = true;
		
		final List<String> locations = new ArrayList<>(SANTA_LOCATION.keySet());
		Collections.shuffle(locations);
		_santaLocation = locations.stream().findAny().orElse("ORC_BARRAKS");
		
		SCHEDULE_NPCS[0] = addSpawn(SANTA_CLAUS_NPC_ID, SANTA_LOCATION.get(_santaLocation)[0]); // Remove Santa.
		SCHEDULE_NPCS[1] = addSpawn(LARGE_CHRISTMAS_TREE_NPC_ID, SANTA_LOCATION.get(_santaLocation)[1]); // Remove tree.
		TREE_NPCs.add(SCHEDULE_NPCS[1]);
		
		World.getInstance().getPlayers().forEach(p -> p.sendPacket(new ExShowScreenMessage(NpcStringId.SANTA_CLAUS_HAS_ARRIVED_ON_THE_ADEN_TERRITORY_FIND_RUDOLPH_IN_THE_CITY_HE_WILL_SEND_YOU_TO_SANTA, ExShowScreenMessage.TOP_CENTER, 10000, true)));
		startQuestTimer("SANTA_DESPAWN", endTimer.next(currentTime) - currentTime, null, null);
	}
	
	private void despawnSanta()
	{
		final long currentTime = System.currentTimeMillis();
		final SchedulingPattern respawnTimer = new SchedulingPattern("0 11,21 * * *");
		if (SCHEDULE_NPCS[0] != null)
		{
			if (SCHEDULE_NPCS[1] != null)
			{
				// Remove tree.
				TREE_NPCs.remove(SCHEDULE_NPCS[1]);
				SCHEDULE_NPCS[1].deleteMe();
				SCHEDULE_NPCS[1] = null;
			}
			// Remove Santa.
			_santaActive = false;
			SCHEDULE_NPCS[0].deleteMe();
			SCHEDULE_NPCS[0] = null;
		}
		_santaLocation = null;
		startQuestTimer("SANTA_SPAWN", respawnTimer.next(currentTime) - currentTime, null, null, false);
	}
	
	public static void main(String[] args)
	{
		new HolidayOutOfSchedule();
	}
}
