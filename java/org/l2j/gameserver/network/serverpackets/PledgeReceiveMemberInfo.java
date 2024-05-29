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

import org.l2j.gameserver.model.clan.ClanMember;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author -Wooden-
 */
public class PledgeReceiveMemberInfo extends ServerPacket
{
	private final ClanMember _member;
	
	public PledgeReceiveMemberInfo(ClanMember member)
	{
		_member = member;
	}
	
	@Override
	public void write()
	{
		ServerPackets.PLEDGE_RECEIVE_MEMBER_INFO.writeId(this);
		writeInt(_member.getPledgeType());
		writeString(_member.getName());
		writeString(_member.getTitle()); // title
		writeInt(_member.getPowerGrade()); // power
		// clan or subpledge name
		if (_member.getPledgeType() != 0)
		{
			writeString((_member.getClan().getSubPledge(_member.getPledgeType())).getName());
		}
		else
		{
			writeString(_member.getClan().getName());
		}
		writeString(_member.getApprenticeOrSponsorName()); // name of this member's apprentice/sponsor
	}
}
