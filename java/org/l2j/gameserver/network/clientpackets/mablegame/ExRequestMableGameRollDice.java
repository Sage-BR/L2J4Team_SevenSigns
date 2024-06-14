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

import org.l2j.commons.util.Rnd;
import org.l2j.gameserver.data.xml.MableGameData;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.mablegame.ExMableGameDiceResult;
import org.l2j.gameserver.network.serverpackets.mablegame.ExMableGamePrison;

public class ExRequestMableGameRollDice extends ClientPacket
{
	private byte _diceType;
	
	@Override
	public void readImpl()
	{
		_diceType = readByte();
	}
	
	@Override
	public void runImpl()
	{
		if (!MableGameData.getInstance().isEnabled())
		{
			return;
		}
		
		final Player player = getClient().getPlayer();
		if (player == null)
		{
			return;
		}
		
		final MableGameData data = MableGameData.getInstance();
		final MableGameData.MableGamePlayerState playerState = data.getPlayerState(player.getAccountName());
		playerState.setMoved(false);
		
		if (playerState.getCurrentCellId() >= data.getHighestCellId())
		{
			return;
		}
		
		if (_diceType == 0)
		{
			if (playerState.getRemainCommonDice() <= 0)
			{
				return;
			}
			
			playerState.setRemainCommonDice(playerState.getRemainCommonDice() - 1);
		}
		
		if (!player.destroyItemByItemId(getClass().getSimpleName(), _diceType == 1 ? MableGameData.ENHANCED_DICE_ITEM_ID : MableGameData.COMMON_DICE_ITEM_ID, 1, player, true))
		{
			return;
		}
		
		int dice = _diceType == 1 ? Rnd.get(5, 6) : Rnd.get(1, 6);
		boolean diceChanged = false;
		if (playerState.getRemainingPrisonRolls() > 0)
		{
			if ((dice >= MableGameData.MIN_PRISON_DICE) && (dice <= MableGameData.MAX_PRISON_DICE))
			{
				playerState.setRemainingPrisonRolls(0);
				player.sendPacket(new ExMableGameDiceResult(dice, playerState.getCurrentCellId() + 1, data.getCellById(playerState.getCurrentCellId() + 1).getColor().getClientId(), playerState.getRemainCommonDice()));
				dice = 1;
				diceChanged = true;
			}
			else
			{
				playerState.setRemainingPrisonRolls(playerState.getRemainingPrisonRolls() - 1);
				if (playerState.getRemainingPrisonRolls() <= 0)
				{
					player.sendPacket(new ExMableGameDiceResult(dice, playerState.getCurrentCellId() + 1, data.getCellById(playerState.getCurrentCellId() + 1).getColor().getClientId(), playerState.getRemainCommonDice()));
					dice = 1;
					diceChanged = true;
				}
				else
				{
					player.sendPacket(new ExMableGameDiceResult(dice, playerState.getCurrentCellId(), data.getCellById(playerState.getCurrentCellId()).getColor().getClientId(), playerState.getRemainCommonDice()));
					player.sendPacket(new ExMableGamePrison(MableGameData.MIN_PRISON_DICE, MableGameData.MAX_PRISON_DICE, playerState.getRemainingPrisonRolls()));
					return;
				}
			}
		}
		
		final int newCellId = Math.min(playerState.getCurrentCellId() + dice, data.getHighestCellId());
		playerState.setCurrentCellId(newCellId);
		
		final MableGameData.MableGameCell newCell = data.getCellById(newCellId);
		if (!diceChanged)
		{
			player.sendPacket(new ExMableGameDiceResult(dice, newCellId, newCell.getColor().getClientId(), playerState.getRemainCommonDice()));
		}
		playerState.handleCell(player, newCell);
	}
}
