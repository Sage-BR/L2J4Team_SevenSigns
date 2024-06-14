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
package org.l2jmobius.gameserver.instancemanager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.holders.SharedTeleportHolder;

/**
 * Shared Teleport Manager
 * @author NasSeKa
 */
public class SharedTeleportManager
{
	protected static final Logger LOGGER = Logger.getLogger(SharedTeleportManager.class.getName());
	
	private static final int TELEPORT_COUNT = 5;
	
	private final Map<Integer, SharedTeleportHolder> _sharedTeleports = new ConcurrentHashMap<>();
	private int _lastSharedTeleportId = 0;
	
	protected SharedTeleportManager()
	{
		LOGGER.info(getClass().getSimpleName() + ": initialized.");
	}
	
	public SharedTeleportHolder getTeleport(int id)
	{
		return _sharedTeleports.get(id);
	}
	
	public synchronized int nextId(Creature creature)
	{
		final int nextId = ++_lastSharedTeleportId;
		_sharedTeleports.put(nextId, new SharedTeleportHolder(nextId, creature.getName(), TELEPORT_COUNT, creature.getX(), creature.getY(), creature.getZ()));
		return nextId;
	}
	
	/**
	 * Gets the single instance of {@code SharedTeleportManager}.
	 * @return single instance of {@code SharedTeleportManager}
	 */
	public static SharedTeleportManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final SharedTeleportManager INSTANCE = new SharedTeleportManager();
	}
}
