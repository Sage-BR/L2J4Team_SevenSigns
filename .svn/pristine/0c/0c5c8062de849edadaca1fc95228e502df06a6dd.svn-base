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
package org.l2jmobius.gameserver.network.clientpackets.revenge;

import org.l2jmobius.gameserver.instancemanager.RevengeHistoryManager;
import org.l2jmobius.gameserver.model.World;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;
import org.l2jmobius.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Mobius
 */
public class RequestExPvpBookShareRevengeReqShareRevengeInfo extends ClientPacket
{
	private String _victimName;
	private String _killerName;
	private int _type;
	
	@Override
	protected void readImpl()
	{
		_victimName = readSizedString();
		_killerName = readSizedString();
		_type = readInt();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
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
