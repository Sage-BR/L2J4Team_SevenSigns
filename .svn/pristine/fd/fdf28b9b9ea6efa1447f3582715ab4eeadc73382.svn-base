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
package org.l2jmobius.gameserver.network.clientpackets.ranking;

import org.l2jmobius.gameserver.instancemanager.GlobalVariablesManager;
import org.l2jmobius.gameserver.instancemanager.RankingPowerManager;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.zone.ZoneId;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;
import org.l2jmobius.gameserver.network.serverpackets.ranking.ExRankingBuffZoneNpcInfo;
import org.l2jmobius.gameserver.network.serverpackets.ranking.ExRankingBuffZoneNpcPosition;

/**
 * @author Serenitty
 */
public class RequestExRankingCharSpawnBuffzoneNpc extends ClientPacket
{
	private static final int COST = 20000000;
	
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
		
		if (GlobalVariablesManager.getInstance().getLong(GlobalVariablesManager.RANKING_POWER_COOLDOWN, 0) > System.currentTimeMillis())
		{
			player.sendPacket(SystemMessageId.LEADER_POWER_COOLDOWN);
			return;
		}
		
		if (!player.isInsideZone(ZoneId.PEACE) || player.isInStoreMode())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_USE_LEADER_POWER_HERE);
			return;
		}
		
		if (player.getAdena() < COST)
		{
			player.sendPacket(SystemMessageId.NOT_ENOUGH_MONEY_TO_USE_THE_FUNCTION);
			return;
		}
		
		player.destroyItemByItemId("Adena", 57, COST, player, true);
		RankingPowerManager.getInstance().activatePower(player);
		player.sendPacket(new ExRankingBuffZoneNpcPosition());
		player.sendPacket(new ExRankingBuffZoneNpcInfo());
	}
}
