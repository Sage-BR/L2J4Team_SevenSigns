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

import org.l2j.gameserver.model.skill.BuffInfo;
import org.l2j.gameserver.network.ServerPackets;

public class AbnormalStatusUpdate extends ServerPacket
{
	private final List<BuffInfo> _effects = new ArrayList<>();
	
	public void addSkill(BuffInfo info)
	{
		if (!info.getSkill().isHealingPotionSkill())
		{
			_effects.add(info);
		}
	}
	
	@Override
	public void write()
	{
		ServerPackets.ABNORMAL_STATUS_UPDATE.writeId(this);
		writeShort(_effects.size());
		for (BuffInfo info : _effects)
		{
			if ((info != null) && info.isInUse())
			{
				writeInt(info.getSkill().getDisplayId());
				writeShort(info.getSkill().getDisplayLevel());
				writeShort(info.getSkill().getSubLevel());
				writeInt(info.getSkill().getAbnormalType().getClientId());
				writeOptionalInt(info.getSkill().isAura() || info.getSkill().isToggle() ? -1 : info.getTime());
			}
		}
	}
}
