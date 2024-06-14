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

import java.util.ArrayList;
import java.util.List;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.enums.ChatType;
import org.l2j.gameserver.instancemanager.MentorManager;
import org.l2j.gameserver.instancemanager.RankManager;
import org.l2j.gameserver.instancemanager.SharedTeleportManager;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.NpcStringId;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.SystemMessageId;

public class CreatureSay extends ServerPacket
{
	private final Creature _sender;
	private final ChatType _chatType;
	private String _senderName = null;
	private String _text = null;
	private int _charId = 0;
	private int _messageId = -1;
	private int _mask;
	private List<String> _parameters;
	private boolean _shareLocation;
	
	/**
	 * @param sender
	 * @param receiver
	 * @param name
	 * @param chatType
	 * @param text
	 */
	public CreatureSay(Player sender, Player receiver, String name, ChatType chatType, String text)
	{
		this(sender, receiver, name, chatType, text, false);
	}
	
	/**
	 * @param sender
	 * @param receiver
	 * @param name
	 * @param chatType
	 * @param text
	 * @param shareLocation
	 */
	public CreatureSay(Player sender, Player receiver, String name, ChatType chatType, String text, boolean shareLocation)
	{
		_sender = sender;
		_senderName = name;
		_chatType = chatType;
		_text = text;
		_shareLocation = shareLocation;
		if (receiver != null)
		{
			if (receiver.getFriendList().contains(sender.getObjectId()))
			{
				_mask |= 0x01;
			}
			if ((receiver.getClanId() > 0) && (receiver.getClanId() == sender.getClanId()))
			{
				_mask |= 0x02;
			}
			if ((MentorManager.getInstance().getMentee(receiver.getObjectId(), sender.getObjectId()) != null) || (MentorManager.getInstance().getMentee(sender.getObjectId(), receiver.getObjectId()) != null))
			{
				_mask |= 0x04;
			}
			if ((receiver.getAllyId() > 0) && (receiver.getAllyId() == sender.getAllyId()))
			{
				_mask |= 0x08;
			}
		}
		// Does not shows level
		if (sender.isGM())
		{
			_mask |= 0x10;
		}
	}
	
	public CreatureSay(Creature sender, ChatType chatType, String senderName, String text)
	{
		this(sender, chatType, senderName, text, false);
	}
	
	public CreatureSay(Creature sender, ChatType chatType, String senderName, String text, boolean shareLocation)
	{
		_sender = sender;
		_chatType = chatType;
		_senderName = senderName;
		_text = text;
		_shareLocation = shareLocation;
	}
	
	public CreatureSay(Creature sender, ChatType chatType, NpcStringId npcStringId)
	{
		_sender = sender;
		_chatType = chatType;
		_messageId = npcStringId.getId();
		if (sender != null)
		{
			_senderName = sender.getName();
		}
	}
	
	public CreatureSay(ChatType chatType, int charId, SystemMessageId systemMessageId)
	{
		_sender = null;
		_chatType = chatType;
		_charId = charId;
		_messageId = systemMessageId.getId();
	}
	
	/**
	 * String parameter for argument S1,S2,.. in npcstring-e.dat
	 * @param text
	 */
	public void addStringParameter(String text)
	{
		if (_parameters == null)
		{
			_parameters = new ArrayList<>();
		}
		_parameters.add(text);
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.SAY2.writeId(this, buffer);
		buffer.writeInt(_sender == null ? 0 : _sender.getObjectId());
		buffer.writeInt(_chatType.getClientId());
		if (_senderName != null)
		{
			buffer.writeString(_senderName);
		}
		else
		{
			buffer.writeInt(_charId);
		}
		buffer.writeInt(_messageId); // High Five NPCString ID
		if (_text != null)
		{
			buffer.writeString(_text);
			if ((_sender != null) && (_sender.isPlayer() || _sender.isFakePlayer()) && (_chatType == ChatType.WHISPER))
			{
				buffer.writeByte(_mask);
				if ((_mask & 0x10) == 0)
				{
					buffer.writeByte(_sender.getLevel());
				}
			}
		}
		else if (_parameters != null)
		{
			for (String s : _parameters)
			{
				buffer.writeString(s);
			}
		}
		// Rank
		if ((_sender != null) && _sender.isPlayer())
		{
			final Clan clan = _sender.getClan();
			if ((clan != null) && ((_chatType == ChatType.CLAN) || (_chatType == ChatType.ALLIANCE)))
			{
				buffer.writeByte(0); // unknown clan byte
			}
			
			final int rank = RankManager.getInstance().getPlayerGlobalRank(_sender.getActingPlayer());
			if ((rank == 0) || (rank > 100))
			{
				buffer.writeByte(0);
			}
			else if (rank <= 10)
			{
				buffer.writeByte(1);
			}
			else if (rank <= 50)
			{
				buffer.writeByte(2);
			}
			else if (rank <= 100)
			{
				buffer.writeByte(3);
			}
			if (clan != null)
			{
				buffer.writeByte(clan.getCastleId());
			}
			else
			{
				buffer.writeByte(0);
			}
			
			if (_shareLocation)
			{
				buffer.writeByte(1);
				buffer.writeShort(SharedTeleportManager.getInstance().nextId(_sender));
			}
		}
		else
		{
			buffer.writeByte(0);
		}
	}
	
	@Override
	public void runImpl(Player player)
	{
		if (player != null)
		{
			player.broadcastSnoop(_chatType, _senderName, _text);
		}
	}
}
