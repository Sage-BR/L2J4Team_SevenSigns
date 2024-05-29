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
package org.l2j.gameserver.network.clientpackets.pledgeV3;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.pledgeV3.ExPledgeV3Info;

/**
 * @author Berezkin Nikolay
 */
public class RequestExPledgeV3SetAnnounce implements ClientPacket
{
	private String _announce;
	private boolean _enterWorldShow;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_announce = packet.readSizedString();
		_enterWorldShow = packet.readByte() == 1;
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player activeChar = client.getPlayer();
		if (activeChar == null)
		{
			return;
		}
		
		final Clan clan = activeChar.getClan();
		if (clan == null)
		{
			return;
		}
		
		clan.setNotice(_announce);
		clan.setNoticeEnabled(_enterWorldShow);
		clan.broadcastToOnlineMembers(new ExPledgeV3Info(clan.getExp(), clan.getRank(), clan.getNotice(), clan.isNoticeEnabled()));
	}
}
