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

import org.l2j.Config;

import org.l2j.gameserver.data.xml.AdminData;
import org.l2j.gameserver.enums.ChatType;
import org.l2j.gameserver.instancemanager.PetitionManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.serverpackets.CreatureSay;
import org.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * <p>
 * Format: (c) d
 * <ul>
 * <li>d: Unknown</li>
 * </ul>
 * </p>
 * @author -Wooden-, TempyIncursion
 */
public class RequestPetitionCancel implements ClientPacket
{
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (PetitionManager.getInstance().isPlayerInConsultation(player))
		{
			if (player.isGM())
			{
				PetitionManager.getInstance().endActivePetition(player);
			}
			else
			{
				player.sendPacket(SystemMessageId.YOUR_GLOBAL_SUPPORT_REQUEST_IS_BEING_PROCESSED);
			}
		}
		else if (PetitionManager.getInstance().isPlayerPetitionPending(player))
		{
			if (PetitionManager.getInstance().cancelActivePetition(player))
			{
				final int numRemaining = Config.MAX_PETITIONS_PER_PLAYER - PetitionManager.getInstance().getPlayerTotalPetitionCount(player);
				final SystemMessage sm = new SystemMessage(SystemMessageId.YOUR_GLOBAL_SUPPORT_REQUEST_HAS_BEEN_REVOKED_NUMBER_OR_REQUESTS_YOU_CAN_SEND_S1);
				sm.addString(String.valueOf(numRemaining));
				player.sendPacket(sm);
				
				// Notify all GMs that the player's pending petition has been cancelled.
				final String msgContent = player.getName() + " has canceled a pending petition.";
				AdminData.getInstance().broadcastToGMs(new CreatureSay(player, ChatType.HERO_VOICE, "Petition System", msgContent));
			}
			else
			{
				player.sendPacket(SystemMessageId.FAILED_TO_CANCEL_YOUR_GLOBAL_SUPPORT_REQUEST_PLEASE_TRY_AGAIN_LATER);
			}
		}
		else
		{
			player.sendPacket(SystemMessageId.GLOBAL_SUPPORT_DOES_NOT_ACCEPT_REQUESTS_AT_THE_MOMENT);
		}
	}
}
