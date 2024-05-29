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
package org.l2j.gameserver.network.serverpackets.vip;

import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.itemcontainer.Inventory;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Gabriel Costa Souza
 */
public class ReceiveVipLuckyGameInfo extends ServerPacket
{
	private final Player _player;
	
	public ReceiveVipLuckyGameInfo(Player player)
	{
		_player = player;
	}
	
	@Override
	public void write()
	{
		ServerPackets.RECIVE_VIP_LUCKY_GAME_INFO.writeId(this);
		writeByte(1); // enabled
		writeShort((int) _player.getAdena());
		final Item item = _player.getInventory().getItemByItemId(Inventory.LCOIN_ID);
		writeShort(item == null ? 0 : (int) item.getCount()); // L Coin count
	}
}
