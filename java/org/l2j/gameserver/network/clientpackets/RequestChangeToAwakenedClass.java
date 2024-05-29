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
package org.l2j.gameserver.network.clientpackets;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.events.EventDispatcher;
import org.l2j.gameserver.model.events.EventType;
import org.l2j.gameserver.model.events.impl.creature.player.OnPlayerChangeToAwakenedClass;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.serverpackets.ActionFailed;

/**
 * @author Sdw
 */
public class RequestChangeToAwakenedClass implements ClientPacket
{
	private boolean _change;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_change = packet.readInt() == 1;
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (_change)
		{
			if (EventDispatcher.getInstance().hasListener(EventType.ON_PLAYER_CHANGE_TO_AWAKENED_CLASS, player))
			{
				EventDispatcher.getInstance().notifyEventAsync(new OnPlayerChangeToAwakenedClass(player), player);
			}
		}
		else
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
		}
	}
}
