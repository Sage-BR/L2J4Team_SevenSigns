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
package ai.bosses.Antharas;

import java.util.Calendar;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.l2jmobius.commons.util.CommonUtil;
import org.l2jmobius.gameserver.ai.CtrlIntention;
import org.l2jmobius.gameserver.data.xml.NpcData;
import org.l2jmobius.gameserver.enums.MountType;
import org.l2jmobius.gameserver.geoengine.GeoEngine;
import org.l2jmobius.gameserver.instancemanager.GlobalVariablesManager;
import org.l2jmobius.gameserver.instancemanager.GrandBossManager;
import org.l2jmobius.gameserver.instancemanager.ZoneManager;
import org.l2jmobius.gameserver.model.Location;
import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.actor.Attackable;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.actor.instance.GrandBoss;
import org.l2jmobius.gameserver.model.holders.SkillHolder;
import org.l2jmobius.gameserver.model.skill.Skill;
import org.l2jmobius.gameserver.model.skill.SkillCaster;
import org.l2jmobius.gameserver.model.zone.type.NoRestartZone;
import org.l2jmobius.gameserver.network.NpcStringId;
import org.l2jmobius.gameserver.network.serverpackets.Earthquake;
import org.l2jmobius.gameserver.network.serverpackets.ExShowScreenMessage;
import org.l2jmobius.gameserver.network.serverpackets.PlaySound;
import org.l2jmobius.gameserver.util.Broadcast;
import org.l2jmobius.gameserver.util.Util;

import ai.AbstractNpcAI;

/**
 * Antharas World Raid
 * @URL https://l2central.info/essence/articles/835.html?lang=en
 */
public class Antharas extends AbstractNpcAI
{
	protected static final int[][] STAGES_NPCS =
	{
		// Stage 1
		{
			29151, // Antharas
			29148, // Tarask Dragon
		},
		// Stage 2
		{
			29152, // Antharas
			29149, // Tarask Dragon
		},
		// Stage 3
		{
			29153, // Antharas
			29150, // Tarask Dragon
		},
	};
	// NPCs
	private static final int ANTHARAS_CLIENT_ID = 29068;
	private static final int ANTHARAS_EGG_ID = 18654;
	private static final int ANTHARAS_EGG_COUNT = 8;
	// Skill
	private static final SkillHolder ANTH_JUMP = new SkillHolder(4106, 1); // Antharas Stun
	private static final SkillHolder ANTH_TAIL = new SkillHolder(4107, 1); // Antharas Stun
	private static final SkillHolder ANTH_FEAR = new SkillHolder(4108, 1); // Antharas Terror
	private static final SkillHolder ANTH_DEBUFF = new SkillHolder(4109, 1); // Curse of Antharas
	private static final SkillHolder ANTH_MOUTH = new SkillHolder(4110, 2); // Breath Attack
	private static final SkillHolder ANTH_BREATH = new SkillHolder(4111, 1); // Antharas Fossilization
	private static final SkillHolder ANTH_NORM_ATTACK = new SkillHolder(4112, 1); // Ordinary Attack
	private static final SkillHolder ANTH_NORM_ATTACK_EX = new SkillHolder(4113, 1); // Animal doing ordinary attack
	private static final SkillHolder ANTH_REGEN_1 = new SkillHolder(4125, 1); // Antharas Regeneration
	private static final SkillHolder ANTH_REGEN_2 = new SkillHolder(4239, 1); // Antharas Regeneration
	private static final SkillHolder ANTH_REGEN_3 = new SkillHolder(4240, 1); // Antharas Regeneration
	private static final SkillHolder ANTH_REGEN_4 = new SkillHolder(4241, 1); // Antharas Regeneration
	private static final SkillHolder ANTH_ANTI_STRIDER = new SkillHolder(4258, 1); // Hinder Strider
	private static final SkillHolder ANTH_FEAR_SHORT = new SkillHolder(5092, 1); // Antharas Terror
	private static final SkillHolder ANTH_METEOR = new SkillHolder(5093, 1); // Antharas Meteor
	// Zone
	private static final NoRestartZone ZONE = ZoneManager.getInstance().getZoneById(70050, NoRestartZone.class); // Antharas Nest zone
	// Status
	private static final int ALIVE = 0;
	private static final int DEAD = 1;
	private static final String ANTHARAS_STAGE = "ANTHARAS_STAGE";
	private static final String ANTHARAS_PREVIOUS_STAGE = "ANTHARAS_PREVIOUS_STAGE";
	// Herphah
	private static final int HERPHAH = 34241;
	private static final Location OUTSIDE_HERPHAH_LOC = new Location(125675, 116869, -3888);
	private static final Location GIRAN_LOC = new Location(83386, 148014, -3400);
	private static final Location ANTHARAS_LOC = new Location(125787, 126452, -3952, 48000);
	private static final int RESPAWN_DAY = Calendar.SUNDAY;
	private static final int RESPAWN_HOUR = 20;
	private static final int RESPAWN_MINUTE = 0;
	private static final long FIGHT_DURATION = 3600000; // 1 hour
	// Runtime
	private static final Set<Integer> UNCLAIMED_REWARDS = ConcurrentHashMap.newKeySet();
	private static GrandBoss _antharas = null;
	private static Player _attacker1 = null;
	private static Player _attacker2 = null;
	private static Player _attacker3 = null;
	private static int _attackerHate1 = 0;
	private static int _attackerHate2 = 0;
	private static int _attackerHate3 = 0;
	private static Npc[] _eggs = new Npc[ANTHARAS_EGG_COUNT];
	private static AtomicInteger _claimedRewardsCount = new AtomicInteger();
	private static int _rewardsStage = -1;
	
	private Antharas()
	{
		for (int[] stagesNpcs : STAGES_NPCS)
		{
			addSpawnId(stagesNpcs[0], stagesNpcs[1]);
			addSpellFinishedId(stagesNpcs[0]);
			addAttackId(stagesNpcs[0]);
			addKillId(stagesNpcs[0]);
		}
		addKillId(ANTHARAS_EGG_ID);
		addFirstTalkId(HERPHAH);
		addTalkId(HERPHAH);
		addSpawnId(HERPHAH);
		
		final StatSet info = GrandBossManager.getInstance().getStatSet(ANTHARAS_CLIENT_ID);
		final long respawnTime = info.getLong("respawn_time");
		
		switch (getStatus())
		{
			case ALIVE:
			{
				if (calcMillisToAntharasEnd() <= 0)
				{
					setStatus(DEAD);
					scheduleNextRespawn();
				}
				else
				{
					_antharas = (GrandBoss) addSpawn(STAGES_NPCS[getAntharasStage() - 1][0], ANTHARAS_LOC, false, 0);
					if (getAntharasPreviousSpawnStage() == getAntharasStage())
					{
						_antharas.setCurrentHpMp(info.getDouble("currentHP"), info.getDouble("currentMP"));
					}
					else
					{
						_antharas.setCurrentHpMp(_antharas.getMaxHp(), _antharas.getMaxMp());
					}
					addBoss(ANTHARAS_CLIENT_ID, _antharas);
					setAntharasPreviousSpawnStage(getAntharasStage());
					notifyEvent("SPAWN_MINION", _antharas, null);
					startQuestTimer("SYNCHRONIZE_EGG_POSITION", 300, _antharas, null);
					startQuestTimer("DESPAWN_ANTHARAS", calcMillisToAntharasEnd(), _antharas, null);
				}
				break;
			}
			case DEAD:
			{
				final long remain = respawnTime - System.currentTimeMillis();
				if (remain > 0)
				{
					startQuestTimer("CLEAR_STATUS", remain, null, null);
				}
				else
				{
					if (calcMillisToAntharasEnd() <= 0)
					{
						setStatus(DEAD);
						scheduleNextRespawn();
					}
					else
					{
						setStatus(ALIVE);
						_antharas = (GrandBoss) addSpawn(STAGES_NPCS[getAntharasStage() - 1][0], ANTHARAS_LOC, false, 0);
						addBoss(ANTHARAS_CLIENT_ID, _antharas);
						setAntharasPreviousSpawnStage(getAntharasStage());
						notifyEvent("SPAWN_MINION", _antharas, null);
						startQuestTimer("SYNCHRONIZE_EGG_POSITION", 300, _antharas, null);
						startQuestTimer("DESPAWN_ANTHARAS", calcMillisToAntharasEnd(), _antharas, null);
					}
				}
				break;
			}
		}
	}
	
	public static void main(String[] args)
	{
		new Antharas();
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		if ((npc == null) || isAntharasId(npc.getId()))
		{
			switch (event)
			{
				case "SET_REGEN":
				{
					if (npc != null)
					{
						if (npc.getCurrentHp() < (npc.getMaxHp() * 0.25))
						{
							if (!npc.isAffectedBySkill(ANTH_REGEN_4.getSkillId()))
							{
								npc.getAI().setIntention(CtrlIntention.AI_INTENTION_CAST, ANTH_REGEN_4.getSkill(), npc);
							}
						}
						else if (npc.getCurrentHp() < (npc.getMaxHp() * 0.5))
						{
							if (!npc.isAffectedBySkill(ANTH_REGEN_3.getSkillId()))
							{
								npc.getAI().setIntention(CtrlIntention.AI_INTENTION_CAST, ANTH_REGEN_3.getSkill(), npc);
							}
						}
						else if (npc.getCurrentHp() < (npc.getMaxHp() * 0.75))
						{
							if (!npc.isAffectedBySkill(ANTH_REGEN_2.getSkillId()))
							{
								npc.getAI().setIntention(CtrlIntention.AI_INTENTION_CAST, ANTH_REGEN_2.getSkill(), npc);
							}
						}
						else if (!npc.isAffectedBySkill(ANTH_REGEN_1.getSkillId()))
						{
							npc.getAI().setIntention(CtrlIntention.AI_INTENTION_CAST, ANTH_REGEN_1.getSkill(), npc);
						}
						startQuestTimer("SET_REGEN", 60000, npc, null);
					}
					break;
				}
				case "SPAWN_MINION":
				{
					if ((_antharas == null) || _antharas.isDead())
					{
						break;
					}
					
					_antharas.setInvul(true);
					for (int i = 1; i <= ANTHARAS_EGG_COUNT; i++)
					{
						if ((_eggs[i - 1] == null) || _eggs[i - 1].isDead())
						{
							Location eggLoc = calcEggPosition(i);
							if (eggLoc == null)
							{
								eggLoc = _antharas.getLocation();
							}
							
							final Npc egg = addSpawn(ANTHARAS_EGG_ID, eggLoc, false, calcMillisToAntharasEnd());
							egg.disableCoreAI(true);
							egg.setImmobilized(true);
							_eggs[i - 1] = egg;
						}
					}
					startQuestTimer("SPAWN_MINION", 900000, npc, null);
					break;
				}
				case "SYNCHRONIZE_EGG_POSITION":
				{
					// _antharas.teleToLocation(loc.getX(), loc.getY(), loc.getZ() + 250);
					for (int i = 1; i <= ANTHARAS_EGG_COUNT; i++)
					{
						final Npc egg = _eggs[i - 1];
						if ((egg != null) && !egg.isDead())
						{
							Location newLoc = calcEggPosition(i);
							if (newLoc == null)
							{
								continue;
							}
							
							if ((Util.calculateDistance(newLoc, egg, true, false) > 100) && !egg.isMoving())
							{
								egg.stopMove(null);
								newLoc = GeoEngine.getInstance().getValidLocation(egg.getX(), egg.getY(), egg.getZ(), newLoc.getX(), newLoc.getY(), newLoc.getZ(), _antharas.getInstanceWorld());
								if ((newLoc != null) && GeoEngine.getInstance().canSeeTarget(egg, _antharas))
								{
									egg.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, newLoc);
								}
								else
								{
									egg.teleToLocation(_antharas);
								}
							}
						}
					}
					startQuestTimer("SYNCHRONIZE_EGG_POSITION", 300, _antharas, null);
					break;
				}
				case "CLEAR_STATUS":
				{
					_antharas = (GrandBoss) addSpawn(STAGES_NPCS[getAntharasStage() - 1][0], ANTHARAS_LOC, false, 0);
					addBoss(ANTHARAS_CLIENT_ID, _antharas);
					Broadcast.toAllOnlinePlayers(new Earthquake(ANTHARAS_LOC.getX(), ANTHARAS_LOC.getY(), ANTHARAS_LOC.getZ(), 20, 10));
					setStatus(ALIVE);
					setAntharasPreviousSpawnStage(getAntharasStage());
					notifyEvent("SPAWN_MINION", _antharas, null);
					startQuestTimer("SYNCHRONIZE_EGG_POSITION", 300, _antharas, null);
					startQuestTimer("DESPAWN_ANTHARAS", calcMillisToAntharasEnd(), _antharas, null);
					break;
				}
				case "DESPAWN_ANTHARAS":
				{
					if ((_antharas != null) && !_antharas.isDead())
					{
						notifyEvent("DESPAWN_MINIONS", null, null);
						cancelQuestTimer("CLEAR_STATUS", null, null);
						cancelQuestTimer("SET_REGEN", npc, null);
						cancelQuestTimer("SPAWN_MINION", npc, null);
						cancelQuestTimer("SYNCHRONIZE_EGG_POSITION", npc, null);
						cancelQuestTimer("DESPAWN_ANTHARAS", npc, null);
						setAntharasStage(1);
						_antharas.deleteMe();
						_antharas = null;
						setStatus(DEAD);
						LOGGER.info(getClass().getSimpleName() + " Despawned, because not killed in time.");
						scheduleNextRespawn();
					}
					break;
				}
				case "RESPAWN_ANTHARAS":
				{
					if (getStatus() == DEAD)
					{
						setRespawn(0);
						cancelQuestTimer("CLEAR_STATUS", null, null);
						notifyEvent("CLEAR_STATUS", null, null);
						player.sendMessage(getClass().getSimpleName() + ": Antharas has been respawned.");
					}
					else
					{
						player.sendMessage(getClass().getSimpleName() + ": You cannot respawn Antharas while he is alive!");
					}
					break;
				}
				case "DESPAWN_MINIONS":
				{
					if (getStatus() == ALIVE)
					{
						for (Creature creature : ZONE.getCharactersInside())
						{
							if ((creature != null) && creature.isNpc() && (isAntharasMinionId(creature.getId())))
							{
								creature.deleteMe();
							}
						}
						if (player != null) // Player cannot be null unless this event is called from GM command.
						{
							player.sendMessage(getClass().getSimpleName() + ": All minions have been deleted!");
						}
					}
					else if (player != null) // Player cannot be null unless this event is called from GM command.
					{
						player.sendMessage(getClass().getSimpleName() + ": You can't despawn minions right now!");
					}
					break;
				}
				case "ABORT_FIGHT":
				{
					if (getStatus() == ALIVE)
					{
						setStatus(ALIVE);
						cancelQuestTimer("SPAWN_MINION", _antharas, null);
						for (Creature creature : ZONE.getCharactersInside())
						{
							if (creature != null)
							{
								if (creature.isNpc())
								{
									if (isAntharasId(creature.getId()))
									{
										creature.teleToLocation(ANTHARAS_LOC);
									}
									else
									{
										creature.deleteMe();
									}
								}
								else if (creature.isPlayer() && !creature.isGM())
								{
									creature.teleToLocation(79800 + getRandom(600), 151200 + getRandom(1100), -3534);
								}
							}
						}
						player.sendMessage(getClass().getSimpleName() + ": Fight has been aborted!");
					}
					else
					{
						player.sendMessage(getClass().getSimpleName() + ": You can't abort fight right now!");
					}
					break;
				}
				case "MANAGE_SKILL":
				{
					manageSkills(npc);
					break;
				}
			}
		}
		else if (npc.getId() == HERPHAH)
		{
			switch (event)
			{
				case "TELEPORT_INSIDE_LIST":
				{
					if (player.getLevel() < 85)
					{
						return npc.getId() + "-WEAK.html";
					}
					else if ((_antharas == null) || _antharas.isDead())
					{
						return npc.getId() + "-NO_ANTHARAS.html";
					}
					return npc.getId() + "-TELEPORTS.html";
				}
				case "TELEPORT_GIRAN":
				{
					if (npc.calculateDistance2D(player) < 500)
					{
						player.teleToLocation(GIRAN_LOC, true);
					}
					break;
				}
				case "TELEPORT_12":
				{
					player.teleToLocation(125912, 121272, -3957, player.getHeading(), true);
					break;
				}
				case "TELEPORT_2":
				{
					player.teleToLocation(128392, 122680, -3957, player.getHeading(), true);
					break;
				}
				case "TELEPORT_5":
				{
					player.teleToLocation(127016, 125560, -3957, player.getHeading(), true);
					break;
				}
				case "TELEPORT_7":
				{
					player.teleToLocation(124312, 125656, -3956, player.getHeading(), true);
					break;
				}
				case "TELEPORT_10":
				{
					player.teleToLocation(123416, 122440, -3956, player.getHeading(), true);
					break;
				}
				case "34241.html":
				{
					return event;
				}
			}
		}
		return super.onEvent(event, npc, player);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		if (npc.calculateDistance2D(OUTSIDE_HERPHAH_LOC) > 200)
		{
			if (UNCLAIMED_REWARDS.contains(player.getObjectId()) && (_claimedRewardsCount.getAndIncrement() < 200))
			{
				UNCLAIMED_REWARDS.remove(player.getObjectId());
				player.addItem("Antharas stage " + _rewardsStage, 96350, _rewardsStage == 1 ? 1 : _rewardsStage == 2 ? 5 : 10, player, true);
			}
			return null;
		}
		
		return npc.getId() + ".html";
	}
	
	@Override
	public String onAttack(Npc npc, Player attacker, int damage, boolean isSummon, Skill skill)
	{
		if (isAntharasId(npc.getId()))
		{
			final Player player = attacker.getActingPlayer();
			if (player == null)
			{
				return super.onAttack(npc, attacker, damage, isSummon, skill);
			}
			
			if ((player.getMountType() == MountType.STRIDER) && !attacker.isAffectedBySkill(ANTH_ANTI_STRIDER.getSkillId()) && SkillCaster.checkUseConditions(npc, ANTH_ANTI_STRIDER.getSkill()))
			{
				addSkillCastDesire(npc, attacker, ANTH_ANTI_STRIDER.getSkill(), 100);
			}
			
			if (skill == null)
			{
				refreshAiParams(player, damage * 1000);
			}
			else if (npc.getCurrentHp() < (npc.getMaxHp() * 0.25))
			{
				refreshAiParams(player, (damage / 3) * 100);
			}
			else if (npc.getCurrentHp() < (npc.getMaxHp() * 0.5))
			{
				refreshAiParams(player, damage * 20);
			}
			else if (npc.getCurrentHp() < (npc.getMaxHp() * 0.75))
			{
				refreshAiParams(player, damage * 10);
			}
			else
			{
				refreshAiParams(player, (damage / 3) * 20);
			}
			manageSkills(npc);
		}
		return super.onAttack(npc, attacker, damage, isSummon, skill);
	}
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		final Player killerPlayer = killer.getActingPlayer();
		if (killerPlayer != null)
		{
			if (isAntharasId(npc.getId()))
			{
				final int currentStage = getAntharasStage();
				_antharas = null;
				notifyEvent("DESPAWN_MINIONS", null, null);
				// zone.broadcastPacket(new SpecialCamera(npc, 1200, 20, -10, 0, 10000, 13000, 0, 0, 0, 0, 0));
				Broadcast.toAllOnlinePlayers(new ExShowScreenMessage(NpcStringId.THE_EVIL_EARTH_DRAGON_ANTHARAS_HAS_BEEN_DEFEATED_BY_BRAVE_HEROES, 2, 15000, true));
				ZONE.broadcastPacket(new PlaySound("BS01_D"));
				scheduleNextRespawn();
				cancelQuestTimer("SET_REGEN", npc, null);
				cancelQuestTimer("SPAWN_MINION", npc, null);
				cancelQuestTimer("SYNCHRONIZE_EGG_POSITION", npc, null);
				cancelQuestTimer("DESPAWN_ANTHARAS", npc, null);
				setStatus(DEAD);
				// LOGGER.info(getClass().getSimpleName() + ": Stage " + currentStage + " rewards.");
				addSpawn(HERPHAH, npc, false, 20 * 60 * 1000);
				UNCLAIMED_REWARDS.clear();
				_claimedRewardsCount.set(0);
				_rewardsStage = currentStage;
				if (killerPlayer.isInCommandChannel())
				{
					UNCLAIMED_REWARDS.addAll(killerPlayer.getCommandChannel().getMembers().stream().map(Player::getObjectId).toList());
				}
				else if (killerPlayer.isInParty())
				{
					UNCLAIMED_REWARDS.addAll(killerPlayer.getParty().getMembers().stream().map(Player::getObjectId).toList());
				}
				else
				{
					UNCLAIMED_REWARDS.add(killerPlayer.getObjectId());
				}
				setAntharasStage(currentStage + 1);
			}
			else if (npc.getId() == ANTHARAS_EGG_ID)
			{
				if ((_antharas != null) && !_antharas.isDead())
				{
					addAttackPlayerDesire(addSpawn(STAGES_NPCS[getAntharasStage() - 1][1], _antharas, false, calcMillisToAntharasEnd()), killer, 999);
				}
				
				boolean eggsAlive = false;
				for (Npc egg : _eggs)
				{
					if ((egg != null) && !egg.isDead())
					{
						eggsAlive = true;
						break;
					}
				}
				if (!eggsAlive && (_antharas != null) && !_antharas.isDead())
				{
					_antharas.setInvul(false);
				}
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		if (isAntharasId(npc.getId()))
		{
			((Attackable) npc).setCanReturnToSpawnPoint(false);
			npc.setRandomWalking(false);
			cancelQuestTimer("SET_REGEN", npc, null);
			startQuestTimer("SET_REGEN", 60000, npc, null);
		}
		else if (isAntharasMinionId(npc.getId()))
		{
			((Attackable) npc).setCanReturnToSpawnPoint(false);
			npc.setRandomWalking(false);
		}
		else if (npc.getId() == HERPHAH)
		{
			npc.setRandomAnimation(false);
		}
		return super.onSpawn(npc);
	}
	
	@Override
	public String onSpellFinished(Npc npc, Player player, Skill skill)
	{
		startQuestTimer("MANAGE_SKILL", 1000, npc, null);
		return super.onSpellFinished(npc, player, skill);
	}
	
	@Override
	public boolean unload(boolean removeFromList)
	{
		if (_antharas != null)
		{
			_antharas.deleteMe();
			_antharas = null;
		}
		return super.unload(removeFromList);
	}
	
	private int getStatus()
	{
		return GrandBossManager.getInstance().getStatus(ANTHARAS_CLIENT_ID);
	}
	
	private void setStatus(int status)
	{
		GrandBossManager.getInstance().setStatus(ANTHARAS_CLIENT_ID, status);
	}
	
	private void addBoss(int bossId, GrandBoss grandboss)
	{
		GrandBossManager.getInstance().addBoss(bossId, grandboss);
	}
	
	private void setRespawn(long respawnTime)
	{
		GrandBossManager.getInstance().getStatSet(ANTHARAS_CLIENT_ID).set("respawn_time", System.currentTimeMillis() + respawnTime);
	}
	
	private final void refreshAiParams(Player attacker, int damage)
	{
		if ((_attacker1 != null) && (attacker == _attacker1))
		{
			if (_attackerHate1 < (damage + 1000))
			{
				_attackerHate1 = damage + getRandom(3000);
			}
		}
		else if ((_attacker2 != null) && (attacker == _attacker2))
		{
			if (_attackerHate2 < (damage + 1000))
			{
				_attackerHate2 = damage + getRandom(3000);
			}
		}
		else if ((_attacker3 != null) && (attacker == _attacker3))
		{
			if (_attackerHate3 < (damage + 1000))
			{
				_attackerHate3 = damage + getRandom(3000);
			}
		}
		else
		{
			final int i1 = CommonUtil.min(_attackerHate1, _attackerHate2, _attackerHate3);
			if (_attackerHate1 == i1)
			{
				_attackerHate1 = damage + getRandom(3000);
				_attacker1 = attacker;
			}
			else if (_attackerHate2 == i1)
			{
				_attackerHate2 = damage + getRandom(3000);
				_attacker2 = attacker;
			}
			else if (_attackerHate3 == i1)
			{
				_attackerHate3 = damage + getRandom(3000);
				_attacker3 = attacker;
			}
		}
	}
	
	private void manageSkills(Npc npc)
	{
		if (npc.isCastingNow() || npc.isCoreAIDisabled() || !npc.isInCombat())
		{
			return;
		}
		
		int i1 = 0;
		int i2 = 0;
		Player c2 = null;
		if ((_attacker1 == null) || (npc.calculateDistance3D(_attacker1) > 9000) || _attacker1.isDead())
		{
			_attackerHate1 = 0;
		}
		
		if ((_attacker2 == null) || (npc.calculateDistance3D(_attacker2) > 9000) || _attacker2.isDead())
		{
			_attackerHate2 = 0;
		}
		
		if ((_attacker3 == null) || (npc.calculateDistance3D(_attacker3) > 9000) || _attacker3.isDead())
		{
			_attackerHate3 = 0;
		}
		
		if (_attackerHate1 > _attackerHate2)
		{
			i1 = 2;
			i2 = _attackerHate1;
			c2 = _attacker1;
		}
		else if (_attackerHate2 > 0)
		{
			i1 = 3;
			i2 = _attackerHate2;
			c2 = _attacker2;
		}
		
		if (_attackerHate3 > i2)
		{
			i1 = 4;
			i2 = _attackerHate3;
			c2 = _attacker3;
		}
		if (i2 > 0)
		{
			if (getRandom(100) < 70)
			{
				switch (i1)
				{
					case 2:
					{
						_attackerHate1 = 500;
						break;
					}
					case 3:
					{
						_attackerHate2 = 500;
						break;
					}
					case 4:
					{
						_attackerHate3 = 500;
						break;
					}
				}
			}
			
			final double distance = npc.calculateDistance3D(c2);
			final double direction = npc.calculateDirectionTo(c2);
			SkillHolder skillToCast = null;
			boolean castOnTarget = false;
			if (npc.getCurrentHp() < (npc.getMaxHp() * 0.25))
			{
				if (getRandom(100) < 30)
				{
					castOnTarget = true;
					skillToCast = ANTH_MOUTH;
				}
				else if ((getRandom(100) < 80) && (((distance < 1423) && (direction < 188) && (direction > 172)) || ((distance < 802) && (direction < 194) && (direction > 166))))
				{
					skillToCast = ANTH_TAIL;
				}
				else if ((getRandom(100) < 40) && (((distance < 850) && (direction < 210) && (direction > 150)) || ((distance < 425) && (direction < 270) && (direction > 90))))
				{
					skillToCast = ANTH_DEBUFF;
				}
				else if ((getRandom(100) < 10) && (distance < 1100))
				{
					skillToCast = ANTH_JUMP;
				}
				else if (getRandom(100) < 10)
				{
					castOnTarget = true;
					skillToCast = ANTH_METEOR;
				}
				else if (getRandom(100) < 6)
				{
					castOnTarget = true;
					skillToCast = ANTH_BREATH;
				}
				else if (getRandomBoolean())
				{
					castOnTarget = true;
					skillToCast = ANTH_NORM_ATTACK_EX;
				}
				else if (getRandom(100) < 5)
				{
					castOnTarget = true;
					skillToCast = getRandomBoolean() ? ANTH_FEAR : ANTH_FEAR_SHORT;
				}
				else
				{
					castOnTarget = true;
					skillToCast = ANTH_NORM_ATTACK;
				}
			}
			else if (npc.getCurrentHp() < (npc.getMaxHp() * 0.5))
			{
				if ((getRandom(100) < 80) && (((distance < 1423) && (direction < 188) && (direction > 172)) || ((distance < 802) && (direction < 194) && (direction > 166))))
				{
					skillToCast = ANTH_TAIL;
				}
				else if ((getRandom(100) < 40) && (((distance < 850) && (direction < 210) && (direction > 150)) || ((distance < 425) && (direction < 270) && (direction > 90))))
				{
					skillToCast = ANTH_DEBUFF;
				}
				else if ((getRandom(100) < 10) && (distance < 1100))
				{
					skillToCast = ANTH_JUMP;
				}
				else if (getRandom(100) < 7)
				{
					castOnTarget = true;
					skillToCast = ANTH_METEOR;
				}
				else if (getRandom(100) < 6)
				{
					castOnTarget = true;
					skillToCast = ANTH_BREATH;
				}
				else if (getRandomBoolean())
				{
					castOnTarget = true;
					skillToCast = ANTH_NORM_ATTACK_EX;
				}
				else if (getRandom(100) < 5)
				{
					castOnTarget = true;
					skillToCast = getRandomBoolean() ? ANTH_FEAR : ANTH_FEAR_SHORT;
				}
				else
				{
					castOnTarget = true;
					skillToCast = ANTH_NORM_ATTACK;
				}
			}
			else if (npc.getCurrentHp() < (npc.getMaxHp() * 0.75))
			{
				if ((getRandom(100) < 80) && (((distance < 1423) && (direction < 188) && (direction > 172)) || ((distance < 802) && (direction < 194) && (direction > 166))))
				{
					skillToCast = ANTH_TAIL;
				}
				else if ((getRandom(100) < 10) && (distance < 1100))
				{
					skillToCast = ANTH_JUMP;
				}
				else if (getRandom(100) < 5)
				{
					castOnTarget = true;
					skillToCast = ANTH_METEOR;
				}
				else if (getRandom(100) < 6)
				{
					castOnTarget = true;
					skillToCast = ANTH_BREATH;
				}
				else if (getRandomBoolean())
				{
					castOnTarget = true;
					skillToCast = ANTH_NORM_ATTACK_EX;
				}
				else if (getRandom(100) < 5)
				{
					castOnTarget = true;
					skillToCast = getRandomBoolean() ? ANTH_FEAR : ANTH_FEAR_SHORT;
				}
				else
				{
					castOnTarget = true;
					skillToCast = ANTH_NORM_ATTACK;
				}
			}
			else if ((getRandom(100) < 80) && (((distance < 1423) && (direction < 188) && (direction > 172)) || ((distance < 802) && (direction < 194) && (direction > 166))))
			{
				skillToCast = ANTH_TAIL;
			}
			else if (getRandom(100) < 3)
			{
				castOnTarget = true;
				skillToCast = ANTH_METEOR;
			}
			else if (getRandom(100) < 6)
			{
				castOnTarget = true;
				skillToCast = ANTH_BREATH;
			}
			else if (getRandomBoolean())
			{
				castOnTarget = true;
				skillToCast = ANTH_NORM_ATTACK_EX;
			}
			else if (getRandom(100) < 5)
			{
				castOnTarget = true;
				skillToCast = getRandomBoolean() ? ANTH_FEAR : ANTH_FEAR_SHORT;
			}
			else
			{
				castOnTarget = true;
				skillToCast = ANTH_NORM_ATTACK;
			}
			
			if ((skillToCast != null) && SkillCaster.checkUseConditions(npc, skillToCast.getSkill()))
			{
				if (castOnTarget)
				{
					addSkillCastDesire(npc, c2, skillToCast.getSkill(), 100);
				}
				else
				{
					npc.getAI().setIntention(CtrlIntention.AI_INTENTION_CAST, skillToCast.getSkill(), npc);
				}
			}
		}
	}
	
	private boolean isAntharasId(int npcId)
	{
		for (int[] stagesNpcs : STAGES_NPCS)
		{
			if (stagesNpcs[0] == npcId)
			{
				return true;
			}
		}
		return false;
	}
	
	private boolean isAntharasMinionId(int npcId)
	{
		for (int[] stagesNpcs : STAGES_NPCS)
		{
			if (stagesNpcs[1] == npcId)
			{
				return true;
			}
		}
		return false;
	}
	
	private int getAntharasStage()
	{
		return GlobalVariablesManager.getInstance().getInt(ANTHARAS_STAGE, 1);
	}
	
	private void setAntharasStage(int newStage)
	{
		GlobalVariablesManager.getInstance().set(ANTHARAS_STAGE, Math.min(newStage, 3));
	}
	
	/**
	 * This is previous spawn stage, not previous antharas stage. Used to set HP properly after server restart.
	 * @return At which stage antharas spawned last time.
	 */
	private int getAntharasPreviousSpawnStage()
	{
		return GlobalVariablesManager.getInstance().getInt(ANTHARAS_PREVIOUS_STAGE, 0);
	}
	
	private void setAntharasPreviousSpawnStage(int newStage)
	{
		GlobalVariablesManager.getInstance().set(ANTHARAS_PREVIOUS_STAGE, Math.min(newStage, 3));
	}
	
	private long calcMillisToAntharasEnd()
	{
		final Calendar calendar = Calendar.getInstance();
		// if (calendar.get(Calendar.DAY_OF_WEEK) != RESPAWN_DAY)
		// {
		// return 0;
		// }
		
		calendar.set(Calendar.HOUR_OF_DAY, RESPAWN_HOUR);
		calendar.set(Calendar.MINUTE, RESPAWN_MINUTE);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		// if (calendar.getTimeInMillis() > System.currentTimeMillis())
		// {
		// return 0;
		// }
		
		calendar.setTimeInMillis(calendar.getTimeInMillis() + FIGHT_DURATION);
		return Math.max(0, calendar.getTimeInMillis() - System.currentTimeMillis());
	}
	
	private Location calcEggPosition(int eggNum)
	{
		if ((_antharas == null) || _antharas.isDead())
		{
			return null;
		}
		
		final double radians = Math.toRadians(45 * (eggNum + 1));
		final int posX = (int) (_antharas.getX() + (1100 * Math.cos(radians)));
		final int posY = (int) (_antharas.getY() + (1100 * Math.sin(radians)));
		final int posZ = _antharas.getZ() + (NpcData.getInstance().getTemplate(ANTHARAS_EGG_ID).getCollisionHeight() * 2);
		return new Location(posX, posY, posZ);
	}
	
	public void scheduleNextRespawn()
	{
		long respawnTime = -1;
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, RESPAWN_HOUR);
		calendar.set(Calendar.MINUTE, RESPAWN_MINUTE);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		for (int i = 0; i < 7; i++)
		{
			if ((calendar.get(Calendar.DAY_OF_WEEK) == RESPAWN_DAY) && (calendar.getTimeInMillis() > System.currentTimeMillis()))
			{
				respawnTime = calendar.getTimeInMillis();
				break;
			}
			calendar.add(Calendar.DAY_OF_WEEK, 1);
		}
		if (respawnTime < 0)
		{
			respawnTime = calendar.getTimeInMillis();
		}
		
		final long respawnDelay = respawnTime - System.currentTimeMillis();
		setRespawn(respawnDelay);
		startQuestTimer("CLEAR_STATUS", respawnDelay, null, null);
		// LOGGER.info(getClass().getSimpleName() + ": Scheduled Antharas respawn time to " + Util.formatDate(new Date(respawnTime), "dd.MM.yyyy HH:mm"));
	}
}
