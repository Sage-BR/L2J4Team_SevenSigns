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

import org.l2j.gameserver.model.AchievementBox;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.AchievementBoxHolder;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Serenitty
 */
public class ExSteadyAllBoxUpdate extends ServerPacket
{
	private final AchievementBox _achievementBox;
	
	public ExSteadyAllBoxUpdate(Player player)
	{
		_achievementBox = player.getAchievementBox();
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_STEADY_ALL_BOX_UPDATE.writeId(this);
		
		writeInt(_achievementBox.getMonsterPoints());
		writeInt(_achievementBox.getPvpPoints());
		writeInt(_achievementBox.getBoxOwned());
		
		for (int i = 1; i <= _achievementBox.getBoxOwned(); i++)
		{
			final AchievementBoxHolder boxholder = _achievementBox.getAchievementBox().get(i - 1);
			writeInt(i); //
			writeInt(boxholder.getState().ordinal());
			writeInt(boxholder.getType().ordinal());
		}
		
		final int rewardTimeStage = (int) ((_achievementBox.getBoxOpenTime() - System.currentTimeMillis()) / 1000);
		writeInt(rewardTimeStage > 0 ? rewardTimeStage : 0);
	}
}
