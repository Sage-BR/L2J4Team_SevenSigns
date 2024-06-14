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
package org.l2jmobius.gameserver.network.clientpackets.pledgeV3;

import org.l2jmobius.gameserver.data.sql.ClanTable;
import org.l2jmobius.gameserver.enums.ClanWarState;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.clan.Clan;
import org.l2jmobius.gameserver.model.clan.ClanMember;
import org.l2jmobius.gameserver.model.clan.ClanPrivilege;
import org.l2jmobius.gameserver.model.clan.ClanWar;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;
import org.l2jmobius.gameserver.network.serverpackets.ActionFailed;
import org.l2jmobius.gameserver.network.serverpackets.SystemMessage;
import org.l2jmobius.gameserver.network.serverpackets.pledgeV3.ExPledgeEnemyInfoList;

/**
 * @author Mobius
 */
public class RequestExPledgeEnemyRegister extends ClientPacket
{
	private String _pledgeName;
	
	@Override
	protected void readImpl()
	{
		_pledgeName = readSizedString();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		final Clan playerClan = player.getClan();
		if (playerClan == null)
		{
			return;
		}
		
		if (!player.hasClanPrivilege(ClanPrivilege.CL_PLEDGE_WAR))
		{
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (playerClan.getWarCount() >= 30)
		{
			player.sendPacket(SystemMessageId.A_DECLARATION_OF_WAR_AGAINST_MORE_THAN_30_CLANS_CAN_T_BE_MADE_AT_THE_SAME_TIME);
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		final Clan enemyClan = ClanTable.getInstance().getClanByName(_pledgeName);
		if (enemyClan == null)
		{
			player.sendPacket(new SystemMessage(SystemMessageId.A_CLAN_WAR_CANNOT_BE_DECLARED_AGAINST_A_CLAN_THAT_DOES_NOT_EXIST));
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (enemyClan == playerClan)
		{
			player.sendPacket(new SystemMessage(SystemMessageId.FOOL_YOU_CANNOT_DECLARE_WAR_AGAINST_YOUR_OWN_CLAN));
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if ((playerClan.getAllyId() == enemyClan.getAllyId()) && (playerClan.getAllyId() != 0))
		{
			player.sendPacket(new SystemMessage(SystemMessageId.A_DECLARATION_OF_CLAN_WAR_AGAINST_AN_ALLIED_CLAN_CAN_T_BE_MADE));
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (enemyClan.getDissolvingExpiryTime() > System.currentTimeMillis())
		{
			player.sendPacket(new SystemMessage(SystemMessageId.A_CLAN_WAR_CAN_NOT_BE_DECLARED_AGAINST_A_CLAN_THAT_IS_BEING_DISSOLVED));
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		final ClanWar clanWar = playerClan.getWarWith(enemyClan.getId());
		if (clanWar != null)
		{
			if (clanWar.getClanWarState(playerClan) == ClanWarState.WIN)
			{
				final SystemMessage sm = new SystemMessage(SystemMessageId.YOU_CAN_T_DECLARE_A_WAR_BECAUSE_THE_21_DAY_PERIOD_HASN_T_PASSED_AFTER_A_DEFEAT_DECLARATION_WITH_THE_S1_CLAN);
				sm.addString(enemyClan.getName());
				player.sendPacket(sm);
				player.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			if ((clanWar.getClanWarState(playerClan) != ClanWarState.BLOOD_DECLARATION) || (clanWar.getAttackerClanId() == playerClan.getId()))
			{
				final SystemMessage sm = new SystemMessage(SystemMessageId.YOU_HAVE_ALREADY_BEEN_AT_WAR_WITH_THE_S1_CLAN_5_DAYS_MUST_PASS_BEFORE_YOU_CAN_DECLARE_WAR_AGAIN);
				sm.addString(enemyClan.getName());
				player.sendPacket(sm);
				player.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			if (clanWar.getClanWarState(playerClan) == ClanWarState.BLOOD_DECLARATION)
			{
				clanWar.mutualClanWarAccepted(enemyClan, playerClan);
				broadcastClanInfo(playerClan, enemyClan);
				return;
			}
		}
		
		final ClanWar newClanWar = new ClanWar(playerClan, enemyClan);
		ClanTable.getInstance().storeClanWars(newClanWar);
		
		broadcastClanInfo(playerClan, enemyClan);
	}
	
	private void broadcastClanInfo(Clan playerClan, Clan enemyClan)
	{
		for (ClanMember member : playerClan.getMembers())
		{
			if ((member != null) && member.isOnline())
			{
				member.getPlayer().sendPacket(new ExPledgeEnemyInfoList(playerClan));
				member.getPlayer().broadcastUserInfo();
			}
		}
		for (ClanMember member : enemyClan.getMembers())
		{
			if ((member != null) && member.isOnline())
			{
				member.getPlayer().sendPacket(new ExPledgeEnemyInfoList(enemyClan));
				member.getPlayer().broadcastUserInfo();
			}
		}
	}
}
