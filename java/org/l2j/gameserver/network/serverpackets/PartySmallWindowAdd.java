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

public class PartySmallWindowAdd extends ServerPacket
{
	private final Player _member;
	private final Party _party;
	
	public PartySmallWindowAdd(Player member, Party party)
	{
		_member = member;
		_party = party;
	}
	
	@Override
	public void write()
	{
		ServerPackets.PARTY_SMALL_WINDOW_ADD.writeId(this);
		writeInt(_party.getLeaderObjectId()); // c3
		writeInt(_party.getDistributionType().getId()); // c3
		writeInt(_member.getObjectId());
		writeString(_member.getName());
		writeInt((int) _member.getCurrentCp()); // c4
		writeInt(_member.getMaxCp()); // c4
		writeInt((int) _member.getCurrentHp());
		writeInt(_member.getMaxHp());
		writeInt((int) _member.getCurrentMp());
		writeInt(_member.getMaxMp());
		writeInt(_member.getVitalityPoints());
		writeByte(_member.getLevel());
		writeShort(_member.getClassId().getId());
		writeByte(0);
		writeShort(_member.getRace().ordinal());
		writeInt(0); // 228
	}
}
