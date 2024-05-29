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

import org.l2j.gameserver.network.ServerPackets;

public class ExAutoSoulShot extends ServerPacket
{
	private final int _itemId;
	private final boolean _enable;
	private final int _type;
	
	/**
	 * @param itemId
	 * @param enable
	 * @param type
	 */
	public ExAutoSoulShot(int itemId, boolean enable, int type)
	{
		_itemId = itemId;
		_enable = enable;
		_type = type;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_AUTO_SOUL_SHOT.writeId(this);
		writeInt(_itemId);
		writeInt(_enable);
		writeInt(_type);
	}
}
