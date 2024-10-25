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
package org.l2j.gameserver.network.serverpackets.enchant.challengepoint;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.ChallengePointInfoHolder;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Serenitty
 */
public class ExEnchantChallengePointInfo extends ServerPacket
{
	private final ChallengePointInfoHolder[] _challengeinfo;
	
	public ExEnchantChallengePointInfo(Player player)
	{
		_challengeinfo = player.getChallengeInfo().initializeChallengePoints();
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_ENCHANT_CHALLENGE_POINT_INFO.writeId(this, buffer);
		buffer.writeInt(_challengeinfo.length); // vCurrentPointInfo
		for (ChallengePointInfoHolder info : _challengeinfo)
		{
			buffer.writeInt(info.getPointGroupId()); // nPointGroupId
			buffer.writeInt(info.getChallengePoint()); // nChallengePoint
			buffer.writeInt(info.getTicketPointOpt1()); // nTicketPointOpt1
			buffer.writeInt(info.getTicketPointOpt2()); // nTicketPointOpt2
			buffer.writeInt(info.getTicketPointOpt3()); // nTicketPointOpt3
			buffer.writeInt(info.getTicketPointOpt4()); // nTicketPointOpt4
			buffer.writeInt(info.getTicketPointOpt5()); // nTicketPointOpt5
			buffer.writeInt(info.getTicketPointOpt6()); // nTicketPointOpt6
		}
	}
}
