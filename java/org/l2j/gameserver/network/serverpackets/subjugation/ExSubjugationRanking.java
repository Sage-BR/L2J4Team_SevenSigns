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
package org.l2j.gameserver.network.serverpackets.subjugation;

import java.util.Map;
import java.util.Map.Entry;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.instancemanager.PurgeRankingManager;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Berezkin Nikolay
 */
public class ExSubjugationRanking extends ServerPacket
{
	private final Map<String, Integer> _ranking;
	private final int _category;
	private final int _objectId;
	
	public ExSubjugationRanking(int category, int objectId)
	{
		_ranking = PurgeRankingManager.getInstance().getTop5(category);
		_category = category;
		_objectId = objectId;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_SUBJUGATION_RANKING.writeId(this, buffer);
		buffer.writeInt(_ranking.entrySet().size());
		int counter = 1;
		for (Entry<String, Integer> data : _ranking.entrySet())
		{
			buffer.writeSizedString(data.getKey());
			buffer.writeInt(data.getValue());
			buffer.writeInt(counter++);
		}
		buffer.writeInt(_category);
		buffer.writeInt(PurgeRankingManager.getInstance().getPlayerRating(_category, _objectId).getValue());
		buffer.writeInt(PurgeRankingManager.getInstance().getPlayerRating(_category, _objectId).getKey());
	}
}
