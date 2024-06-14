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
package org.l2jmobius.gameserver.network.serverpackets.revenge;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.enums.RevengeType;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Mobius
 */
public class ExPvpBookShareRevengeNewRevengeInfo extends ServerPacket
{
	private final String _victimName;
	private final String _killerName;
	private final RevengeType _type;
	
	public ExPvpBookShareRevengeNewRevengeInfo(String victimName, String killerName, RevengeType type)
	{
		_victimName = victimName;
		_killerName = killerName;
		_type = type;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_PVPBOOK_SHARE_REVENGE_NEW_REVENGEINFO.writeId(this, buffer);
		buffer.writeInt(_type.ordinal());
		buffer.writeSizedString(_victimName);
		buffer.writeSizedString(_killerName);
	}
}