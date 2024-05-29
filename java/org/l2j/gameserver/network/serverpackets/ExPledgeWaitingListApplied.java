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

import org.l2j.gameserver.instancemanager.ClanEntryManager;
import org.l2j.gameserver.model.clan.entry.PledgeApplicantInfo;
import org.l2j.gameserver.model.clan.entry.PledgeRecruitInfo;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author Sdw
 */
public class ExPledgeWaitingListApplied extends ServerPacket
{
	private final PledgeApplicantInfo _pledgePlayerRecruitInfo;
	private final PledgeRecruitInfo _pledgeRecruitInfo;
	
	public ExPledgeWaitingListApplied(int clanId, int playerId)
	{
		_pledgePlayerRecruitInfo = ClanEntryManager.getInstance().getPlayerApplication(clanId, playerId);
		_pledgeRecruitInfo = ClanEntryManager.getInstance().getClanById(clanId);
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_PLEDGE_WAITING_LIST_APPLIED.writeId(this);
		writeInt(_pledgeRecruitInfo.getClan().getId());
		writeString(_pledgeRecruitInfo.getClan().getName());
		writeString(_pledgeRecruitInfo.getClan().getLeaderName());
		writeInt(_pledgeRecruitInfo.getClan().getLevel());
		writeInt(_pledgeRecruitInfo.getClan().getMembersCount());
		writeInt(_pledgeRecruitInfo.getKarma());
		writeString(_pledgeRecruitInfo.getInformation());
		writeString(_pledgePlayerRecruitInfo.getMessage());
	}
}
