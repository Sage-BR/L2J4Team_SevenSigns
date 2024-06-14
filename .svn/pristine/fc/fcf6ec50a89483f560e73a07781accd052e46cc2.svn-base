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
package org.l2jmobius.gameserver.network.serverpackets;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.Config;
import org.l2jmobius.gameserver.instancemanager.events.LetterCollectorManager;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;

/**
 * @author Index
 */
public class ExLetterCollectorUI extends ServerPacket
{
	private final int _minimumLevel;
	
	public ExLetterCollectorUI(Player player)
	{
		_minimumLevel = player.getLevel() <= LetterCollectorManager.getInstance().getMaxLevel() ? LetterCollectorManager.getInstance().getMinLevel() : Config.PLAYER_MAXIMUM_LEVEL;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_LETTER_COLLECTOR_UI_LAUNCHER.writeId(this, buffer);
		buffer.writeByte(1); // enabled (0x00 - no, 0x01 -yes)
		buffer.writeInt(_minimumLevel); // Minimum Level
	}
}