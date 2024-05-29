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
package org.l2j.gameserver.network.clientpackets.teleports;

import java.util.ArrayList;
import java.util.List;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.data.xml.TeleportListData;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.variables.PlayerVariables;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.PacketLogger;
import org.l2j.gameserver.network.clientpackets.ClientPacket;

/**
 * @author Mobius
 */
public class ExRequestTeleportFavoritesAddDel implements ClientPacket
{
	private boolean _enable;
	private int _teleportId;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_enable = packet.readByte() == 1;
		_teleportId = packet.readInt();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (TeleportListData.getInstance().getTeleport(_teleportId) == null)
		{
			PacketLogger.warning("No registered teleport location for id: " + _teleportId);
			return;
		}
		
		final List<Integer> favorites = new ArrayList<>();
		for (int id : player.getVariables().getIntegerList(PlayerVariables.FAVORITE_TELEPORTS))
		{
			if (TeleportListData.getInstance().getTeleport(_teleportId) == null)
			{
				PacketLogger.warning("No registered teleport location for id: " + _teleportId);
			}
			else
			{
				favorites.add(id);
			}
		}
		
		if (_enable)
		{
			if (!favorites.contains(_teleportId))
			{
				favorites.add(_teleportId);
			}
		}
		else
		{
			favorites.remove((Integer) _teleportId);
		}
		
		player.getVariables().setIntegerList(PlayerVariables.FAVORITE_TELEPORTS, favorites);
	}
}
