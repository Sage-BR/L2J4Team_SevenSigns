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
package org.l2j.gameserver.network.clientpackets.ranking;

import org.l2j.gameserver.instancemanager.GlobalVariablesManager;
import org.l2j.gameserver.instancemanager.RankingPowerManager;
import org.l2j.gameserver.model.World;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.zone.ZoneId;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.ranking.ExRankingBuffZoneNpcInfo;
import org.l2j.gameserver.network.serverpackets.ranking.ExRankingBuffZoneNpcPosition;

/**
 * @author Serenitty
 */
public class RequestExRankingCharSpawnBuffzoneNpc implements ClientPacket
{
	private static final int COST = 20000000;
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (GlobalVariablesManager.getInstance().getLong(GlobalVariablesManager.RANKING_POWER_COOLDOWN, 0) > System.currentTimeMillis())
		{
			player.sendPacket(SystemMessageId.LEADER_POWER_COOLDOWN);
			return;
		}
		
		if (!player.destroyItemByItemId("Adena", 57, COST, player, true))
		{
			player.sendPacket(SystemMessageId.NOT_ENOUGH_MONEY_TO_USE_THE_FUNCTION);
			return;
		}
		
		if (!player.isInsideZone(ZoneId.PEACE) || player.isInStoreMode() || !World.getInstance().getVisibleObjectsInRange(player, Creature.class, 50).isEmpty())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_USE_LEADER_POWER_HERE);
			return;
		}
		
		RankingPowerManager.getInstance().activatePower(player);
		player.sendPacket(new ExRankingBuffZoneNpcPosition());
		player.sendPacket(new ExRankingBuffZoneNpcInfo());
	}
}
