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
import org.l2j.gameserver.model.actor.Summon;
import org.l2j.gameserver.network.ServerPackets;

public class PartySmallWindowAll extends ServerPacket
{
	private final Party _party;
	private final Player _exclude;
	
	public PartySmallWindowAll(Player exclude, Party party)
	{
		_exclude = exclude;
		_party = party;
	}
	
	@Override
	public void write()
	{
		ServerPackets.PARTY_SMALL_WINDOW_ALL.writeId(this);
		writeInt(_party.getLeaderObjectId());
		writeByte(_party.getDistributionType().getId());
		writeByte(_party.getMemberCount() - 1);
		for (Player member : _party.getMembers())
		{
			if ((member != null) && (member != _exclude))
			{
				writeInt(member.getObjectId());
				writeString(member.getName());
				writeInt((int) member.getCurrentCp()); // c4
				writeInt(member.getMaxCp()); // c4
				writeInt((int) member.getCurrentHp());
				writeInt(member.getMaxHp());
				writeInt((int) member.getCurrentMp());
				writeInt(member.getMaxMp());
				writeInt(member.getVitalityPoints());
				writeByte(member.getLevel());
				writeShort(member.getClassId().getId());
				writeByte(1); // Unk
				writeShort(member.getRace().ordinal());
				writeInt(0); // 228
				final Summon pet = member.getPet();
				writeInt(member.getServitors().size() + (pet != null ? 1 : 0)); // Summon size, one only atm
				if (pet != null)
				{
					writeInt(pet.getObjectId());
					writeInt(pet.getId() + 1000000);
					writeByte(pet.getSummonType());
					writeString(pet.getName());
					writeInt((int) pet.getCurrentHp());
					writeInt(pet.getMaxHp());
					writeInt((int) pet.getCurrentMp());
					writeInt(pet.getMaxMp());
					writeByte(pet.getLevel());
				}
				member.getServitors().values().forEach(s ->
				{
					writeInt(s.getObjectId());
					writeInt(s.getId() + 1000000);
					writeByte(s.getSummonType());
					writeString(s.getName());
					writeInt((int) s.getCurrentHp());
					writeInt(s.getMaxHp());
					writeInt((int) s.getCurrentMp());
					writeInt(s.getMaxMp());
					writeByte(s.getLevel());
				});
			}
		}
	}
}
