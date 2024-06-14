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
package org.l2j.gameserver.network.serverpackets;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author Sdw
 */
public class ExShowUsm extends ServerPacket
{
	public static final ExShowUsm GOD_INTRO = new ExShowUsm(2);
	public static final ExShowUsm SECOND_TRANSFER_QUEST = new ExShowUsm(4);
	public static final ExShowUsm OCTAVIS_INSTANCE_END = new ExShowUsm(6);
	public static final ExShowUsm AWAKENING_END = new ExShowUsm(10);
	public static final ExShowUsm ERTHEIA_FIRST_QUEST = new ExShowUsm(14);
	public static final ExShowUsm USM_Q015_E = new ExShowUsm(15); // Chamber of Prophecies instance
	public static final ExShowUsm ERTHEIA_INTRO_FOR_ERTHEIA = new ExShowUsm(147);
	public static final ExShowUsm ERTHEIA_INTRO_FOR_OTHERS = new ExShowUsm(148);
	public static final ExShowUsm ANTHARAS_INTRO = new ExShowUsm(149);
	public static final ExShowUsm DEATH_KNIGHT_INTRO = new ExShowUsm(150);
	public static final ExShowUsm CONQUEST_INTRO = new ExShowUsm(151);
	public static final ExShowUsm SHINE_MAKER_INTRO = new ExShowUsm(152);
	
	private final int _videoId;
	
	private ExShowUsm(int videoId)
	{
		_videoId = videoId;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_SHOW_USM.writeId(this, buffer);
		buffer.writeInt(_videoId);
	}
}
