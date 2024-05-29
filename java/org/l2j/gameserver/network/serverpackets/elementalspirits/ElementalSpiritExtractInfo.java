/*
 * This file is part of the L2J 4Team project.
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

import org.l2j.gameserver.data.xml.ElementalSpiritData;
import org.l2j.gameserver.enums.ElementalType;
import org.l2j.gameserver.model.ElementalSpirit;
import org.l2j.gameserver.model.actor.Player;
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
	public void write()
	{
		ServerPackets.EX_ELEMENTAL_SPIRIT_EXTRACT_INFO.writeId(this);
		final ElementalSpirit spirit = _player.getElementalSpirit(ElementalType.of(_type));
		if (spirit == null)
		{
			writeByte(0);
			writeByte(0);
			return;
		}
		writeByte(_type); // active elemental spirit
		writeByte(1); // is extract ?
		writeByte(1); // cost count
		// for each cost count
		writeInt(57); // item id
		writeInt(ElementalSpiritData.EXTRACT_FEES[spirit.getStage() - 1]); // item count
		writeInt(spirit.getExtractItem());
		writeInt(spirit.getExtractAmount());
	}
}
