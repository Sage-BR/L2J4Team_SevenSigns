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
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.SystemMessageId;

/**
 * @author Serenitty
 */
public class RequestChangeNicknameEmote implements ClientPacket
{
	private static final int ESPECIAL_COLOR_TITLE_EMOTE = 95892;
	private static final int ESPECIAL_COLOR_TITLE_SEALED = 94764;
	private static final int ESPECIAL_STYLISH_COLOR_TITLE = 49662;
	private static final int[] COLORS =
	{
		0x9393FF, // Pink 1
		0x7C49FC, // Rose Pink 2
		0x97F8FC, // Yellow 3
		0xFA9AEE, // Lilac 4
		0xFF5D93, // Cobalt Violet 5
		0x00FCA0, // Mint Green 6
		0xA0A601, // Peacock Green 7
		0x7898AF, // Ochre 8
		0x486295, // Chocolate 9
		0x999999, // Silver 10 ** good here
		0xF3DC09, // SkyBlue 11
		0x05D3F6, // Gold 12
		0x3CB1F4, // Orange 13
		0xF383F3, // Pink 14
		0x0909F3, // Red 15
		0xF3DC09, // SkyBlue 16
		0x000000, // dummy
	};
	
	private int _colorNum;
	private int _itemId;
	private String _title;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_itemId = packet.readInt();
		_colorNum = packet.readInt();
		_title = packet.readSizedString();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final Item item = player.getInventory().getItemByItemId(_itemId);
		if ((item == null) || (item.getEtcItem() == null) || (item.getEtcItem().getHandlerName() == null) || !item.getEtcItem().getHandlerName().equalsIgnoreCase("NicknameColor"))
		{
			return;
		}
		
		if ((_colorNum < 0) || (_colorNum >= COLORS.length))
		{
			return;
		}
		
		if (_title.contains("{"))
		{
			player.sendMessage("Cannot use this type of characters {}");
			return;
		}
		
		if (((_itemId == ESPECIAL_COLOR_TITLE_EMOTE) || (_itemId == ESPECIAL_COLOR_TITLE_SEALED) || (_itemId == ESPECIAL_STYLISH_COLOR_TITLE)) && player.destroyItem("Consume", item, 1, null, true))
		{
			player.setTitle(_title);
			player.getAppearance().setTitleColor(COLORS[_colorNum - 1]);
			player.broadcastUserInfo();
			player.sendPacket(SystemMessageId.YOUR_TITLE_HAS_BEEN_CHANGED);
			return;
		}
		
		if (player.destroyItem("Consume", item, 1, null, true))
		{
			int skyblue = _colorNum - 2;
			if ((skyblue > 11) && (player.getLevel() >= 90))
			{
				skyblue = 15;
			}
			
			player.setTitle(_title);
			player.sendPacket(SystemMessageId.YOUR_TITLE_HAS_BEEN_CHANGED);
			player.getAppearance().setTitleColor(COLORS[skyblue]);
			player.broadcastUserInfo();
		}
		
	}
	
}