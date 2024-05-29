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
package org.l2j.gameserver.network.serverpackets.pledgedonation;

import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Berezkin Nikolay
 */
public class ExPledgeDonationRequest extends ServerPacket
{
	private final boolean _success;
	private final int _type;
	private final int _curPoints;
	
	public ExPledgeDonationRequest(boolean success, int type, int curPoints)
	{
		_success = success;
		_type = type;
		_curPoints = curPoints;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_PLEDGE_DONATION_REQUEST.writeId(this);
		writeByte(_type);
		writeInt(_success);
		writeShort(0);
		writeInt(3);
		writeInt(14);
		writeLong(0);
		writeShort(0);
		writeInt(_curPoints);
	}
}
