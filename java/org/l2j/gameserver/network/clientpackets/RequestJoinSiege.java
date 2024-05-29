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
import org.l2j.gameserver.instancemanager.CastleManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.model.clan.ClanPrivilege;
import org.l2j.gameserver.model.siege.Castle;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.serverpackets.castlewar.MercenaryCastleWarCastleSiegeAttackerList;
import org.l2j.gameserver.network.serverpackets.castlewar.MercenaryCastleWarCastleSiegeDefenderList;

/**
 * @author KenM
 */
public class RequestJoinSiege implements ClientPacket
{
	private int _castleId;
	private int _isAttacker;
	private int _isJoining;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_castleId = packet.readInt();
		_isAttacker = packet.readInt();
		_isJoining = packet.readInt();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (!player.hasClanPrivilege(ClanPrivilege.CS_MANAGE_SIEGE))
		{
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return;
		}
		
		final Clan clan = player.getClan();
		if (clan == null)
		{
			return;
		}
		
		final Castle castle = CastleManager.getInstance().getCastleById(_castleId);
		if (castle != null)
		{
			if (_isJoining == 1)
			{
				if (System.currentTimeMillis() < clan.getDissolvingExpiryTime())
				{
					player.sendPacket(SystemMessageId.YOUR_CLAN_MAY_NOT_REGISTER_TO_PARTICIPATE_IN_A_SIEGE_WHILE_UNDER_A_GRACE_PERIOD_OF_THE_CLAN_S_DISSOLUTION);
					return;
				}
				if (_isAttacker == 1)
				{
					castle.getSiege().registerAttacker(player);
					player.sendPacket(new MercenaryCastleWarCastleSiegeAttackerList(castle.getResidenceId()));
				}
				else
				{
					castle.getSiege().registerDefender(player);
					player.sendPacket(new MercenaryCastleWarCastleSiegeDefenderList(castle.getResidenceId()));
				}
			}
			else
			{
				castle.getSiege().removeSiegeClan(player);
				if (_isAttacker == 1)
				{
					player.sendPacket(new MercenaryCastleWarCastleSiegeAttackerList(castle.getResidenceId()));
				}
				else
				{
					player.sendPacket(new MercenaryCastleWarCastleSiegeDefenderList(castle.getResidenceId()));
				}
			}
			
			// Managed by new packets.
			// castle.getSiege().listRegisterClan(player);
		}
	}
}
