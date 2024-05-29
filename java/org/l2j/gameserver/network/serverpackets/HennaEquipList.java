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

import java.util.List;

import org.l2j.gameserver.data.xml.HennaData;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.item.henna.Henna;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author Zoey76
 */
public class HennaEquipList extends ServerPacket
{
	private final Player _player;
	private final List<Henna> _hennaEquipList;
	
	public HennaEquipList(Player player)
	{
		_player = player;
		_hennaEquipList = HennaData.getInstance().getHennaList(player);
	}
	
	public HennaEquipList(Player player, List<Henna> list)
	{
		_player = player;
		_hennaEquipList = list;
	}
	
	@Override
	public void write()
	{
		ServerPackets.HENNA_EQUIP_LIST.writeId(this);
		writeLong(_player.getAdena()); // activeChar current amount of Adena
		writeInt(3); // available equip slot
		writeInt(_hennaEquipList.size());
		for (Henna henna : _hennaEquipList)
		{
			// Player must have at least one dye in inventory
			// to be able to see the Henna that can be applied with it.
			if ((_player.getInventory().getItemByItemId(henna.getDyeItemId())) != null)
			{
				writeInt(henna.getDyeId()); // dye Id
				writeInt(henna.getDyeItemId()); // item Id of the dye
				writeLong(henna.getWearCount()); // amount of dyes required
				writeLong(henna.getWearFee()); // amount of Adena required
				writeInt(henna.isAllowedClass(_player)); // meet the requirement or not
				// writeInt(0); // Does not exist in Classic.
			}
		}
	}
}
