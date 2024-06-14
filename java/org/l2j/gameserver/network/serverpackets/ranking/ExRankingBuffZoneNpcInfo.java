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
package org.l2j.gameserver.network.serverpackets.ranking;

import java.util.concurrent.TimeUnit;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.instancemanager.GlobalVariablesManager;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Serenitty
 */
public class ExRankingBuffZoneNpcInfo extends ServerPacket
{
	public ExRankingBuffZoneNpcInfo()
	{
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_RANKING_CHAR_BUFFZONE_NPC_INFO.writeId(this, buffer);
		final long cooldown = GlobalVariablesManager.getInstance().getLong(GlobalVariablesManager.RANKING_POWER_COOLDOWN, 0);
		final long currentTime = System.currentTimeMillis();
		if (cooldown > currentTime)
		{
			final long reuseTime = TimeUnit.MILLISECONDS.toSeconds(cooldown - currentTime);
			buffer.writeInt((int) reuseTime);
		}
		else
		{
			buffer.writeInt(0);
		}
	}
}
