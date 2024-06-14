/*
 * This file is part of the L2J 4Team Project.
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
package org.l2j.gameserver.network.serverpackets.pet;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.model.actor.instance.Pet;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Berezkin Nikolay
 */
public class ExPetSkillList extends ServerPacket
{
	private final boolean _onEnter;
	private final Pet _pet;
	
	public ExPetSkillList(boolean onEnter, Pet pet)
	{
		_onEnter = onEnter;
		_pet = pet;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_PET_SKILL_LIST.writeId(this, buffer);
		buffer.writeByte(_onEnter);
		buffer.writeInt(_pet.getAllSkills().size());
		for (Skill sk : _pet.getAllSkills())
		{
			buffer.writeInt(sk.getDisplayId());
			buffer.writeInt(sk.getDisplayLevel());
			buffer.writeInt(sk.getReuseDelayGroup());
			buffer.writeByte(0);
			buffer.writeByte(0);
		}
	}
}
