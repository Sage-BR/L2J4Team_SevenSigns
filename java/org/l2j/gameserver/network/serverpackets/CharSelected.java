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
package org.l2j.gameserver.network.serverpackets;

import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.taskmanager.GameTimeTaskManager;

public class CharSelected extends ServerPacket
{
	private final Player _player;
	private final int _sessionId;
	
	/**
	 * @param player
	 * @param sessionId
	 */
	public CharSelected(Player player, int sessionId)
	{
		_player = player;
		_sessionId = sessionId;
	}
	
	@Override
	public void write()
	{
		ServerPackets.CHARACTER_SELECTED.writeId(this);
		writeString(_player.getName());
		writeInt(_player.getObjectId());
		writeString(_player.getTitle());
		writeInt(_sessionId);
		writeInt(_player.getClanId());
		writeInt(0); // ??
		writeInt(_player.getAppearance().isFemale());
		writeInt(_player.getRace().ordinal());
		writeInt(_player.getClassId().getId());
		writeInt(1); // active ??
		writeInt(_player.getX());
		writeInt(_player.getY());
		writeInt(_player.getZ());
		writeDouble(_player.getCurrentHp());
		writeDouble(_player.getCurrentMp());
		writeLong(_player.getSp());
		writeLong(_player.getExp());
		writeInt(_player.getLevel());
		writeInt(_player.getReputation());
		writeInt(_player.getPkKills());
		writeInt(GameTimeTaskManager.getInstance().getGameTime() % (24 * 60)); // "reset" on 24th hour
		writeInt(0);
		writeInt(_player.getClassId().getId());
		writeBytes(new byte[16]);
		writeInt(0);
		writeInt(0);
		writeInt(0);
		writeInt(0);
		writeInt(0);
		writeInt(0);
		writeInt(0);
		writeInt(0);
		writeInt(0);
		writeBytes(new byte[28]);
		writeInt(0);
	}
}
