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
import org.l2j.commons.threads.ThreadPool;
import org.l2j.gameserver.data.xml.FakePlayerData;
import org.l2j.gameserver.model.World;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.serverpackets.AskJoinPledge;
import org.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * @version $Revision: 1.3.4.4 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestJoinPledge implements ClientPacket
{
	private int _target;
	private int _pledgeType;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_target = packet.readInt();
		_pledgeType = packet.readInt();
	}
	
	private void scheduleDeny(Player player, String name)
	{
		if (player != null)
		{
			final SystemMessage sm = new SystemMessage(SystemMessageId.S1_DID_NOT_RESPOND_INVITATION_TO_THE_CLAN_HAS_BEEN_CANCELLED);
			sm.addString(name);
			player.sendPacket(sm);
			player.onTransactionResponse();
		}
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final Clan clan = player.getClan();
		if (clan == null)
		{
			return;
		}
		
		if ((player.getTarget() != null) && (FakePlayerData.getInstance().isTalkable(player.getTarget().getName())))
		{
			if (FakePlayerData.getInstance().getInfo(player.getTarget().getId()).getClanId() > 0)
			{
				player.sendPacket(SystemMessageId.THAT_PLAYER_ALREADY_BELONGS_TO_ANOTHER_CLAN);
			}
			else
			{
				if (!player.isProcessingRequest())
				{
					ThreadPool.schedule(() -> scheduleDeny(player, player.getTarget().getName()), 10000);
					player.blockRequest();
				}
				else
				{
					final SystemMessage msg = new SystemMessage(SystemMessageId.C1_IS_ON_ANOTHER_TASK_PLEASE_TRY_AGAIN_LATER);
					msg.addString(player.getTarget().getName());
					player.sendPacket(msg);
				}
			}
			return;
		}
		
		final Player target = World.getInstance().getPlayer(_target);
		if (target == null)
		{
			player.sendPacket(SystemMessageId.THE_TARGET_CANNOT_BE_INVITED);
			return;
		}
		
		if (!clan.checkClanJoinCondition(player, target, _pledgeType) || !player.getRequest().setRequest(target, this))
		{
			return;
		}
		
		final String pledgeName = player.getClan().getName();
		target.sendPacket(new AskJoinPledge(player, pledgeName));
	}
	
	public int getPledgeType()
	{
		return _pledgeType;
	}
}
