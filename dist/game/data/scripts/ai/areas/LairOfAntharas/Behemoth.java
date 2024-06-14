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
package ai.areas.LairOfAntharas;

import java.util.Calendar;

import org.l2j.commons.threads.ThreadPool;
import org.l2j.gameserver.ai.CtrlIntention;
import org.l2j.gameserver.data.SpawnTable;
import org.l2j.gameserver.data.xml.NpcData;
import org.l2j.gameserver.enums.MountType;
import org.l2j.gameserver.instancemanager.DBSpawnManager;
import org.l2j.gameserver.instancemanager.ZoneManager;
import org.l2j.gameserver.model.Location;
import org.l2j.gameserver.model.Spawn;
import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.templates.NpcTemplate;
import org.l2j.gameserver.model.holders.SkillHolder;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.model.skill.SkillCaster;
import org.l2j.gameserver.model.zone.ZoneType;
import org.l2j.gameserver.network.NpcStringId;
import org.l2j.gameserver.network.serverpackets.Earthquake;
import org.l2j.gameserver.network.serverpackets.ExShowScreenMessage;
import org.l2j.gameserver.network.serverpackets.PlaySound;

import ai.AbstractNpcAI;

/**
 * Behemoth AI
 * @URL https://l2central.info/essence/locations/special_zones/antharas_lair/
 */
public class Behemoth extends AbstractNpcAI
{
	// NPC
	private static final int BEHEMOTH = 25912;
	private static final int ULTIMATE_BEHEMOTH = 25935;
	// Location
	private static final ZoneType ZONE = ZoneManager.getInstance().getZoneByName("antharas_lair");
	private static final Location LOCATION = new Location(148386, 117952, -3715);
	// skills
	private static final SkillHolder ANTI_STRIDER = new SkillHolder(4258, 1); // Hinder Strider
	private static final SkillHolder BEHEMOTH_ATTACK = new SkillHolder(48081, 1);
	private static final int BEHEMOTH_ATTACK_SKILL_MIN_COOLTIME = 30000; // 30 seconds
	private static final SkillHolder BEHEMOTH_FEAR = new SkillHolder(48082, 1);
	private static final int BEHEMOTH_FEAR_SKILL_MIN_COOLTIME = 33000; // 33 seconds
	private long _lastBehemothAttackSkillTime;
	private long _lastBehemothFearSkillTime;
	
	private Behemoth()
	{
		addKillId(BEHEMOTH, ULTIMATE_BEHEMOTH);
		addAttackId(BEHEMOTH, ULTIMATE_BEHEMOTH);
		
		// Schedule reset everyday from 22:00 till 22:30
		final long currentTime = System.currentTimeMillis();
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 22);
		calendar.set(Calendar.MINUTE, getRandom(0, 0));
		calendar.set(Calendar.SECOND, getRandom(0, 0));
		if (calendar.getTimeInMillis() < currentTime)
		{
			calendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		
		final long calendarTime = calendar.getTimeInMillis();
		final long startDelay = Math.max(0, calendarTime - currentTime);
		// LOGGER.info(getClass().getSimpleName() + ": Loaded: Behemoth will spawn at: " + startDelay);
		ThreadPool.scheduleAtFixedRate(this::onSpawn, startDelay, 86400000); // 86400000 = 1 day
	}
	
	private void onSpawn()
	{
		boolean spawnStatus = false;
		for (Spawn spawn : SpawnTable.getInstance().getSpawns(BEHEMOTH))
		{
			for (Npc monster : spawn.getSpawnedNpcs())
			{
				if (monster.isDead())
				{
					DBSpawnManager.getInstance().deleteSpawn(spawn, true);
				}
				else
				{
					spawnStatus = true;
				}
			}
		}
		
		if (!spawnStatus)
		{
			final NpcTemplate template = NpcData.getInstance().getTemplate(BEHEMOTH);
			if (template != null)
			{
				try
				{
					final Spawn spawn = new Spawn(template);
					spawn.setXYZ(LOCATION);
					spawn.setRespawnDelay(86400000);
					DBSpawnManager.getInstance().addNewSpawn(spawn, true);
					if (ZONE != null)
					{
						ZONE.broadcastPacket(new ExShowScreenMessage(NpcStringId.ANTHARAS_AVATAR_BEHEMOTH_APPEARED, 2, 5000));
						ZONE.broadcastPacket(new ExShowScreenMessage("Behemoth has spawned!", 5000));
						ZONE.broadcastPacket(new Earthquake(LOCATION, 40, 10));
						ZONE.broadcastPacket(new PlaySound("BS02_A"));
					}
					else
					{
						LOGGER.warning(getClass().getSimpleName() + ": Zone is not initialized properly!");
					}
					startQuestTimer("CHECK_ATTACK", 60000, null, null);
				}
				catch (Exception e)
				{
					LOGGER.warning(getClass().getSimpleName() + ": Problem with onSpawn! " + e.getMessage());
				}
			}
		}
	}
	
	@Override
	public String onAttack(Npc npc, Player attacker, int damage, boolean isSummon, Skill skill)
	{
		if ((npc.getId() == BEHEMOTH) || (npc.getId() == ULTIMATE_BEHEMOTH))
		{
			if ((attacker.getMountType() == MountType.STRIDER) && !attacker.isAffectedBySkill(ANTI_STRIDER.getSkillId()) && !npc.isSkillDisabled(ANTI_STRIDER.getSkill()))
			{
				npc.setTarget(attacker);
				npc.doCast(ANTI_STRIDER.getSkill());
			}
			
			manageSkills(npc);
		}
		
		return super.onAttack(npc, attacker, damage, isSummon, skill);
	}
	
	private void manageSkills(Npc npc)
	{
		if (npc.isCastingNow() || npc.isCoreAIDisabled() || !npc.isInCombat())
		{
			return;
		}
		
		SkillHolder skillToCast = null;
		if (npc.getId() == BEHEMOTH)
		{
			if (((_lastBehemothAttackSkillTime + BEHEMOTH_ATTACK_SKILL_MIN_COOLTIME) < System.currentTimeMillis()))
			{
				skillToCast = BEHEMOTH_ATTACK;
				_lastBehemothAttackSkillTime = System.currentTimeMillis();
			}
			else if (((_lastBehemothFearSkillTime + BEHEMOTH_FEAR_SKILL_MIN_COOLTIME) < System.currentTimeMillis()))
			{
				_lastBehemothFearSkillTime = System.currentTimeMillis();
				skillToCast = BEHEMOTH_FEAR;
			}
		}
		
		if ((skillToCast != null) && SkillCaster.checkUseConditions(npc, skillToCast.getSkill()))
		{
			npc.getAI().setIntention(CtrlIntention.AI_INTENTION_CAST, skillToCast.getSkill(), npc);
		}
	}
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		if ((npc.getId() == BEHEMOTH) && (getRandom(100) <= 15))
		{
			final NpcTemplate template = NpcData.getInstance().getTemplate(ULTIMATE_BEHEMOTH);
			if (template != null)
			{
				try
				{
					final Spawn spawn = new Spawn(template);
					Location locationWhereBehemothDie = npc.getLocation();
					spawn.setXYZ(locationWhereBehemothDie);
					spawn.setRespawnDelay(86400000);
					DBSpawnManager.getInstance().addNewSpawn(spawn, true);
					if (ZONE != null)
					{
						ZONE.broadcastPacket(new ExShowScreenMessage(NpcStringId.ANTHARAS_AVATAR_ULTIMATE_BEHEMOTH_APPEARED, 2, 5000));
						ZONE.broadcastPacket(new ExShowScreenMessage("Ultimate Behemoth has spawned!", 5000));
						ZONE.broadcastPacket(new Earthquake(npc.getX(), npc.getY(), npc.getZ(), 40, 10));
						ZONE.broadcastPacket(new PlaySound("BS02_A"));
					}
					else
					{
						LOGGER.warning(getClass().getSimpleName() + ": Zone is not initialized properly!");
					}
				}
				catch (final Exception e)
				{
					LOGGER.warning(getClass().getSimpleName() + ": Problem with onSpawn! " + e.getMessage());
				}
			}
		}
		
		if (npc.getId() == ULTIMATE_BEHEMOTH)
		{
			for (Spawn spawn : SpawnTable.getInstance().getSpawns(ULTIMATE_BEHEMOTH))
			{
				for (Npc monster : spawn.getSpawnedNpcs())
				{
					if (monster.isDead())
					{
						DBSpawnManager.getInstance().deleteSpawn(spawn, true);
					}
				}
			}
		}
		
		return super.onKill(npc, killer, isSummon);
	}
	
	public static void main(String[] args)
	{
		new Behemoth();
	}
}
