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
package org.l2j.gameserver.network.clientpackets.revenge;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.instancemanager.RevengeHistoryManager;
import org.l2j.gameserver.model.World;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Mobius
 */
public class RequestExPvpBookShareRevengeReqShareRevengeInfo implements ClientPacket
{
	private String _victimName;
	private String _killerName;
	private int _type;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_victimName = packet.readSizedString();
		_killerName = packet.readSizedString();
		_type = packet.readInt();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (!_victimName.equals(player.getName()))
		{
			return;
		}
		
		final Player killer = World.getInstance().getPlayer(_killerName);
		if ((killer == null) || !killer.isOnline())
		{
			SystemMessage sm = new SystemMessage(SystemMessageId.S1_OFFLINE);
			sm.addString(_killerName);
			player.sendPacket(sm);
			return;
		}
		
		RevengeHistoryManager.getInstance().requestHelp(player, killer, _type);
	}
}
