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
package org.l2j.gameserver.network.clientpackets.castlewar;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.instancemanager.CastleManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.siege.Castle;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.clientpackets.ClientPacket;

/**
 * @author Mobius
 */
public class ExCastleWarObserverStart implements ClientPacket
{
	private int _castleId;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_castleId = packet.readInt();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (player.hasSummon())
		{
			player.sendPacket(SystemMessageId.YOU_MAY_NOT_OBSERVE_A_SIEGE_WITH_A_SERVITOR_SUMMONED);
			return;
		}
		
		if (player.isOnEvent())
		{
			player.sendMessage("Cannot use while on an event.");
			return;
		}
		
		final Castle castle = CastleManager.getInstance().getCastleById(_castleId);
		if ((castle == null) || !castle.getSiege().isInProgress())
		{
			return;
		}
		
		final Player random = castle.getSiege().getPlayersInZone().stream().findAny().orElse(null);
		if (random == null)
		{
			return;
		}
		
		player.enterObserverMode(random.getLocation());
	}
}
