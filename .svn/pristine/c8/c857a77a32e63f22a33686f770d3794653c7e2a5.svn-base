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
package ai.areas.Hellbound;

import java.util.Calendar;

import org.l2jmobius.commons.threads.ThreadPool;
import org.l2jmobius.gameserver.instancemanager.ZoneManager;
import org.l2jmobius.gameserver.model.zone.ZoneType;

import ai.AbstractNpcAI;

/**
 * @author Mobius
 */
public class IvoryTowerTeleportZones extends AbstractNpcAI
{
	private static final int HOURS_24 = 86400000;
	
	private static final String[] ZONE_NAMES =
	{
		"hellbound_tp_1",
		"hellbound_tp_2",
		"hellbound_tp_3",
		"hellbound_tp_4"
	};
	
	private IvoryTowerTeleportZones()
	{
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		// Current day check.
		if ((calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) && (calendar.get(Calendar.HOUR_OF_DAY) >= 10))
		{
			enableZones();
			calendar.add(Calendar.DAY_OF_WEEK, 7);
			calendar.set(Calendar.HOUR, 10);
			closeZones();
		}
		else
		{
			disableZones();
			calendar.add(Calendar.DAY_OF_WEEK, Calendar.SATURDAY - calendar.get(Calendar.DAY_OF_WEEK));
			calendar.set(Calendar.HOUR, 10);
		}
		
		// Schedule task to check if it is Saturday.
		ThreadPool.scheduleAtFixedRate(this::checkCondition, calendar.getTimeInMillis() - System.currentTimeMillis(), HOURS_24 * 7); // Check every week.
	}
	
	private void checkCondition()
	{
		final Calendar calendar = Calendar.getInstance();
		if ((calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) && (calendar.get(Calendar.HOUR_OF_DAY) >= 10))
		{
			enableZones();
			closeZones();
		}
	}
	
	private void closeZones()
	{
		final Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_WEEK, 1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		ThreadPool.schedule(this::disableZones, calendar.getTimeInMillis() - System.currentTimeMillis());
	}
	
	private void enableZones()
	{
		for (String name : ZONE_NAMES)
		{
			final ZoneType zone = ZoneManager.getInstance().getZoneByName(name);
			if (zone != null)
			{
				zone.setEnabled(true);
			}
		}
	}
	
	private void disableZones()
	{
		for (String name : ZONE_NAMES)
		{
			final ZoneType zone = ZoneManager.getInstance().getZoneByName(name);
			if (zone != null)
			{
				zone.setEnabled(false);
			}
		}
	}
	
	public static void main(String[] args)
	{
		new IvoryTowerTeleportZones();
	}
}