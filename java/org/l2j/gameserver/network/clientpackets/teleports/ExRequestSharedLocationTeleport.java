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
package org.l2j.gameserver.network.clientpackets.teleports;

import org.l2j.Config;
import org.l2j.gameserver.instancemanager.SharedTeleportManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.SharedTeleportHolder;
import org.l2j.gameserver.model.itemcontainer.Inventory;
import org.l2j.gameserver.model.zone.ZoneId;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.clientpackets.ClientPacket;

/**
 * @author NasSeKa
 */
public class ExRequestSharedLocationTeleport extends ClientPacket
{
	private int _id;
	
	@Override
	protected void readImpl()
	{
		_id = (readInt() - 1) / 256;
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		final SharedTeleportHolder teleport = SharedTeleportManager.getInstance().getTeleport(_id);
		if ((teleport == null) || (teleport.getCount() == 0))
		{
			player.sendPacket(SystemMessageId.TELEPORTATION_LIMIT_FOR_THE_COORDINATES_RECEIVED_IS_REACHED);
			return;
		}
		
		if (player.getName().equals(teleport.getName()))
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_TELEPORT_TO_YOURSELF);
			return;
		}
		
		if (player.getInventory().getInventoryItemCount(Inventory.LCOIN_ID, -1) < Config.TELEPORT_SHARE_LOCATION_COST)
		{
			player.sendPacket(SystemMessageId.THERE_ARE_NOT_ENOUGH_L_COINS);
			return;
		}
		
		if ((player.getMovieHolder() != null) || player.isFishing() || player.isInInstance() || player.isOnEvent() || player.isInOlympiadMode() || player.inObserverMode() || player.isInTraingCamp() || player.isInTimedHuntingZone() || player.isInsideZone(ZoneId.SIEGE))
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_TELEPORT_RIGHT_NOW);
			return;
		}
		
		if (player.destroyItemByItemId("Shared Location", Inventory.LCOIN_ID, Config.TELEPORT_SHARE_LOCATION_COST, player, true))
		{
			teleport.decrementCount();
			player.abortCast();
			player.stopMove(null);
			player.teleToLocation(teleport.getLocation());
		}
	}
}
