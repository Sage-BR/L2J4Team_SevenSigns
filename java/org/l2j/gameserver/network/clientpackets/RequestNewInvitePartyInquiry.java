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
import org.l2j.gameserver.enums.ChatType;
import org.l2j.gameserver.enums.PlayerCondOverride;
import org.l2j.gameserver.model.Party;
import org.l2j.gameserver.model.World;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.serverpackets.ExRequestNewInvitePartyInquiry;
import org.l2j.gameserver.util.Broadcast;

/**
 * @author Serenitty
 */
public class RequestNewInvitePartyInquiry extends ClientPacket
{
	private int _reqType;
	private ChatType _sayType;
	
	@Override
	protected void readImpl()
	{
		_reqType = readByte();
		
		final int chatTypeValue = readByte();
		
		switch (chatTypeValue)
		{
			case 0:
			{
				_sayType = ChatType.GENERAL;
				break;
			}
			case 1:
			{
				_sayType = ChatType.SHOUT;
				break;
			}
			case 3:
			{
				_sayType = ChatType.PARTY;
				break;
			}
			case 4:
			{
				_sayType = ChatType.CLAN;
				break;
			}
			case 8:
			{
				_sayType = ChatType.TRADE;
				break;
			}
		}
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (player.isChatBanned())
		{
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_ALLOWED_TO_CHAT_WITH_A_CONTACT_WHILE_A_CHATTING_BLOCK_IS_IMPOSED);
			return;
		}
		
		// Ten second delay.
		// TODO: Create another flood protection for this?
		if (!getClient().getFloodProtectors().canSendMail())
		{
			return;
		}
		
		if (Config.JAIL_DISABLE_CHAT && player.isJailed() && !player.canOverrideCond(PlayerCondOverride.CHAT_CONDITIONS))
		{
			player.sendPacket(SystemMessageId.CHATTING_IS_CURRENTLY_PROHIBITED);
			return;
		}
		
		if (player.isInOlympiadMode())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_CHAT_WHILE_PARTICIPATING_IN_THE_OLYMPIAD);
			return;
		}
		
		if ((_sayType != ChatType.GENERAL) && (_sayType != ChatType.TRADE) && (_sayType != ChatType.SHOUT) && (_sayType != ChatType.CLAN) && (_sayType != ChatType.ALLIANCE))
		{
			return;
		}
		
		switch (_reqType)
		{
			case 0: // Party
			{
				if (player.isInParty())
				{
					return;
				}
				break;
			}
			case 1: // Command Channel
			{
				final Party party = player.getParty();
				if ((party == null) || !party.isLeader(player) || (party.getCommandChannel() != null))
				{
					return;
				}
				break;
			}
		}
		
		switch (_sayType)
		{
			case SHOUT:
			{
				if (player.inObserverMode())
				{
					player.sendPacket(SystemMessageId.YOU_CANNOT_CHAT_WHILE_IN_THE_SPECTATOR_MODE);
					return;
				}
				
				Broadcast.toAllOnlinePlayers(new ExRequestNewInvitePartyInquiry(player, _reqType, _sayType));
				break;
			}
			case TRADE:
			{
				if (player.inObserverMode())
				{
					player.sendPacket(SystemMessageId.YOU_CANNOT_CHAT_WHILE_IN_THE_SPECTATOR_MODE);
					return;
				}
				
				Broadcast.toAllOnlinePlayers(new ExRequestNewInvitePartyInquiry(player, _reqType, _sayType));
				break;
			}
			case GENERAL:
			{
				if (player.inObserverMode())
				{
					player.sendPacket(SystemMessageId.YOU_CANNOT_CHAT_WHILE_IN_THE_SPECTATOR_MODE);
					return;
				}
				
				final ExRequestNewInvitePartyInquiry msg = new ExRequestNewInvitePartyInquiry(player, _reqType, _sayType);
				player.sendPacket(msg);
				World.getInstance().forEachVisibleObjectInRange(player, Player.class, Config.ALT_PARTY_RANGE, nearby -> nearby.sendPacket(msg));
				break;
			}
			case CLAN:
			{
				final Clan clan = player.getClan();
				if (clan == null)
				{
					player.sendPacket(SystemMessageId.YOU_ARE_NOT_IN_A_CLAN);
					return;
				}
				
				clan.broadcastToOnlineMembers(new ExRequestNewInvitePartyInquiry(player, _reqType, _sayType));
				break;
			}
			case ALLIANCE:
			{
				if ((player.getClan() == null) || ((player.getClan() != null) && (player.getClan().getAllyId() == 0)))
				{
					player.sendPacket(SystemMessageId.YOU_ARE_NOT_IN_AN_ALLIANCE);
					return;
				}
				
				player.getClan().broadcastToOnlineAllyMembers(new ExRequestNewInvitePartyInquiry(player, _reqType, _sayType));
				break;
			}
		}
	}
}
