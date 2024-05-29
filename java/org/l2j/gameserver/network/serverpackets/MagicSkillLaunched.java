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

import java.util.Collection;
import java.util.Collections;

import org.l2j.gameserver.model.WorldObject;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.skill.SkillCastingType;
import org.l2j.gameserver.network.ServerPackets;

/**
 * MagicSkillLaunched server packet implementation.
 * @author UnAfraid
 */
public class MagicSkillLaunched extends ServerPacket
{
	private final int _objectId;
	private final int _skillId;
	private final int _skillLevel;
	private final SkillCastingType _castingType;
	private final Collection<WorldObject> _targets;
	
	public MagicSkillLaunched(Creature creature, int skillId, int skillLevel, SkillCastingType castingType, Collection<WorldObject> targets)
	{
		_objectId = creature.getObjectId();
		_skillId = skillId;
		_skillLevel = skillLevel;
		_castingType = castingType;
		if (targets == null)
		{
			_targets = Collections.singletonList(creature);
			return;
		}
		_targets = targets;
	}
	
	public MagicSkillLaunched(Creature creature, int skillId, int skillLevel, SkillCastingType castingType, WorldObject target)
	{
		this(creature, skillId, skillLevel, castingType, Collections.singletonList(target == null ? creature : target));
	}
	
	public MagicSkillLaunched(Creature creature, int skillId, int skillLevel)
	{
		this(creature, skillId, skillLevel, SkillCastingType.NORMAL, Collections.singletonList(creature));
	}
	
	@Override
	public void write()
	{
		ServerPackets.MAGIC_SKILL_LAUNCHED.writeId(this);
		writeInt(_castingType.getClientBarId()); // MagicSkillUse castingType
		writeInt(_objectId);
		writeInt(_skillId);
		writeInt(_skillLevel);
		writeInt(_targets.size());
		for (WorldObject target : _targets)
		{
			writeInt(target.getObjectId());
		}
	}
}
