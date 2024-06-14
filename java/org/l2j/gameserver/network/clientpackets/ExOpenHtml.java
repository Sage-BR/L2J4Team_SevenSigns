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
import org.l2j.gameserver.cache.HtmCache;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.PacketLogger;
import org.l2j.gameserver.network.serverpackets.ExPremiumManagerShowHtml;
import org.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * @author Mobius
 */
public class ExOpenHtml extends ClientPacket
{
	private int _type;
	
	@Override
	protected void readImpl()
	{
		_type = readByte();
	}
	
	@Override
	protected void runImpl()
	{
		final GameClient client = getClient();
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		switch (_type)
		{
			case 1:
			{
				if (Config.PC_CAFE_ENABLED)
				{
					final NpcHtmlMessage html = new NpcHtmlMessage();
					html.setFile(player, "data/html/pccafe.htm");
					player.sendPacket(html);
				}
				break;
			}
			case 5:
			{
				if (Config.GAME_ASSISTANT_ENABLED)
				{
					client.sendPacket(new ExPremiumManagerShowHtml(HtmCache.getInstance().getHtm(player, "data/scripts/ai/others/GameAssistant/32478.html")));
				}
				break;
			}
			// case 7:
			// {
			// if (Config.EINHASAD_STORE_ENABLED)
			// {
			// client.sendPacket(new ExPremiumManagerShowHtml(HtmCache.getInstance().getHtm(player, "data/scripts/ai/others/EinhasadStore/34487.html")));
			// }
			// break;
			// }
			default:
			{
				PacketLogger.warning("Unknown ExOpenHtml type (" + _type + ")");
				break;
			}
		}
	}
}
