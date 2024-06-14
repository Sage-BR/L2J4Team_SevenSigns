/*
 * This file is part of the L2J Mobius project.
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
package org.l2jmobius.gameserver.network.serverpackets.raidbossinfo;

import java.util.Map;
import java.util.Map.Entry;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.enums.RaidBossStatus;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Mobius
 */
public class ExRaidBossSpawnInfo extends ServerPacket
{
	
	private final Map<Integer, RaidBossStatus> _statuses;
	
	public ExRaidBossSpawnInfo(Map<Integer, RaidBossStatus> statuses)
	{
		_statuses = statuses;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_RAID_BOSS_SPAWN_INFO.writeId(this, buffer);
		buffer.writeInt(0); // BossRespawnFactor
		buffer.writeInt(_statuses.size()); // count
		for (Entry<Integer, RaidBossStatus> entry : _statuses.entrySet())
		{
			buffer.writeInt(entry.getKey());
			buffer.writeInt(entry.getValue().ordinal());
			buffer.writeInt(0); // DeadDateTime
		}
	}
}
