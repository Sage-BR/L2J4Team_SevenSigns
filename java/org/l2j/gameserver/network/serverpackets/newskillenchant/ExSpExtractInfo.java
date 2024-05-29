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
package org.l2j.gameserver.network.serverpackets.newskillenchant;

import org.l2j.gameserver.model.ItemInfo;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.itemcontainer.Inventory;
import org.l2j.gameserver.model.variables.PlayerVariables;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.AbstractItemPacket;

/**
 * @author Serenitty
 */
public class ExSpExtractInfo extends AbstractItemPacket
{
	private final Player _player;
	
	public ExSpExtractInfo(Player player)
	{
		_player = player;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_SP_EXTRACT_INFO.writeId(this);
		writeInt(Inventory.SP_POUCH); // ItemID
		writeInt(1); // ExtractCount
		writeLong(5000000000L); // NeedSP
		writeInt(100); // chance
		writeInt(0); // critical rate
		writeShort(calculatePacketSize(new ItemInfo(new Item(Inventory.SP_POINTS))));
		writeInt(Inventory.SP_POINTS);
		writeLong(5000000000L);
		writeShort(calculatePacketSize(new ItemInfo(new Item(Inventory.ADENA_ID))));
		writeInt(Inventory.ADENA_ID);
		writeLong(3000000);
		writeShort(calculatePacketSize(new ItemInfo(new Item(Inventory.ADENA_ID))));
		writeInt(Inventory.ADENA_ID);
		writeLong(1);
		writeInt(_player.getVariables().getInt(PlayerVariables.DAILY_EXTRACT_ITEM + Inventory.SP_POUCH, 5));
		writeInt(5);
	}
}
