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
package org.l2j.gameserver.network.serverpackets.enchant.multi;

import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.request.EnchantItemRequest;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Index
 */
public class ExResultSetMultiEnchantItemList extends ServerPacket
{
	private final Player _player;
	private final int _resultType;
	
	public ExResultSetMultiEnchantItemList(Player player, int resultType)
	{
		_player = player;
		_resultType = resultType;
	}
	
	@Override
	public void write()
	{
		if (_player.getRequest(EnchantItemRequest.class) == null)
		{
			return;
		}
		
		ServerPackets.EX_RES_SET_MULTI_ENCHANT_ITEM_LIST.writeId(this);
		writeInt(_resultType);
	}
}
