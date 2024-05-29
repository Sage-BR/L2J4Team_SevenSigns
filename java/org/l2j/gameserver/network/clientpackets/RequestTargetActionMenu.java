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
import org.l2j.gameserver.model.World;
import org.l2j.gameserver.model.WorldObject;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.GameClient;

/**
 * @author Mobius
 */
public class RequestTargetActionMenu implements ClientPacket
{
	private int _objectId;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_objectId = packet.readInt();
		packet.readShort(); // action?
	}
	
	@Override
	public void run(GameClient client)
	{
		if (!client.getFloodProtectors().canPerformPlayerAction())
		{
			return;
		}
		
		final Player player = client.getPlayer();
		if ((player == null) || player.isTargetingDisabled())
		{
			return;
		}
		
		for (WorldObject object : World.getInstance().getVisibleObjects(player, WorldObject.class))
		{
			if (_objectId == object.getObjectId())
			{
				if (object.isTargetable() && object.isAutoAttackable(player))
				{
					player.setTarget(object);
				}
				break;
			}
		}
	}
}
