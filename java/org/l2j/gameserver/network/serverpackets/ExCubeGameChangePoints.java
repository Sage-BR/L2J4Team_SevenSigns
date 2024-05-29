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

import org.l2j.gameserver.network.ServerPackets;

/**
 * @author mrTJO
 */
public class ExCubeGameChangePoints extends ServerPacket
{
	private final int _timeLeft;
	private final int _bluePoints;
	private final int _redPoints;
	
	/**
	 * Change Client Point Counter
	 * @param timeLeft Time Left before Minigame's End
	 * @param bluePoints Current Blue Team Points
	 * @param redPoints Current Red Team Points
	 */
	public ExCubeGameChangePoints(int timeLeft, int bluePoints, int redPoints)
	{
		_timeLeft = timeLeft;
		_bluePoints = bluePoints;
		_redPoints = redPoints;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_BLOCK_UP_SET_STATE.writeId(this);
		writeInt(2);
		writeInt(_timeLeft);
		writeInt(_bluePoints);
		writeInt(_redPoints);
	}
}
