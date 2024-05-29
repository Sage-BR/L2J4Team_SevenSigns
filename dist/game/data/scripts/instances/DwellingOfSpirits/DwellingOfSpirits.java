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
package instances.DwellingOfSpirits;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.l2j.commons.util.Rnd;
import org.l2j.gameserver.enums.TeleportWhereType;
import org.l2j.gameserver.model.Location;
import org.l2j.gameserver.model.Party;
import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.instancezone.Instance;
import org.l2j.gameserver.model.zone.ZoneType;
import org.l2j.gameserver.network.NpcStringId;
import org.l2j.gameserver.network.serverpackets.ExSendUIEvent;
import org.l2j.gameserver.network.serverpackets.ExShowScreenMessage;
import org.l2j.gameserver.network.serverpackets.OnEventTrigger;

import instances.AbstractInstance;

/**
 * @author Serenitty
 */
public class DwellingOfSpirits extends AbstractInstance
{
	private static final int PORTAL_OPEN_CHANCE = 40;
	
	// CRITICAL NPCs
	private static final int RUIP = 22273;
	private static final int FAIRY_C1 = 22271;
	private static final int FAIRY_C2 = 22272;
	private static final int SEALSTONE = 34178;
	private static final int ANIMAEL = 34176;
	private static final int PORTAL_EVENT_FIRE = 15969;
	private static final int PORTAL_EVENT_WATER = 15970;
	private static final int PORTAL_EVENT_EARTH = 15972;
	private static final int PORTAL_EVENT_WIND = 15971;
	
	private static final int KING_PROCELLA = 29107;
	private static final int KING_PETRAM = 29108;
	private static final int KING_IGNIS = 29105;
	private static final int KING_NEBULA = 29106;
	
	// Entrace Portal Triggers
	private static final int WIND_FIRST_TRIGGER_1 = 16158880;
	private static final int WIND_FIRST_TRIGGER_2 = 16158882;
	private static final int EARTH_FIRST_TRIGGER_1 = 16156660;
	private static final int EARTH_FIRST_TRIGGER_2 = 16156662;
	private static final int FIRE_FIRST_TRIGGER_1 = 16155550;
	private static final int FIRE_FIRST_TRIGGER_2 = 16155552;
	private static final int WATER_FIRST_TRIGGER_1 = 16157770;
	private static final int WATER_FIRST_TRIGGER_2 = 16157772;
	
	// @formatter:off
	private static final int[][] PORTAL_TRIGGER_IDS = 
	{
	    { 16158880, 16158882 },
	    { 16156660, 16156662 },
	    { 16155550, 16155552 },
	    { 16157770, 16157772 }
	};
	
	private static final Location IGNIS_FIRE_L = new Location(202374, 168153, -15485);
	private static final Location PETRAM_EARTH_L = new Location(222081, 190538, -15485);
	private static final Location NEBULA_WATER_L = new Location(222149, 168087, -15485);
	private static final Location PROCELLA_WIN_L = new Location(212884, 178847, -15485);
	
	private static final Location PETRAM_EARTH_RETURN_L = new Location(-114321, -77262, -11445);
	private static final Location IGNIS_FIRE_RETURN_L = new Location(-114322, -77262, -11445);
	private static final Location NEBULA_WATER_RETURN_L = new Location(-114323, -77262, -11445);
	private static final Location PROCELLA_WIN_RETURN_L = new Location(-114324, -77262, -11445);
	
	private static final int[][][] portalConfigs = 
	{
	    {{EARTH_FIRST_TRIGGER_1, EARTH_FIRST_TRIGGER_2, KING_PETRAM}},
	    {{FIRE_FIRST_TRIGGER_1, FIRE_FIRST_TRIGGER_2, KING_IGNIS}},
	    {{WATER_FIRST_TRIGGER_1, WATER_FIRST_TRIGGER_2, KING_NEBULA}},
	    {{WIND_FIRST_TRIGGER_1, WIND_FIRST_TRIGGER_2, KING_PROCELLA}}
	};

	private static final int[][] portalSpawnCoordinates = 
	{
	    {222063, 191514, -15486, 50142},  
	    {202350, 169121, -15484, 48103},  
	    {222127, 169057, -15486, 48730},  
	    {212862, 179828, -15489, 48103}   
	};
	// @formatter:on
	
	private static final int TEMPLATE_ID = 214;
	
	private static final Map<Integer, NpcStringId> PORTAL_MSG = new HashMap<>();
	static
	{
		PORTAL_MSG.put(0, NpcStringId.DIMENSIONAL_DOOR_TO_THE_SPIRIT_OF_EARTH_IS_OPEN);
		PORTAL_MSG.put(1, NpcStringId.DIMENSIONAL_DOOR_TO_THE_SPIRIT_OF_FIRE_IS_OPEN);
		PORTAL_MSG.put(2, NpcStringId.DIMENSIONAL_DOOR_TO_THE_SPIRIT_OF_WATER_IS_OPEN);
		PORTAL_MSG.put(3, NpcStringId.DIMENSIONAL_DOOR_TO_THE_SPIRIT_OF_WIND_IS_OPEN);
		PORTAL_MSG.put(4, NpcStringId.SEAL_STONE_DISAPPEARS_AFTER_RESONATING_WITH_THE_STATUE);
	}
	
	// Zone
	private static final int DWELLING_CENTRAL = 21400000;
	
	private static final int IGNIS_PORTAL_ENTER_ID = 202201;
	private static final int PETRAM_PORTAL_ENTER_ID = 202202;
	private static final int NEBULA_PORTAL_ENTER_ID = 202203;
	private static final int PROCELLA_PORTAL_ENTER_ID = 202204;
	
	// Portals return IDs
	private static final int IGNIS_PORTAL_RETURN_ID = 202205;
	private static final int PETRAM_PORTAL_RETURN_ID = 202206;
	private static final int NEBULA_PORTAL_RETURN_ID = 202207;
	private static final int PROCELLA_PORTAL_RETURN_ID = 202208;
	
	public DwellingOfSpirits()
	{
		super(TEMPLATE_ID);
		addKillId(KING_PETRAM, KING_NEBULA, KING_PROCELLA, KING_IGNIS);
		addKillId(FAIRY_C1, FAIRY_C2, RUIP);
		addAttackId(KING_PETRAM, KING_NEBULA, KING_PROCELLA, KING_IGNIS);
		addSpawnId(KING_NEBULA);
		addFirstTalkId(ANIMAEL, SEALSTONE);
		addTalkId(ANIMAEL);
		addStartNpc(SEALSTONE);
		addCreatureSeeId(ANIMAEL);
		addEnterZoneId(DWELLING_CENTRAL);
		addEnterZoneId(IGNIS_PORTAL_ENTER_ID, IGNIS_PORTAL_RETURN_ID);
		addEnterZoneId(PETRAM_PORTAL_ENTER_ID, PETRAM_PORTAL_RETURN_ID);
		addEnterZoneId(NEBULA_PORTAL_ENTER_ID, NEBULA_PORTAL_RETURN_ID);
		addEnterZoneId(PROCELLA_PORTAL_ENTER_ID, PROCELLA_PORTAL_RETURN_ID);
		addInstanceEnterId(TEMPLATE_ID);
		addInstanceLeaveId(TEMPLATE_ID);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		switch (event)
		{
			case "ENTER":
			{
				if (player.isInParty())
				{
					final Party party = player.getParty();
					final boolean isInCC = party.isInCommandChannel();
					final List<Player> members = (isInCC) ? party.getCommandChannel().getMembers() : party.getMembers();
					for (Player member : members)
					{
						if (!member.isInsideRadius3D(npc, 1000))
						{
							player.sendMessage("Player " + member.getName() + " must go closer to Parme.");
						}
						enterInstance(member, npc, TEMPLATE_ID);
					}
				}
				else if (player.isGM())
				{
					enterInstance(player, npc, TEMPLATE_ID);
					player.sendMessage("SYS: You have entered as GM/Admin to Dwelling.");
				}
				else
				{
					if (!player.isInsideRadius3D(npc, 1000))
					{
						player.sendMessage("You must go closer to Parme.");
					}
					enterInstance(player, npc, TEMPLATE_ID);
				}
				break;
			}
			case "TELEPORT":
			{
				final Instance instance = player.getInstanceWorld();
				if (instance != null)
				{
					instance.finishInstance();
					player.teleToLocation(TeleportWhereType.TOWN, null);
				}
			}
		}
		
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onKill(Npc npc, Player player, boolean isSummon)
	{
		final Instance instance = npc.getInstanceWorld();
		final StatSet worldParameters = instance.getParameters();
		
		int totalBossDefeatCount = worldParameters.getInt("totalBossDefeatCount", 0);
		
		switch (npc.getId())
		{
			case RUIP:
			case FAIRY_C1:
			case FAIRY_C2:
			{
				if (Rnd.get(100) <= PORTAL_OPEN_CHANCE)
				{
					int portalId = Rnd.get(4);
					boolean isPortalOpened = worldParameters.getBoolean("portal" + portalId + "Opened", false);
					
					if (!isPortalOpened)
					{
						if (instance.getStatus() > totalBossDefeatCount)
						{
							return super.onKill(npc, player, isSummon);
						}
						instance.setStatus(instance.getStatus() + 1);
						openPortal(player, portalId, instance);
						worldParameters.set("portal" + portalId + "Opened", true);
					}
				}
				break;
			}
			
			case KING_PETRAM:
			case KING_PROCELLA:
			case KING_IGNIS:
			case KING_NEBULA:
			{
				spawnExitPortal(instance, player, npc.getId());
				CheckGeneralResidenceStages(npc, instance);
				
				break;
			}
		}
		
		return super.onKill(npc, player, isSummon);
	}
	
	@Override
	public String onEnterZone(Creature creature, ZoneType zone)
	{
		Instance instance = creature.getInstanceWorld();
		if ((instance != null) && creature.isPlayer())
		{
			
			final StatSet worldParameters = instance.getParameters();
			
			if (zone.getId() == DWELLING_CENTRAL)
			{
				worldParameters.set("InsideResidence", true);
				handleCentralZone(instance, worldParameters);
			}
			
			int portalId = getPortalIdByZone(zone.getId());
			Location[] locations = getLocationsForPortal(portalId);
			
			if ((portalId >= 0) && (locations[0] != null))
			{
				boolean isPortalOpened = worldParameters.getBoolean("portal" + portalId + "Opened", false);
				boolean isPortalExitOpened = worldParameters.getBoolean("portalExit" + portalId, false);
				boolean InsideResidence = worldParameters.getBoolean("InsideResidence", false);
				
				if (isPortalOpened && (InsideResidence))
				{
					creature.teleToLocation(locations[0], instance);
					worldParameters.set("InsideResidence", false);
				}
				
				if (isPortalExitOpened && (!InsideResidence))
				{
					creature.teleToLocation(locations[1], instance);
				}
			}
			
		}
		return null;
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		if (npc.getId() == ANIMAEL)
		{
			return "34176.htm";
		}
		
		if (!player.getInstanceWorld().getParameters().getBoolean("PlayerEnter", false))
		{
			player.getInstanceWorld().setParameter("PlayerEnter", true);
			player.getInstanceWorld().setDuration(30);
			startEvent(npc, player);
		}
		
		return super.onFirstTalk(npc, player);
	}
	
	private void openPortal(Player player, int id, Instance instance)
	{
		
		if ((id >= 0) && (id < portalSpawnCoordinates.length))
		{
			int[] coordinates = portalSpawnCoordinates[id];
			int trigger1 = portalConfigs[id][0][0];
			int trigger2 = portalConfigs[id][0][1];
			int kingId = portalConfigs[id][0][2];
			
			if ((id >= 0) && (id < PORTAL_MSG.size()))
			{
				NpcStringId portalMessage = PORTAL_MSG.get(id);
				player.sendPacket(new ExShowScreenMessage(2, -1, 2, 0, 0, 0, 0, true, 8000, false, null, portalMessage, null));
			}
			addSpawn(kingId, coordinates[0], coordinates[1], coordinates[2], coordinates[3], false, 0, false, instance.getId());
			
			instance.setParameter("TRIGGER_1_" + id, trigger1);
			instance.setParameter("TRIGGER_2_" + id, trigger2);
			
			instance.broadcastPacket(new OnEventTrigger(trigger1, true));
			instance.broadcastPacket(new OnEventTrigger(trigger2, true));
			
		}
	}
	
	private void spawnExitPortal(Instance instance, Player player, int bossId)
	{
		Npc portal;
		int portalId;
		Location portalLocation;
		
		final StatSet worldParameters = instance.getParameters();
		worldParameters.set("portalExit" + getPortalByBossId(bossId), true);
		
		switch (bossId)
		{
			case KING_PROCELLA:
			{
				portalId = PORTAL_EVENT_WIND;
				portalLocation = new Location(212863, 181090, -15487);
				break;
			}
			case KING_PETRAM:
			{
				portalId = PORTAL_EVENT_EARTH;
				portalLocation = new Location(222065, 192767, -15488);
				break;
			}
			case KING_IGNIS:
			{
				portalId = PORTAL_EVENT_FIRE;
				portalLocation = new Location(202349, 170533, -15488);
				break;
			}
			case KING_NEBULA:
			{
				portalId = PORTAL_EVENT_WATER;
				portalLocation = new Location(222127, 170488, -15488);
				break;
			}
			default:
			{
				return;
			}
		}
		portal = addSpawn(portalId, portalLocation, false, 0, false, instance.getId());
		
		if (portal != null)
		{
			portal.setDisplayEffect(1);
		}
		
	}
	
	private int getPortalByBossId(int bossId)
	{
		switch (bossId)
		{
			case KING_PETRAM:
			{
				return 0;
			}
			case KING_IGNIS:
			{
				return 1;
			}
			case KING_NEBULA:
			{
				return 2;
			}
			case KING_PROCELLA:
			{
				return 3;
			}
			default:
				return -1;
		}
	}
	
	private int getPortalIdByZone(int zoneId)
	{
		switch (zoneId)
		{
			case PETRAM_PORTAL_ENTER_ID:
			case PETRAM_PORTAL_RETURN_ID:
			{
				return 0;
			}
			case IGNIS_PORTAL_ENTER_ID:
			case IGNIS_PORTAL_RETURN_ID:
			{
				return 1;
			}
			case NEBULA_PORTAL_ENTER_ID:
			case NEBULA_PORTAL_RETURN_ID:
			{
				return 2;
			}
			case PROCELLA_PORTAL_ENTER_ID:
			case PROCELLA_PORTAL_RETURN_ID:
			{
				return 3;
			}
			default:
			{
				return -1;
			}
		}
	}
	
	private Location[] getLocationsForPortal(int portalId)
	{
		Location[] locations = new Location[2];
		
		switch (portalId)
		{
			case 0:
			{
				locations[0] = PETRAM_EARTH_L;
				locations[1] = PETRAM_EARTH_RETURN_L;
				break;
			}
			case 1:
			{
				locations[0] = IGNIS_FIRE_L;
				locations[1] = IGNIS_FIRE_RETURN_L;
				break;
			}
			case 2:
			{
				locations[0] = NEBULA_WATER_L;
				locations[1] = NEBULA_WATER_RETURN_L;
				break;
			}
			case 3:
			{
				locations[0] = PROCELLA_WIN_L;
				locations[1] = PROCELLA_WIN_RETURN_L;
				break;
			}
		}
		
		return locations;
	}
	
	private void CheckGeneralResidenceStages(Npc npc, Instance instance)
	{
		final StatSet worldParameters = instance.getParameters();
		
		int portalId = getPortalByBossId(npc.getId());
		
		if ((portalId >= 0) && (portalId < portalConfigs.length))
		{
			String bossDefeatCountKey = "bossOfPortal_" + portalId + "_defeat_count";
			int currentDefeatCount = worldParameters.getInt(bossDefeatCountKey, 0);
			int newDefeatCount = currentDefeatCount + 1;
			
			worldParameters.set(bossDefeatCountKey, newDefeatCount);
		}
		
		int totalBossDefeatCount = 0;
		
		for (int i = 0; i < portalConfigs.length; i++)
		{
			String bossDefeatCountKey = "bossOfPortal_" + i + "_defeat_count";
			totalBossDefeatCount += worldParameters.getInt(bossDefeatCountKey, 0);
		}
		
		worldParameters.set("totalBossDefeatCount", totalBossDefeatCount);
		
		if (totalBossDefeatCount >= 4)
		{
			instance.spawnGroup("animael");
			instance.despawnGroup("ruipwave_1");
			instance.despawnGroup("NormalMobs");
		}
	}
	
	private void handleCentralZone(Instance instance, StatSet worldParameters)
	{
		for (int id = 0; id < PORTAL_TRIGGER_IDS.length; id++)
		{
			int trigger1 = worldParameters.getInt("TRIGGER_1_" + id, -1);
			int trigger2 = worldParameters.getInt("TRIGGER_2_" + id, -1);
			
			if ((trigger1 != -1) && (trigger2 != -1))
			{
				instance.broadcastPacket(new OnEventTrigger(trigger1, true));
				instance.broadcastPacket(new OnEventTrigger(trigger2, true));
			}
		}
	}
	
	private void startEvent(Npc npc, Player player)
	{
		final Instance instance = player.getInstanceWorld();
		instance.setParameter("Running", true);
		player.getInstanceWorld().broadcastPacket(new ExSendUIEvent(player, false, false, (int) (instance.getRemainingTime() / 1000), 0, NpcStringId.TIME_LEFT));
		player.sendPacket(new ExShowScreenMessage(2, -1, 2, 0, 0, 0, 0, true, 8000, false, null, PORTAL_MSG.get(4), null));
		instance.spawnGroup("ruipwave_1");
		instance.spawnGroup("NormalMobs");
		
		if (npc.getId() == SEALSTONE)
		{
			instance.despawnGroup("sealstone");
		}
		
	}
	
	@Override
	public void onInstanceEnter(Player player, Instance instance)
	{
		boolean Running = instance.getParameters().getBoolean("Running", false);
		if ((instance.getRemainingTime() > 0) && Running)
		{
			player.sendPacket(new ExSendUIEvent(player, false, false, (int) (instance.getRemainingTime() / 1000), 0, NpcStringId.TIME_LEFT));
		}
	}
	
	@Override
	public void onInstanceLeave(Player player, Instance instance)
	{
		
		player.sendPacket(new ExSendUIEvent(player, false, false, 0, 0, NpcStringId.TIME_LEFT));
	}
	
	public static void main(String[] args)
	{
		new DwellingOfSpirits();
	}
}
