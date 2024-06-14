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
package org.l2j.gameserver.network.clientpackets.gacha;

import java.util.List;

import org.l2j.gameserver.instancemanager.events.UniqueGachaManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.GachaItemTimeStampHolder;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.gacha.UniqueGachaHistory;

public class ExUniqueGachaHistory extends ClientPacket
{
	@Override
	protected void readImpl()
	{
		readByte(); // _inventoryType
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		final List<GachaItemTimeStampHolder> itemHistory = UniqueGachaManager.getInstance().getGachaCharacterHistory(player);
		player.sendPacket(new UniqueGachaHistory(itemHistory));
	}
}
