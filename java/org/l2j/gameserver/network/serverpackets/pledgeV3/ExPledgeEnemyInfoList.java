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
package org.l2j.gameserver.network.serverpackets.pledgeV3;

import java.util.List;
import java.util.stream.Collectors;

import org.l2j.gameserver.enums.ClanWarState;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.model.clan.ClanWar;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Berezkin Nikolay
 */
public class ExPledgeEnemyInfoList extends ServerPacket
{
	private final Clan _playerClan;
	private final List<ClanWar> _warList;
	
	public ExPledgeEnemyInfoList(Clan playerClan)
	{
		_playerClan = playerClan;
		_warList = playerClan.getWarList().values().stream().filter(it -> (it.getClanWarState(playerClan) == ClanWarState.MUTUAL) || (it.getAttackerClanId() == playerClan.getId())).collect(Collectors.toList());
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_PLEDGE_ENEMY_INFO_LIST.writeId(this);
		writeInt(_warList.size());
		for (ClanWar war : _warList)
		{
			final Clan clan = war.getOpposingClan(_playerClan);
			writeInt(clan.getRank());
			writeInt(clan.getId());
			writeSizedString(clan.getName());
			writeSizedString(clan.getLeaderName());
			writeInt((int) (war.getStartTime() / 1000)); // 430
		}
	}
}
