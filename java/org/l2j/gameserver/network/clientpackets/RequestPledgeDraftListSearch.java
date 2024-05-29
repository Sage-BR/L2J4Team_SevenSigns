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
import org.l2j.commons.util.CommonUtil;
import org.l2j.gameserver.instancemanager.ClanEntryManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.serverpackets.ExPledgeDraftListSearch;

/**
 * @author Sdw
 */
public class RequestPledgeDraftListSearch implements ClientPacket
{
	private int _levelMin;
	private int _levelMax;
	private int _classId;
	private String _query;
	private int _sortBy;
	private boolean _descending;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_levelMin = CommonUtil.constrain(packet.readInt(), 0, 107);
		_levelMax = CommonUtil.constrain(packet.readInt(), 0, 107);
		_classId = packet.readInt();
		_query = packet.readString();
		_sortBy = packet.readInt();
		_descending = packet.readInt() == 2;
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (_query.isEmpty())
		{
			player.sendPacket(new ExPledgeDraftListSearch(ClanEntryManager.getInstance().getSortedWaitingList(_levelMin, _levelMax, _classId, _sortBy, _descending)));
		}
		else
		{
			player.sendPacket(new ExPledgeDraftListSearch(ClanEntryManager.getInstance().queryWaitingListByName(_query.toLowerCase())));
		}
	}
}
