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
package org.l2j.gameserver.network.serverpackets.pledgeV3;

import org.l2j.gameserver.instancemanager.GlobalVariablesManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Index
 */
public class ExPledgeClassicRaidInfo extends ServerPacket
{
	private final Clan _clan;
	
	public ExPledgeClassicRaidInfo(Player player)
	{
		_clan = player.getClan();
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_PLEDGE_CLASSIC_RAID_INFO.writeId(this);
		if (_clan == null)
		{
			writeInt(0);
		}
		else
		{
			final int stage = GlobalVariablesManager.getInstance().getInt(GlobalVariablesManager.MONSTER_ARENA_VARIABLE + _clan.getId(), 0);
			writeInt(stage);
			// Skill rewards.
			writeInt(5);
			for (int i = 1; i <= 5; i++)
			{
				writeInt(1867);
				writeInt(i);
			}
		}
	}
}
