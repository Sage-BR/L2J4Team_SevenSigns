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

import org.l2j.gameserver.data.sql.ClanTable;
import org.l2j.gameserver.enums.ClanWarState;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.model.clan.ClanMember;
import org.l2j.gameserver.model.clan.ClanPrivilege;
import org.l2j.gameserver.model.clan.ClanWar;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.serverpackets.ActionFailed;
import org.l2j.gameserver.network.serverpackets.SystemMessage;

public class RequestSurrenderPledgeWar extends ClientPacket
{
	private String _pledgeName;
	
	@Override
	protected void readImpl()
	{
		_pledgeName = readString();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		final Clan myClan = player.getClan();
		if (myClan == null)
		{
			return;
		}
		
		for (ClanMember member : myClan.getMembers())
		{
			if ((member != null) && member.isOnline() && member.getPlayer().isInCombat())
			{
				player.sendPacket(SystemMessageId.THE_CLAN_WAR_CANNOT_BE_STOPPED_BECAUSE_SOMEONE_FROM_YOUR_CLAN_IS_STILL_ENGAGED_IN_BATTLE);
				player.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
		}
		
		final Clan targetClan = ClanTable.getInstance().getClanByName(_pledgeName);
		if (targetClan == null)
		{
			player.sendMessage("No such clan.");
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		else if (!player.hasClanPrivilege(ClanPrivilege.CL_PLEDGE_WAR))
		{
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		final ClanWar clanWar = myClan.getWarWith(targetClan.getId());
		if (clanWar == null)
		{
			final SystemMessage sm = new SystemMessage(SystemMessageId.YOU_HAVE_NOT_DECLARED_A_CLAN_WAR_AGAINST_THE_CLAN_S1);
			sm.addString(targetClan.getName());
			player.sendPacket(sm);
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (clanWar.getState() == ClanWarState.BLOOD_DECLARATION)
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_DECLARE_DEFEAT_AS_IT_HAS_NOT_BEEN_7_DAYS_SINCE_STARTING_A_CLAN_WAR_WITH_CLAN_S1);
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		clanWar.cancel(player, myClan);
	}
}