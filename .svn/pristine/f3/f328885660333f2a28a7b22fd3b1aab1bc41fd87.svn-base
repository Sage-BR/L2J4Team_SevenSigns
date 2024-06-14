/*
 * This file is part of the L2J Mobius project.
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
package org.l2jmobius.gameserver.network.serverpackets.balok;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.instancemanager.BattleWithBalokManager;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.variables.PlayerVariables;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Serenitty
 */
public class BalrogWarShowUI extends ServerPacket
{
	private final Player _player;
	
	public BalrogWarShowUI(Player player)
	{
		_player = player;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_BALROGWAR_SHOW_UI.writeId(this, buffer);
		final int personalPoints = BattleWithBalokManager.getInstance().getMonsterPoints(_player);
		buffer.writeInt(personalPoints < 1 ? 0 : BattleWithBalokManager.getInstance().getPlayerRank(_player)); // personal rank
		buffer.writeInt(personalPoints); // personal points
		buffer.writeLong(BattleWithBalokManager.getInstance().getGlobalPoints()); // total points of players
		buffer.writeInt(_player.getVariables().getInt(PlayerVariables.BALOK_AVAILABLE_REWARD, 0)); // reward activated or not
		buffer.writeInt(BattleWithBalokManager.getInstance().getReward()); // RewardItemID
		buffer.writeLong(1); // unknown
	}
}
