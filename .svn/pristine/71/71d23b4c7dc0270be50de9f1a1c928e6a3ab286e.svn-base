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
package org.l2jmobius.gameserver.network.serverpackets.huntingzones;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Mobius, Index, NasSeKa`, Serenitty
 */
public class TimedHuntingZoneEnter extends ServerPacket
{
	private final Player _player;
	private final int _zoneId;
	
	public TimedHuntingZoneEnter(Player player, int zoneId)
	{
		_player = player;
		_zoneId = zoneId;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_TIME_RESTRICT_FIELD_USER_ENTER.writeId(this, buffer);
		buffer.writeByte(1); // bEnterSuccess
		buffer.writeInt(_zoneId);
		buffer.writeInt((int) (System.currentTimeMillis() / 1000)); // nEnterTimeStamp
		buffer.writeInt((_player.getTimedHuntingZoneRemainingTime(_zoneId) / 1000) + 59); // nRemainTime (zone left time)
	}
}