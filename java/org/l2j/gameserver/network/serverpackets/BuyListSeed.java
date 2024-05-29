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

import org.l2j.gameserver.instancemanager.CastleManorManager;
import org.l2j.gameserver.model.SeedProduction;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author l3x
 */
public class BuyListSeed extends ServerPacket
{
	private final int _manorId;
	private final long _money;
	private final List<SeedProduction> _list = new ArrayList<>();
	
	public BuyListSeed(long currentMoney, int castleId)
	{
		_money = currentMoney;
		_manorId = castleId;
		for (SeedProduction s : CastleManorManager.getInstance().getSeedProduction(castleId, false))
		{
			if ((s.getAmount() > 0) && (s.getPrice() > 0))
			{
				_list.add(s);
			}
		}
	}
	
	@Override
	public void write()
	{
		ServerPackets.BUY_LIST_SEED.writeId(this);
		writeLong(_money); // current money
		writeInt(0); // TODO: Find me!
		writeInt(_manorId); // manor id
		if (!_list.isEmpty())
		{
			writeShort(_list.size()); // list length
			for (SeedProduction s : _list)
			{
				writeByte(0); // mask item 0 to print minimum item information
				writeInt(s.getId()); // ObjectId
				writeInt(s.getId()); // ItemId
				writeByte(0xFF); // T1
				writeLong(s.getAmount()); // Quantity
				writeByte(5); // Item Type 2 : 00-weapon, 01-shield/armor, 02-ring/earring/necklace, 03-questitem, 04-adena, 05-item
				writeByte(0); // Filler (always 0)
				writeShort(0); // Equipped : 00-No, 01-yes
				writeLong(0); // Slot : 0006-lr.ear, 0008-neck, 0030-lr.finger, 0040-head, 0100-l.hand, 0200-gloves, 0400-chest, 0800-pants, 1000-feet, 4000-r.hand, 8000-r.hand
				writeShort(0); // Enchant level (pet level shown in control item)
				writeInt(-1);
				writeInt(-9999);
				writeByte(1); // GOD Item enabled = 1 disabled (red) = 0
				writeLong(s.getPrice()); // price
			}
			_list.clear();
		}
		else
		{
			writeShort(0);
		}
	}
}
