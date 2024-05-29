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
package org.l2j.gameserver.network.clientpackets.balok;

import org.l2j.gameserver.instancemanager.BattleWithBalokManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.variables.PlayerVariables;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.balok.BalrogWarGetReward;

/**
 * @author Serenitty
 */
public class ExBalrogWarGetReward implements ClientPacket
{
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final int availableReward = player.getVariables().getInt(PlayerVariables.BALOK_AVAILABLE_REWARD, 0);
		if (availableReward != 1)
		{
			return;
		}
		
		int count = 1;
		final int globalStage = BattleWithBalokManager.getInstance().getGlobalStage();
		if (globalStage < 4)
		{
			count = 30; // sayha potion sealed
		}
		
		final int reward = BattleWithBalokManager.getInstance().getReward();
		player.addItem("Battle with Balok", reward, count, player, true);
		player.getVariables().set(PlayerVariables.BALOK_AVAILABLE_REWARD, -1);
		player.sendPacket(new BalrogWarGetReward(true));
	}
}
