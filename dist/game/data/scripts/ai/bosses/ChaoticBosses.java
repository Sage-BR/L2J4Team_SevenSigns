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
package ai.bosses;

import java.util.Calendar;

import org.l2j.commons.threads.ThreadPool;
import org.l2j.commons.util.CommonUtil;
import org.l2j.gameserver.data.SpawnTable;
import org.l2j.gameserver.data.xml.NpcData;
import org.l2j.gameserver.instancemanager.DBSpawnManager;
import org.l2j.gameserver.model.Spawn;
import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.templates.NpcTemplate;

import ai.AbstractNpcAI;

/**
 * @URL https://l2central.info/essence/articles/23.html?lang=en
 * @author NasSeKa
 */
public final class ChaoticBosses extends AbstractNpcAI
{
	private static final int[] RAID_BOSSES =
	{
		29170, // Chaotic Core
		29171, // Chaotic Orfen
		29172, // Chaotic Queen Ant
		29173, // Chaotic Zaken
	};
	
	private ChaoticBosses()
	{
		addKillId(RAID_BOSSES);
		
		// Schedule reset everyday at 20:00.
		final long currentTime = System.currentTimeMillis();
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 20);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		if (calendar.getTimeInMillis() < currentTime)
		{
			calendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		
		// Daily reset task.
		final long calendarTime = calendar.getTimeInMillis();
		final long startDelay = Math.max(0, calendarTime - currentTime);
		ThreadPool.scheduleAtFixedRate(this::onSpawn, startDelay, 86400000); // 86400000 = 1 day
	}
	
	private void onSpawn()
	{
		for (int npcId : RAID_BOSSES)
		{
			for (Spawn spawn : SpawnTable.getInstance().getSpawns(npcId))
			{
				for (Npc monster : spawn.getSpawnedNpcs())
				{
					if (!monster.isAlikeDead())
					{
						DBSpawnManager.getInstance().deleteSpawn(spawn, true);
						monster.deleteMe();
					}
				}
			}
		}
		
		final int chaosBossId = RAID_BOSSES[getRandom(0, 3)];
		final NpcTemplate template = NpcData.getInstance().getTemplate(chaosBossId);
		try
		{
			if (template != null)
			{
				final Spawn spawn = new Spawn(template);
				spawn.setXYZ(191512, 21855, -3680);
				spawn.setRespawnDelay(86400000);
				DBSpawnManager.getInstance().addNewSpawn(spawn, true);
			}
		}
		catch (Exception e)
		{
			LOGGER.warning(getClass().getSimpleName() + ": Caused an exception " + e.getMessage());
		}
	}
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		if (CommonUtil.contains(RAID_BOSSES, npc.getId()))
		{
			for (int npcId : RAID_BOSSES)
			{
				for (Spawn spawn : SpawnTable.getInstance().getSpawns(npcId))
				{
					for (Npc monster : spawn.getSpawnedNpcs())
					{
						if (!monster.isAlikeDead())
						{
							DBSpawnManager.getInstance().deleteSpawn(spawn, true);
							monster.deleteMe();
						}
					}
				}
			}
		}
		
		return super.onKill(npc, killer, isSummon);
	}
	
	public static void main(String[] args)
	{
		new ChaoticBosses();
	}
}
