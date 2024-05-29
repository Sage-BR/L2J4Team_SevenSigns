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

/**
 * @author Sdw
 */
public class ExResponseBeautyRegistReset extends ServerPacket
{
	public static final int FAILURE = 0;
	public static final int SUCCESS = 1;
	public static final int CHANGE = 0;
	public static final int RESTORE = 1;
	
	private final Player _player;
	private final int _type;
	private final int _result;
	
	public ExResponseBeautyRegistReset(Player player, int type, int result)
	{
		_player = player;
		_type = type;
		_result = result;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_RESPONSE_BEAUTY_REGIST_RESET.writeId(this);
		writeLong(_player.getAdena());
		writeLong(_player.getBeautyTickets());
		writeInt(_type);
		writeInt(_result);
		writeInt(_player.getVisualHair());
		writeInt(_player.getVisualFace());
		writeInt(_player.getVisualHairColor());
	}
}
