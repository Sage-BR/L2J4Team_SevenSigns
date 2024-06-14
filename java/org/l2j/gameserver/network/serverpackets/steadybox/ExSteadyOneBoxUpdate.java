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
package org.l2j.gameserver.network.serverpackets.steadybox;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.model.AchievementBox;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.AchievementBoxHolder;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Serenitty
 */
public class ExSteadyOneBoxUpdate extends ServerPacket
{
	private final AchievementBox _achievementBox;
	private final int _slotId;
	
	public ExSteadyOneBoxUpdate(Player player, int slotId)
	{
		_achievementBox = player.getAchievementBox();
		_slotId = slotId;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_STEADY_ONE_BOX_UPDATE.writeId(this, buffer);
		buffer.writeInt(_achievementBox.getMonsterPoints());
		buffer.writeInt(_achievementBox.getPvpPoints());
		final AchievementBoxHolder boxholder = _achievementBox.getAchievementBox().get(_slotId - 1);
		buffer.writeInt(_slotId);
		buffer.writeInt(boxholder.getState().ordinal());
		buffer.writeInt(boxholder.getType().ordinal());
		final int rewardTimeStage = ((int) ((_achievementBox.getBoxOpenTime() - System.currentTimeMillis()) / 1000));
		buffer.writeInt(rewardTimeStage > 0 ? rewardTimeStage : 0);
	}
}