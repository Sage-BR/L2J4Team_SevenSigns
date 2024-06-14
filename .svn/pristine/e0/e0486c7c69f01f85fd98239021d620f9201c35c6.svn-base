/*
 * This file is part of the L2J Mobius project.
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
package org.l2jmobius.gameserver.network.serverpackets.variation;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Index
 */
public class ApplyVariationOption extends ServerPacket
{
	private final int _result;
	private final int _enchantedObjectId;
	private final int _option1;
	private final int _option2;
	
	public ApplyVariationOption(int result, int enchantedObjectId, int option1, int option2)
	{
		_result = result;
		_enchantedObjectId = enchantedObjectId;
		_option1 = option1;
		_option2 = option2;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_APPLY_VARIATION_OPTION.writeId(this, buffer);
		buffer.writeByte(_result);
		buffer.writeInt(_enchantedObjectId);
		buffer.writeInt(_option1);
		buffer.writeInt(_option2);
	}
}
