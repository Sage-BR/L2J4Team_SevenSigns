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
package org.l2j.gameserver.network.serverpackets.fishing;

import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.interfaces.ILocational;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Sdw
 */
public class ExUserInfoFishing extends ServerPacket
{
	private final Player _player;
	private final boolean _isFishing;
	private final ILocational _baitLocation;
	
	public ExUserInfoFishing(Player player, boolean isFishing, ILocational baitLocation)
	{
		_player = player;
		_isFishing = isFishing;
		_baitLocation = baitLocation;
	}
	
	public ExUserInfoFishing(Player player, boolean isFishing)
	{
		_player = player;
		_isFishing = isFishing;
		_baitLocation = null;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_USER_INFO_FISHING.writeId(this);
		writeInt(_player.getObjectId());
		writeByte(_isFishing);
		if (_baitLocation == null)
		{
			writeInt(0);
			writeInt(0);
			writeInt(0);
		}
		else
		{
			writeInt(_baitLocation.getX());
			writeInt(_baitLocation.getY());
			writeInt(_baitLocation.getZ());
		}
	}
}
