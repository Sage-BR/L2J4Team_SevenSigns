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
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.model.clan.Clan.SubPledge;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author Sdw
 */
public class ExPledgeRecruitInfo extends ServerPacket
{
	private final Clan _clan;
	
	public ExPledgeRecruitInfo(int clanId)
	{
		_clan = ClanTable.getInstance().getClan(clanId);
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_PLEDGE_RECRUIT_INFO.writeId(this);
		final Collection<SubPledge> subPledges = _clan.getAllSubPledges();
		writeString(_clan.getName());
		writeString(_clan.getLeaderName());
		writeInt(_clan.getLevel());
		writeInt(_clan.getMembersCount());
		writeInt(subPledges.size());
		for (SubPledge subPledge : subPledges)
		{
			writeInt(subPledge.getId());
			writeString(subPledge.getName());
		}
	}
}
