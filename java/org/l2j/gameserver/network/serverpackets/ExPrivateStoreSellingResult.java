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

public class ExPrivateStoreSellingResult extends ServerPacket
{
	private final int _objectId;
	private final long _count;
	private final String _buyer;
	
	public ExPrivateStoreSellingResult(int objectId, long count, String buyer)
	{
		_objectId = objectId;
		_count = count;
		_buyer = buyer;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_PRIVATE_STORE_SELLING_RESULT.writeId(this);
		writeInt(_objectId);
		writeLong(_count);
		writeString(_buyer);
	}
}