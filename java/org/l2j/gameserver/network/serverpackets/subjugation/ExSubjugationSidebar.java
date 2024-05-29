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
package org.l2j.gameserver.network.serverpackets.subjugation;

import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.PurgePlayerHolder;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Berezkin Nikolay, Serenitty
 */
public class ExSubjugationSidebar extends ServerPacket
{
	private final Player _player;
	private final PurgePlayerHolder _purgeData;
	
	public ExSubjugationSidebar(Player player, PurgePlayerHolder purgeData)
	{
		_player = player;
		_purgeData = purgeData;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_SUBJUGATION_SIDEBAR.writeId(this);
		writeInt(_player == null ? 0 : _player.getPurgeLastCategory());
		writeInt(_purgeData == null ? 0 : _purgeData.getPoints()); // 1000000 = 100 percent
		writeInt(_purgeData == null ? 0 : _purgeData.getKeys());
		writeInt(0);
	}
}
