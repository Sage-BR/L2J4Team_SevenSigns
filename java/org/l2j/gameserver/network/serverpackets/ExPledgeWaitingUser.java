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

import org.l2j.gameserver.model.clan.entry.PledgeApplicantInfo;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author Sdw
 */
public class ExPledgeWaitingUser extends ServerPacket
{
	private final PledgeApplicantInfo _pledgeRecruitInfo;
	
	public ExPledgeWaitingUser(PledgeApplicantInfo pledgeRecruitInfo)
	{
		_pledgeRecruitInfo = pledgeRecruitInfo;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_PLEDGE_WAITING_USER.writeId(this);
		writeInt(_pledgeRecruitInfo.getPlayerId());
		writeString(_pledgeRecruitInfo.getMessage());
	}
}