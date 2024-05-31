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
package org.l2j.commons.threads;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.l2j.Config;
import org.l2j.commons.util.CommonUtil;

/**
 * This class is a thread pool manager that handles two types of thread pools, the scheduled pool and the instant pool, using a ScheduledThreadPoolExecutor and a ThreadPoolExecutor respectively.<br>
 * It uses the Config class to set the size of the pools and has a method to remove old tasks. It also provides scheduling methods and logs useful information in case of exceptions.
 * @author Mobius
 */
public class ThreadPool
{
	private static final Logger LOGGER = Logger.getLogger(ThreadPool.class.getName());
	
	private static final ScheduledThreadPoolExecutor SCHEDULED_POOL = new ScheduledThreadPoolExecutor(Config.SCHEDULED_THREAD_POOL_SIZE, new ThreadProvider("L2J4TeamScheduledThread"), new ThreadPoolExecutor.CallerRunsPolicy());
	private static final ThreadPoolExecutor INSTANT_POOL = new ThreadPoolExecutor(Config.INSTANT_THREAD_POOL_SIZE, Integer.MAX_VALUE, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(), new ThreadProvider("L2J4TeamThread"));
	private static final long MAX_DELAY = 3155695200000L; // One hundred years.
	private static final long MIN_DELAY = 0L;
	
	public static void init()
	{
		LOGGER.info("ThreadPool: Initialized");
		
		// Configure ScheduledThreadPoolExecutor.
		SCHEDULED_POOL.setRejectedExecutionHandler(new RejectedExecutionHandlerImpl());
		SCHEDULED_POOL.setRemoveOnCancelPolicy(true);
		SCHEDULED_POOL.prestartAllCoreThreads();
		
		// Configure ThreadPoolExecutor.
		INSTANT_POOL.setRejectedExecutionHandler(new RejectedExecutionHandlerImpl());
		INSTANT_POOL.prestartAllCoreThreads();
		
		// Schedule the purge task.
		scheduleAtFixedRate(ThreadPool::purge, 60000, 60000);
		
		// Log information.
		LOGGER.info("...scheduled pool executor with " + Config.SCHEDULED_THREAD_POOL_SIZE + " total threads.");
		LOGGER.info("...instant pool executor with " + Config.INSTANT_THREAD_POOL_SIZE + " total threads.");
	}
	
	public static void purge()
	{
		SCHEDULED_POOL.purge();
		INSTANT_POOL.purge();
	}
	
	/**
	 * Creates and executes a one-shot action that becomes enabled after the given delay.
	 * @param runnable : the task to execute.
	 * @param delay : the time from now to delay execution.
	 * @return a ScheduledFuture representing pending completion of the task and whose get() method will return null upon completion.
	 */
	public static ScheduledFuture<?> schedule(Runnable runnable, long delay)
	{
		try
		{
			return SCHEDULED_POOL.schedule(new RunnableWrapper(runnable), validate(delay), TimeUnit.MILLISECONDS);
		}
		catch (Exception e)
		{
			LOGGER.warning(runnable.getClass().getSimpleName() + Config.EOL + e.getMessage() + Config.EOL + e.getStackTrace());
			return null;
		}
	}
	
	/**
	 * Creates and executes a periodic action that becomes enabled first after the given initial delay.
	 * @param runnable : the task to execute.
	 * @param initialDelay : the time to delay first execution.
	 * @param period : the period between successive executions.
	 * @return a ScheduledFuture representing pending completion of the task and whose get() method will throw an exception upon cancellation.
	 */
	public static ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long initialDelay, long period)
	{
		try
		{
			return SCHEDULED_POOL.scheduleAtFixedRate(new RunnableWrapper(runnable), validate(initialDelay), validate(period), TimeUnit.MILLISECONDS);
		}
		catch (Exception e)
		{
			LOGGER.warning(runnable.getClass().getSimpleName() + Config.EOL + e.getMessage() + Config.EOL + e.getStackTrace());
			return null;
		}
	}
	
	/**
	 * Executes the given task sometime in the future.
	 * @param runnable : the task to execute.
	 */
	public static void execute(Runnable runnable)
	{
		try
		{
			INSTANT_POOL.execute(new RunnableWrapper(runnable));
		}
		catch (Exception e)
		{
			LOGGER.warning(runnable.getClass().getSimpleName() + Config.EOL + e.getMessage() + Config.EOL + e.getStackTrace());
		}
	}
	
	/**
	 * @param delay : the delay to validate.
	 * @return a valid value, from MIN_DELAY to MAX_DELAY.
	 */
	private static long validate(long delay)
	{
		if (delay < MIN_DELAY)
		{
			LOGGER.warning("ThreadPool found delay " + delay + "!");
			LOGGER.warning(CommonUtil.getStackTrace(new Exception()));
			return MIN_DELAY;
		}
		if (delay > MAX_DELAY)
		{
			LOGGER.warning("ThreadPool found delay " + delay + "!");
			LOGGER.warning(CommonUtil.getStackTrace(new Exception()));
			return MAX_DELAY;
		}
		return delay;
	}
	
	public static String[] getStats()
	{
		final String[] stats = new String[20];
		int pos = 0;
		stats[pos++] = "Scheduled pool:";
		stats[pos++] = " |- ActiveCount: ...... " + SCHEDULED_POOL.getActiveCount();
		stats[pos++] = " |- CorePoolSize: ..... " + SCHEDULED_POOL.getCorePoolSize();
		stats[pos++] = " |- PoolSize: ......... " + SCHEDULED_POOL.getPoolSize();
		stats[pos++] = " |- LargestPoolSize: .. " + SCHEDULED_POOL.getLargestPoolSize();
		stats[pos++] = " |- MaximumPoolSize: .. " + SCHEDULED_POOL.getMaximumPoolSize();
		stats[pos++] = " |- CompletedTaskCount: " + SCHEDULED_POOL.getCompletedTaskCount();
		stats[pos++] = " |- QueuedTaskCount: .. " + SCHEDULED_POOL.getQueue().size();
		stats[pos++] = " |- TaskCount: ........ " + SCHEDULED_POOL.getTaskCount();
		stats[pos++] = " | -------";
		stats[pos++] = "Instant pool:";
		stats[pos++] = " |- ActiveCount: ...... " + INSTANT_POOL.getActiveCount();
		stats[pos++] = " |- CorePoolSize: ..... " + INSTANT_POOL.getCorePoolSize();
		stats[pos++] = " |- PoolSize: ......... " + INSTANT_POOL.getPoolSize();
		stats[pos++] = " |- LargestPoolSize: .. " + INSTANT_POOL.getLargestPoolSize();
		stats[pos++] = " |- MaximumPoolSize: .. " + INSTANT_POOL.getMaximumPoolSize();
		stats[pos++] = " |- CompletedTaskCount: " + INSTANT_POOL.getCompletedTaskCount();
		stats[pos++] = " |- QueuedTaskCount: .. " + INSTANT_POOL.getQueue().size();
		stats[pos++] = " |- TaskCount: ........ " + INSTANT_POOL.getTaskCount();
		stats[pos] = " | -------";
		return stats;
	}
	
	/**
	 * Shutdown thread pooling system correctly. Send different informations.
	 */
	public static void shutdown()
	{
		try
		{
			LOGGER.info("ThreadPool: Shutting down.");
			SCHEDULED_POOL.shutdownNow();
			INSTANT_POOL.shutdownNow();
		}
		catch (Throwable t)
		{
			LOGGER.info("ThreadPool: Problem at Shutting down. " + t.getMessage());
		}
	}
}
