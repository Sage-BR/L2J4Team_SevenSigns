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

public class ExPutIntensiveResultForVariationMake extends ServerPacket
{
	private final int _refinerItemObjId;
	private final int _lifestoneItemId;
	private final int _insertResult;
	
	public ExPutIntensiveResultForVariationMake(int lifeStoneId)
	{
		_lifestoneItemId = lifeStoneId;
		_refinerItemObjId = 0;
		_insertResult = 0;
	}
	
	public ExPutIntensiveResultForVariationMake(int lifeStoneId, int refinerItemObjId, int insertResult)
	{
		_refinerItemObjId = refinerItemObjId;
		_lifestoneItemId = lifeStoneId;
		_insertResult = insertResult;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_PUT_INTENSIVE_RESULT_FOR_VARIATION_MAKE.writeId(this);
		writeInt(_lifestoneItemId);
		writeInt(_refinerItemObjId);
		writeByte(_insertResult);
	}
}
