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

import org.l2j.gameserver.model.ElementalSpirit;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author Magik
 */
public class ElementalSpiritInfo extends AbstractElementalSpiritPacket
{
	private final Player _player;
	private final byte _type;
	
	public ElementalSpiritInfo(Player player, byte packetType)
	{
		_player = player;
		_type = packetType;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_ELEMENTAL_SPIRIT_INFO.writeId(this);
		final ElementalSpirit[] spirits = _player.getSpirits();
		if (spirits == null)
		{
			writeByte(0);
			writeByte(0);
			writeByte(0);
			return;
		}
		
		writeByte(_type); // show spirit info window 1; Change type 2; Only update 0
		writeByte(spirits.length); // spirit count
		
		for (ElementalSpirit spirit : spirits)
		{
			writeByte(spirit.getType());
			writeByte(1);
			writeSpiritInfo(spirit);
		}
		
		writeInt(1); // Reset talent items count
		for (int i = 0; i < 1; i++)
		{
			writeInt(57);
			writeLong(50000);
		}
	}
}
