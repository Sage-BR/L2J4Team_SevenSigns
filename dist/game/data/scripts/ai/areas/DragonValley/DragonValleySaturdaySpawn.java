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
package ai.areas.DragonValley;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

import org.l2j.commons.threads.ThreadPool;
import org.l2j.gameserver.data.xml.SpawnData;

import ai.AbstractNpcAI;

public class DragonValleySaturdaySpawn extends AbstractNpcAI
{
	private boolean _isScheduling = false;
	
	private DragonValleySaturdaySpawn()
	{
		scheduleDragonValleySpawns();
	}
	
	private void changeDragonValleySpawns(boolean spawnDragons)
	{
		SpawnData.getInstance().getSpawns().stream().filter(spawnTemplate -> (spawnTemplate.getName() != null) && spawnTemplate.getName().equals("Dragon Valley No Antharas")).forEach(spawnTemplate -> spawnTemplate.getGroups().forEach(holder -> holder.despawnAll()));
		SpawnData.getInstance().getSpawns().stream().filter(spawnTemplate -> (spawnTemplate.getName() != null) && spawnTemplate.getName().equals("Dragon Valley Antharas")).forEach(spawnTemplate -> spawnTemplate.getGroups().forEach(holder -> holder.despawnAll()));
		if (spawnDragons)
		{
			SpawnData.getInstance().getSpawns().stream().filter(spawnTemplate -> (spawnTemplate.getName() != null) && spawnTemplate.getName().equals("Dragon Valley Antharas")).forEach(spawnTemplate -> spawnTemplate.getGroups().forEach(holder -> holder.spawnAll()));
		}
		else
		{
			SpawnData.getInstance().getSpawns().stream().filter(spawnTemplate -> (spawnTemplate.getName() != null) && spawnTemplate.getName().equals("Dragon Valley No Antharas")).forEach(spawnTemplate -> spawnTemplate.getGroups().forEach(holder -> holder.spawnAll()));
		}
	}
	
	private void scheduleDragonValleySpawns()
	{
		if (_isScheduling)
		{
			return;
		}
		_isScheduling = true;
		
		final long currentMillis = System.currentTimeMillis();
		final Calendar saturday = Calendar.getInstance();
		saturday.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		saturday.set(Calendar.HOUR_OF_DAY, 0);
		saturday.set(Calendar.MINUTE, 0);
		saturday.set(Calendar.SECOND, 0);
		saturday.set(Calendar.MILLISECOND, 0);
		
		final Calendar today = Calendar.getInstance();
		if ((saturday.getTimeInMillis() <= System.currentTimeMillis()) && (today.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)) // Is Saturday (and not Sunday).
		{
			changeDragonValleySpawns(true);
			
			final LocalDateTime start = LocalDateTime.now();
			final LocalDateTime end = start.plusDays(1).truncatedTo(ChronoUnit.DAYS);
			final long millis = Duration.between(start, end).toMillis() + 10000; // Sunday +10 sec to make sure it doesnt happen at 23:59 saturday.
			ThreadPool.schedule(() ->
			{
				scheduleDragonValleySpawns();
				_isScheduling = false;
			}, millis);
		}
		else // Not saturday.
		{
			changeDragonValleySpawns(false);
			ThreadPool.schedule(() ->
			{
				scheduleDragonValleySpawns();
				_isScheduling = false;
			}, Math.max(0, (saturday.getTimeInMillis() - currentMillis) + 10000)); // Time remaining till saturday (10s delay just in case?).
		}
	}
	
	public static void main(String[] args)
	{
		new DragonValleySaturdaySpawn();
	}
}
