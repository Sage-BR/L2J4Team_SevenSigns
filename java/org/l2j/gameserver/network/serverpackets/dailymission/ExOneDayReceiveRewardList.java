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
package org.l2j.gameserver.network.serverpackets.dailymission;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

import org.l2j.commons.time.SchedulingPattern;
import org.l2j.gameserver.data.xml.DailyMissionData;
import org.l2j.gameserver.model.DailyMissionDataHolder;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Sdw
 */
public class ExOneDayReceiveRewardList extends ServerPacket
{
	private static final SchedulingPattern DAILY_REUSE_PATTERN = new SchedulingPattern("30 6 * * *");
	private static final SchedulingPattern WEEKLY_REUSE_PATTERN = new SchedulingPattern("30 6 * * 1");
	private static final SchedulingPattern MONTHLY_REUSE_PATTERN = new SchedulingPattern("30 6 1 * *");
	
	private final Player _player;
	private final Collection<DailyMissionDataHolder> _rewards;
	private final int _dayRemainTime;
	private final int _weekRemainTime;
	private final int _monthRemainTime;
	
	public ExOneDayReceiveRewardList(Player player, boolean sendRewards)
	{
		_player = player;
		_rewards = sendRewards ? DailyMissionData.getInstance().getDailyMissionData(player) : Collections.emptyList();
		_dayRemainTime = (int) ((DAILY_REUSE_PATTERN.next(System.currentTimeMillis()) - System.currentTimeMillis()) / 1000);
		_weekRemainTime = (int) ((WEEKLY_REUSE_PATTERN.next(System.currentTimeMillis()) - System.currentTimeMillis()) / 1000);
		_monthRemainTime = (int) ((MONTHLY_REUSE_PATTERN.next(System.currentTimeMillis()) - System.currentTimeMillis()) / 1000);
	}
	
	@Override
	public void write()
	{
		if (!DailyMissionData.getInstance().isAvailable())
		{
			return;
		}
		
		ServerPackets.EX_ONE_DAY_RECEIVE_REWARD_LIST.writeId(this);
		writeInt(_dayRemainTime);
		writeInt(_weekRemainTime);
		writeInt(_monthRemainTime);
		writeByte(0x17);
		writeInt(_player.getClassId().getId());
		writeInt(LocalDate.now().getDayOfWeek().ordinal()); // Day of week
		writeInt(_rewards.size());
		for (DailyMissionDataHolder reward : _rewards)
		{
			writeShort(reward.getId());
			final int status = reward.getStatus(_player);
			writeByte(status);
			writeByte(reward.getRequiredCompletions() > 1);
			writeInt(reward.getParams().getInt("level", -1) == -1 ? (status == 1 ? 0 : reward.getProgress(_player)) : _player.getLevel());
			writeInt(reward.getRequiredCompletions());
		}
	}
}
