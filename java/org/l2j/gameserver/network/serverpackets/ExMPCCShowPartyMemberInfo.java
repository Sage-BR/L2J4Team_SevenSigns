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

import org.l2j.gameserver.model.Party;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author chris_00
 */
public class ExMPCCShowPartyMemberInfo extends ServerPacket
{
	private final Party _party;
	
	public ExMPCCShowPartyMemberInfo(Party party)
	{
		_party = party;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_MPCCSHOW_PARTY_MEMBER_INFO.writeId(this);
		writeInt(_party.getMemberCount());
		for (Player pc : _party.getMembers())
		{
			writeString(pc.getName());
			writeInt(pc.getObjectId());
			writeInt(pc.getClassId().getId());
		}
	}
}
