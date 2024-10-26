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
package org.l2j.gameserver.network.serverpackets.attendance;

import java.util.List;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.data.xml.AttendanceRewardData;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.AttendanceInfoHolder;
import org.l2j.gameserver.model.holders.ItemHolder;
import org.l2j.gameserver.network.GameClient;
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
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_VIP_ATTENDANCE_LIST.writeId(this, buffer);
		
		buffer.writeInt(_rewardItems.size());
		for (ItemHolder reward : _rewardItems)
		{
			buffer.writeInt(reward.getId());
			buffer.writeLong(reward.getCount());
			buffer.writeByte(0); // Enchant level?
		}
		
		buffer.writeInt(1); // MinimumLevel
		buffer.writeInt(_delayreward); // RemainCheckTime
		if (_available)
		{
			buffer.writeByte(_index + 1); // RollBookDay
			if ((_delayreward == 0) && (_available))
			{
				buffer.writeByte(_index + 1); // AttendanceDay
			}
			else
			{
				buffer.writeByte(_index); // AttendanceDay
			}
			buffer.writeByte(_index); // RewardDay
			buffer.writeByte(0); // FollowBaseDay
			// buffer.writeByte(_available);
			buffer.writeByte(0); // FollowBaseDay
		}
		else
		{
			buffer.writeByte(_index); // RollBookDay
			if ((_delayreward == 0) && (_available))
			{
				buffer.writeByte(_index + 1); // AttendanceDay
			}
			else
			{
				buffer.writeByte(_index); // AttendanceDay
			}
			buffer.writeByte(_index); // RewardDay
			buffer.writeByte(0); // FollowBaseDay
			// buffer.writeByte(_available);
			buffer.writeByte(1); // FollowBaseDay
		}
	}
}
