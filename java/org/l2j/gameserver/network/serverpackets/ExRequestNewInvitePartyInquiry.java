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
package org.l2j.gameserver.network.serverpackets;

import org.l2j.gameserver.enums.ChatType;
import org.l2j.gameserver.instancemanager.RankManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author Serenitty
 */
public class ExRequestNewInvitePartyInquiry extends ServerPacket
{
	private final int _reqType;
	private final ChatType _sayType;
	private final int _charRankGrade;
	private final int _pledgeCastleDBID;
	private final int _userID;
	private final Player _player;
	
	public ExRequestNewInvitePartyInquiry(Player player, int reqType, ChatType sayType)
	{
		_player = player;
		_userID = _player.getObjectId();
		_reqType = reqType;
		_sayType = sayType;
		
		final int rank = RankManager.getInstance().getPlayerGlobalRank(player);
		_charRankGrade = (rank == 1) ? 1 : (rank <= 30) ? 2 : (rank <= 100) ? 3 : 0;
		
		int castle = 0;
		final Clan clan = _player.getClan();
		if (clan != null)
		{
			castle = clan.getCastleId();
		}
		_pledgeCastleDBID = castle;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_REQUEST_INVITE_PARTY.writeId(this);
		writeSizedString(_player.getName());
		writeByte(_reqType);
		writeByte(_sayType.ordinal());
		writeByte(_charRankGrade);
		writeByte(_pledgeCastleDBID);
		writeByte(_player.isInTimedHuntingZone() || _player.isInSiege() || _player.isRegisteredOnEvent());
		writeInt(0); // Chat background.
		writeInt(_userID);
	}
}
