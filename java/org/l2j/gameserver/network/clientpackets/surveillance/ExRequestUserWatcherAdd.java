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
package org.l2j.gameserver.network.clientpackets.surveillance;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.l2j.commons.database.DatabaseFactory;
import org.l2j.gameserver.data.sql.CharInfoTable;
import org.l2j.gameserver.model.World;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.PacketLogger;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.RelationChanged;
import org.l2j.gameserver.network.serverpackets.SystemMessage;
import org.l2j.gameserver.network.serverpackets.surveillance.ExUserWatcherTargetList;

/**
 * @author MacuK
 */
public class ExRequestUserWatcherAdd extends ClientPacket
{
	private String _name;
	
	@Override
	protected void readImpl()
	{
		_name = readSizedString();
		readInt(); // World Id
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		final int targetId = CharInfoTable.getInstance().getIdByName(_name);
		if (targetId == -1)
		{
			player.sendPacket(SystemMessageId.THAT_CHARACTER_DOES_NOT_EXIST);
			return;
		}
		
		// You cannot add yourself to your own friend list.
		if (targetId == player.getObjectId())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_ADD_YOURSELF_TO_YOUR_SURVEILLANCE_LIST);
			return;
		}
		
		// Target already in surveillance list.
		if (player.getSurveillanceList().contains(targetId))
		{
			player.sendPacket(SystemMessageId.THE_CHARACTER_IS_ALREADY_IN_YOUR_SURVEILLANCE_LIST);
			return;
		}
		
		if (player.getSurveillanceList().size() >= 10)
		{
			player.sendPacket(SystemMessageId.MAXIMUM_NUMBER_OF_PEOPLE_ADDED_YOU_CANNOT_ADD_MORE);
			return;
		}
		
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement statement = con.prepareStatement("INSERT INTO character_surveillances (charId, targetId) VALUES (?, ?)"))
		{
			statement.setInt(1, player.getObjectId());
			statement.setInt(2, targetId);
			statement.execute();
		}
		catch (Exception e)
		{
			PacketLogger.warning("ExRequestUserWatcherAdd: Could not add surveillance objectid: " + e.getMessage());
		}
		
		// Player added to your friend list.
		final SystemMessage sm = new SystemMessage(SystemMessageId.YOU_VE_ADDED_C1_TO_YOUR_SURVEILLANCE_LIST);
		sm.addString(_name);
		player.sendPacket(sm);
		player.getSurveillanceList().add(targetId);
		player.sendPacket(new ExUserWatcherTargetList(player));
		
		final Player target = World.getInstance().getPlayer(targetId);
		if ((target != null) && target.isVisibleFor(player))
		{
			player.sendPacket(new RelationChanged());
		}
	}
}
