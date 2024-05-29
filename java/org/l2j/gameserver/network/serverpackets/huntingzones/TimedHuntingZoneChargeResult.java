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
package org.l2j.gameserver.network.serverpackets.huntingzones;

import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author NasSeKa
 */
public class TimedHuntingZoneChargeResult extends ServerPacket
{
	private final int _zoneId;
	private final int _remainTime;
	private final int _refillTime;
	private final int _chargeTime;
	
	public TimedHuntingZoneChargeResult(int zoneId, int remainTime, int refillTime, int chargeTime)
	{
		_zoneId = zoneId;
		_remainTime = remainTime;
		_refillTime = refillTime;
		_chargeTime = chargeTime;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_TIME_RESTRICT_FIELD_USER_CHARGE_RESULT.writeId(this);
		writeInt(_zoneId);
		writeInt(_remainTime);
		writeInt(_refillTime);
		writeInt(_chargeTime);
	}
}
