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

import org.l2j.gameserver.model.clan.Clan.RankPrivs;
import org.l2j.gameserver.network.ServerPackets;

public class PledgePowerGradeList extends ServerPacket
{
	private final Collection<RankPrivs> _privs;
	
	public PledgePowerGradeList(Collection<RankPrivs> privs)
	{
		_privs = privs;
	}
	
	@Override
	public void write()
	{
		ServerPackets.PLEDGE_POWER_GRADE_LIST.writeId(this);
		writeInt(_privs.size());
		for (RankPrivs temp : _privs)
		{
			writeInt(temp.getRank());
			writeInt(temp.getParty());
		}
	}
}
