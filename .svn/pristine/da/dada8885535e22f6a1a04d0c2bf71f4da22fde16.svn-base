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
package org.l2jmobius.gameserver.network.clientpackets;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.network.serverpackets.ExReplyPostItemList;

/**
 * @author Migi, DS
 */
public class RequestPostItemList extends ClientPacket
{
	@Override
	protected void readImpl()
	{
	}
	
	@Override
	protected void runImpl()
	{
		if (!Config.ALLOW_MAIL || !Config.ALLOW_ATTACHMENTS)
		{
			return;
		}
		
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		// if (!player.isInsideZone(ZoneId.PEACE))
		// {
		// player.sendPacket(SystemMessageId.THE_MAILBOX_FUNCTIONS_CAN_BE_USED_ONLY_IN_PEACE_ZONES_OUTSIDE_OF_THEM_YOU_CAN_ONLY_CHECK_ITS_CONTENTS);
		// return;
		// }
		
		player.sendPacket(new ExReplyPostItemList(1, player));
		player.sendPacket(new ExReplyPostItemList(2, player));
	}
}
