/*
 * This file is part of the L2J Mobius project.
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
package org.l2jmobius.gameserver.network.serverpackets;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.enums.HtmlActionScope;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;

/**
 * NpcHtmlMessage server packet implementation.
 * @author HorridoJoho
 */
public class NpcHtmlMessage extends AbstractHtmlPacket
{
	private final int _itemId;
	private final int _size;
	
	public NpcHtmlMessage()
	{
		_itemId = 0;
		_size = 0;
	}
	
	public NpcHtmlMessage(int npcObjId)
	{
		super(npcObjId);
		_itemId = 0;
		_size = 0;
	}
	
	public NpcHtmlMessage(String html)
	{
		super(html);
		_itemId = 0;
		_size = 0;
	}
	
	public NpcHtmlMessage(int npcObjId, String html)
	{
		super(npcObjId, html);
		_itemId = 0;
		_size = 0;
	}
	
	public NpcHtmlMessage(int npcObjId, int itemId)
	{
		super(npcObjId);
		if (itemId < 0)
		{
			throw new IllegalArgumentException();
		}
		_itemId = itemId;
		_size = 0;
	}
	
	public NpcHtmlMessage(int npcObjId, int itemId, String html)
	{
		super(npcObjId, html);
		if (itemId < 0)
		{
			throw new IllegalArgumentException();
		}
		_itemId = itemId;
		_size = 0;
	}
	
	/**
	 * @param npcObjId
	 * @param itemId
	 * @param html
	 * @param windowSize 0 - default, 1 - huge, 2 - max
	 */
	public NpcHtmlMessage(int npcObjId, int itemId, String html, int windowSize)
	{
		super(npcObjId, html);
		if (itemId < 0)
		{
			throw new IllegalArgumentException();
		}
		_itemId = itemId;
		_size = windowSize;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.NPC_HTML_MESSAGE.writeId(this, buffer);
		buffer.writeInt(getNpcObjId());
		buffer.writeString(getHtml());
		buffer.writeInt(_itemId);
		buffer.writeInt(0); // play sound - 0 = enabled, 1 = disabled
		buffer.writeByte(_size);
	}
	
	@Override
	public HtmlActionScope getScope()
	{
		return _itemId == 0 ? HtmlActionScope.NPC_HTML : HtmlActionScope.NPC_ITEM_HTML;
	}
}
