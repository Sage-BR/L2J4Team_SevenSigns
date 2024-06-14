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
package org.l2jmobius.gameserver.network.clientpackets.olympiad;

import org.l2jmobius.gameserver.enums.CategoryType;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.olympiad.CompetitionType;
import org.l2jmobius.gameserver.model.olympiad.OlympiadManager;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;
import org.l2jmobius.gameserver.network.serverpackets.olympiad.ExOlympiadMatchMakingResult;

public class OlympiadMatchMaking extends ClientPacket
{
	private byte _gameRuleType;
	
	@Override
	protected void readImpl()
	{
		_gameRuleType = 1;
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (_gameRuleType == 1)
		{
			if (!player.isInCategory(CategoryType.FOURTH_CLASS_GROUP) || (player.getLevel() < 75))
			{
				player.sendPacket(SystemMessageId.CHARACTER_S_LEVEL_IS_TOO_LOW);
			}
			else if (!player.isInventoryUnder80(false))
			{
				player.sendPacket(SystemMessageId.UNABLE_TO_PROCESS_THIS_REQUEST_UNTIL_YOUR_INVENTORY_S_WEIGHT_AND_SLOT_COUNT_ARE_LESS_THAN_80_PERCENT_OF_CAPACITY);
			}
			else
			{
				OlympiadManager.getInstance().registerNoble(player, CompetitionType.NON_CLASSED);
			}
		}
		
		player.sendPacket(new ExOlympiadMatchMakingResult(_gameRuleType, 1));
	}
}
