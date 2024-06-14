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
 * @author JoeAlisson
 */
public class ExPremiumManagerShowHtml extends AbstractHtmlPacket
{
	public ExPremiumManagerShowHtml(String html)
	{
		super(html);
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_PREMIUM_MANAGER_SHOW_HTML.writeId(this, buffer);
		buffer.writeInt(getNpcObjId());
		buffer.writeString(getHtml());
		buffer.writeInt(-1);
		buffer.writeInt(0);
	}
	
	@Override
	public HtmlActionScope getScope()
	{
		return HtmlActionScope.PREMIUM_HTML;
	}
}
