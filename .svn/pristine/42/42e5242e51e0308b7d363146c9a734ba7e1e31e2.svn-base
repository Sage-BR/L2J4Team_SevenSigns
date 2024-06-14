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
package org.l2jmobius.gameserver.network.clientpackets.stats;

import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.itemcontainer.Inventory;
import org.l2jmobius.gameserver.model.variables.PlayerVariables;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;

/**
 * @author Mobius
 */
public class ExResetStatusBonus extends ClientPacket
{
	@Override
	protected void readImpl()
	{
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		final PlayerVariables vars = player.getVariables();
		int points = vars.getInt(PlayerVariables.STAT_STR.toString(), 0) + vars.getInt(PlayerVariables.STAT_DEX.toString(), 0) + vars.getInt(PlayerVariables.STAT_CON.toString(), 0) + vars.getInt(PlayerVariables.STAT_INT.toString(), 0) + vars.getInt(PlayerVariables.STAT_WIT.toString(), 0) + vars.getInt(PlayerVariables.STAT_MEN.toString(), 0);
		int adenaCost;
		int lcoinCost;
		
		if (points < 6)
		{
			lcoinCost = 200;
			adenaCost = 200_000;
		}
		else if (points < 11)
		{
			lcoinCost = 300;
			adenaCost = 500_000;
		}
		else if (points < 16)
		{
			lcoinCost = 400;
			adenaCost = 1_000_000;
		}
		else if (points < 21)
		{
			lcoinCost = 500;
			adenaCost = 2_000_000;
		}
		else if (points < 26)
		{
			lcoinCost = 600;
			adenaCost = 5_000_000;
		}
		else
		{
			lcoinCost = 700;
			adenaCost = 10_000_000;
		}
		
		long adena = player.getAdena();
		long lcoin = player.getInventory().getItemByItemId(Inventory.LCOIN_ID) == null ? 0 : player.getInventory().getItemByItemId(Inventory.LCOIN_ID).getCount();
		
		if ((adena < adenaCost) || (lcoin < lcoinCost))
		{
			player.sendPacket(SystemMessageId.NOT_ENOUGH_MONEY_TO_USE_THE_FUNCTION);
			return;
		}
		
		if (player.reduceAdena("ExResetStatusBonus", adenaCost, player, true) && player.destroyItemByItemId("ExResetStatusBonus", Inventory.LCOIN_ID, lcoinCost, player, true))
		{
			player.getVariables().remove(PlayerVariables.STAT_POINTS);
			player.getVariables().remove(PlayerVariables.STAT_STR);
			player.getVariables().remove(PlayerVariables.STAT_DEX);
			player.getVariables().remove(PlayerVariables.STAT_CON);
			player.getVariables().remove(PlayerVariables.STAT_INT);
			player.getVariables().remove(PlayerVariables.STAT_WIT);
			player.getVariables().remove(PlayerVariables.STAT_MEN);
			player.getVariables().set(PlayerVariables.ELIXIRS_AVAILABLE, player.getVariables().getInt(PlayerVariables.ELIXIRS_AVAILABLE, 0));
			
			player.getStat().recalculateStats(true);
			
			// Calculate stat increase skills.
			player.calculateStatIncreaseSkills();
			player.updateUserInfo();
		}
	}
}
