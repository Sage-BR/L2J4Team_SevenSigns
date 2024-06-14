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
import org.l2j.gameserver.network.PacketLogger;
import org.l2j.gameserver.network.clientpackets.ClientPacket;

public class ExRequestMableGamePopupOk extends ClientPacket
{
	@Override
	public void readImpl()
	{
		readByte(); // cellType
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
		
		final MableGameData.MableGamePlayerState playerState = data.getPlayerState(player.getAccountName());
		final int pendingCellId = playerState.getPendingCellIdPopup();
		if (pendingCellId < 1)
		{
			return;
		}
		
		final MableGameData.MableGameCell cell = data.getCellById(pendingCellId);
		if (cell != null)
		{
			// if ((cell.getId() != data.getHighestCellId()) && (cell.getColor().getClientId() != _cellType) && (MableGameCellColor.getByClientId(_cellType) != MableGameCellColor.LIGHT_BLUE))
			// if (MableGameCellColor.getByClientId(_cellType) == MableGameCellColor.LIGHT_BLUE)
			// {
			// return;
			// }
			
			playerState.setCurrentCellId(pendingCellId);
			playerState.setPendingCellIdPopup(-1);
			switch (cell.getColor())
			{
				case LIGHT_BLUE:
				case GREEN:
				case RED:
				case BURNING_RED:
				{
					if (playerState.getPendingReward() != null)
					{
						player.addItem(getClass().getSimpleName(), playerState.getPendingReward(), player, true);
						playerState.setPendingReward(null);
					}
					else if (playerState.isMoved())
					{
						playerState.handleCell(player, cell);
					}
					break;
				}
				case YELLOW:
				{
					// Popup is handling other type.
					break;
				}
				case PURPLE:
				{
					if (playerState.isMoved())
					{
						playerState.handleCell(player, cell);
					}
					break;
				}
				default:
				{
					PacketLogger.warning(getClass().getSimpleName() + ": Unhandled Cell Id:" + cell.getId() + " Color:" + cell.getColor());
					break;
				}
			}
		}
	}
}
