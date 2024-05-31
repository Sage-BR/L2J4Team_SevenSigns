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
package org.l2j.gameserver.network.serverpackets.steadybox;

import org.l2j.Config;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.itemcontainer.Inventory;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Serenitty
 */
public class ExSteadyBoxUiInit extends ServerPacket
{
	private static final int[] OPEN_PRICE =
	{
		500,
		1000,
		1500
	};
	private static final int[] WAIT_TIME =
	{
		0,
		60,
		180,
		360,
		540
	};
	private static final int[] TIME_PRICE =
	{
		100,
		500,
		1000,
		1500,
		2000
	};
	
	private final Player _player;
	
	public ExSteadyBoxUiInit(Player player)
	{
		_player = player;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_STEADY_BOX_UI_INIT.writeId(this);
		
		writeInt(Config.ACHIEVEMENT_BOX_POINTS_FOR_REWARD);
		writeInt(Config.ACHIEVEMENT_BOX_PVP_POINTS_FOR_REWARD);
		if (Config.ENABLE_ACHIEVEMENT_PVP)
		{
			writeInt(2); // EventID Normal Point + Pvp Point Bar
		}
		else
		{
			writeInt(0); // EventID Normal Point + Pvp Point Bar
		}
		writeInt(0); // nEventStartTime time for limitkill
		writeInt(_player.getAchievementBox().pvpEndDate());
		
		writeInt(OPEN_PRICE.length);
		for (int i = 0; i < OPEN_PRICE.length; i++)
		{
			writeInt(i + 1);
			writeInt(Inventory.LCOIN_ID);
			writeLong(OPEN_PRICE[i]);
		}
		
		writeInt(TIME_PRICE.length);
		for (int i = 0; i < TIME_PRICE.length; i++)
		{
			writeInt(WAIT_TIME[i]);
			writeInt(Inventory.LCOIN_ID);
			writeLong(TIME_PRICE[i]);
		}
		
		final int rewardTimeStage = (int) (_player.getAchievementBox().getBoxOpenTime() - System.currentTimeMillis()) / 1000;
		writeInt(rewardTimeStage > 0 ? rewardTimeStage : 0);
	}
}
