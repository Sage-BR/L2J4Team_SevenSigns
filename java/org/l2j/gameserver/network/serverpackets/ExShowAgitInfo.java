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

import java.util.Collection;

import org.l2j.gameserver.data.sql.ClanTable;
import org.l2j.gameserver.data.xml.ClanHallData;
import org.l2j.gameserver.model.residences.ClanHall;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author KenM
 */
public class ExShowAgitInfo extends ServerPacket
{
	public static final ExShowAgitInfo STATIC_PACKET = new ExShowAgitInfo();
	
	private ExShowAgitInfo()
	{
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_SHOW_AGIT_INFO.writeId(this);
		final Collection<ClanHall> clanHalls = ClanHallData.getInstance().getClanHalls();
		writeInt(clanHalls.size());
		clanHalls.forEach(clanHall ->
		{
			writeInt(clanHall.getResidenceId());
			writeString(clanHall.getOwnerId() <= 0 ? "" : ClanTable.getInstance().getClan(clanHall.getOwnerId()).getName()); // owner clan name
			writeString(clanHall.getOwnerId() <= 0 ? "" : ClanTable.getInstance().getClan(clanHall.getOwnerId()).getLeaderName()); // leader name
			writeInt(clanHall.getType().getClientVal()); // Clan hall type
		});
	}
}
