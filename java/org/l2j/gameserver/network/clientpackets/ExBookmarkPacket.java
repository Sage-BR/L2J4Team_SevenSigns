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
package org.l2j.gameserver.network.clientpackets;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.network.GameClient;

/**
 * @author Sdw
 */
public class ExBookmarkPacket implements ClientPacket
{
	private ClientPacket _exBookmarkPacket;
	
	@Override
	public void read(ReadablePacket packet)
	{
		final int subId = packet.readInt();
		switch (subId)
		{
			case 0:
			{
				_exBookmarkPacket = new RequestBookMarkSlotInfo();
				break;
			}
			case 1:
			{
				_exBookmarkPacket = new RequestSaveBookMarkSlot();
				break;
			}
			case 2:
			{
				_exBookmarkPacket = new RequestModifyBookMarkSlot();
				break;
			}
			case 3:
			{
				_exBookmarkPacket = new RequestDeleteBookMarkSlot();
				break;
			}
			case 4:
			{
				_exBookmarkPacket = new RequestTeleportBookMark();
				break;
			}
			case 5:
			{
				_exBookmarkPacket = new RequestChangeBookMarkSlot();
				break;
			}
		}
		
		if (_exBookmarkPacket != null)
		{
			_exBookmarkPacket.read(packet);
		}
	}
	
	@Override
	public void run(GameClient client)
	{
		if (_exBookmarkPacket != null)
		{
			_exBookmarkPacket.run(client);
		}
	}
}
