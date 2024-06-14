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
package org.l2j.gameserver.network.clientpackets.ranking;

import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.ranking.ExPvpRankingList;

/**
 * @author Berezkin Nikolay
 */
public class RequestPvpRankingList extends ClientPacket
{
	private int _season;
	private int _tabId;
	private int _type;
	private int _race;
	
	@Override
	protected void readImpl()
	{
		_season = readByte(); // CurrentSeason
		_tabId = readByte(); // RankingGroup
		_type = readByte(); // RankingScope
		_race = readInt(); // Race
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		player.sendPacket(new ExPvpRankingList(player, _season, _tabId, _type, _race, player.getBaseClass()));
	}
}
