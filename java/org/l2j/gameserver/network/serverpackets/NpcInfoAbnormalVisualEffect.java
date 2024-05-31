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

import java.util.Set;

import org.l2j.Config;
import org.l2j.gameserver.enums.Team;
import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.skill.AbnormalVisualEffect;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author Sdw
 */
public class NpcInfoAbnormalVisualEffect extends ServerPacket
{
	private final Npc _npc;
	
	public NpcInfoAbnormalVisualEffect(Npc npc)
	{
		_npc = npc;
	}
	
	@Override
	public void write()
	{
		ServerPackets.NPC_INFO_ABNORMAL_VISUAL_EFFECT.writeId(this);
		writeInt(_npc.getObjectId());
		writeInt(_npc.getTransformationDisplayId());
		final Set<AbnormalVisualEffect> abnormalVisualEffects = _npc.getEffectList().getCurrentAbnormalVisualEffects();
		final Team team = (Config.BLUE_TEAM_ABNORMAL_EFFECT != null) && (Config.RED_TEAM_ABNORMAL_EFFECT != null) ? _npc.getTeam() : Team.NONE;
		writeInt(abnormalVisualEffects.size() + (team != Team.NONE ? 1 : 0));
		for (AbnormalVisualEffect abnormalVisualEffect : abnormalVisualEffects)
		{
			writeShort(abnormalVisualEffect.getClientId());
		}
		if (team == Team.BLUE)
		{
			if (Config.BLUE_TEAM_ABNORMAL_EFFECT != null)
			{
				writeShort(Config.BLUE_TEAM_ABNORMAL_EFFECT.getClientId());
			}
		}
		else if ((team == Team.RED) && (Config.RED_TEAM_ABNORMAL_EFFECT != null))
		{
			writeShort(Config.RED_TEAM_ABNORMAL_EFFECT.getClientId());
		}
	}
}
