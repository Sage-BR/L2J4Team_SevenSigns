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

import java.util.List;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.instancemanager.GlobalVariablesManager;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Serenitty
 */
public class ExRankingBuffZoneNpcPosition extends ServerPacket
{
	public ExRankingBuffZoneNpcPosition()
	{
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_RANKING_CHAR_BUFFZONE_NPC_POSITION.writeId(this, buffer);
		if (GlobalVariablesManager.getInstance().getLong(GlobalVariablesManager.RANKING_POWER_COOLDOWN, 0) > System.currentTimeMillis())
		{
			final List<Integer> location = GlobalVariablesManager.getInstance().getIntegerList(GlobalVariablesManager.RANKING_POWER_LOCATION);
			buffer.writeByte(1);
			buffer.writeInt(location.get(0));
			buffer.writeInt(location.get(1));
			buffer.writeInt(location.get(2));
		}
		else
		{
			buffer.writeByte(0);
			buffer.writeInt(0);
			buffer.writeInt(0);
			buffer.writeInt(0);
		}
	}
}
