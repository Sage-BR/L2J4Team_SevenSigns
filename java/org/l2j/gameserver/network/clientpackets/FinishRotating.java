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
package org.l2j.gameserver.network.clientpackets;

import org.l2j.Config;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.serverpackets.StopRotation;

public class FinishRotating extends ClientPacket
{
	private int _degree;
	
	@Override
	protected void readImpl()
	{
		_degree = readInt();
		readInt(); // Unknown.
	}
	
	@Override
	protected void runImpl()
	{
		if (!Config.ENABLE_KEYBOARD_MOVEMENT)
		{
			return;
		}
		
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (player.isInAirShip() && player.getAirShip().isCaptain(player))
		{
			player.getAirShip().setHeading(_degree);
			player.getAirShip().broadcastPacket(new StopRotation(player.getAirShip().getObjectId(), _degree, 0));
		}
		else
		{
			player.setHeading(_degree);
			player.broadcastPacket(new StopRotation(player.getObjectId(), _degree, 0));
		}
	}
}
