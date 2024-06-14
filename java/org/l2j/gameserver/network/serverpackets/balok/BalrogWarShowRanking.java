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
package org.l2j.gameserver.network.serverpackets.balok;

import java.util.Map;
import java.util.Map.Entry;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.data.sql.CharInfoTable;
import org.l2j.gameserver.instancemanager.BattleWithBalokManager;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Serenitty, Index
 */
public class BalrogWarShowRanking extends ServerPacket
{
	private final Map<Integer, Integer> _rankingData;
	
	public BalrogWarShowRanking()
	{
		_rankingData = BattleWithBalokManager.getInstance().getTopPlayers(150);
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_BALROGWAR_SHOW_RANKING.writeId(this, buffer);
		buffer.writeInt(_rankingData.size());
		int rank = 0;
		for (Entry<Integer, Integer> entry : _rankingData.entrySet())
		{
			rank++;
			buffer.writeInt(rank); // Rank
			buffer.writeSizedString(CharInfoTable.getInstance().getNameById(entry.getKey())); // Name
			buffer.writeInt(entry.getValue()); // Score
		}
	}
}