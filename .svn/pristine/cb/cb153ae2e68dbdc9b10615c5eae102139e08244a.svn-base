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
package ai.areas.FrostCastleZone;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.l2jmobius.commons.threads.ThreadPool;
import org.l2jmobius.gameserver.instancemanager.GlobalVariablesManager;

/**
 * @author Serenitty
 */
public class FrostLordAvailability
{
	private static final Logger LOGGER = Logger.getLogger(FrostLordAvailability.class.getName());
	
	private static final int[] DAYS_OF_WEEK =
	{
		Calendar.TUESDAY,
		Calendar.THURSDAY
	};
	private static final int[] ACTIVATION_TIME =
	{
		18,
		0
	};
	private static final int[] DEACTIVATION_TIME =
	{
		11,
		59
	};
	private static final long ONE_DAY_IN_MILLIS = TimeUnit.DAYS.toMillis(1);
	
	public FrostLordAvailability()
	{
		frostLordCastleActivation(DAYS_OF_WEEK, ACTIVATION_TIME);
		frostLordCastleDeactivation(DAYS_OF_WEEK, DEACTIVATION_TIME);
	}
	
	private void frostLordCastleActivation(int[] daysOfWeek, int[] time)
	{
		for (int dayOfWeek : daysOfWeek)
		{
			final long initialDelay = getNextDelay(dayOfWeek, time[0], time[1]);
			final long period = ONE_DAY_IN_MILLIS * 7;
			ThreadPool.scheduleAtFixedRate(this::enableFrostLord, initialDelay, period);
		}
	}
	
	private void frostLordCastleDeactivation(int[] daysOfWeek, int[] time)
	{
		for (int dayOfWeek : daysOfWeek)
		{
			final long initialDelay = getNextDelay(dayOfWeek, time[0], time[1]);
			final long period = ONE_DAY_IN_MILLIS * 7;
			ThreadPool.scheduleAtFixedRate(this::disableFrostLord, initialDelay, period);
		}
	}
	
	private long getNextDelay(int dayOfWeek, int hour, int minute)
	{
		final Calendar now = Calendar.getInstance();
		final int currentDayOfWeek = now.get(Calendar.DAY_OF_WEEK);
		final int daysUntilNextActivation = ((dayOfWeek + 7) - currentDayOfWeek) % 7;
		
		final Calendar activationTime = Calendar.getInstance();
		activationTime.add(Calendar.DAY_OF_YEAR, daysUntilNextActivation);
		activationTime.set(Calendar.HOUR_OF_DAY, hour);
		activationTime.set(Calendar.MINUTE, minute);
		activationTime.set(Calendar.SECOND, 0);
		
		if (activationTime.getTimeInMillis() < now.getTimeInMillis())
		{
			activationTime.add(Calendar.DAY_OF_YEAR, 1);
		}
		
		return activationTime.getTimeInMillis() - now.getTimeInMillis();
	}
	
	private void enableFrostLord()
	{
		GlobalVariablesManager.getInstance().set("AvailableFrostLord", true);
		LOGGER.info("Frost Lord enabled.");
	}
	
	private void disableFrostLord()
	{
		GlobalVariablesManager.getInstance().set("AvailableFrostLord", false);
		LOGGER.info("Frost Lord disabled.");
	}
	
	public static void main(String[] args)
	{
		new FrostLordAvailability();
	}
}
