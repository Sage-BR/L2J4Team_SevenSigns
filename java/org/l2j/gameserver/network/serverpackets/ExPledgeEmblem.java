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

import org.l2j.Config;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author -Wooden-, Sdw
 */
public class ExPledgeEmblem extends ServerPacket
{
	private static final int TOTAL_SIZE = 65664;
	
	private final int _crestId;
	private final int _clanId;
	private final byte[] _data;
	private final int _chunkId;
	
	public ExPledgeEmblem(int crestId, byte[] chunkedData, int clanId, int chunkId)
	{
		_crestId = crestId;
		_data = chunkedData;
		_clanId = clanId;
		_chunkId = chunkId;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_PLEDGE_EMBLEM.writeId(this);
		writeInt(Config.SERVER_ID);
		writeInt(_clanId);
		writeInt(_crestId);
		writeInt(_chunkId);
		writeInt(TOTAL_SIZE);
		if (_data != null)
		{
			writeInt(_data.length);
			writeBytes(_data);
		}
		else
		{
			writeInt(0);
		}
	}
}