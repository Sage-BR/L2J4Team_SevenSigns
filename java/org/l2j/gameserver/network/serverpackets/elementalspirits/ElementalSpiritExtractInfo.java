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
package org.l2j.gameserver.network.serverpackets.elementalspirits;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.data.xml.ElementalSpiritData;
import org.l2j.gameserver.enums.ElementalType;
import org.l2j.gameserver.model.ElementalSpirit;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author JoeAlisson
 */
public class ElementalSpiritExtractInfo extends ServerPacket
{
	private final Player _player;
	private final byte _type;
	
	public ElementalSpiritExtractInfo(Player player, byte type)
	{
		_player = player;
		_type = type;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_ELEMENTAL_SPIRIT_EXTRACT_INFO.writeId(this, buffer);
		final ElementalSpirit spirit = _player.getElementalSpirit(ElementalType.of(_type));
		if (spirit == null)
		{
			buffer.writeByte(0);
			buffer.writeByte(0);
			return;
		}
		buffer.writeByte(_type); // active elemental spirit
		buffer.writeByte(1); // is extract ?
		buffer.writeByte(1); // cost count
		// for each cost count
		buffer.writeInt(57); // item id
		buffer.writeInt(ElementalSpiritData.EXTRACT_FEES[spirit.getStage() - 1]); // item count
		buffer.writeInt(spirit.getExtractItem());
		buffer.writeInt(spirit.getExtractAmount());
	}
}
