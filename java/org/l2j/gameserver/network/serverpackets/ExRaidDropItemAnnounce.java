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
package org.l2j.gameserver.network.serverpackets;

import java.util.Collection;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author MacuK
 */
public class ExRaidDropItemAnnounce extends ServerPacket
{
	private final String _killerName;
	private final int _npcId;
	private final Collection<Integer> _items;
	
	public ExRaidDropItemAnnounce(String killerName, int npcId, Collection<Integer> items)
	{
		_killerName = killerName;
		_npcId = npcId + 1000000;
		_items = items;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_RAID_DROP_ITEM_ANNOUNCE.writeId(this, buffer);
		buffer.writeSizedString(_killerName);
		buffer.writeInt(_npcId);
		buffer.writeInt(_items.size());
		for (int itemId : _items)
		{
			buffer.writeInt(itemId);
		}
	}
}
