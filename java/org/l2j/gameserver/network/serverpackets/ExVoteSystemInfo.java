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
 * ExVoteSystemInfo packet implementation.
 * @author Gnacik
 */
public class ExVoteSystemInfo extends ServerPacket
{
	private final int _recomLeft;
	private final int _recomHave;
	private final int _bonusTime;
	private final int _bonusVal;
	private final int _bonusType;
	
	public ExVoteSystemInfo(Player player)
	{
		_recomLeft = player.getRecomLeft();
		_recomHave = player.getRecomHave();
		_bonusTime = 0;
		_bonusVal = 0;
		_bonusType = 0;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_VOTE_SYSTEM_INFO.writeId(this);
		writeInt(_recomLeft);
		writeInt(_recomHave);
		writeInt(_bonusTime);
		writeInt(_bonusVal);
		writeInt(_bonusType);
	}
}
