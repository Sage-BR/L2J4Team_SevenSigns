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
package org.l2j.gameserver.network.serverpackets.enchant.challengepoint;

import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.ChallengePointInfoHolder;
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
	public void write()
	{
		ServerPackets.EX_ENCHANT_CHALLENGE_POINT_INFO.writeId(this);
		writeInt(_challengeinfo.length); // vCurrentPointInfo
		for (ChallengePointInfoHolder info : _challengeinfo)
		{
			writeInt(info.getPointGroupId()); // nPointGroupId
			writeInt(info.getChallengePoint()); // nChallengePoint
			writeInt(info.getTicketPointOpt1()); // nTicketPointOpt1
			writeInt(info.getTicketPointOpt2()); // nTicketPointOpt2
			writeInt(info.getTicketPointOpt3()); // nTicketPointOpt3
			writeInt(info.getTicketPointOpt4()); // nTicketPointOpt4
			writeInt(info.getTicketPointOpt5()); // nTicketPointOpt5
			writeInt(info.getTicketPointOpt6()); // nTicketPointOpt6
		}
	}
}
