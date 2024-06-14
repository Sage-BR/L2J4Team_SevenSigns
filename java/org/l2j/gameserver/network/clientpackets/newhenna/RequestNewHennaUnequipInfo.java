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
package org.l2j.gameserver.network.clientpackets.newhenna;

import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.item.henna.Henna;
import org.l2j.gameserver.network.PacketLogger;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.ActionFailed;
import org.l2j.gameserver.network.serverpackets.HennaItemRemoveInfo;

/**
 * @author Index
 */
public class RequestNewHennaUnequipInfo extends ClientPacket
{
	private int _hennaId;
	
	@Override
	protected void readImpl()
	{
		_hennaId = readInt();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if ((player == null) || (_hennaId == 0))
		{
			return;
		}
		
		Henna henna = null;
		for (int slot = 1; slot <= 4; slot++)
		{
			final Henna equipedHenna = player.getHenna(slot);
			if ((equipedHenna != null) && (equipedHenna.getDyeId() == _hennaId))
			{
				henna = equipedHenna;
				break;
			}
		}
		if (henna == null)
		{
			PacketLogger.warning("Invalid Henna Id: " + _hennaId + " from " + player);
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		player.sendPacket(new HennaItemRemoveInfo(henna, player));
	}
}
