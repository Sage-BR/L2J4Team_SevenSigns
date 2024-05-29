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
package org.l2j.gameserver.network.clientpackets.ranking;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.ranking.ExPvpRankingList;

/**
 * @author Berezkin Nikolay
 */
public class RequestPvpRankingList implements ClientPacket
{
	private int _season;
	private int _tabId;
	private int _type;
	private int _race;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_season = packet.readByte(); // CurrentSeason
		_tabId = packet.readByte(); // RankingGroup
		_type = packet.readByte(); // RankingScope
		_race = packet.readInt(); // Race
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		player.sendPacket(new ExPvpRankingList(player, _season, _tabId, _type, _race, player.getBaseClass()));
	}
}
