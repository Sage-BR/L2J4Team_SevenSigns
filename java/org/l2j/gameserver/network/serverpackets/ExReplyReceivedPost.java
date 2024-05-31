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
package org.l2j.gameserver.network.serverpackets;

import java.util.Collection;

import org.l2j.gameserver.enums.MailType;
import org.l2j.gameserver.model.Message;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.itemcontainer.ItemContainer;
import org.l2j.gameserver.network.PacketLogger;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.SystemMessageId;

/**
 * @author Migi, DS
 */
public class ExReplyReceivedPost extends AbstractItemPacket
{
	private final Message _msg;
	private Collection<Item> _items = null;
	
	public ExReplyReceivedPost(Message msg)
	{
		_msg = msg;
		if (msg.hasAttachments())
		{
			final ItemContainer attachments = msg.getAttachments();
			if ((attachments != null) && (attachments.getSize() > 0))
			{
				_items = attachments.getItems();
			}
			else
			{
				PacketLogger.warning("Message " + msg.getId() + " has attachments but itemcontainer is empty.");
			}
		}
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_REPLY_RECEIVED_POST.writeId(this);
		writeInt(_msg.getMailType().ordinal()); // GOD
		if (_msg.getMailType() == MailType.COMMISSION_ITEM_RETURNED)
		{
			writeInt(SystemMessageId.THE_REGISTRATION_PERIOD_FOR_THE_ITEM_YOU_REGISTERED_HAS_EXPIRED.getId());
			writeInt(SystemMessageId.THE_AUCTION_HOUSE_REGISTRATION_PERIOD_HAS_EXPIRED_AND_THE_CORRESPONDING_ITEM_IS_BEING_FORWARDED.getId());
		}
		else if (_msg.getMailType() == MailType.COMMISSION_ITEM_SOLD)
		{
			writeInt(_msg.getItemId());
			writeInt(_msg.getEnchantLvl());
			for (int i = 0; i < 6; i++)
			{
				writeInt(_msg.getElementals()[i]);
			}
			writeInt(SystemMessageId.THE_ITEM_YOU_REGISTERED_HAS_BEEN_SOLD.getId());
			writeInt(SystemMessageId.S1_SOLD.getId());
		}
		writeInt(_msg.getId());
		writeInt(_msg.isLocked());
		writeInt(0); // Unknown
		writeString(_msg.getSenderName());
		writeString(_msg.getSubject());
		writeString(_msg.getContent());
		if ((_items != null) && !_items.isEmpty())
		{
			writeInt(_items.size());
			for (Item item : _items)
			{
				writeItem(item);
				writeInt(item.getObjectId());
			}
		}
		else
		{
			writeInt(0);
		}
		writeLong(_msg.getReqAdena());
		writeInt(_msg.hasAttachments());
		writeInt(_msg.isReturned());
	}
}
