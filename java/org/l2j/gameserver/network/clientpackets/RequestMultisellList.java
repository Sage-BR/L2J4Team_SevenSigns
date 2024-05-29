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

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.data.xml.MultisellData;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.MultisellListHolder;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.PacketLogger;

/**
 * @author Serenitty
 */
public class RequestMultisellList implements ClientPacket
{
	private int _multiSellId;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_multiSellId = packet.readInt();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final MultisellListHolder multisell = MultisellData.getInstance().getMultisell(_multiSellId);
		if (multisell == null)
		{
			PacketLogger.warning("RequestMultisellList: " + player + " requested non-existent list " + _multiSellId + ".");
			return;
		}
		
		MultisellData.getInstance().separateAndSend(_multiSellId, player, null, false, Double.NaN, Double.NaN, 4);
	}
}
