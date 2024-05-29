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
package org.l2j.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.item.henna.Henna;
import org.l2j.gameserver.model.item.henna.HennaPoten;
import org.l2j.gameserver.model.stats.BaseStat;
import org.l2j.gameserver.network.ServerPackets;

/**
 * This server packet sends the player's henna information.
 * @author Zoey76
 */
public class HennaInfo extends ServerPacket
{
	private final Player _player;
	private final List<Henna> _hennas = new ArrayList<>();
	
	public HennaInfo(Player player)
	{
		_player = player;
		for (HennaPoten hennaPoten : _player.getHennaPotenList())
		{
			if (hennaPoten == null)
			{
				continue;
			}
			
			final Henna henna = hennaPoten.getHenna();
			if (henna != null)
			{
				_hennas.add(henna);
			}
		}
	}
	
	@Override
	public void write()
	{
		ServerPackets.HENNA_INFO.writeId(this);
		writeShort(_player.getHennaValue(BaseStat.INT)); // equip INT
		writeShort(_player.getHennaValue(BaseStat.STR)); // equip STR
		writeShort(_player.getHennaValue(BaseStat.CON)); // equip CON
		writeShort(_player.getHennaValue(BaseStat.MEN)); // equip MEN
		writeShort(_player.getHennaValue(BaseStat.DEX)); // equip DEX
		writeShort(_player.getHennaValue(BaseStat.WIT)); // equip WIT
		writeShort(0); // equip LUC
		writeShort(0); // equip CHA
		writeInt(3 - _player.getHennaEmptySlots()); // Slots
		writeInt(_hennas.size()); // Size
		for (Henna henna : _hennas)
		{
			writeInt(henna.getDyeId());
			writeInt(henna.isAllowedClass(_player));
		}
		writeInt(0); // Premium Slot Dye ID
		writeInt(0); // Premium Slot Dye Time Left
		writeInt(0); // Premium Slot Dye ID isValid
	}
}
