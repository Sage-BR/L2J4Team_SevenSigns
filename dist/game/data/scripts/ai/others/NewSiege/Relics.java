package ai.others.NewSiege;

import java.util.HashSet;
import java.util.Set;

import org.l2j.gameserver.instancemanager.ZoneManager;
import org.l2j.gameserver.model.Location;
import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.zone.type.SiegeZone;
import org.l2j.gameserver.network.NpcStringId;
import org.l2j.gameserver.network.serverpackets.ExShowScreenMessage;

import ai.AbstractNpcAI;

public class Relics extends AbstractNpcAI
{
	
	private final static int RELIC_GIRAN = 36792;
	private final static int RELIC_GODDART = 36793;
	
	private final static int ARTIFACT_GIRAN = 35147;
	private final static int ARTIFACT_GODDART_1 = 35323;
	private final static int ARTIFACT_GODDART_2 = 35322;
	
	private static final Set<Integer> ARTIFACTS = new HashSet<>();
	static
	{
		ARTIFACTS.add(35147);
		ARTIFACTS.add(35323);
		ARTIFACTS.add(35322);
		
	}
	
	private static final SiegeZone GIRAN_SIEGE_ZONE = ZoneManager.getInstance().getZoneByName("giran_castle_battlefield_territory", SiegeZone.class);
	
	public Relics()
	{
		addKillId(RELIC_GIRAN, RELIC_GODDART);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		if (ARTIFACTS.contains(npc.getId()))
		{
			npc.setDecayed(false);
			npc.disableCoreAI(true);
			npc.setImmobilized(true);
		}
		return super.onSpawn(npc);
	}
	
	// * TODO pend rework
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		if (npc.getId() == RELIC_GIRAN)
		{
			GIRAN_SIEGE_ZONE.broadcastPacket(new ExShowScreenMessage(NpcStringId.GIRAN_HOLY_ARTIFACT_HAS_APPEARED, ExShowScreenMessage.TOP_CENTER, 10000, true));
			addSpawn(ARTIFACT_GIRAN, new Location(113926, 145088, -2760), false, 60 * 60 * 1000); // 1 hour
			
		}
		else
		{
			addSpawn(ARTIFACT_GODDART_1, new Location(145288, -45385, -1727), false, 60 * 60 * 1000); // 1 hour
			addSpawn(ARTIFACT_GODDART_2, new Location(149652, -45371, -1727), false, 60 * 60 * 1000); // 1 hour
			GIRAN_SIEGE_ZONE.broadcastPacket(new ExShowScreenMessage(NpcStringId.GIRAN_HOLY_ARTIFACT_HAS_APPEARED, ExShowScreenMessage.TOP_CENTER, 10000, true));
		}
		
		return null;
	}
	
	public static void main(String[] args)
	{
		new Relics();
	}
}
