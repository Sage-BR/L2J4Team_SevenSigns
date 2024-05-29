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
package instances.TrainingZone;

import java.util.Arrays;

import org.l2j.gameserver.data.xml.TimedHuntingZoneData;
import org.l2j.gameserver.enums.ChatType;
import org.l2j.gameserver.model.Location;
import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.WorldObject;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.SkillHolder;
import org.l2j.gameserver.model.holders.TimedHuntingZoneHolder;
import org.l2j.gameserver.model.instancezone.Instance;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.model.skill.SkillCaster;
import org.l2j.gameserver.model.variables.PlayerVariables;
import org.l2j.gameserver.network.NpcStringId;
import org.l2j.gameserver.network.serverpackets.ExSendUIEvent;
import org.l2j.gameserver.network.serverpackets.huntingzones.TimedHuntingZoneExit;

import instances.AbstractInstance;

/**
 * @author Serenitty
 * @URL https://www.youtube.com/watch?v=SuRXhj79-rI
 */
public class TrainingZone extends AbstractInstance
{
	// NPCs
	private static final int GROWN = 34307;
	private static final int TIND = 34308;
	private static final int TOKA = 34305;
	private static final int ERI = 34306;
	private static final int ARBANA = 34309;
	// BOSSES
	private static final int BOOJUDU = 25952; // Local area boss
	private static final int PETRON = 25953; // Local area boss
	private static final int KERION = 25954; // Local area boss
	private static final int TUKHAH = 25955; // Local area boss
	// TOI
	private static final int CHEL = 25963; // Insolence Boss
	private static final int RILVA = 25961; // Insolence Boss
	private static final int RYUN = 25962; // Insolence Boss
	// Attack buff
	private static final SkillHolder MENTOR_PATK = new SkillHolder(48490, 1); // P atk +1000 buff
	private static final SkillHolder MENTOR_MATK = new SkillHolder(48491, 1); // M atk +1000 buff
	private static final SkillHolder MENTOR_ATK_SPEED = new SkillHolder(48492, 1); // ATK Spd +24%
	private static final SkillHolder MENTOR_CST_SPEED = new SkillHolder(48493, 1); // Casting Spd +24%
	private static final SkillHolder MENTOR_PSKILL_CRIT = new SkillHolder(48494, 1); // P skill critical rate +11%
	private static final SkillHolder MENTOR_MSKILL_CRIT = new SkillHolder(48495, 1); // M skill critical Damage +21%
	private static final SkillHolder MENTOR_ALL_CRITICAL_DAMAGE = new SkillHolder(48496, 1); // critical damage +500
	private static final SkillHolder MENTOR_SS_DAMAGE = new SkillHolder(48497, 1); // Soulshots Spirit shots damage
	private static final SkillHolder MENTOR_SKILL_POWER = new SkillHolder(48498, 1); // skill power +10%
	private static final SkillHolder MENTOR_ALL_PMCRITICAL_DAMAGE = new SkillHolder(48499, 1); // All critical damage 15%
	private static final SkillHolder MENTOR_ABSORBS_DAMAGE_MP = new SkillHolder(48500, 1); // Absorbs 3% inflicted damage as mp
	private static final SkillHolder MENTOR_ABSORBS_DAMAGE_HP = new SkillHolder(48501, 1); // Absorbs 8% inflicted damage as hp
	// Defense buff
	private static final SkillHolder MENTOR_PDEF = new SkillHolder(48502, 1); // P Def +1000 buff
	private static final SkillHolder MENTOR_MDEF = new SkillHolder(48503, 1); // M Def +1000 buff
	private static final SkillHolder MENTOR_MAXHP_MP = new SkillHolder(48504, 1); // Max hp mp +33%
	private static final SkillHolder MENTOR_SPEED = new SkillHolder(48505, 1); // Speed +12
	private static final SkillHolder MENTOR_RECEIVED_CRIT_DAMAGE = new SkillHolder(48506, 1); // P critical damage -15%
	private static final SkillHolder MENTOR_RECEIVED_CRIT_DAMAGE2 = new SkillHolder(48507, 1); // P critical damage -300
	private static final SkillHolder MENTOR_RATE_CRITICAL_RECEIVED = new SkillHolder(48508, 1); // All received critical rate -15%
	private static final SkillHolder MENTOR_RECEIVED_PVE = new SkillHolder(48509, 1); // Received pve critical damage -10%
	private static final SkillHolder MENTOR_HP_POTION_RECOVERY = new SkillHolder(48510, 1); // HP potion recovery potions effect +100
	// Event Buff: example Defeat the qeeen etc.
	protected static final int[] NO_DELETE_BUFFS =
	{
		48200,
		48233,
		48235,
		48236,
		48483,
	};
	private static final int[] LOCAL_MOBS =
	{
		22152,
		22153,
		22154,
		22155,
		22242,
		22245,
		22243,
		22276,
		22135,
		22139,
		22135,
		22141,
		22145,
		22144,
		22143,
		22141,
	};
	private static final int[] TOI_MOBS =
	{
		21990,
		21989,
		20812,
		21991,
		21994,
		21995,
		21996,
		22000,
		22001,
		22002,
		22003,
		22025,
		22026,
		22027,
		22010,
		22011,
		22012,
		22013,
		22016,
		22017,
		22021,
		22022,
		22024,
		22026,
		22028,
		22029,
		22030,
		22032,
		22033,
		22035,
		22039,
		22037,
		22038,
	};
	private static final Location LOCAL_AREA = new Location(-56255, 13537, -3336);
	private static final Location WAITING_AREA = new Location(-49550, 17189, -3016);
	private static final Location INSOLENCE_TOWER = new Location(-52849, 5272, -240);
	private static final String TRAINING_AREA_TELEPORT = "TRAINING_AREA_TELEPORT";
	private static final int TEMPLATE_ID = 228;
	
	public TrainingZone()
	{
		super(TEMPLATE_ID);
		addFirstTalkId(GROWN, TIND, TOKA, ERI, ARBANA);
		addTalkId(GROWN, TIND, TOKA, ERI, ARBANA);
		addAttackId(LOCAL_MOBS);
		addAttackId(TOI_MOBS);
		addCreatureSeeId(GROWN, TIND);
		addInstanceEnterId(TEMPLATE_ID);
		addInstanceLeaveId(TEMPLATE_ID);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
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
		switch (event)
		{
			case "34307.htm":
			{
				break;
			}
			case "GivePAtk":
			{
				if ((npc.getId() == TOKA) && npc.isInInstance())
				{
					SkillCaster.triggerCast(npc, player, MENTOR_PATK.getSkill());
				}
				break;
			}
			case "GiveMAtk":
			{
				if ((npc.getId() == TOKA) && npc.isInInstance())
				{
					SkillCaster.triggerCast(npc, player, MENTOR_MATK.getSkill());
				}
				break;
			}
			case "GiveAtkSpeed":
			{
				if ((npc.getId() == TOKA) && npc.isInInstance())
				{
					SkillCaster.triggerCast(npc, player, MENTOR_ATK_SPEED.getSkill());
				}
				break;
			}
			case "GiveCastingSpeed":
			{
				if ((npc.getId() == TOKA) && npc.isInInstance())
				{
					SkillCaster.triggerCast(npc, player, MENTOR_CST_SPEED.getSkill());
				}
				break;
			}
			case "GivePSkillCriticalRate":
			{
				if ((npc.getId() == TOKA) && npc.isInInstance())
				{
					SkillCaster.triggerCast(npc, player, MENTOR_PSKILL_CRIT.getSkill());
				}
				break;
			}
			case "GivePSkillCriticalDamage":
			{
				if ((npc.getId() == TOKA) && npc.isInInstance())
				{
					SkillCaster.triggerCast(npc, player, MENTOR_MSKILL_CRIT.getSkill());
				}
				break;
			}
			case "GivePSkillCriticalDamage+500":
			{
				if ((npc.getId() == TOKA) && npc.isInInstance())
				{
					SkillCaster.triggerCast(npc, player, MENTOR_ALL_CRITICAL_DAMAGE.getSkill());
				}
				break;
			}
			case "GiveShotsDamage":
			{
				if ((npc.getId() == TOKA) && npc.isInInstance())
				{
					SkillCaster.triggerCast(npc, player, MENTOR_SS_DAMAGE.getSkill());
				}
				break;
			}
			case "GivePSkillPower":
			{
				if ((npc.getId() == TOKA) && npc.isInInstance())
				{
					SkillCaster.triggerCast(npc, player, MENTOR_SKILL_POWER.getSkill());
				}
				break;
			}
			case "GiveAllCriticalDamage":
			{
				if ((npc.getId() == TOKA) && npc.isInInstance())
				{
					SkillCaster.triggerCast(npc, player, MENTOR_ALL_PMCRITICAL_DAMAGE.getSkill());
				}
				break;
			}
			case "GiveAbsorbs3InflictedMp":
			{
				if ((npc.getId() == TOKA) && npc.isInInstance())
				{
					SkillCaster.triggerCast(npc, player, MENTOR_ABSORBS_DAMAGE_MP.getSkill());
				}
				break;
			}
			case "GiveAbsorbs8InflictedHp":
			{
				if ((npc.getId() == TOKA) && npc.isInInstance())
				{
					SkillCaster.triggerCast(npc, player, MENTOR_ABSORBS_DAMAGE_HP.getSkill());
				}
				break;
			}
			case "GivePDef":
			{
				if ((npc.getId() == ERI) && npc.isInInstance())
				{
					SkillCaster.triggerCast(npc, player, MENTOR_PDEF.getSkill());
				}
				break;
			}
			case "GiveMDef":
			{
				if ((npc.getId() == ERI) && npc.isInInstance())
				{
					SkillCaster.triggerCast(npc, player, MENTOR_MDEF.getSkill());
				}
				break;
			}
			case "GiveMxHpMp":
			{
				if ((npc.getId() == ERI) && npc.isInInstance())
				{
					SkillCaster.triggerCast(npc, player, MENTOR_MAXHP_MP.getSkill());
				}
				break;
			}
			case "GiveSpeed":
			{
				if ((npc.getId() == ERI) && npc.isInInstance())
				{
					SkillCaster.triggerCast(npc, player, MENTOR_SPEED.getSkill());
				}
				break;
			}
			case "GiveReceivedCritDamage":
			{
				if ((npc.getId() == ERI) && npc.isInInstance())
				{
					SkillCaster.triggerCast(npc, player, MENTOR_RECEIVED_CRIT_DAMAGE.getSkill());
				}
				break;
			}
			case "GiveReceivedCritDamage2":
			{
				if ((npc.getId() == ERI) && npc.isInInstance())
				{
					SkillCaster.triggerCast(npc, player, MENTOR_RECEIVED_CRIT_DAMAGE2.getSkill());
				}
				break;
			}
			case "GiveRateCriticalRcv":
			{
				if ((npc.getId() == ERI) && npc.isInInstance())
				{
					SkillCaster.triggerCast(npc, player, MENTOR_RATE_CRITICAL_RECEIVED.getSkill());
				}
				break;
			}
			case "GiveReceivedPve":
			{
				if ((npc.getId() == ERI) && npc.isInInstance())
				{
					SkillCaster.triggerCast(npc, player, MENTOR_RECEIVED_PVE.getSkill());
				}
				break;
			}
			case "GiveHpPotionRecovery":
			{
				if ((npc.getId() == ERI) && npc.isInInstance())
				{
					SkillCaster.triggerCast(npc, player, MENTOR_HP_POTION_RECOVERY.getSkill());
				}
				break;
			}
			case "npc_talk":
			{
				final Instance world = npc.getInstanceWorld();
				if (world != null)
				{
					npc.broadcastSay(ChatType.NPC_SHOUT, "I can summon mobs for the training!");
					startQuestTimer(event, 11500, npc, player);
				}
				break;
			}
			
			case "NPC_BUFF_SUPPORT":
			{
				if (npc.getId() == TOKA)
				{
					return npc.getId() + "-tokaBuff.html";
				}
				if (npc.getId() == ERI)
				{
					return npc.getId() + "-eriBuff.html";
				}
				break;
			}
			case "INSOLENCE_TOWER":
			{
				if (npc.getId() == GROWN)
				{
					return npc.getId() + "-tower.html";
				}
				break;
			}
			case "SpawnBossClockList":
			{
				final Instance world = npc.getInstanceWorld();
				final int areaTeleport = world.getParameters().getInt(TRAINING_AREA_TELEPORT, 0);
				if (areaTeleport == 1)
				{
					addSpawn(BOOJUDU, -56776, 12532, -3384, 33897, false, 0, true, player.getInstanceId());
				}
				else if (areaTeleport == 2)
				{
					addSpawn(PETRON, -56776, 12532, -3384, 33897, false, 0, true, player.getInstanceId());
				}
				else if (areaTeleport == 3)
				{
					addSpawn(KERION, -56776, 12532, -3384, 33897, false, 0, true, player.getInstanceId());
				}
				else if (areaTeleport == 4)
				{
					addSpawn(TUKHAH, -56776, 12532, -3384, 33897, false, 0, true, player.getInstanceId());
				}
				else if (areaTeleport == 5)
				{
					addSpawn(RILVA, -51725, 6149, -245, 54160, false, 0, true, player.getInstanceId());
				}
				else if (areaTeleport == 6)
				{
					addSpawn(RILVA, -51725, 6149, -245, 54160, false, 0, true, player.getInstanceId());
				}
				else if (areaTeleport == 7)
				{
					addSpawn(RILVA, -51725, 6149, -245, 54160, false, 0, true, player.getInstanceId());
				}
				else if (areaTeleport == 8)
				{
					addSpawn(RILVA, -51725, 6149, -245, 54160, false, 0, true, player.getInstanceId());
				}
				else if (areaTeleport == 9)
				{
					addSpawn(RYUN, -51725, 6149, -245, 54160, false, 0, true, player.getInstanceId());
				}
				else if (areaTeleport == 10)
				{
					addSpawn(RYUN, -51725, 6149, -245, 54160, false, 0, true, player.getInstanceId());
				}
				else if (areaTeleport == 11)
				{
					addSpawn(RYUN, -51725, 6149, -245, 54160, false, 0, true, player.getInstanceId());
				}
				else if (areaTeleport == 12)
				{
					addSpawn(RYUN, -51725, 6149, -245, 54160, false, 0, true, player.getInstanceId());
				}
				else if (areaTeleport >= 13)
				{
					addSpawn(CHEL, -51725, 6149, -245, 54160, false, 0, true, player.getInstanceId());
				}
				break;
			}
			case "LIZARD_MEN_SELECT":
			{
				final Instance world = npc.getInstanceWorld();
				final StatSet worldParameters = world.getParameters();
				final int areaTeleport = worldParameters.getInt(TRAINING_AREA_TELEPORT, 0);
				if (areaTeleport >= 1)
				{
					world.getNpcs().stream().filter(WorldObject::isAttackable).forEach(Npc::deleteMe);
					worldParameters.remove(TRAINING_AREA_TELEPORT);
					return npc.getId() + "-removeselect.html";
					
				}
				else if (areaTeleport == 0)
				{
					despawnAllGroups(world);
					world.spawnGroup("PlainsOfTheLizardmen");
					worldParameters.set(TRAINING_AREA_TELEPORT, areaTeleport + 1);
					return npc.getId() + "-Selected.html";
				}
				break;
			}
			case "SEL_MAHUM_SELECT":
			{
				final Instance world = npc.getInstanceWorld();
				final StatSet worldParameters = world.getParameters();
				final int areaTeleport = worldParameters.getInt(TRAINING_AREA_TELEPORT, 0);
				if (areaTeleport >= 1)
				{
					world.getNpcs().stream().filter(WorldObject::isAttackable).forEach(Npc::deleteMe);
					worldParameters.remove(TRAINING_AREA_TELEPORT);
					return npc.getId() + "-removeselect.html";
				}
				else if (areaTeleport == 0)
				{
					despawnAllGroups(world);
					world.spawnGroup("SelMahumBase");
					worldParameters.set(TRAINING_AREA_TELEPORT, areaTeleport + 2);
					return npc.getId() + "-Selected.html";
				}
				break;
			}
			case "ORC_BARRACKS_SELECT":
			{
				final Instance world = npc.getInstanceWorld();
				final StatSet worldParameters = world.getParameters();
				final int areaTeleport = worldParameters.getInt(TRAINING_AREA_TELEPORT, 0);
				if (areaTeleport >= 1)
				{
					world.getNpcs().stream().filter(WorldObject::isAttackable).forEach(Npc::deleteMe);
					worldParameters.remove(TRAINING_AREA_TELEPORT);
					return npc.getId() + "-removeselect.html";
				}
				else if (areaTeleport == 0)
				{
					despawnAllGroups(world);
					world.spawnGroup("OrcBarracksKerrs");
					worldParameters.set(TRAINING_AREA_TELEPORT, areaTeleport + 3);
					return npc.getId() + "-Selected.html";
				}
				break;
			}
			case "ORC_BARRACKS_TUREK_SELECT":
			{
				final Instance world = npc.getInstanceWorld();
				final StatSet worldParameters = world.getParameters();
				final int areaTeleport = worldParameters.getInt(TRAINING_AREA_TELEPORT, 0);
				if (areaTeleport >= 1)
				{
					world.getNpcs().stream().filter(WorldObject::isAttackable).forEach(Npc::deleteMe);
					worldParameters.remove(TRAINING_AREA_TELEPORT);
					return npc.getId() + "-removeselect.html";
				}
				else if (areaTeleport == 0)
				{
					despawnAllGroups(world);
					world.spawnGroup("OrcBarracksTureks");
					worldParameters.set(TRAINING_AREA_TELEPORT, areaTeleport + 4);
					return npc.getId() + "-Selected.html";
				}
				break;
			}
			case "TOWER_OF_INSOLENCE_SELECT":
			{
				final Instance world = npc.getInstanceWorld();
				final StatSet worldParameters = world.getParameters();
				final int areaTeleport = worldParameters.getInt(TRAINING_AREA_TELEPORT, 0);
				if (areaTeleport >= 1)
				{
					world.getNpcs().stream().filter(WorldObject::isAttackable).forEach(Npc::deleteMe);
					worldParameters.remove(TRAINING_AREA_TELEPORT);
					return npc.getId() + "-removeselect.html";
				}
				else if (areaTeleport == 0)
				{
					despawnAllGroups(world);
					world.spawnGroup("TowerOfInsolence1");
					worldParameters.set(TRAINING_AREA_TELEPORT, areaTeleport + 5);
					return npc.getId() + "-Selected.html";
				}
				break;
			}
			case "TOWER_OF_INSOLENCE_SELECT2":
			{
				final Instance world = npc.getInstanceWorld();
				final StatSet worldParameters = world.getParameters();
				final int areaTeleport = worldParameters.getInt(TRAINING_AREA_TELEPORT, 0);
				if (areaTeleport >= 1)
				{
					world.getNpcs().stream().filter(WorldObject::isAttackable).forEach(Npc::deleteMe);
					worldParameters.remove(TRAINING_AREA_TELEPORT);
					return npc.getId() + "-removeselect.html";
				}
				else if (areaTeleport == 0)
				{
					despawnAllGroups(world);
					world.spawnGroup("TowerOfInsolence2");
					worldParameters.set(TRAINING_AREA_TELEPORT, areaTeleport + 6);
					return npc.getId() + "-Selected.html";
				}
				break;
			}
			case "TOWER_OF_INSOLENCE_SELECT3":
			{
				final Instance world = npc.getInstanceWorld();
				final StatSet worldParameters = world.getParameters();
				final int areaTeleport = worldParameters.getInt(TRAINING_AREA_TELEPORT, 0);
				if (areaTeleport >= 1)
				{
					world.getNpcs().stream().filter(WorldObject::isAttackable).forEach(Npc::deleteMe);
					worldParameters.remove(TRAINING_AREA_TELEPORT);
					return npc.getId() + "-removeselect.html";
				}
				else if (areaTeleport == 0)
				{
					despawnAllGroups(world);
					world.spawnGroup("TowerOfInsolence3");
					worldParameters.set(TRAINING_AREA_TELEPORT, areaTeleport + 7);
					return npc.getId() + "-Selected.html";
				}
				break;
			}
			case "TOWER_OF_INSOLENCE_SELECT4":
			{
				final Instance world = npc.getInstanceWorld();
				final StatSet worldParameters = world.getParameters();
				final int areaTeleport = worldParameters.getInt(TRAINING_AREA_TELEPORT, 0);
				if (areaTeleport >= 1)
				{
					world.getNpcs().stream().filter(WorldObject::isAttackable).forEach(Npc::deleteMe);
					worldParameters.remove(TRAINING_AREA_TELEPORT);
					return npc.getId() + "-removeselect.html";
				}
				else if (areaTeleport == 0)
				{
					despawnAllGroups(world);
					world.spawnGroup("TowerOfInsolence4");
					worldParameters.set(TRAINING_AREA_TELEPORT, areaTeleport + 8);
					return npc.getId() + "-Selected.html";
				}
				break;
			}
			case "TOWER_OF_INSOLENCE_SELECT5":
			{
				final Instance world = npc.getInstanceWorld();
				final StatSet worldParameters = world.getParameters();
				final int areaTeleport = worldParameters.getInt(TRAINING_AREA_TELEPORT, 0);
				if (areaTeleport >= 1)
				{
					world.getNpcs().stream().filter(WorldObject::isAttackable).forEach(Npc::deleteMe);
					worldParameters.remove(TRAINING_AREA_TELEPORT);
					return npc.getId() + "-removeselect.html";
				}
				else if (areaTeleport == 0)
				{
					despawnAllGroups(world);
					world.spawnGroup("TowerOfInsolence5");
					worldParameters.set(TRAINING_AREA_TELEPORT, areaTeleport + 9);
					return npc.getId() + "-Selected.html";
				}
				break;
			}
			case "TOWER_OF_INSOLENCE_SELECT6":
			{
				final Instance world = npc.getInstanceWorld();
				final StatSet worldParameters = world.getParameters();
				final int areaTeleport = worldParameters.getInt(TRAINING_AREA_TELEPORT, 0);
				if (areaTeleport >= 1)
				{
					world.getNpcs().stream().filter(WorldObject::isAttackable).forEach(Npc::deleteMe);
					worldParameters.remove(TRAINING_AREA_TELEPORT);
					return npc.getId() + "-removeselect.html";
				}
				else if (areaTeleport == 0)
				{
					despawnAllGroups(world);
					world.spawnGroup("TowerOfInsolence6");
					worldParameters.set(TRAINING_AREA_TELEPORT, areaTeleport + 10);
					return npc.getId() + "-Selected.html";
				}
				break;
			}
			case "TOWER_OF_INSOLENCE_SELECT7":
			{
				final Instance world = npc.getInstanceWorld();
				final StatSet worldParameters = world.getParameters();
				final int areaTeleport = worldParameters.getInt(TRAINING_AREA_TELEPORT, 0);
				if (areaTeleport >= 1)
				{
					world.getNpcs().stream().filter(WorldObject::isAttackable).forEach(Npc::deleteMe);
					worldParameters.remove(TRAINING_AREA_TELEPORT);
					return npc.getId() + "-removeselect.html";
				}
				else if (areaTeleport == 0)
				{
					despawnAllGroups(world);
					world.spawnGroup("TowerOfInsolence7");
					worldParameters.set(TRAINING_AREA_TELEPORT, areaTeleport + 11);
					return npc.getId() + "-Selected.html";
				}
				break;
			}
			case "TOWER_OF_INSOLENCE_SELECT8":
			{
				final Instance world = npc.getInstanceWorld();
				final StatSet worldParameters = world.getParameters();
				final int areaTeleport = worldParameters.getInt(TRAINING_AREA_TELEPORT, 0);
				if (areaTeleport >= 1)
				{
					world.getNpcs().stream().filter(WorldObject::isAttackable).forEach(Npc::deleteMe);
					worldParameters.remove(TRAINING_AREA_TELEPORT);
					return npc.getId() + "-removeselect.html";
				}
				else if (areaTeleport == 0)
				{
					despawnAllGroups(world);
					world.spawnGroup("TowerOfInsolence8");
					worldParameters.set(TRAINING_AREA_TELEPORT, areaTeleport + 12);
					return npc.getId() + "-Selected.html";
				}
				break;
			}
			case "TOWER_OF_INSOLENCE_SELECT9":
			{
				final Instance world = npc.getInstanceWorld();
				final StatSet worldParameters = world.getParameters();
				final int areaTeleport = worldParameters.getInt(TRAINING_AREA_TELEPORT, 0);
				if (areaTeleport >= 1)
				{
					world.getNpcs().stream().filter(WorldObject::isAttackable).forEach(Npc::deleteMe);
					worldParameters.remove(TRAINING_AREA_TELEPORT);
					return npc.getId() + "-removeselect.html";
				}
				else if (areaTeleport == 0)
				{
					despawnAllGroups(world);
					world.spawnGroup("TowerOfInsolence9");
					worldParameters.set(TRAINING_AREA_TELEPORT, areaTeleport + 13);
					return npc.getId() + "-Selected.html";
				}
				break;
			}
			case "TOWER_OF_INSOLENCE_SELECT10":
			{
				final Instance world = npc.getInstanceWorld();
				final StatSet worldParameters = world.getParameters();
				final int areaTeleport = worldParameters.getInt(TRAINING_AREA_TELEPORT, 0);
				if (areaTeleport >= 1)
				{
					world.getNpcs().stream().filter(WorldObject::isAttackable).forEach(Npc::deleteMe);
					worldParameters.remove(TRAINING_AREA_TELEPORT);
					return npc.getId() + "-removeselect.html";
				}
				else if (areaTeleport == 0)
				{
					despawnAllGroups(world);
					world.spawnGroup("TowerOfInsolence10");
					worldParameters.set(TRAINING_AREA_TELEPORT, areaTeleport + 14);
					return npc.getId() + "-Selected.html";
				}
				break;
			}
			case "TOWER_OF_INSOLENCE_SELECT11":
			{
				final Instance world = npc.getInstanceWorld();
				final StatSet worldParameters = world.getParameters();
				final int areaTeleport = worldParameters.getInt(TRAINING_AREA_TELEPORT, 0);
				if (areaTeleport >= 1)
				{
					world.getNpcs().stream().filter(WorldObject::isAttackable).forEach(Npc::deleteMe);
					worldParameters.remove(TRAINING_AREA_TELEPORT);
					return npc.getId() + "-removeselect.html";
					
				}
				else if (areaTeleport == 0)
				{
					despawnAllGroups(world);
					world.spawnGroup("TowerOfInsolence11");
					worldParameters.set(TRAINING_AREA_TELEPORT, areaTeleport + 15);
					return npc.getId() + "-Selected.html";
				}
				break;
			}
			case "TOWER_OF_INSOLENCE_SELECT12":
			{
				final Instance world = npc.getInstanceWorld();
				final StatSet worldParameters = world.getParameters();
				final int areaTeleport = worldParameters.getInt(TRAINING_AREA_TELEPORT, 0);
				if (areaTeleport >= 1)
				{
					world.getNpcs().stream().filter(WorldObject::isAttackable).forEach(Npc::deleteMe);
					worldParameters.remove(TRAINING_AREA_TELEPORT);
					return npc.getId() + "-removeselect.html";
				}
				else if (areaTeleport == 0)
				{
					despawnAllGroups(world);
					world.spawnGroup("TowerOfInsolence12");
					worldParameters.set(TRAINING_AREA_TELEPORT, areaTeleport + 16);
					return npc.getId() + "-Selected.html";
				}
				break;
			}
			case "LOCAL_TELEPORT":
			{
				final Instance world = npc.getInstanceWorld();
				final int areaTeleport = world.getParameters().getInt(TRAINING_AREA_TELEPORT, 0);
				if (areaTeleport <= 4)
				{
					player.teleToLocation(LOCAL_AREA);
				}
				else if (areaTeleport >= 5)
				{
					player.teleToLocation(INSOLENCE_TOWER);
				}
				world.setParameter("BATTLEZONE", true);
				break;
			}
			case "LOCAL_TELEPORT_RETURN":
			{
				final Instance world = npc.getInstanceWorld();
				if (world.getParameters().getBoolean("BATTLEZONE", true))
				{
					player.teleToLocation(WAITING_AREA);
					world.setParameter("BATTLEZONE", false);
				}
				break;
			}
		}
		
		return null;
	}
	
	@Override
	public String onCreatureSee(Npc npc, Creature creature)
	{
		if (creature.isPlayer())
		{
			final Instance world = creature.getInstanceWorld();
			final Npc grown = world.getNpc(GROWN);
			startQuestTimer("npc_talk", 2000, grown, null);
			if (!world.getParameters().getBoolean("SayhaActive", false))
			{
				startQuestTimer("DEBUFF", 500, grown, null, true);
			}
		}
		return super.onCreatureSee(npc, creature);
	}
	
	@Override
	public void onInstanceEnter(Player player, Instance instance)
	{
		instance.setParameter("BATTLEZONE", false);
		player.sendPacket(new ExSendUIEvent(player, false, false, Math.min(3600, (int) (instance.getRemainingTime() / 1000)), 0, NpcStringId.TIME_LEFT));
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		switch (npc.getId())
		{
			case TIND:
			{
				final Instance world = npc.getInstanceWorld();
				if (world.getParameters().getBoolean("BATTLEZONE", true))
				{
					return npc.getId() + "-waitingzone.html";
				}
				
				final int areaTeleport = world.getParameters().getInt(TRAINING_AREA_TELEPORT, 0);
				if (areaTeleport >= 1)
				{
					return npc.getId() + "-teleport.html";
				}
				
				return npc.getId() + "-NoSummonStatus.html";
			}
			case TOKA:
			{
				return npc.getId() + "-toka.html";
			}
			case ERI:
			{
				return npc.getId() + "-eri.html";
			}
		}
		
		return super.onFirstTalk(npc, player);
	}
	
	@Override
	public String onAttack(Npc npc, Player attacker, int damage, boolean isSummon, Skill skill)
	{
		final Instance world = npc.getInstanceWorld();
		if ((world != null) && (world.getRemainingTime() < 600000) && !world.getParameters().getBoolean("spawnedBoss", false))
		{
			world.getParameters().set("spawnedBoss", true);
			startQuestTimer("SpawnBossClockList", 1000, npc, attacker);
		}
		return super.onAttack(npc, attacker, damage, isSummon, skill);
	}
	
	@Override
	public void onInstanceLeave(Player player, Instance instance)
	{
		player.sendPacket(new ExSendUIEvent(player, true, false, 3600, 0, NpcStringId.TIME_LEFT));
		player.sendPacket(new TimedHuntingZoneExit(player.getVariables().getInt(PlayerVariables.LAST_HUNTING_ZONE_ID, 0)));
		removeBuffs(player);
		instance.getParameters().remove(TRAINING_AREA_TELEPORT);
	}
	
	private void removeBuffs(Creature ch)
	{
		ch.getEffectList().stopEffects(info -> (info != null) && !info.getSkill().isStayAfterDeath() && (Arrays.binarySearch(NO_DELETE_BUFFS, info.getSkill().getId()) < 0), true, true);
	}
	
	private void despawnAllGroups(Instance world)
	{
		world.despawnGroup("PlainsOfTheLizardmen");
		world.despawnGroup("SelMahumBase");
		world.despawnGroup("OrcBarracksKerrs");
		world.despawnGroup("OrcBarracksTureks");
		world.despawnGroup("TowerOfInsolence1");
		world.despawnGroup("TowerOfInsolence2");
		world.despawnGroup("TowerOfInsolence3");
		world.despawnGroup("TowerOfInsolence4");
		world.despawnGroup("TowerOfInsolence5");
		world.despawnGroup("TowerOfInsolence6");
		world.despawnGroup("TowerOfInsolence7");
		world.despawnGroup("TowerOfInsolence8");
		world.despawnGroup("TowerOfInsolence9");
		world.despawnGroup("TowerOfInsolence10");
		world.despawnGroup("TowerOfInsolence11");
		world.despawnGroup("TowerOfInsolence12");
	}
	
	public static void main(String[] args)
	{
		new TrainingZone();
	}
}
