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

import org.l2j.Config;

import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.serverpackets.ExPledgeCount;
import org.l2j.gameserver.network.serverpackets.PledgeShowMemberListDelete;
import org.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * @version $Revision: 1.3.2.1.2.3 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestWithdrawalPledge implements ClientPacket
{
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		if (player.getClan() == null)
		{
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_A_CLAN_MEMBER_2);
			return;
		}
		if (player.isClanLeader())
		{
			player.sendPacket(SystemMessageId.A_CLAN_LEADER_CANNOT_WITHDRAW_FROM_THEIR_OWN_CLAN);
			return;
		}
		if (player.isInCombat())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_LEAVE_A_CLAN_WHILE_ENGAGED_IN_COMBAT);
			return;
		}
		
		final Clan clan = player.getClan();
		clan.removeClanMember(player.getObjectId(), System.currentTimeMillis() + (Config.ALT_CLAN_JOIN_MINS * 60000)); // 60*1000 = 60000
		
		final SystemMessage sm = new SystemMessage(SystemMessageId.S1_HAS_LEFT_THE_CLAN);
		sm.addString(player.getName());
		clan.broadcastToOnlineMembers(sm);
		
		// Remove the Player From the Member list
		clan.broadcastToOnlineMembers(new PledgeShowMemberListDelete(player.getName()));
		clan.broadcastToOnlineMembers(new ExPledgeCount(clan));
		player.sendPacket(SystemMessageId.YOU_HAVE_LEFT_THE_CLAN);
		player.sendPacket(SystemMessageId.YOU_CANNOT_JOIN_ANOTHER_CLAN_FOR_24_H_AFTER_LEAVING_THE_PREVIOUS_ONE);
	}
}
