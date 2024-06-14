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
package org.l2j.gameserver.network.clientpackets;

import org.l2j.Config;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.model.clan.ClanMember;
import org.l2j.gameserver.model.clan.ClanPrivilege;
import org.l2j.gameserver.network.PacketLogger;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.serverpackets.ExPledgeCount;
import org.l2j.gameserver.network.serverpackets.PledgeShowMemberListDelete;
import org.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * @version $Revision: 1.3.2.1.2.3 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestOustPledgeMember extends ClientPacket
{
	private String _target;
	
	@Override
	protected void readImpl()
	{
		_target = readString();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		if (player.getClan() == null)
		{
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_A_CLAN_MEMBER_2);
			return;
		}
		if (!player.hasClanPrivilege(ClanPrivilege.CL_DISMISS))
		{
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return;
		}
		if (player.getName().equalsIgnoreCase(_target))
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_DISMISS_YOURSELF);
			return;
		}
		
		final Clan clan = player.getClan();
		final ClanMember member = clan.getClanMember(_target);
		if (member == null)
		{
			PacketLogger.warning("Target (" + _target + ") is not member of the clan");
			return;
		}
		if (member.isOnline() && member.getPlayer().isInCombat())
		{
			player.sendPacket(SystemMessageId.A_CLAN_MEMBER_MAY_NOT_BE_DISMISSED_DURING_COMBAT);
			return;
		}
		
		// this also updates the database
		clan.removeClanMember(member.getObjectId(), System.currentTimeMillis() + (Config.ALT_CLAN_JOIN_MINS * 60000)); // 60*1000 = 60000
		clan.setCharPenaltyExpiryTime(System.currentTimeMillis() + (Config.ALT_CLAN_JOIN_MINS * 86400000)); // 60*1000 = 60000
		clan.updateClanInDB();
		
		final SystemMessage sm = new SystemMessage(SystemMessageId.S1_IS_DISMISSED_FROM_THE_CLAN);
		sm.addString(member.getName());
		clan.broadcastToOnlineMembers(sm);
		player.sendPacket(SystemMessageId.THE_CLAN_MEMBER_IS_DISMISSED);
		player.sendPacket(SystemMessageId.YOU_CANNOT_ACCEPT_A_NEW_CLAN_MEMBER_FOR_24_H_AFTER_DISMISSING_SOMEONE);
		
		// Remove the Player From the Member list
		clan.broadcastToOnlineMembers(new PledgeShowMemberListDelete(_target));
		clan.broadcastToOnlineMembers(new ExPledgeCount(clan));
		if (member.isOnline())
		{
			final Player target = member.getPlayer();
			target.sendPacket(SystemMessageId.YOU_ARE_DISMISSED_FROM_A_CLAN_YOU_CANNOT_JOIN_ANOTHER_FOR_24_H);
		}
	}
}
