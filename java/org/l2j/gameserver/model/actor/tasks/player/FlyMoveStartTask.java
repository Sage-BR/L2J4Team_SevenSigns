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
package org.l2j.gameserver.model.actor.tasks.player;

import java.util.Objects;

import org.l2j.commons.threads.ThreadPool;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.request.SayuneRequest;
import org.l2j.gameserver.model.zone.ZoneType;
import org.l2j.gameserver.network.serverpackets.sayune.ExNotifyFlyMoveStart;

/**
 * @author UnAfraid
 */
public class FlyMoveStartTask implements Runnable
{
	private final Player _player;
	private final ZoneType _zone;
	
	public FlyMoveStartTask(ZoneType zone, Player player)
	{
		Objects.requireNonNull(zone);
		Objects.requireNonNull(player);
		_player = player;
		_zone = zone;
	}
	
	@Override
	public void run()
	{
		if (!_zone.isCharacterInZone(_player))
		{
			return;
		}
		
		if (!_player.hasRequest(SayuneRequest.class))
		{
			_player.sendPacket(ExNotifyFlyMoveStart.STATIC_PACKET);
			ThreadPool.schedule(this, 1000);
		}
	}
}