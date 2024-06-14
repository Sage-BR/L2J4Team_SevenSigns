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
package ai.areas.PaganTemple;

import java.util.Calendar;

import org.l2j.commons.threads.ThreadPool;
import org.l2j.commons.time.SchedulingPattern;
import org.l2j.gameserver.data.xml.SpawnData;
import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.spawns.SpawnGroup;
import org.l2j.gameserver.model.spawns.SpawnTemplate;

import ai.AbstractNpcAI;

/**
 * @author Index
 */
public class PrimeTimePaganTemple extends AbstractNpcAI
{
	private static final SchedulingPattern FIRST_PATTERN = new SchedulingPattern("0 18 * * 5");
	private static final SchedulingPattern SECOND_PATTERN = new SchedulingPattern("0 22 * * 5");
	private static final SchedulingPattern REMOVE_PATTERN = new SchedulingPattern("00 00 * * *");
	
	private static final SpawnTemplate FIRST_SPAWN; // Prime Spawn First hall 22-00 -- 00-00 only 5
	private static final SpawnTemplate SECOND_SPAWN; // No Prime Spawn First hall 18-00 -- 00-00
	private static final SpawnTemplate THIRD_SPAWN; // Prime Spawn Second hall 22-00 -- 00-00 only 5
	private static final SpawnTemplate FORTH_SPAWN; // No Prime Spawn Second hall 18-00 -- 00-00
	static
	{
		SpawnTemplate first = null;
		SpawnTemplate second = null;
		SpawnTemplate third = null;
		SpawnTemplate fourth = null;
		for (SpawnTemplate spawnTemplate : SpawnData.getInstance().getSpawns())
		{
			for (SpawnGroup spawnGroups : spawnTemplate.getGroups())
			{
				if ((spawnGroups == null) || (spawnGroups.getName() == null))
				{
					continue;
				}
				
				switch (spawnGroups.getName())
				{
					case "Prime Spawn First hall":
					{
						first = spawnTemplate;
						break;
					}
					case "No Prime Spawn First hall":
					{
						second = spawnTemplate;
						break;
					}
					case "Prime Spawn Second hall":
					{
						third = spawnTemplate;
						break;
					}
					case "No Prime Spawn Second hall":
					{
						fourth = spawnTemplate;
						break;
					}
				}
			}
		}
		FIRST_SPAWN = first;
		SECOND_SPAWN = second;
		THIRD_SPAWN = third;
		FORTH_SPAWN = fourth;
	}
	
	private PrimeTimePaganTemple()
	{
		spawnOrStartScheduler();
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		final int id = Integer.parseInt(event);
		switch (id)
		{
			case 1:
			{
				changeFirstRoomMonsters(false);
				break;
			}
			case 2:
			{
				changeFirstRoomMonsters(true);
				break;
			}
			case 3:
			{
				changeSecondRoomMonsters(false);
				break;
			}
			case 4:
			{
				changeSecondRoomMonsters(true);
				break;
			}
		}
		return super.onEvent(event, npc, player);
	}
	
	private void spawnOrStartScheduler()
	{
		final int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		final int hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		// final int minutes = Calendar.getInstance().get(Calendar.MINUTE);
		if (dayOfWeek == 5)
		{
			if (hourOfDay < 18)
			{
				changeFirstRoomMonsters(true);
				changeSecondRoomMonsters(true);
				startSpawnTask();
			}
			if (hourOfDay >= 18)
			{
				// private final static SpawnTemplate FIRST_SPAWN; // Prime Spawn First hall 22-00 -- 00-00
				// private final static SpawnTemplate SECOND_SPAWN; // No Prime Spawn First hall 18-00 -- 00-00
				changeFirstRoomMonsters(false);
				ThreadPool.schedule(() -> changeFirstRoomMonsters(true), REMOVE_PATTERN.getDelayToNextFromNow());
			}
			if (hourOfDay >= 22)
			{
				// private final static SpawnTemplate THIRD_SPAWN; // Prime Spawn Second hall 22-00 -- 00-00
				// private final static SpawnTemplate FORTH_SPAWN; // No Prime Spawn Second hall 18-00 -- 00-00
				changeSecondRoomMonsters(false);
				ThreadPool.schedule(() -> changeSecondRoomMonsters(true), REMOVE_PATTERN.getDelayToNextFromNow());
			}
		}
		else
		{
			changeFirstRoomMonsters(true);
			changeSecondRoomMonsters(true);
			startSpawnTask();
		}
	}
	
	private void changeFirstRoomMonsters(boolean despawnNew)
	{
		if ((FIRST_SPAWN == null) || (SECOND_SPAWN == null))
		{
			LOGGER.warning(getClass().getSimpleName() + ": Spawns is null! Check me!");
			return;
		}
		// On Friday from 18:00 to 00:00 in Pagan, the first group of monsters is replaced by "elite".
		// On any other days and after 23:59 in Pagan, the first group becomes normal again.
		// private final static SpawnTemplate FIRST_SPAWN; // Prime Spawn First hall 22-00 -- 00-00
		// private final static SpawnTemplate SECOND_SPAWN; // No Prime Spawn First hall 18-00 -- 00-00
		if (despawnNew)
		{
			FIRST_SPAWN.despawnAll();
			SECOND_SPAWN.spawnAll();
		}
		else
		{
			FIRST_SPAWN.spawnAll();
			SECOND_SPAWN.despawnAll();
		}
	}
	
	private void changeSecondRoomMonsters(boolean despawnNew)
	{
		if ((THIRD_SPAWN == null) || (FORTH_SPAWN == null))
		{
			LOGGER.warning(getClass().getSimpleName() + ": Spawns is null! Check me!");
			return;
		}
		// On Friday from 22:00 to 00:00 in Pagan, the second group of monsters is replaced by "elite".
		// On any other days and after 23:59 in Pagan, the second group becomes normal again.
		// private final static SpawnTemplate THIRD_SPAWN; // Prime Spawn Second hall 22-00 -- 00-00 Friday only
		// private final static SpawnTemplate FORTH_SPAWN; // No Prime Spawn Second hall 18-00 -- 00-00 normal.
		if (despawnNew)
		{
			THIRD_SPAWN.despawnAll();
			FORTH_SPAWN.spawnAll();
		}
		else
		{
			THIRD_SPAWN.spawnAll();
			FORTH_SPAWN.despawnAll();
		}
	}
	
	private void startSpawnTask()
	{
		ThreadPool.schedule(() -> changeFirstRoomMonsters(false), FIRST_PATTERN.getDelayToNextFromNow());
		ThreadPool.schedule(() -> changeSecondRoomMonsters(false), SECOND_PATTERN.getDelayToNextFromNow());
	}
	
	public static void main(String[] args)
	{
		new PrimeTimePaganTemple();
	}
}
