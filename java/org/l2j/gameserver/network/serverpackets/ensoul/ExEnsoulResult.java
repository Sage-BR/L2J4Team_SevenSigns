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
package org.l2j.gameserver.network.serverpackets.ensoul;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.model.ensoul.EnsoulOption;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author UnAfraid
 */
public class ExEnsoulResult extends ServerPacket
{
	private final int _success;
	private final Item _item;
	
	public ExEnsoulResult(int success, Item item)
	{
		_success = success;
		_item = item;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_ENSOUL_RESULT.writeId(this, buffer);
		buffer.writeByte(_success); // success / failure
		buffer.writeByte(_item.getSpecialAbilities().size());
		for (EnsoulOption option : _item.getSpecialAbilities())
		{
			buffer.writeInt(option.getId());
		}
		buffer.writeByte(_item.getAdditionalSpecialAbilities().size());
		for (EnsoulOption option : _item.getAdditionalSpecialAbilities())
		{
			buffer.writeInt(option.getId());
		}
	}
}
