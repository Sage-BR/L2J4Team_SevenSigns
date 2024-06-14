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
package org.l2j.gameserver.network.clientpackets.mablegame;

import org.l2j.gameserver.data.xml.MableGameData;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.ItemHolder;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.mablegame.ExMableGameShowPlayerState;

public class ExRequestMableGameReset extends ClientPacket
{
	@Override
	public void readImpl()
	{
		readInt(); // ResetItemType (Always 1)
	}
	
	@Override
	public void runImpl()
	{
		final MableGameData data = MableGameData.getInstance();
		if (!data.isEnabled())
		{
			return;
		}
		
		final Player player = getClient().getPlayer();
		if (player == null)
		{
			return;
		}
		
		// Check if can reset.
		final MableGameData.MableGamePlayerState playerState = data.getPlayerState(player.getAccountName());
		if ((playerState.getCurrentCellId() < data.getHighestCellId()) || (playerState.getRound() == data.getDailyAvailableRounds()))
		{
			return;
		}
		
		// Check items.
		for (ItemHolder itemHolder : data.getResetItems())
		{
			if (player.getInventory().getInventoryItemCount(itemHolder.getId(), -1) < itemHolder.getCount())
			{
				player.sendPacket(SystemMessageId.INCORRECT_ITEM_COUNT);
				return;
			}
		}
		
		// Remove Items.
		for (ItemHolder itemHolder : data.getResetItems())
		{
			if (!player.destroyItemByItemId(getClass().getSimpleName(), itemHolder.getId(), itemHolder.getCount(), player, true))
			{
				player.sendPacket(SystemMessageId.INCORRECT_ITEM_COUNT);
				return;
			}
		}
		
		// Reset.
		playerState.setRound(playerState.getRound() + 1);
		playerState.setCurrentCellId(1);
		playerState.setRemainCommonDice(data.getCommonDiceLimit());
		player.sendPacket(new ExMableGameShowPlayerState(player));
	}
}
