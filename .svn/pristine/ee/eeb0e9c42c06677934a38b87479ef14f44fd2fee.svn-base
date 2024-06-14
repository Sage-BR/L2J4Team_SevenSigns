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
package org.l2jmobius.gameserver.network.clientpackets.huntingzones;

import org.l2jmobius.commons.threads.ThreadPool;
import org.l2jmobius.gameserver.enums.TeleportWhereType;
import org.l2jmobius.gameserver.model.Location;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.TimedHuntingZoneHolder;
import org.l2jmobius.gameserver.model.instancezone.Instance;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;
import org.l2jmobius.gameserver.network.serverpackets.SystemMessage;
import org.l2jmobius.gameserver.network.serverpackets.huntingzones.TimedHuntingZoneExit;

/**
 * @author Mobius
 */
public class ExTimedHuntingZoneLeave extends ClientPacket
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
		
		if (player.isInCombat())
		{
			player.sendPacket(new SystemMessage(SystemMessageId.YOU_CANNOT_TELEPORT_IN_BATTLE));
			return;
		}
		
		final TimedHuntingZoneHolder huntingZone = player.getTimedHuntingZone();
		if (huntingZone == null)
		{
			return;
		}
		
		final Location exitLocation = huntingZone.getExitLocation();
		if (exitLocation != null)
		{
			player.teleToLocation(exitLocation, null);
		}
		else
		{
			final Instance world = player.getInstanceWorld();
			if (world != null)
			{
				world.ejectPlayer(player);
			}
			else
			{
				player.teleToLocation(TeleportWhereType.TOWN);
			}
		}
		
		ThreadPool.schedule(() -> player.sendPacket(new TimedHuntingZoneExit(huntingZone.getZoneId())), 3000);
	}
}
