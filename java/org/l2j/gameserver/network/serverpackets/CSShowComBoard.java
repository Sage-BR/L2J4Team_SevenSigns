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

public class CSShowComBoard extends ServerPacket
{
	private final byte[] _html;
	
	public CSShowComBoard(byte[] html)
	{
		_html = html;
	}
	
	@Override
	public void write()
	{
		ServerPackets.SHOW_BOARD.writeId(this);
		writeByte(1); // c4 1 to show community 00 to hide
		writeBytes(_html);
	}
}
