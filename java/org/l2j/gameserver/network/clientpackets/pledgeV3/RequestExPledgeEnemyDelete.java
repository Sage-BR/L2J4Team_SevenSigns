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
import org.l2j.gameserver.data.sql.ClanTable;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.model.clan.ClanMember;
import org.l2j.gameserver.model.clan.ClanPrivilege;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.ActionFailed;
import org.l2j.gameserver.network.serverpackets.pledgeV3.ExPledgeEnemyInfoList;
import org.l2j.gameserver.taskmanager.AttackStanceTaskManager;

/**
 * @author Mobius
 */
public class RequestExPledgeEnemyDelete implements ClientPacket
{
	private int _clanId;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_clanId = packet.readInt();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final Clan playerClan = player.getClan();
		if (playerClan == null)
		{
			return;
		}
		
		final Clan enemyClan = ClanTable.getInstance().getClan(_clanId);
		if (enemyClan == null)
		{
			player.sendPacket(SystemMessageId.THERE_IS_NO_SUCH_CLAN);
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (!playerClan.isAtWarWith(enemyClan.getId()))
		{
			player.sendPacket(SystemMessageId.ENTER_THE_NAME_OF_THE_CLAN_YOU_WISH_TO_END_THE_WAR_WITH);
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (!player.hasClanPrivilege(ClanPrivilege.CL_PLEDGE_WAR))
		{
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return;
		}
		
		for (ClanMember member : playerClan.getMembers())
		{
			if ((member == null) || (member.getPlayer() == null))
			{
				continue;
			}
			if (AttackStanceTaskManager.getInstance().hasAttackStanceTask(member.getPlayer()))
			{
				player.sendPacket(SystemMessageId.THE_CLAN_WAR_CANNOT_BE_STOPPED_BECAUSE_SOMEONE_FROM_YOUR_CLAN_IS_STILL_ENGAGED_IN_BATTLE);
				return;
			}
		}
		
		// Reduce reputation.
		playerClan.takeReputationScore(500);
		ClanTable.getInstance().deleteClanWars(playerClan.getId(), enemyClan.getId());
		
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
