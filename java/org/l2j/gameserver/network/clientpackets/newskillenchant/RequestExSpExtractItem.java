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
package org.l2j.gameserver.network.clientpackets.newskillenchant;

import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.itemcontainer.Inventory;
import org.l2j.gameserver.model.variables.PlayerVariables;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.newskillenchant.ExSpExtractItem;

/**
 * @author Serenitty
 */
public class RequestExSpExtractItem implements ClientPacket
{
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if ((player.getSp() >= 5000000000L) && (player.getAdena() >= 3000000) && (player.getVariables().getInt(PlayerVariables.DAILY_EXTRACT_ITEM + Inventory.SP_POUCH, 5) > 0))
		{
			player.removeExpAndSp(0, 5000000000L);
			player.broadcastUserInfo();
			player.reduceAdena("SpExtract", 3000000, null, true);
			player.addItem("AddSpExtract", Inventory.SP_POUCH, 1, null, true);
			final int current = player.getVariables().getInt(PlayerVariables.DAILY_EXTRACT_ITEM + Inventory.SP_POUCH, 5);
			player.getVariables().set(PlayerVariables.DAILY_EXTRACT_ITEM + Inventory.SP_POUCH, current - 1);
			player.sendPacket(new ExSpExtractItem());
		}
	}
}
