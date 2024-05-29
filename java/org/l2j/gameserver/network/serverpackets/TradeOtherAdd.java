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

import org.l2j.gameserver.model.TradeItem;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author Yme
 */
public class TradeOtherAdd extends AbstractItemPacket
{
	private final int _sendType;
	private final TradeItem _item;
	
	public TradeOtherAdd(int sendType, TradeItem item)
	{
		_sendType = sendType;
		_item = item;
	}
	
	@Override
	public void write()
	{
		ServerPackets.TRADE_OTHER_ADD.writeId(this);
		writeByte(_sendType);
		if (_sendType == 2)
		{
			writeInt(1);
		}
		writeInt(1);
		writeItem(_item);
	}
}
