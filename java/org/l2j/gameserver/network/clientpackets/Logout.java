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
package org.l2j.gameserver.network.clientpackets;

import java.util.logging.Logger;

import org.l2j.Config;

import org.l2j.gameserver.enums.TeleportWhereType;
import org.l2j.gameserver.model.Location;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.instancezone.Instance;
import org.l2j.gameserver.model.olympiad.OlympiadManager;
import org.l2j.gameserver.network.Disconnection;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.serverpackets.LeaveWorld;
import org.l2j.gameserver.util.OfflineTradeUtil;

/**
 * @version $Revision: 1.9.4.3 $ $Date: 2005/03/27 15:29:30 $
 */
public class Logout implements ClientPacket
{
	protected static final Logger LOGGER_ACCOUNTING = Logger.getLogger("accounting");
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			client.disconnect();
			return;
		}
		
		// Protocol 338: You can exit game any time.
		// if (!player.canLogout())
		// {
		// player.sendPacket(ActionFailed.STATIC_PACKET);
		// return;
		// }
		
		// Unregister from olympiad.
		if (OlympiadManager.getInstance().isRegistered(player))
		{
			OlympiadManager.getInstance().unRegisterNoble(player);
		}
		
		final Instance world = player.getInstanceWorld();
		if (world != null)
		{
			if (Config.RESTORE_PLAYER_INSTANCE)
			{
				player.getVariables().set("INSTANCE_RESTORE", world.getId());
			}
			else
			{
				final Location location = world.getExitLocation(player);
				if (location != null)
				{
					player.teleToLocation(location);
				}
				else
				{
					player.teleToLocation(TeleportWhereType.TOWN);
				}
				player.getSummonedNpcs().forEach(npc -> npc.teleToLocation(player, true));
			}
			world.onInstanceChange(player, false);
		}
		
		LOGGER_ACCOUNTING.info("Logged out, " + client);
		
		if (!OfflineTradeUtil.enteredOfflineMode(player))
		{
			Disconnection.of(client, player).defaultSequence(LeaveWorld.STATIC_PACKET);
		}
	}
}
