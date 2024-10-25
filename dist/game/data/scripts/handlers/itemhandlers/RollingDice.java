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
package handlers.itemhandlers;

import org.l2j.commons.util.Rnd;
import org.l2j.gameserver.geoengine.GeoEngine;
import org.l2j.gameserver.handler.IItemHandler;
import org.l2j.gameserver.model.Location;
import org.l2j.gameserver.model.actor.Playable;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.zone.ZoneId;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.serverpackets.Dice;
import org.l2j.gameserver.network.serverpackets.SystemMessage;
import org.l2j.gameserver.util.Broadcast;
import org.l2j.gameserver.util.Util;

public class RollingDice implements IItemHandler
{
	@Override
	public boolean useItem(Playable playable, Item item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.YOUR_PET_CANNOT_CARRY_THIS_ITEM);
			return false;
		}
		
		final Player player = playable.getActingPlayer();
		final int itemId = item.getId();
		if (player.isInOlympiadMode())
		{
			player.sendPacket(SystemMessageId.THE_ITEM_CANNOT_BE_USED_IN_THE_OLYMPIAD);
			return false;
		}
		
		final int number = rollDice(player);
		if (number == 0)
		{
			player.sendPacket(SystemMessageId.YOU_MAY_NOT_THROW_THE_DICE_AT_THIS_TIME_TRY_AGAIN_LATER);
			return false;
		}
		
		// Mobius: Retail dice position land calculation.
		final double angle = Util.convertHeadingToDegree(player.getHeading());
		final double radian = Math.toRadians(angle);
		final double course = Math.toRadians(180);
		final int x1 = (int) (Math.cos(Math.PI + radian + course) * 40);
		final int y1 = (int) (Math.sin(Math.PI + radian + course) * 40);
		final int x = player.getX() + x1;
		final int y = player.getY() + y1;
		final int z = player.getZ();
		final Location destination = GeoEngine.getInstance().getValidLocation(player.getX(), player.getY(), player.getZ(), x, y, z, player.getInstanceWorld());
		Broadcast.toSelfAndKnownPlayers(player, new Dice(player.getObjectId(), itemId, number, destination.getX(), destination.getY(), destination.getZ()));
		
		final SystemMessage sm = new SystemMessage(SystemMessageId.C1_HAS_ROLLED_A_S2);
		sm.addString(player.getName());
		sm.addInt(number);
		
		player.sendPacket(sm);
		if (player.isInsideZone(ZoneId.PEACE))
		{
			Broadcast.toKnownPlayers(player, sm);
		}
		else if (player.isInParty()) // TODO: Verify this!
		{
			player.getParty().broadcastToPartyMembers(player, sm);
		}
		return true;
	}
	
	private int rollDice(Player player)
	{
		// Check if the dice is ready
		if (!player.getClient().getFloodProtectors().canRollDice())
		{
			return 0;
		}
		return Rnd.get(1, 6);
	}
}
