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
package org.l2jmobius.gameserver.network.serverpackets;

import java.time.Instant;
import java.time.ZoneId;
import java.time.zone.ZoneRules;

import org.l2jmobius.commons.network.WritableBuffer;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;

/**
 * @author Mobius
 */
public class ExEnterWorld extends ServerPacket
{
	private final int _zoneIdOffsetSeconds;
	private final int _epochInSeconds;
	private final int _daylight;
	
	public ExEnterWorld()
	{
		Instant now = Instant.now();
		_epochInSeconds = (int) now.getEpochSecond();
		ZoneRules rules = ZoneId.systemDefault().getRules();
		_zoneIdOffsetSeconds = rules.getStandardOffset(now).getTotalSeconds();
		_daylight = (int) (rules.getDaylightSavings(now).toMillis() / 1000);
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_ENTER_WORLD.writeId(this, buffer);
		buffer.writeInt(_epochInSeconds);
		buffer.writeInt(-_zoneIdOffsetSeconds);
		buffer.writeInt(_daylight);
		buffer.writeInt(Config.MAX_FREE_TELEPORT_LEVEL);
	}
}