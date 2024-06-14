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
package ai.areas.KelbimFortress;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.l2j.commons.threads.ThreadPool;
import org.l2j.commons.util.Rnd;
import org.l2j.commons.util.TimeUtil;
import org.l2j.gameserver.data.xml.SkillData;
import org.l2j.gameserver.enums.Movie;
import org.l2j.gameserver.instancemanager.ZoneManager;
import org.l2j.gameserver.model.Location;
import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.quest.Event;
import org.l2j.gameserver.model.skill.BuffInfo;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.model.skill.SkillCaster;
import org.l2j.gameserver.model.zone.type.ScriptZone;
import org.l2j.gameserver.network.NpcStringId;
import org.l2j.gameserver.network.serverpackets.ExShowScreenMessage;
import org.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import org.l2j.gameserver.util.Util;

public class KelbimRaid extends Event
{
	private static final int KELBIM_STAGE_1 = 29206;
	private static final int KELBIM_STAGE_2 = 29205;
	private static final int NPC_TELEPORT_TO_STAGE_2 = 18829;
	private static final int NPC_GUARD_STAGE_2 = 18851;
	private static final int[] GUARDS =
	{
		18853,
		18854,
		18855,
		18856,
		18857,
		18858,
		18859
	};
	private static final int DOOR_1 = 18190002;
	private static final int DOOR_2 = 18190004;
	private static final int TELEPORT_OVERLOAD = 48690;
	private static final Location[] TELEPORTS =
	{
		new Location(-54707, 60038, -269),
		new Location(-55374, 59126, -269),
		new Location(-56814, 59018, -269)
	};
	private static final Location LOCATION_STAGE_2 = new Location(-56184, 60534, -269, 53739);
	private static final List<Npc> _spawnedGuardsList = new ArrayList<>();
	private static final List<Npc> _spawnedNpcList = new ArrayList<>();
	private static final ScriptZone _zoneStage1 = ZoneManager.getInstance().getZoneById(60024, ScriptZone.class);
	private static final ScriptZone _zoneStage2 = ZoneManager.getInstance().getZoneById(60023, ScriptZone.class);
	private static int _stageGuardCount = 0;
	private static boolean _tryChangeStage = false;
	
	public KelbimRaid()
	{
		addKillId(KELBIM_STAGE_1, KELBIM_STAGE_2);
		addAttackId(KELBIM_STAGE_1, KELBIM_STAGE_2);
		addFirstTalkId(NPC_TELEPORT_TO_STAGE_2);
		addSpawnId(NPC_GUARD_STAGE_2);
		final Calendar start = TimeUtil.getCloseNextDay(Calendar.FRIDAY, 22, 0);
		ThreadPool.scheduleAtFixedRate(() -> eventStart(null), start.getTimeInMillis() - System.currentTimeMillis(), 86400000);
		// LOGGER.info("KelbimRaid start in - " + start.getTime());
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		if (event.equalsIgnoreCase("start_stage_2"))
		{
			playMovie(_zoneStage2.getPlayersInside(), Movie.SC_KELBIM_OPENING);
			startQuestTimer("spawnKelbimStage2", 15000, npc, player);
		}
		else if (event.equalsIgnoreCase("spawnKelbimStage2"))
		{
			final Npc spawned = addSpawn(KELBIM_STAGE_2, LOCATION_STAGE_2);
			_spawnedNpcList.add(spawned);
		}
		else if (event.startsWith("teleport_to_"))
		{
			final BuffInfo buffInfo = player.getEffectList().getBuffInfoBySkillId(TELEPORT_OVERLOAD);
			if (buffInfo != null)
			{
				int priceTeleport = 2500000;
				if (buffInfo.getSkill().getLevel() < 40)
				{
					priceTeleport *= buffInfo.getSkill().getLevel();
				}
				if (player.getAdena() >= priceTeleport)
				{
					player.reduceAdena("KelbimRaid", priceTeleport, null, true);
					final int level = Math.min(40, buffInfo.getSkill().getLevel());
					final Skill skill = SkillData.getInstance().getSkill(TELEPORT_OVERLOAD, level);
					skill.applyEffects(player, player);
				}
				else
				{
					return null;
				}
			}
			else
			{
				final Skill skill = SkillData.getInstance().getSkill(TELEPORT_OVERLOAD, 1);
				skill.applyEffects(player, player);
			}
			player.teleToLocation(TELEPORTS[Integer.parseInt(event.split("_")[2])]);
		}
		else if (event.equalsIgnoreCase("FINISH"))
		{
			eventStop();
		}
		else if (event.startsWith("CAST_SKILL_"))
		{
			final int skillId = Integer.parseInt(event.substring(11));
			final Skill skill = SkillData.getInstance().getSkill(skillId, 1);
			if ((skill != null) && SkillCaster.checkUseConditions(npc, skill))
			{
				npc.doCast(skill);
			}
		}
		return super.onEvent(event, npc, player);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		final String htmltext = null;
		final NpcHtmlMessage packet = new NpcHtmlMessage(npc.getObjectId());
		packet.setHtml(getHtm(player, npc.getId() + ".htm"));
		String cost = "";
		final BuffInfo buffInfo = player.getEffectList().getBuffInfoBySkillId(TELEPORT_OVERLOAD);
		if (buffInfo != null)
		{
			int priceTeleport = 2500000;
			if (buffInfo.getSkill().getLevel() < 40)
			{
				priceTeleport *= buffInfo.getSkill().getLevel();
				cost = "Cost: " + priceTeleport + " adena.<br>";
			}
		}
		packet.replace("%cost%", cost);
		player.sendPacket(packet);
		return htmltext;
	}
	
	@Override
	public String onAttack(Npc npc, Player attacker, int damage, boolean isSummon)
	{
		if (npc.getId() == KELBIM_STAGE_1)
		{
			final double result = npc.getMaxHp() - npc.getCurrentHp();
			double percentage = (result / npc.getMaxHp()) * 100;
			percentage = 100 - percentage;
			if ((_stageGuardCount < 6) || !_tryChangeStage)
			{
				if (((_stageGuardCount == 0) && (percentage < 95.0)) || ((_stageGuardCount == 1) && (percentage < 85.0)) || ((_stageGuardCount == 2) && (percentage < 75.0)) || ((_stageGuardCount == 3) && (percentage < 65.0)) || ((_stageGuardCount == 4) && (percentage < 55.0)) || ((_stageGuardCount == 5) && (percentage < 45.0)))
				{
					_stageGuardCount++;
					spawnGuards(npc);
				}
				else if ((percentage < 40.0) && !_tryChangeStage)
				{
					_tryChangeStage = true;
					if (Rnd.get(100) < 50)
					{
						_zoneStage1.broadcastPacket(new ExShowScreenMessage(NpcStringId.FOLLOW_ME_TO_THE_ROOF_AND_I_LL_SHOW_YOU_MY_TRUE_POWER, ExShowScreenMessage.TOP_CENTER, 7000, true));
						npc.deleteMe();
						final Npc spawned = addSpawn(NPC_TELEPORT_TO_STAGE_2, new Location(-55050, 56166, -1871));
						_spawnedNpcList.add(spawned);
						startQuestTimer("start_stage_2", 60000, null, attacker);
						_stageGuardCount = 0;
						despawnNpcs(_spawnedGuardsList);
					}
				}
			}
		}
		else if (npc.getId() == KELBIM_STAGE_2)
		{
			final double result = npc.getMaxHp() - npc.getCurrentHp();
			double percentage = (result / npc.getMaxHp()) * 100;
			percentage = 100 - percentage;
			if (_stageGuardCount < 12)
			{
				if (((_stageGuardCount == 0) && (percentage < 95.0)) || ((_stageGuardCount == 1) && (percentage < 88.0)) || ((_stageGuardCount == 2) && (percentage < 74.0)) || ((_stageGuardCount == 3) && (percentage < 67.0)) || ((_stageGuardCount == 4) && (percentage < 60.0)) || ((_stageGuardCount == 5) && (percentage < 53.0)) || ((_stageGuardCount == 6) && (percentage < 39.0)) || ((_stageGuardCount == 7) && (percentage < 32.0)) || ((_stageGuardCount == 8) && (percentage < 25.0)) || ((_stageGuardCount == 9) && (percentage < 18.0)) || ((_stageGuardCount == 10) && (percentage < 11.0)) || ((_stageGuardCount == 11) && (percentage < 4.0)))
				{
					_stageGuardCount++;
					final Location loc = Util.getRandomPosition(npc.getLocation(), 300, 400);
					Npc guard = addSpawn(NPC_GUARD_STAGE_2, loc, false, 10000);
					_spawnedGuardsList.add(guard);
				}
			}
		}
		return super.onAttack(npc, attacker, damage, isSummon);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		if (npc.getId() == NPC_GUARD_STAGE_2)
		{
			int skillId;
			switch (_stageGuardCount)
			{
				case 1:
				case 6:
				{
					skillId = 48665;
					break;
				}
				case 2:
				case 7:
				{
					skillId = 48666;
					break;
				}
				case 3:
				case 8:
				{
					skillId = 48667;
					break;
				}
				case 4:
				case 9:
				{
					skillId = 48668;
					break;
				}
				default:
				{
					skillId = 48664;
				}
			}
			startQuestTimer("CAST_SKILL_" + skillId, 2000, npc, null);
		}
		
		return super.onSpawn(npc);
	}
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		if ((npc.getId() == KELBIM_STAGE_1) || (npc.getId() == KELBIM_STAGE_2))
		{
			startQuestTimer("FINISH", 5 * 60000, null, null);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public boolean eventStart(Player eventMaker)
	{
		openDoor(DOOR_1, 0);
		openDoor(DOOR_2, 0);
		_spawnedNpcList.add(addSpawn(KELBIM_STAGE_1, new Location(-55041, 53953, -2072), false));
		return true;
	}
	
	@Override
	public boolean eventStop()
	{
		despawnNpcs(_spawnedNpcList);
		despawnNpcs(_spawnedGuardsList);
		_stageGuardCount = 0;
		closeDoor(DOOR_1, 0);
		closeDoor(DOOR_2, 0);
		_tryChangeStage = false;
		return true;
	}
	
	@Override
	public boolean eventBypass(Player player, String bypass)
	{
		return true;
	}
	
	private void despawnNpcs(List<Npc> npcList)
	{
		for (Npc npc : npcList)
		{
			if (npc != null)
			{
				npc.deleteMe();
			}
		}
	}
	
	private void spawnGuards(Npc npc)
	{
		final int id = getRandomEntry(GUARDS);
		final Location loc = Util.getRandomPosition(npc.getLocation(), 70, 110);
		final Npc guard = addSpawn(id, loc, false);
		_spawnedGuardsList.add(guard);
	}
	
	public static void main(String[] args)
	{
		new KelbimRaid();
	}
}
