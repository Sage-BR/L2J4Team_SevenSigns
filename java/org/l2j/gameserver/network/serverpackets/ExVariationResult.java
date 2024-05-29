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

public class ExVariationResult extends ServerPacket
{
	public static final ExVariationResult FAIL = new ExVariationResult(0, 0, false);
	
	private final int _option1;
	private final int _option2;
	private final boolean _success;
	
	public ExVariationResult(int option1, int option2, boolean success)
	{
		_option1 = option1;
		_option2 = option2;
		_success = success;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_VARIATION_RESULT.writeId(this);
		writeInt(_option1);
		writeInt(_option2);
		writeLong(0); // GemStoneCount
		writeLong(0); // NecessaryGemStoneCount
		writeInt(_success);
	}
}
