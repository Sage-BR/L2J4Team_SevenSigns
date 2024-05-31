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

import java.util.Calendar;

import org.l2j.Config;
import org.l2j.gameserver.data.sql.ClanTable;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.model.siege.Castle;
import org.l2j.gameserver.network.PacketLogger;
import org.l2j.gameserver.network.ServerPackets;

/**
 * Shows the Siege Info<br>
 * <br>
 * c = c9<br>
 * d = CastleID<br>
 * d = Show Owner Controls (0x00 default || >=0x02(mask?) owner)<br>
 * d = Owner ClanID<br>
 * S = Owner ClanName<br>
 * S = Owner Clan LeaderName<br>
 * d = Owner AllyID<br>
 * S = Owner AllyName<br>
 * d = current time (seconds)<br>
 * d = Siege time (seconds) (0 for selectable)<br>
 * d = (UNKNOW) Siege Time Select Related?
 * @author KenM
 */
public class SiegeInfo extends ServerPacket
{
	private final Castle _castle;
	private final Player _player;
	
	public SiegeInfo(Castle castle, Player player)
	{
		_castle = castle;
		_player = player;
	}
	
	@Override
	public void write()
	{
		ServerPackets.CASTLE_SIEGE_INFO.writeId(this);
		if (_castle != null)
		{
			writeInt(_castle.getResidenceId());
			final int ownerId = _castle.getOwnerId();
			writeInt((ownerId == _player.getClanId()) && (_player.isClanLeader()));
			writeInt(ownerId);
			if (ownerId > 0)
			{
				final Clan owner = ClanTable.getInstance().getClan(ownerId);
				if (owner != null)
				{
					writeString(owner.getName()); // Clan Name
					writeString(owner.getLeaderName()); // Clan Leader Name
					writeInt(owner.getAllyId()); // Ally ID
					writeString(owner.getAllyName()); // Ally Name
				}
				else
				{
					PacketLogger.warning("Null owner for castle: " + _castle.getName());
				}
			}
			else
			{
				writeString(""); // Clan Name
				writeString(""); // Clan Leader Name
				writeInt(0); // Ally ID
				writeString(""); // Ally Name
			}
			writeInt((int) (System.currentTimeMillis() / 1000));
			if (!_castle.isTimeRegistrationOver() && _player.isClanLeader() && (_player.getClanId() == _castle.getOwnerId()))
			{
				final Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(_castle.getSiegeDate().getTimeInMillis());
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				writeInt(0);
				writeInt(Config.SIEGE_HOUR_LIST.size());
				for (int hour : Config.SIEGE_HOUR_LIST)
				{
					cal.set(Calendar.HOUR_OF_DAY, hour);
					writeInt((int) (cal.getTimeInMillis() / 1000));
				}
			}
			else
			{
				writeInt((int) (_castle.getSiegeDate().getTimeInMillis() / 1000));
				writeInt(0);
			}
		}
	}
}
