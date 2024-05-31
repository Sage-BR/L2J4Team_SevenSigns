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

import org.l2j.Config;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.network.ServerPackets;

public class PledgeShowInfoUpdate extends ServerPacket
{
	private final Clan _clan;
	
	public PledgeShowInfoUpdate(Clan clan)
	{
		_clan = clan;
	}
	
	@Override
	public void write()
	{
		ServerPackets.PLEDGE_SHOW_INFO_UPDATE.writeId(this);
		// sending empty data so client will ask all the info in response ;)
		writeInt(_clan.getId());
		writeInt(Config.SERVER_ID);
		writeInt(_clan.getCrestId());
		writeInt(_clan.getLevel()); // clan level
		writeInt(_clan.getCastleId());
		writeInt(0); // castle state ?
		writeInt(_clan.getHideoutId());
		writeInt(_clan.getFortId());
		writeInt(_clan.getRank());
		writeInt(_clan.getReputationScore()); // clan reputation score
		writeInt(0); // ?
		writeInt(0); // ?
		writeInt(_clan.getAllyId());
		writeString(_clan.getAllyName()); // c5
		writeInt(_clan.getAllyCrestId()); // c5
		writeInt(_clan.isAtWar()); // c5
		writeInt(0); // TODO: Find me!
		writeInt(0); // TODO: Find me!
	}
}
