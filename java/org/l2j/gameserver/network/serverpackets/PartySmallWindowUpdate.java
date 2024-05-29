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

import org.l2j.gameserver.enums.PartySmallWindowUpdateType;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.ServerPackets;

public class PartySmallWindowUpdate extends AbstractMaskPacket<PartySmallWindowUpdateType>
{
	private final Player _member;
	private int _flags = 0;
	
	public PartySmallWindowUpdate(Player member, boolean addAllFlags)
	{
		_member = member;
		if (addAllFlags)
		{
			for (PartySmallWindowUpdateType type : PartySmallWindowUpdateType.values())
			{
				addComponentType(type);
			}
		}
	}
	
	@Override
	public void write()
	{
		ServerPackets.PARTY_SMALL_WINDOW_UPDATE.writeId(this);
		writeInt(_member.getObjectId());
		writeShort(_flags);
		if (containsMask(PartySmallWindowUpdateType.CURRENT_CP))
		{
			writeInt((int) _member.getCurrentCp()); // c4
		}
		if (containsMask(PartySmallWindowUpdateType.MAX_CP))
		{
			writeInt(_member.getMaxCp()); // c4
		}
		if (containsMask(PartySmallWindowUpdateType.CURRENT_HP))
		{
			writeInt((int) _member.getCurrentHp());
		}
		if (containsMask(PartySmallWindowUpdateType.MAX_HP))
		{
			writeInt(_member.getMaxHp());
		}
		if (containsMask(PartySmallWindowUpdateType.CURRENT_MP))
		{
			writeInt((int) _member.getCurrentMp());
		}
		if (containsMask(PartySmallWindowUpdateType.MAX_MP))
		{
			writeInt(_member.getMaxMp());
		}
		if (containsMask(PartySmallWindowUpdateType.LEVEL))
		{
			writeByte(_member.getLevel());
		}
		if (containsMask(PartySmallWindowUpdateType.CLASS_ID))
		{
			writeShort(_member.getClassId().getId());
		}
		if (containsMask(PartySmallWindowUpdateType.PARTY_SUBSTITUTE))
		{
			writeByte(0);
		}
		if (containsMask(PartySmallWindowUpdateType.VITALITY_POINTS))
		{
			writeInt(_member.getVitalityPoints());
		}
	}
	
	@Override
	protected void addMask(int mask)
	{
		_flags |= mask;
	}
	
	@Override
	public boolean containsMask(PartySmallWindowUpdateType component)
	{
		return containsMask(_flags, component);
	}
	
	@Override
	protected byte[] getMasks()
	{
		return new byte[0];
	}
}
