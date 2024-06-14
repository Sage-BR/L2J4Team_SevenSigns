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
package org.l2jmobius.gameserver.network.clientpackets.settings;

import org.l2jmobius.gameserver.model.ClientSettings;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;

/**
 * @author Index
 */
public class ExInteractModify extends ClientPacket
{
	private int _type;
	private int _settings;
	
	@Override
	protected void readImpl()
	{
		_type = readByte();
		_settings = readByte();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		final ClientSettings clientSettings = player.getClientSettings();
		switch (_type)
		{
			case 0:
			{
				clientSettings.setPartyRequestRestrictedFromOthers((_settings & 1) == 1);
				clientSettings.setPartyRequestRestrictedFromClan((_settings & 2) == 2);
				clientSettings.setPartyRequestRestrictedFromFriends((_settings & 4) == 4);
				clientSettings.storeSettings();
				break;
			}
			case 1:
			{
				clientSettings.setFriendRequestRestrictedFromOthers((_settings & 1) == 1);
				clientSettings.setFriendRequestRestrictionFromClan((_settings & 2) == 2);
				clientSettings.storeSettings();
				break;
			}
		}
	}
}
