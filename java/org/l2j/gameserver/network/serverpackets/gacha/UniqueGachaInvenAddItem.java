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
package org.l2j.gameserver.network.serverpackets.gacha;

import java.util.List;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.model.holders.GachaItemHolder;
import org.l2j.gameserver.model.holders.ItemHolder;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

public class UniqueGachaInvenAddItem extends ServerPacket
{
	private final List<GachaItemHolder> _rewards;
	
	public UniqueGachaInvenAddItem(List<GachaItemHolder> rewards)
	{
		_rewards = rewards;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_UNIQUE_GACHA_INVEN_ADD_ITEM.writeId(this, buffer);
		buffer.writeInt(_rewards.size());
		for (ItemHolder item : _rewards)
		{
			buffer.writeInt(item.getId());
			buffer.writeLong(item.getCount());
		}
	}
}
