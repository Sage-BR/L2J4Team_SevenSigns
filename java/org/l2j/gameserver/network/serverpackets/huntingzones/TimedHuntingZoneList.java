/*
 * This file is part of the L2J 4Team Project.
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

import java.util.List;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.data.xml.TimedHuntingZoneData;
import org.l2j.gameserver.instancemanager.GlobalVariablesManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.TimedHuntingZoneHolder;
import org.l2j.gameserver.model.variables.PlayerVariables;
import org.l2j.gameserver.model.zone.ZoneId;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Mobius
 */
public class TimedHuntingZoneList extends ServerPacket
{
	private final Player _player;
	private final boolean _isInTimedHuntingZone;
	
	public TimedHuntingZoneList(Player player)
	{
		_player = player;
		_isInTimedHuntingZone = player.isInsideZone(ZoneId.TIMED_HUNTING);
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_TIME_RESTRICT_FIELD_LIST.writeId(this, buffer);
		final long currentTime = System.currentTimeMillis();
		final List<TimedHuntingZoneHolder> timedHuntingZoneList = TimedHuntingZoneData.getInstance().getAllHuntingZones().stream().parallel().filter(t -> (t.isEvenWeek() == GlobalVariablesManager.getInstance().getBoolean(GlobalVariablesManager.IS_EVEN_WEEK, true)) || !t.isSwapWeek()).toList();
		buffer.writeInt(timedHuntingZoneList.size()); // zone count
		for (TimedHuntingZoneHolder holder : timedHuntingZoneList)
		{
			buffer.writeInt(holder.getEntryFee() != 0); // required item count
			buffer.writeInt(holder.getEntryItemId());
			buffer.writeLong(holder.getEntryFee());
			buffer.writeInt(!holder.isWeekly()); // reset cycle
			buffer.writeInt(holder.getZoneId());
			buffer.writeInt(holder.getMinLevel());
			buffer.writeInt(holder.getMaxLevel());
			buffer.writeInt(holder.getInitialTime() / 1000); // remain time base
			int remainingTime = _player.getTimedHuntingZoneRemainingTime(holder.getZoneId());
			if ((remainingTime == 0) && ((_player.getTimedHuntingZoneInitialEntry(holder.getZoneId()) + holder.getResetDelay()) < currentTime))
			{
				remainingTime = holder.getInitialTime();
			}
			buffer.writeInt(remainingTime / 1000); // remain time
			buffer.writeInt(holder.getMaximumAddedTime() / 1000);
			buffer.writeInt(_player.getVariables().getInt(PlayerVariables.HUNTING_ZONE_REMAIN_REFILL + holder.getZoneId(), holder.getRemainRefillTime()));
			buffer.writeInt(holder.getRefillTimeMax());
			boolean isFieldActivated = !_isInTimedHuntingZone;
			if ((holder.getZoneId() == 18) && !GlobalVariablesManager.getInstance().getBoolean("AvailableFrostLord", false))
			{
				isFieldActivated = false;
			}
			buffer.writeByte(isFieldActivated);
			buffer.writeByte(0); // bUserBound
			buffer.writeByte(0); // bCanReEnter
			buffer.writeByte(holder.zonePremiumUserOnly()); // bIsInZonePCCafeUserOnly
			buffer.writeByte(_player.hasPremiumStatus()); // bIsPCCafeUser
			buffer.writeByte(holder.useWorldPrefix()); // bWorldInZone
			buffer.writeByte(0); // bCanUseEntranceTicket
			buffer.writeInt(0); // nEntranceCount
		}
	}
}
