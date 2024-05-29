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
package ai.others.ClanStrongholdDevice;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.l2j.gameserver.enums.ChatType;
import org.l2j.gameserver.model.Location;
import org.l2j.gameserver.model.World;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.SkillHolder;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.network.NpcStringId;
import org.l2j.gameserver.network.serverpackets.ExChangeNpcState;
import org.l2j.gameserver.network.serverpackets.ExShowScreenMessage;
import org.l2j.gameserver.network.serverpackets.NpcSay;

import ai.AbstractNpcAI;

/**
 * @author Index
 */
public class ClanStrongholdDevice extends AbstractNpcAI
{
	// NPCs
	private static final int CLAN_STRONGHOLD_DEVICE = 34156;
	private static final int[] NEARBY_MONSTER_IDS =
	{
		22200, // Porta
		22201, // Excuro
		22202, // Mordeo
		22203, // Ricenseo
		22204, // Krator
		22205, // Catherok
		22206, // Premo
		22207, // Validus
		22208, // Dicor
		22209, // Perum
		22210, // Torfe
		22211, // Death Lord
	};
	// Skill
	private static final SkillHolder CLAN_STRONGHOLD_EFFECT = new SkillHolder(48078, 1);
	// Misc
	private static final Map<Integer, Integer> CURRENT_CLAN_ID = new ConcurrentHashMap<>(); // Clan id key - NPC object id value (can be taken from npc.getScriptValue)
	private static final Map<Integer, Long> LAST_ATTACK = new ConcurrentHashMap<>(); // NPC object id key - Time value
	private static final Map<Integer, Location> DEVICE_LOCATION = new ConcurrentHashMap<>();
	
	private ClanStrongholdDevice()
	{
		addCreatureSeeId(CLAN_STRONGHOLD_DEVICE);
		addFirstTalkId(CLAN_STRONGHOLD_DEVICE);
		addAttackId(CLAN_STRONGHOLD_DEVICE);
		addSpawnId(CLAN_STRONGHOLD_DEVICE);
		addTalkId(CLAN_STRONGHOLD_DEVICE);
		addKillId(CLAN_STRONGHOLD_DEVICE);
		addKillId(NEARBY_MONSTER_IDS);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		if ((npc.getTemplate().getId() != CLAN_STRONGHOLD_DEVICE) || (player == null) || (event == null))
		{
			return super.onAdvEvent(event, npc, player);
		}
		
		if (event.equals("capture"))
		{
			if (npc.isAlikeDead())
			{
				return super.onAdvEvent(event, npc, player);
			}
			
			if (CURRENT_CLAN_ID.containsKey(npc.getScriptValue()))
			{
				return "34156-02.htm";
			}
			
			if (player.getClan() == null)
			{
				return "34156-03.htm";
			}
			
			CURRENT_CLAN_ID.put(player.getClanId(), npc.getObjectId());
			npc.setScriptValue(player.getClanId());
			npc.setTitle(player.getClan().getName());
			npc.setClanId(player.getClanId());
			npc.setDisplayEffect(2);
			npc.setInvul(false);
			npc.broadcastInfo();
			return "34156-01.htm";
		}
		else if (event.equals("back"))
		{
			if (npc.isAlikeDead())
			{
				return super.onAdvEvent(event, npc, player);
			}
			
			return npc.getId() + (CURRENT_CLAN_ID.containsKey(npc.getScriptValue()) ? "-02" : "") + ".htm";
		}
		
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		if (npc.isAlikeDead())
		{
			return super.onFirstTalk(npc, player);
		}
		
		return npc.getId() + (CURRENT_CLAN_ID.containsKey(npc.getScriptValue()) ? "-01" : "") + ".htm";
	}
	
	@Override
	public String onCreatureSee(Npc npc, Creature creature)
	{
		if (npc.getTemplate().getId() == CLAN_STRONGHOLD_DEVICE)
		{
			creature.sendPacket(new ExChangeNpcState(npc.getObjectId(), CURRENT_CLAN_ID.containsKey(npc.getScriptValue()) ? 1 : 2));
		}
		return super.onCreatureSee(npc, creature);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		if (npc.getTemplate().getId() != CLAN_STRONGHOLD_DEVICE)
		{
			return super.onSpawn(npc);
		}
		
		npc.disableCoreAI(true);
		npc.setAutoAttackable(false);
		npc.setImmobilized(true);
		npc.setDisplayEffect(1);
		npc.setUndying(false);
		npc.setScriptValue(0);
		npc.setInvul(true);
		npc.setClanId(0);
		npc.setTitle("");
		npc.broadcastInfo();
		npc.broadcastPacket(new ExShowScreenMessage(NpcStringId.THE_CLAN_STRONGHOLD_DEVICE_CAN_BE_CAPTURED, 2, 5000, true));
		DEVICE_LOCATION.put(npc.getObjectId(), npc.getLocation());
		
		return super.onSpawn(npc);
	}
	
	@Override
	public String onAttack(Npc npc, Player attacker, int damage, boolean isSummon, Skill skill)
	{
		if (CURRENT_CLAN_ID.containsKey(npc.getScriptValue()) && (LAST_ATTACK.getOrDefault(npc.getObjectId(), 0L) < (System.currentTimeMillis() - 5000)))
		{
			npc.broadcastPacket(new NpcSay(npc, ChatType.NPC_GENERAL, NpcStringId.AT_TACK_SIG_NAL_DE_TEC_TED_S1).addStringParameter(attacker.getName()));
			LAST_ATTACK.put(npc.getObjectId(), System.currentTimeMillis());
		}
		return super.onAttack(npc, attacker, damage, isSummon, skill);
	}
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		if (npc.getTemplate().getId() == CLAN_STRONGHOLD_DEVICE)
		{
			npc.setClanId(0);
			CURRENT_CLAN_ID.remove(npc.getScriptValue());
			LAST_ATTACK.remove(npc.getObjectId());
			DEVICE_LOCATION.remove(npc.getObjectId());
			return super.onKill(npc, killer, isSummon);
		}
		
		if (!CURRENT_CLAN_ID.containsKey(killer.getClanId()))
		{
			return super.onKill(npc, killer, isSummon);
		}
		
		CLAN_STRONGHOLD_EFFECT.getSkill().activateSkill(npc, killer);
		for (Player clanMate : World.getInstance().getVisibleObjects(killer, Player.class))
		{
			if (clanMate.getClanId() != killer.getClanId())
			{
				continue;
			}
			
			final Location deviceLocation = DEVICE_LOCATION.get(CURRENT_CLAN_ID.get(killer.getClanId()));
			if ((clanMate.calculateDistance2D(deviceLocation) < 900) && (Math.abs(clanMate.getZ() - deviceLocation.getZ()) < 200))
			{
				clanMate.doCast(CLAN_STRONGHOLD_EFFECT.getSkill());
			}
		}
		
		return super.onKill(npc, killer, isSummon);
	}
	
	public static void main(String[] args)
	{
		new ClanStrongholdDevice();
	}
}
