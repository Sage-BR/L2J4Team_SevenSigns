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
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Berezkin Nikolay
 */
public class ExSubjugationGacha extends ServerPacket
{
	private final Map<Integer, Integer> _rewards;
	
	public ExSubjugationGacha(Map<Integer, Integer> rewards)
	{
		_rewards = rewards;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_SUBJUGATION_GACHA.writeId(this, buffer);
		buffer.writeInt(_rewards.size());
		for (Entry<Integer, Integer> entry : _rewards.entrySet())
		{
			buffer.writeInt(entry.getKey());
			buffer.writeInt(entry.getValue());
		}
	}
}
