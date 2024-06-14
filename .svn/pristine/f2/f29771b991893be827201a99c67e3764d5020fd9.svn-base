/*
 * This file is part of the L2J Mobius project.
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
package org.l2jmobius.gameserver.network.serverpackets.equipmentupgradenormal;

import java.util.ArrayList;
import java.util.List;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.data.xml.EquipmentUpgradeNormalData;
import org.l2jmobius.gameserver.model.holders.ItemHolder;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.AbstractItemPacket;

/**
 * @author Index
 */
public class ExShowUpgradeSystemNormal extends AbstractItemPacket
{
	private final int _mode;
	private final int _type;
	private final int _commission;
	private final List<Integer> _materials = new ArrayList<>();
	private final List<Integer> _discountRatio = new ArrayList<>();
	
	public ExShowUpgradeSystemNormal(int mode, int type)
	{
		_mode = mode;
		_type = type;
		_commission = EquipmentUpgradeNormalData.getInstance().getCommission();
		for (ItemHolder item : EquipmentUpgradeNormalData.getInstance().getDiscount())
		{
			_materials.add(item.getId());
			_discountRatio.add((int) item.getCount());
		}
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_SHOW_UPGRADE_SYSTEM_NORMAL.writeId(this, buffer);
		buffer.writeShort(_mode);
		buffer.writeShort(_type);
		buffer.writeShort(_commission); // default - 100
		buffer.writeInt(_materials.size()); // array of materials with discount
		for (int id : _materials)
		{
			buffer.writeInt(id);
		}
		buffer.writeInt(_discountRatio.size()); // array of discount count
		for (int discount : _discountRatio)
		{
			buffer.writeInt(discount);
		}
	}
}
