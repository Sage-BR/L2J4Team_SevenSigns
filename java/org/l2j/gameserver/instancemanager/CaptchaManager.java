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
package org.l2j.gameserver.instancemanager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.l2j.Config;
import org.l2j.commons.util.Rnd;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.instance.Monster;
import org.l2j.gameserver.model.actor.request.CaptchaRequest;
import org.l2j.gameserver.model.captcha.Captcha;
import org.l2j.gameserver.model.captcha.CaptchaGenerator;
import org.l2j.gameserver.model.events.Containers;
import org.l2j.gameserver.model.events.EventType;
import org.l2j.gameserver.model.events.impl.creature.OnCreatureKilled;
import org.l2j.gameserver.model.events.listeners.ConsumerEventListener;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.serverpackets.captcha.ReceiveBotCaptchaImage;

/**
 * @author Mobius
 */
public class CaptchaManager
{
	private static final Map<Integer, Integer> MONSTER_COUNTER = new ConcurrentHashMap<>();
	private static final Map<Integer, Long> LAST_KILL_TIME = new ConcurrentHashMap<>();
	
	private final Consumer<OnCreatureKilled> _onCreatureKilled = event -> updateCounter(event.getAttacker(), event.getTarget());
	
	protected CaptchaManager()
	{
		if (Config.ENABLE_CAPTCHA)
		{
			Containers.Players().addListener(new ConsumerEventListener(Containers.Players(), EventType.ON_CREATURE_KILLED, _onCreatureKilled, this));
		}
	}
	
	public void updateCounter(Creature player, Creature monster)
	{
		if (!Config.ENABLE_CAPTCHA)
		{
			return;
		}
		
		if (!(player instanceof Player) || !(monster instanceof Monster))
		{
			return;
		}
		
		// Check if auto-play is enabled and player is auto-playing.
		final Player killer = player.getActingPlayer();
		if (Config.ENABLE_AUTO_PLAY && killer.isAutoPlaying())
		{
			return; // Don't count kills when auto-play is enabled.
		}
		
		if (Config.KILL_COUNTER_RESET)
		{
			final long currentTime = System.currentTimeMillis();
			final long previousKillTime = LAST_KILL_TIME.getOrDefault(killer.getObjectId(), currentTime);
			if ((currentTime - previousKillTime) > Config.KILL_COUNTER_RESET_TIME)
			{
				MONSTER_COUNTER.put(killer.getObjectId(), 0);
			}
			LAST_KILL_TIME.put(killer.getObjectId(), currentTime);
		}
		
		int count = 1;
		if (MONSTER_COUNTER.get(killer.getObjectId()) != null)
		{
			count = MONSTER_COUNTER.get(killer.getObjectId()) + 1;
		}
		
		final int next = Rnd.get(Config.KILL_COUNTER_RANDOMIZATION);
		if ((Config.KILL_COUNTER + next) < count)
		{
			MONSTER_COUNTER.remove(killer.getObjectId());
			
			final Captcha captcha = CaptchaGenerator.getInstance().next();
			if (!killer.hasRequest(CaptchaRequest.class))
			{
				final CaptchaRequest request = new CaptchaRequest(killer, captcha);
				killer.addRequest(request);
				killer.sendPacket(new ReceiveBotCaptchaImage(captcha, request.getRemainingTime()));
				killer.sendPacket(SystemMessageId.PLEASE_ENTER_THE_AUTHENTICATION_CODE_IN_TIME_TO_CONTINUE_PLAYING);
			}
		}
		else
		{
			MONSTER_COUNTER.put(killer.getObjectId(), count);
		}
	}
	
	public static final CaptchaManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final CaptchaManager INSTANCE = new CaptchaManager();
	}
}
