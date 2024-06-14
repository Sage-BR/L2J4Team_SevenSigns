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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import org.l2jmobius.commons.threads.ThreadPool;
import org.l2jmobius.commons.util.Rnd;
import org.l2jmobius.gameserver.data.xml.NpcData;
import org.l2jmobius.gameserver.model.Location;
import org.l2jmobius.gameserver.model.World;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.actor.instance.Decoy;
import org.l2jmobius.gameserver.model.actor.templates.NpcTemplate;
import org.l2jmobius.gameserver.model.events.AbstractScript;
import org.l2jmobius.gameserver.model.holders.SkillHolder;
import org.l2jmobius.gameserver.model.skill.BuffInfo;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.serverpackets.MagicSkillUse;
import org.l2jmobius.gameserver.network.serverpackets.SystemMessage;
import org.l2jmobius.gameserver.util.Broadcast;

/**
 * @author Serenitty
 */
public class RankingPowerManager
{
	private static final int COOLDOWN = 43200000;
	private static final int LEADER_STATUE = 18485;
	private static final SkillHolder LEADER_POWER = new SkillHolder(52018, 1);
	
	private Decoy _decoyInstance;
	private ScheduledFuture<?> _decoyTask;
	
	protected RankingPowerManager()
	{
		reset();
	}
	
	public void activatePower(Player player)
	{
		final Location location = player.getLocation();
		final List<Integer> array = new ArrayList<>(3);
		array.add(location.getX());
		array.add(location.getY());
		array.add(location.getZ());
		GlobalVariablesManager.getInstance().setIntegerList(GlobalVariablesManager.RANKING_POWER_LOCATION, array);
		GlobalVariablesManager.getInstance().set(GlobalVariablesManager.RANKING_POWER_COOLDOWN, System.currentTimeMillis() + COOLDOWN);
		createClone(player);
		cloneTask();
		final SystemMessage msg = new SystemMessage(SystemMessageId.A_RANKING_LEADER_C1_USED_LEADER_POWER_IN_S2);
		msg.addString(player.getName());
		msg.addZoneName(location.getX(), location.getY(), location.getZ());
		Broadcast.toAllOnlinePlayers(msg);
	}
	
	private void createClone(Player player)
	{
		final Location location = player.getLocation();
		
		final NpcTemplate template = NpcData.getInstance().getTemplate(LEADER_STATUE);
		_decoyInstance = new Decoy(template, player, COOLDOWN, false);
		_decoyInstance.setTargetable(false);
		_decoyInstance.setImmobilized(true);
		_decoyInstance.setInvul(true);
		_decoyInstance.spawnMe(location.getX(), location.getY(), location.getZ());
		_decoyInstance.setHeading(location.getHeading());
		_decoyInstance.broadcastStatusUpdate();
		
		AbstractScript.addSpawn(null, LEADER_STATUE, location, false, COOLDOWN);
	}
	
	private void cloneTask()
	{
		_decoyTask = ThreadPool.scheduleAtFixedRate(() ->
		{
			World.getInstance().forEachVisibleObjectInRange(_decoyInstance, Player.class, 300, nearby ->
			{
				final BuffInfo info = nearby.getEffectList().getBuffInfoBySkillId(LEADER_POWER.getSkillId());
				if ((info == null) || (info.getTime() < (LEADER_POWER.getSkill().getAbnormalTime() - 60)))
				{
					nearby.sendPacket(new MagicSkillUse(_decoyInstance, nearby, LEADER_POWER.getSkillId(), LEADER_POWER.getSkillLevel(), 0, 0));
					LEADER_POWER.getSkill().applyEffects(_decoyInstance, nearby);
				}
			});
			if (Rnd.nextBoolean()) // Add some randomness?
			{
				ThreadPool.schedule(() -> _decoyInstance.broadcastSocialAction(2), 4500);
			}
		}, 1000, 10000);
		
		ThreadPool.schedule(this::reset, COOLDOWN);
	}
	
	public void reset()
	{
		if (_decoyTask != null)
		{
			_decoyTask.cancel(false);
			_decoyTask = null;
		}
		if (_decoyInstance != null)
		{
			_decoyInstance.deleteMe();
		}
		GlobalVariablesManager.getInstance().remove(GlobalVariablesManager.RANKING_POWER_COOLDOWN);
		GlobalVariablesManager.getInstance().remove(GlobalVariablesManager.RANKING_POWER_LOCATION);
	}
	
	public static RankingPowerManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final RankingPowerManager INSTANCE = new RankingPowerManager();
	}
}
