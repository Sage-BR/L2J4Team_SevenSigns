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
package org.l2j.gameserver.network.clientpackets.balok;

import org.l2j.gameserver.model.Location;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.effects.EffectFlag;
import org.l2j.gameserver.model.skill.CommonSkill;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.clientpackets.ClientPacket;

/**
 * @author Serenitty
 */
public class ExBalrogWarTeleport extends ClientPacket
{
	private static final Location BALOK_LOCATION = new Location(-18414, 180442, -3862);
	private static final int TELEPORT_COST = 50000;
	
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
		
		// Dead characters cannot use teleports.
		if (player.isDead())
		{
			player.sendPacket(SystemMessageId.DEAD_CHARACTERS_CANNOT_USE_TELEPORTS);
			return;
		}
		
		// Players should not be able to teleport if in a special location.
		if ((player.getMovieHolder() != null) || player.isFishing() || player.isInInstance() || player.isOnEvent() || player.isInOlympiadMode() || player.inObserverMode() || player.isInTraingCamp() || player.isInTimedHuntingZone())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_TELEPORT_RIGHT_NOW);
			return;
		}
		
		// Cannot teleport in combat.
		if ((player.isInCombat() || player.isCastingNow()))
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_TELEPORT_WHILE_IN_COMBAT);
			return;
		}
		
		// Cannot escape effect.
		if (player.isAffected(EffectFlag.CANNOT_ESCAPE))
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_TELEPORT_RIGHT_NOW);
			return;
		}
		
		// Take teleport fee.
		if (!player.destroyItemByItemId("Battle with Balok Teleport", 57, TELEPORT_COST, player, true))
		{
			player.sendPacket(SystemMessageId.NOT_ENOUGH_MONEY_TO_USE_THE_FUNCTION);
			return;
		}
		
		// Stop moving.
		player.abortCast();
		player.stopMove(null);
		
		// Teleport to Balok location.
		player.setTeleportLocation(BALOK_LOCATION);
		player.doCast(CommonSkill.TELEPORT.getSkill());
	}
}
