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
package org.l2j.gameserver.network.serverpackets.huntpass;

import org.l2j.gameserver.model.HuntPass;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Serenitty
 */
public class HuntPassSimpleInfo extends ServerPacket
{
	private final HuntPass _huntPassInfo;
	
	public HuntPassSimpleInfo(Player player)
	{
		_huntPassInfo = player.getHuntPass();
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_L2PASS_SIMPLE_INFO.writeId(this);
		
		writeInt(1); // passInfos
		writeByte(0);
		writeByte(1); // isOn
		
		writeByte(_huntPassInfo.rewardAlert());
		writeInt(0);
	}
}
