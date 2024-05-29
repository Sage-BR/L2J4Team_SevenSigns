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
package org.l2j.gameserver.network.clientpackets.attendance;

import java.util.List;

import org.l2j.Config;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.data.xml.AttendanceRewardData;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.AttendanceInfoHolder;
import org.l2j.gameserver.model.holders.ItemHolder;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.PacketLogger;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.SystemMessage;
import org.l2j.gameserver.network.serverpackets.attendance.ExVipAttendanceReward;

/**
 * @author Serenitty
 */
public class RequestVipAttendanceItemReward implements ClientPacket
{
	private int _day;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_day = packet.readByte();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (!Config.ENABLE_ATTENDANCE_REWARDS)
		{
			player.sendPacket(SystemMessageId.DUE_TO_A_SYSTEM_ERROR_THE_ATTENDANCE_REWARD_CANNOT_BE_RECEIVED_PLEASE_TRY_AGAIN_LATER_BY_GOING_TO_MENU_ATTENDANCE_CHECK);
			return;
		}
		
		if (Config.PREMIUM_ONLY_ATTENDANCE_REWARDS && !player.hasPremiumStatus())
		{
			player.sendPacket(SystemMessageId.YOUR_VIP_RANK_IS_TOO_LOW_TO_RECEIVE_THE_REWARD);
			return;
		}
		else if (Config.VIP_ONLY_ATTENDANCE_REWARDS && (player.getVipTier() <= 0))
		{
			player.sendPacket(SystemMessageId.YOUR_VIP_RANK_IS_TOO_LOW_TO_RECEIVE_THE_REWARD);
			return;
		}
		
		final AttendanceInfoHolder attendanceInfo = player.getAttendanceInfo();
		final int rewardIndex = attendanceInfo.getRewardIndex();
		final List<ItemHolder> rewards = AttendanceRewardData.getInstance().getRewards();
		
		if ((_day > 0) && (_day <= rewards.size()))
		{
			// Claim all unreclaimed rewards before the current day.
			for (int i = rewardIndex; i < (_day - 1); i++)
			{
				final ItemHolder unreclaimedReward = rewards.get(i);
				player.addItem("Attendance Reward", unreclaimedReward, player, true);
			}
			
			// Claim the current day's reward
			final ItemHolder reward = rewards.get(_day - 1); // Subtract 1 because the index is 0-based.
			player.addItem("Attendance Reward", reward, player, true);
			player.setAttendanceInfo(_day); // Update reward index.
			
			// Send message.
			final SystemMessage msg = new SystemMessage(SystemMessageId.YOU_VE_RECEIVED_YOUR_VIP_ATTENDANCE_REWARD_FOR_DAY_S1);
			msg.addInt(_day);
			player.sendPacket(msg);
			
			// Send confirm packet.
			player.sendPacket(new ExVipAttendanceReward());
		}
		else
		{
			PacketLogger.warning(getClass().getSimpleName() + player + ": Invalid attendance day: " + _day);
		}
	}
}
