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
import org.l2j.gameserver.model.events.impl.creature.player.OnPlayerPressTutorialMark;
import org.l2j.gameserver.network.GameClient;

public class RequestTutorialQuestionMark implements ClientPacket
{
	private int _number = 0;
	
	@Override
	public void read(ReadablePacket packet)
	{
		packet.readByte(); // index ?
		_number = packet.readInt();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		// Notify scripts
		if (EventDispatcher.getInstance().hasListener(EventType.ON_PLAYER_PRESS_TUTORIAL_MARK, player))
		{
			EventDispatcher.getInstance().notifyEventAsync(new OnPlayerPressTutorialMark(player, _number), player);
		}
	}
}
