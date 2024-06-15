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
package org.l2j.gameserver.network.clientpackets;

import org.l2j.Config;
import org.l2j.gameserver.data.xml.AdminData;
import org.l2j.gameserver.instancemanager.PetitionManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * <p>
 * Format: (c) Sd
 * <ul>
 * <li>S: content</li>
 * <li>d: type</li>
 * </ul>
 * </p>
 * @author -Wooden-, TempyIncursion
 */
public class RequestPetition extends ClientPacket
{
	private String _content;
	private int _type; // 1 = on : 0 = off;
	
	@Override
	protected void readImpl()
	{
		_content = readString();
		_type = readInt();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if ((player == null) || (_type <= 0) || (_type >= 10))
		{
			return;
		}
		
		if (!AdminData.getInstance().isGmOnline(false))
		{
			player.sendPacket(SystemMessageId.THERE_ARE_NO_GMS_CURRENTLY_VISIBLE_IN_THE_PUBLIC_LIST_AS_THEY_MAY_BE_PERFORMING_OTHER_FUNCTIONS_AT_THE_MOMENT);
			return;
		}
		
		if (!PetitionManager.getInstance().isPetitioningAllowed())
		{
			player.sendPacket(SystemMessageId.UNABLE_TO_CONNECT_TO_THE_GLOBAL_SUPPORT_SERVER);
			return;
		}
		
		if (PetitionManager.getInstance().isPlayerPetitionPending(player))
		{
			player.sendPacket(SystemMessageId.YOUR_GLOBAL_SUPPORT_REQUEST_WAS_RECEIVED);
			return;
		}
		
		if (PetitionManager.getInstance().getPendingPetitionCount() == Config.MAX_PETITIONS_PENDING)
		{
			player.sendPacket(SystemMessageId.UNABLE_TO_SEND_YOUR_REQUEST_TO_THE_GLOBAL_SUPPORT_PLEASE_TRY_AGAIN_LATER);
			return;
		}
		
		final int totalPetitions = PetitionManager.getInstance().getPlayerTotalPetitionCount(player) + 1;
		if (totalPetitions > Config.MAX_PETITIONS_PER_PLAYER)
		{
			final SystemMessage sm = new SystemMessage(SystemMessageId.YOU_HAVE_SUBMITTED_MAXIMUM_NUMBER_OF_S1_GLOBAL_SUPPORT_REQUESTS_TODAY_YOU_CANNOT_SUBMIT_MORE_REQUESTS);
			sm.addInt(totalPetitions);
			player.sendPacket(sm);
			return;
		}
		
		if (_content.length() > 255)
		{
			player.sendPacket(SystemMessageId.YOUR_GLOBAL_SUPPORT_REQUEST_CAN_CONTAIN_UP_TO_800_CHARACTERS);
			return;
		}
		
		final int petitionId = PetitionManager.getInstance().submitPetition(player, _content, _type);
		SystemMessage sm = new SystemMessage(SystemMessageId.YOUR_GLOBAL_SUPPORT_REQUEST_WAS_RECEIVED_REQUEST_NO_S1);
		sm.addInt(petitionId);
		player.sendPacket(sm);
		
		sm = new SystemMessage(SystemMessageId.SUPPORT_RECEIVED_S1_TIME_S_GLOBAL_SUPPORT_REQUESTS_LEFT_FOR_TODAY_S2);
		sm.addInt(totalPetitions);
		sm.addInt(Config.MAX_PETITIONS_PER_PLAYER - totalPetitions);
		player.sendPacket(sm);
		
		sm = new SystemMessage(SystemMessageId.S1_USERS_ARE_IN_LINE_TO_GET_THE_GLOBAL_SUPPORT);
		sm.addInt(PetitionManager.getInstance().getPendingPetitionCount());
		player.sendPacket(sm);
	}
}
