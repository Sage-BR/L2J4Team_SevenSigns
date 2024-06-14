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
package ai.bosses.SevenSignsRBs;

import java.util.List;

import org.l2jmobius.gameserver.enums.RaidBossStatus;
import org.l2jmobius.gameserver.instancemanager.DBSpawnManager;
import org.l2jmobius.gameserver.instancemanager.ZoneManager;
import org.l2jmobius.gameserver.model.Location;
import org.l2jmobius.gameserver.model.Party;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.skill.AbnormalVisualEffect;
import org.l2jmobius.gameserver.model.zone.type.NoRestartZone;

import ai.AbstractNpcAI;

/**
 * @author RobikBobik
 * @NOTE: Retail like work
 */
public class SevenSignsRBs extends AbstractNpcAI
{
	// NPCs
	private static final int ANAKIM_GATEKEEPER_SPIRIT = 31089;
	private static final int LILITH_GATEKEEPER_SPIRIT = 31087;
	private static final int GATEKEEPER_SPIRIT_OUT_TELEPORT = 31088;
	private static final int ANAKIM = 25286;
	private static final int LILITH = 25283;
	// Misc
	private static final int MAX_PLAYERS_IN_ZONE = 300;
	private static final NoRestartZone ANAKIM_ZONE = ZoneManager.getInstance().getZoneById(70052, NoRestartZone.class);
	private static final NoRestartZone LILITH_ZONE = ZoneManager.getInstance().getZoneById(70053, NoRestartZone.class);
	// TELEPORTS
	private static final Location TELEPORT_TO_LILITH = new Location(185657, -10112, -5496);
	private static final Location TELEPORT_TO_ANAKIM = new Location(-7283, 19086, -5496);
	
	private static final Location[] TELEPORT_TO_DARK_ELVEN =
	{
		new Location(12168, 17149, -4575),
		new Location(11688, 18219, -4585),
		new Location(10502, 17112, -4588),
		new Location(11169, 15922, -4585),
	};
	
	private static final Location[] TELEPORT_TO_ADEN =
	{
		new Location(148053, 26935, -2206),
		new Location(148053, 28017, -2269),
		new Location(146558, 28017, -2269),
		new Location(146558, 26935, -2206),
	};
	
	public SevenSignsRBs()
	{
		addStartNpc(ANAKIM_GATEKEEPER_SPIRIT, LILITH_GATEKEEPER_SPIRIT);
		addTalkId(ANAKIM_GATEKEEPER_SPIRIT, LILITH_GATEKEEPER_SPIRIT, GATEKEEPER_SPIRIT_OUT_TELEPORT);
		addKillId(ANAKIM, LILITH);
		addAttackId(ANAKIM, LILITH);
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		switch (event)
		{
			case "ANAKIM_ENTER":
			{
				if (DBSpawnManager.getInstance().getStatus(ANAKIM) != RaidBossStatus.ALIVE)
				{
					player.sendMessage("Anakim is not present at the moment");
					break;
				}
				if (player.isInParty())
				{
					final Party party = player.getParty();
					final boolean isInCC = party.isInCommandChannel();
					final List<Player> members = (isInCC) ? party.getCommandChannel().getMembers() : party.getMembers();
					if (members.size() > (MAX_PLAYERS_IN_ZONE - ANAKIM_ZONE.getPlayersInside().size()))
					{
						player.sendMessage("Anakims Sanctum reached " + MAX_PLAYERS_IN_ZONE + " players. You cannot enter now.");
					}
					else
					{
						for (Player member : members)
						{
							if (!member.isInsideRadius3D(npc, 1000))
							{
								player.sendMessage("Player " + member.getName() + " must go closer to Gatekeeper Spirit.");
							}
							member.teleToLocation(TELEPORT_TO_ANAKIM);
						}
					}
				}
				else if (player.isGM())
				{
					player.teleToLocation(TELEPORT_TO_ANAKIM);
					player.sendMessage("SYS: You have entered as GM/Admin to Anakim Instance");
				}
				else
				{
					if (!player.isInsideRadius3D(npc, 1000))
					{
						player.sendMessage("You must go closer to Gatekeeper Spirit.");
						break;
					}
					player.teleToLocation(TELEPORT_TO_ANAKIM);
				}
				break;
			}
			case "LILITH_ENTER":
			{
				if (DBSpawnManager.getInstance().getStatus(LILITH) != RaidBossStatus.ALIVE)
				{
					player.sendMessage("Lilith is not present at the moment");
					break;
				}
				if (player.isInParty())
				{
					final Party party = player.getParty();
					final boolean isInCC = party.isInCommandChannel();
					final List<Player> members = (isInCC) ? party.getCommandChannel().getMembers() : party.getMembers();
					if (members.size() > (MAX_PLAYERS_IN_ZONE - LILITH_ZONE.getPlayersInside().size()))
					{
						player.sendMessage("Lilith Sanctum reached " + MAX_PLAYERS_IN_ZONE + " players. You cannot enter now.");
					}
					else
					{
						for (Player member : members)
						{
							if (!member.isInsideRadius3D(npc, 1000))
							{
								player.sendMessage("Player " + member.getName() + " must go closer to Gatekeeper Spirit.");
							}
							member.teleToLocation(TELEPORT_TO_LILITH);
						}
					}
				}
				else if (player.isGM())
				{
					player.teleToLocation(TELEPORT_TO_LILITH);
					player.sendMessage("SYS: You have entered as GM/Admin to Anakim Instance");
				}
				else
				{
					if (!player.isInsideRadius3D(npc, 1000))
					{
						player.sendMessage("You must go closer to Gatekeeper Spirit.");
						break;
					}
					player.teleToLocation(TELEPORT_TO_LILITH);
				}
				break;
			}
			case "REMOVE_PLAYERS_FROM_ZONE_ANAKIM":
			{
				for (Creature charInside : ANAKIM_ZONE.getCharactersInside())
				{
					if ((charInside != null) && charInside.isPlayer())
					{
						charInside.teleToLocation(-20185 + getRandom(50), 13476 + getRandom(50), -4901);
					}
				}
				break;
			}
			case "REMOVE_PLAYERS_FROM_ZONE_LILITH":
			{
				for (Creature charInside : LILITH_ZONE.getCharactersInside())
				{
					if ((charInside != null) && charInside.isPlayer())
					{
						charInside.teleToLocation(171346 + getRandom(50), -17599 + getRandom(50), -4901);
					}
				}
				break;
			}
			case "TELEPORT_OUT":
			{
				if (ANAKIM_ZONE.isInsideZone(player.getLocation()))
				{
					final Location destination = TELEPORT_TO_DARK_ELVEN[getRandom(TELEPORT_TO_DARK_ELVEN.length)];
					player.teleToLocation(destination.getX() + getRandom(100), destination.getY() + getRandom(100), destination.getZ());
					break;
				}
				if (LILITH_ZONE.isInsideZone(player.getLocation()))
				{
					final Location destination = TELEPORT_TO_ADEN[getRandom(TELEPORT_TO_ADEN.length)];
					player.teleToLocation(destination.getX() + getRandom(100), destination.getY() + getRandom(100), destination.getZ());
					break;
				}
				break;
			}
			case "ANAKIM_DEATH_CAST_LILITH_INVUL":
			{
				if (DBSpawnManager.getInstance().getStatus(LILITH) == RaidBossStatus.ALIVE)
				{
					Npc LILITH_NPC = DBSpawnManager.getInstance().getNpcs().get(LILITH);
					LILITH_NPC.setInvul(true);
					LILITH_NPC.getEffectList().startAbnormalVisualEffect(AbnormalVisualEffect.INVINCIBILITY);
					startQuestTimer("LILITH_INVUL_END", 300000, null, player);
				}
				
				break;
			}
			case "LILITH_DEATH_CAST_ANAKIM_INVUL":
			{
				if (DBSpawnManager.getInstance().getStatus(ANAKIM) == RaidBossStatus.ALIVE)
				{
					Npc ANAKIM_NPC = DBSpawnManager.getInstance().getNpcs().get(ANAKIM);
					ANAKIM_NPC.setInvul(true);
					ANAKIM_NPC.getEffectList().startAbnormalVisualEffect(AbnormalVisualEffect.INVINCIBILITY);
					startQuestTimer("ANAKIM_INVUL_END", 300000, null, player);
				}
				break;
			}
			case "LILITH_INVUL_END":
			{
				Npc LILITH_NPC = DBSpawnManager.getInstance().getNpcs().get(LILITH);
				LILITH_NPC.setInvul(false);
				LILITH_NPC.getEffectList().stopAbnormalVisualEffect(AbnormalVisualEffect.INVINCIBILITY);
				break;
			}
			case "ANAKIM_INVUL_END":
			{
				Npc ANAKIM_NPC = DBSpawnManager.getInstance().getNpcs().get(ANAKIM);
				ANAKIM_NPC.setInvul(false);
				ANAKIM_NPC.getEffectList().stopAbnormalVisualEffect(AbnormalVisualEffect.INVINCIBILITY);
				break;
			}
		}
		return null;
	}
	
	@Override
	public String onKill(Npc npc, Player player, boolean isSummon)
	{
		switch (npc.getId())
		{
			case ANAKIM:
			{
				startQuestTimer("ANAKIM_DEATH_CAST_LILITH_INVUL", 1000, null, null);
				addSpawn(GATEKEEPER_SPIRIT_OUT_TELEPORT, -6664, 18501, -5495, 0, false, 600000, false, npc.getInstanceId());
				break;
			}
			case LILITH:
			{
				startQuestTimer("LILITH_DEATH_CAST_ANAKIM_INVUL", 1000, null, null);
				addSpawn(GATEKEEPER_SPIRIT_OUT_TELEPORT, 185062, -9612, -5493, 0, false, 600000, false, npc.getInstanceId());
				break;
			}
		}
		return super.onKill(npc, player, isSummon);
	}
	
	public static void main(String[] args)
	{
		new SevenSignsRBs();
	}
}
