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
package org.l2j.gameserver.network.serverpackets.appearance;

import org.l2j.gameserver.model.item.appearance.AppearanceStone;
import org.l2j.gameserver.model.item.appearance.AppearanceTargetType;
import org.l2j.gameserver.model.item.appearance.AppearanceType;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author UnAfraid
 */
public class ExChooseShapeShiftingItem extends ServerPacket
{
	private final AppearanceType _type;
	private final AppearanceTargetType _targetType;
	private final int _itemId;
	
	public ExChooseShapeShiftingItem(AppearanceStone stone)
	{
		_type = stone.getType();
		_targetType = stone.getTargetTypes().size() > 1 ? AppearanceTargetType.ALL : stone.getTargetTypes().stream().findFirst().get();
		_itemId = stone.getId();
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_CHOOSE_SHAPE_SHIFTING_ITEM.writeId(this);
		writeInt(_targetType != null ? _targetType.ordinal() : 0);
		writeInt(_type != null ? _type.ordinal() : 0);
		writeInt(_itemId);
	}
}
