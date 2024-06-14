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
package ai.areas.Hellbound;

import java.util.Calendar;
import java.util.concurrent.Future;

import org.l2j.commons.threads.ThreadPool;
import org.l2j.commons.time.SchedulingPattern;
import org.l2j.gameserver.instancemanager.ZoneManager;
import org.l2j.gameserver.model.Location;
import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.zone.type.ScriptZone;
import org.l2j.gameserver.network.serverpackets.ExShowScreenMessage;

import ai.AbstractNpcAI;

/**
 * @author GustavoFonseca
 */
public class Satina extends AbstractNpcAI
{
	// NPC
	private static final int SATINA = 25934;
	// Locations
	private static final Location[] SPAWNS =
	{
		new Location(7647, 242440, -2429),
		new Location(8209, 236339, -2290),
	};
	// Zone
	private static final ScriptZone ZONE = ZoneManager.getInstance().getZoneByName("hellboud_island1", ScriptZone.class);
	// Misc
	private static final int ALIVE_MILLISECONDS = 3600000;
	private static final String SATINA_RESPAWN_PATTERN = "0 14 * * 6";
	private static SchedulingPattern _respawnPattern = null;
	private static Future<?> _deleteTask;
	private static Future<?> _startTask;
	
	private Satina()
	{
		addKillId(SATINA);
		_respawnPattern = new SchedulingPattern(SATINA_RESPAWN_PATTERN);
		_startTask = ThreadPool.schedule(new ScheduleAiTask(), (getNextRespawn() - System.currentTimeMillis()));
	}
	
	public static AbstractNpcAI provider()
	{
		return new Satina();
	}
	
	public class ScheduleAiTask implements Runnable
	{
		private Npc _npc;
		
		public ScheduleAiTask()
		{
		}
		
		@Override
		public void run()
		{
			final Calendar calendar = Calendar.getInstance();
			if (calendar.get(Calendar.DAY_OF_WEEK) == 7)
			{
				if (_npc != null)
				{
					_npc.deleteMe();
				}
				
				try
				{
					final Location loc = getRandomEntry(SPAWNS);
					_npc = addSpawn(SATINA, loc.getX(), loc.getY(), loc.getZ(), 49151, false, 0, true);
					_deleteTask = ThreadPool.schedule(() -> deleteMe(_npc), ALIVE_MILLISECONDS);
					ZONE.broadcastPacket(new ExShowScreenMessage("Raidboss: Vampire Queen Satina - Spawned", 6000));
				}
				catch (Exception e)
				{
				}
			}
			else
			{
				if (_startTask != null)
				{
					_startTask.cancel(true);
				}
				_startTask = ThreadPool.schedule(new ScheduleAiTask(), (getNextRespawn() - System.currentTimeMillis()));
			}
		}
	}
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		if (npc != null)
		{
			if (_startTask != null)
			{
				_startTask.cancel(true);
			}
			
			final long millsecondsToNextRespawn = (getNextRespawn() - System.currentTimeMillis());
			_startTask = ThreadPool.schedule(new ScheduleAiTask(), millsecondsToNextRespawn);
		}
		
		if (_deleteTask != null)
		{
			_deleteTask.cancel(true);
		}
		
		return super.onKill(npc, killer, isSummon);
	}
	
	public void deleteMe(Npc npc)
	{
		if (_startTask != null)
		{
			_startTask.cancel(true);
		}
		
		final long millsecondsToNextRespawn = (getNextRespawn() - System.currentTimeMillis());
		_startTask = ThreadPool.schedule(new ScheduleAiTask(), millsecondsToNextRespawn);
		
		if (npc != null)
		{
			npc.deleteMe();
		}
	}
	
	public long getNextRespawn()
	{
		final long respawnTime = _respawnPattern.next(System.currentTimeMillis());
		// LOGGER.info(getClass().getSimpleName() + ": New " + "Satina" + " respawn time to " + Util.formatDate(new Date(respawnTime), "dd.MM.yyyy HH:mm"));
		return respawnTime;
	}
	
	public static void main(String[] args)
	{
		new Satina();
	}
}
