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

import org.l2j.gameserver.enums.ElementalType;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Magik
 */
public class ExElementalSpiritAttackType extends ServerPacket
{
	private final Player _player;
	
	public ExElementalSpiritAttackType(Player player)
	{
		_player = player;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_ELEMENTAL_SPIRIT_ATTACK_TYPE.writeId(this);
		final byte elementalId = _player.getActiveElementalSpiritType();
		if (elementalId == ElementalType.WIND.getId())
		{
			writeByte(4);
		}
		else if (elementalId == ElementalType.EARTH.getId())
		{
			writeByte(8);
		}
		else
		{
			writeByte(elementalId);
		}
	}
}
