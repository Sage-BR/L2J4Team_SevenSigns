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
package org.l2j.gameserver.network.serverpackets.attendance;

import java.util.List;

import org.l2j.gameserver.data.xml.AttendanceRewardData;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.AttendanceInfoHolder;
import org.l2j.gameserver.model.holders.ItemHolder;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Mobius, Serenitty
 */
public class ExVipAttendanceList extends ServerPacket
{
	private final int _index;
	private final int _delayreward;
	private final boolean _available;
	private final List<ItemHolder> _rewardItems;
	
	public ExVipAttendanceList(Player player)
	{
		final AttendanceInfoHolder attendanceInfo = player.getAttendanceInfo();
		_index = attendanceInfo.getRewardIndex();
		_delayreward = player.getAttendanceDelay();
		_available = attendanceInfo.isRewardAvailable();
		_rewardItems = AttendanceRewardData.getInstance().getRewards();
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_VIP_ATTENDANCE_LIST.writeId(this);
		
		writeInt(_rewardItems.size());
		for (ItemHolder reward : _rewardItems)
		{
			writeInt(reward.getId());
			writeLong(reward.getCount());
			writeByte(0); // Enchant level?
		}
		
		writeInt(1); // MinimumLevel
		writeInt(_delayreward); // RemainCheckTime
		if (_available)
		{
			writeByte(_index + 1); // RollBookDay
			if ((_delayreward == 0) && (_available))
			{
				writeByte(_index + 1); // AttendanceDay
			}
			else
			{
				writeByte(_index); // AttendanceDay
			}
			writeByte(_index); // RewardDay
			writeByte(0); // FollowBaseDay
			// writeByte(_available);
			writeByte(0); // FollowBaseDay
		}
		else
		{
			writeByte(_index); // RollBookDay
			if ((_delayreward == 0) && (_available))
			{
				writeByte(_index + 1); // AttendanceDay
			}
			else
			{
				writeByte(_index); // AttendanceDay
			}
			writeByte(_index); // RewardDay
			writeByte(0); // FollowBaseDay
			// writeByte(_available);
			writeByte(1); // FollowBaseDay
		}
	}
}
