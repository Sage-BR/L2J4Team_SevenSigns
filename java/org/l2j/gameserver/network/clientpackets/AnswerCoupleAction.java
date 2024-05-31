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
package org.l2j.gameserver.network.clientpackets;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.model.World;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.serverpackets.ExRotation;
import org.l2j.gameserver.network.serverpackets.SocialAction;
import org.l2j.gameserver.network.serverpackets.SystemMessage;
import org.l2j.gameserver.util.Util;

/**
 * @author JIV
 */
public class AnswerCoupleAction implements ClientPacket
{
	private int _objectId;
	private int _actionId;
	private int _answer;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_actionId = packet.readInt();
		_answer = packet.readInt();
		_objectId = packet.readInt();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		final Player target = World.getInstance().getPlayer(_objectId);
		if ((player == null) || (target == null) || (target.getMultiSocialTarget() != player.getObjectId()) || (target.getMultiSociaAction() != _actionId))
		{
			return;
		}
		
		if (_answer == 0) // cancel
		{
			target.sendPacket(SystemMessageId.THE_COUPLE_ACTION_REQUEST_HAS_BEEN_DENIED);
		}
		else if (_answer == 1) // approve
		{
			final int distance = (int) player.calculateDistance2D(target);
			if ((distance > 125) || (distance < 15) || (player.getObjectId() == target.getObjectId()))
			{
				player.sendPacket(SystemMessageId.THE_REQUEST_CANNOT_BE_COMPLETED_BECAUSE_THE_TARGET_DOES_NOT_MEET_LOCATION_REQUIREMENTS);
				target.sendPacket(SystemMessageId.THE_REQUEST_CANNOT_BE_COMPLETED_BECAUSE_THE_TARGET_DOES_NOT_MEET_LOCATION_REQUIREMENTS);
				return;
			}
			int heading = Util.calculateHeadingFrom(player, target);
			player.broadcastPacket(new ExRotation(player.getObjectId(), heading));
			player.setHeading(heading);
			heading = Util.calculateHeadingFrom(target, player);
			target.setHeading(heading);
			target.broadcastPacket(new ExRotation(target.getObjectId(), heading));
			player.broadcastPacket(new SocialAction(player.getObjectId(), _actionId));
			target.broadcastPacket(new SocialAction(_objectId, _actionId));
		}
		else if (_answer == -1) // refused
		{
			final SystemMessage sm = new SystemMessage(SystemMessageId.C1_IS_SET_TO_REFUSE_COUPLE_ACTIONS_AND_CANNOT_BE_REQUESTED_FOR_A_COUPLE_ACTION);
			sm.addPcName(player);
			target.sendPacket(sm);
		}
		target.setMultiSocialAction(0, 0);
	}
}
