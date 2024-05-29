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

import org.l2j.gameserver.model.actor.Summon;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author KenM
 */
public class ExPartyPetWindowAdd extends ServerPacket
{
	private final Summon _summon;
	
	public ExPartyPetWindowAdd(Summon summon)
	{
		_summon = summon;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_PARTY_PET_WINDOW_ADD.writeId(this);
		writeInt(_summon.getObjectId());
		writeInt(_summon.getTemplate().getDisplayId() + 1000000);
		writeByte(_summon.getSummonType());
		writeInt(_summon.getOwner().getObjectId());
		writeInt((int) _summon.getCurrentHp());
		writeInt(_summon.getMaxHp());
		writeInt((int) _summon.getCurrentMp());
		writeInt(_summon.getMaxMp());
	}
}
