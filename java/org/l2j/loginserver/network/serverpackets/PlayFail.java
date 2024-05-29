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
package org.l2j.loginserver.network.serverpackets;

import org.l2j.commons.network.WritablePacket;
import org.l2j.loginserver.enums.PlayFailReason;
import org.l2j.loginserver.network.LoginServerPackets;

/**
 * @version $Revision: 1.2.4.1 $ $Date: 2005/03/27 15:30:11 $
 */
public class PlayFail extends WritablePacket
{
	private final PlayFailReason _reason;
	
	public PlayFail(PlayFailReason reason)
	{
		_reason = reason;
	}
	
	@Override
	public void write()
	{
		LoginServerPackets.PLAY_FAIL.writeId(this);
		writeByte(_reason.getCode());
	}
}
