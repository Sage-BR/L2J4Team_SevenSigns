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

import java.util.EnumMap;
import java.util.Map;

import org.l2j.gameserver.model.skill.SkillCastingType;
import org.l2j.gameserver.network.ServerPackets;

public class ActionFailed extends ServerPacket
{
	public static final ActionFailed STATIC_PACKET = new ActionFailed();
	
	private static final Map<SkillCastingType, ActionFailed> STATIC_PACKET_BY_CASTING_TYPE = new EnumMap<>(SkillCastingType.class);
	static
	{
		for (SkillCastingType castingType : SkillCastingType.values())
		{
			STATIC_PACKET_BY_CASTING_TYPE.put(castingType, new ActionFailed(castingType.getClientBarId()));
		}
	}
	
	private final int _castingType;
	
	private ActionFailed()
	{
		_castingType = 0;
	}
	
	private ActionFailed(int castingType)
	{
		_castingType = castingType;
	}
	
	public static ActionFailed get(SkillCastingType castingType)
	{
		return STATIC_PACKET_BY_CASTING_TYPE.getOrDefault(castingType, STATIC_PACKET);
	}
	
	@Override
	public void write()
	{
		ServerPackets.ACTION_FAIL.writeId(this);
		writeInt(_castingType); // MagicSkillUse castingType
	}
}
