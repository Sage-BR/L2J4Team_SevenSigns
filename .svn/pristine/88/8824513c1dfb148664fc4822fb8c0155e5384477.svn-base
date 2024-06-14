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
package org.l2jmobius.gameserver.network.clientpackets.autopeel;

import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.actor.request.AutoPeelRequest;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;
import org.l2jmobius.gameserver.network.serverpackets.autopeel.ExReadyItemAutoPeel;
import org.l2jmobius.gameserver.network.serverpackets.autopeel.ExStopItemAutoPeel;

/**
 * @author Mobius
 */
public class ExRequestStopItemAutoPeel extends ClientPacket
{
	@Override
	protected void readImpl()
	{
		readByte();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		player.removeRequest(AutoPeelRequest.class);
		player.sendPacket(new ExStopItemAutoPeel(true));
		player.sendPacket(new ExReadyItemAutoPeel(false, 0));
	}
}