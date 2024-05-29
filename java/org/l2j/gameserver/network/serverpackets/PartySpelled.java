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

import java.util.ArrayList;
import java.util.List;

import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.skill.BuffInfo;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.network.ServerPackets;

public class PartySpelled extends ServerPacket
{
	private final List<BuffInfo> _effects = new ArrayList<>();
	private final List<Skill> _effects2 = new ArrayList<>();
	private final Creature _creature;
	
	public PartySpelled(Creature creature)
	{
		_creature = creature;
	}
	
	public void addSkill(BuffInfo info)
	{
		_effects.add(info);
	}
	
	public void addSkill(Skill skill)
	{
		_effects2.add(skill);
	}
	
	@Override
	public void write()
	{
		ServerPackets.PARTY_SPELLED.writeId(this);
		writeInt(_creature.isServitor() ? 2 : _creature.isPet() ? 1 : 0);
		writeInt(_creature.getObjectId());
		writeInt(_effects.size() + _effects2.size());
		for (BuffInfo info : _effects)
		{
			if ((info != null) && info.isInUse())
			{
				writeInt(info.getSkill().getDisplayId());
				writeShort(info.getSkill().getDisplayLevel());
				writeShort(0); // Sub level
				writeInt(info.getSkill().getAbnormalType().getClientId());
				writeOptionalInt(info.getTime());
			}
		}
		for (Skill skill : _effects2)
		{
			if (skill != null)
			{
				writeInt(skill.getDisplayId());
				writeShort(skill.getDisplayLevel());
				writeShort(0); // Sub level
				writeInt(skill.getAbnormalType().getClientId());
				writeShort(-1);
			}
		}
	}
}
