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

import org.l2j.gameserver.data.sql.CrestTable;
import org.l2j.gameserver.model.Crest;
import org.l2j.gameserver.network.ServerPackets;

public class AllyCrest extends ServerPacket
{
	private final int _clanId;
	private final int _crestId;
	private final byte[] _data;
	
	public AllyCrest(int crestId, int clanId)
	{
		_crestId = crestId;
		_clanId = clanId;
		final Crest crest = CrestTable.getInstance().getCrest(crestId);
		_data = crest != null ? crest.getData() : null;
	}
	
	public AllyCrest(int crestId, int clanId, byte[] data)
	{
		_crestId = crestId;
		_clanId = clanId;
		_data = data;
	}
	
	@Override
	public void write()
	{
		ServerPackets.ALLIANCE_CREST.writeId(this);
		writeInt(_crestId);
		writeInt(_clanId);
		if (_data != null)
		{
			writeInt(_data.length);
			writeInt(_data.length);
			writeBytes(_data);
		}
		else
		{
			writeInt(0);
		}
	}
}
