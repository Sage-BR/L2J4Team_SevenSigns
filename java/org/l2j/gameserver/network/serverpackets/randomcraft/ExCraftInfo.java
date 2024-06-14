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
package org.l2j.gameserver.network.serverpackets.randomcraft;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.itemcontainer.PlayerRandomCraft;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Mode
 */
public class ExCraftInfo extends ServerPacket
{
	private final Player _player;
	
	public ExCraftInfo(Player player)
	{
		_player = player;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_CRAFT_INFO.writeId(this, buffer);
		final PlayerRandomCraft rc = _player.getRandomCraft();
		buffer.writeInt(rc.getFullCraftPoints()); // Full points owned
		buffer.writeInt(rc.getCraftPoints()); // Craft Points (10k = 1%)
		buffer.writeByte(rc.isSayhaRoll()); // Will get sayha?
	}
}
