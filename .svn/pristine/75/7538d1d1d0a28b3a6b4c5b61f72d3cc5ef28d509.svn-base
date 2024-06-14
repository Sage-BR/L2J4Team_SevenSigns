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

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.model.actor.Player;

/**
 * @author Serenitty, Index
 */
public class BattleWithBalokManager
{
	private final Map<Integer, Integer> _playerPoints = new ConcurrentHashMap<>();
	private boolean _inBattle = false;
	private int _reward = 0;
	private int _globalPoints = 0;
	private int _globalStage = 0;
	private int _globalStatus = 0;
	
	public BattleWithBalokManager()
	{
	}
	
	public void addPointsForPlayer(Player player, boolean isScorpion)
	{
		final int pointsToAdd = isScorpion ? Config.BALOK_POINTS_PER_MONSTER * 10 : Config.BALOK_POINTS_PER_MONSTER;
		final int currentPoints = _playerPoints.computeIfAbsent(player.getObjectId(), pts -> 0);
		int sum = pointsToAdd + currentPoints;
		_playerPoints.put(player.getObjectId(), sum);
	}
	
	public Map<Integer, Integer> getTopPlayers(int count)
	{
		return _playerPoints.entrySet().stream().sorted(Entry.comparingByValue(Comparator.reverseOrder())).limit(count).collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}
	
	public int getPlayerRank(Player player)
	{
		if (!_playerPoints.containsKey(player.getObjectId()))
		{
			return 0;
		}
		
		final Map<Integer, Integer> sorted = _playerPoints.entrySet().stream().sorted(Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		return sorted.keySet().stream().toList().indexOf(player.getObjectId()) + 1;
	}
	
	public int getMonsterPoints(Player player)
	{
		return _playerPoints.computeIfAbsent(player.getObjectId(), pts -> 0);
	}
	
	public int getReward()
	{
		return _reward;
	}
	
	public void setReward(int value)
	{
		_reward = value;
	}
	
	public boolean getInBattle()
	{
		return _inBattle;
	}
	
	public void setInBattle(boolean value)
	{
		_inBattle = value;
	}
	
	public int getGlobalPoints()
	{
		return _globalPoints;
	}
	
	public void setGlobalPoints(int value)
	{
		_globalPoints = value;
	}
	
	public int getGlobalStage()
	{
		return _globalStage;
	}
	
	public void setGlobalStage(int value)
	{
		_globalStage = value;
	}
	
	public int getGlobalStatus()
	{
		return _globalStatus;
	}
	
	public void setGlobalStatus(int value)
	{
		_globalStatus = value;
	}
	
	public static BattleWithBalokManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final BattleWithBalokManager INSTANCE = new BattleWithBalokManager();
	}
}
