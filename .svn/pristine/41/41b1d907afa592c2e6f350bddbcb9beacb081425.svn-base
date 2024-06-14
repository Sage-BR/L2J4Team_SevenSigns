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
package org.l2jmobius.gameserver.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.l2jmobius.Config;
import org.l2jmobius.commons.database.DatabaseFactory;
import org.l2jmobius.gameserver.instancemanager.RankManager;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.RankingHistoryDataHolder;

/**
 * @author Serenitty
 */
public class RankingHistory
{
	private static final Logger LOGGER = Logger.getLogger(RankingHistory.class.getName());
	
	private static final int NUM_HISTORY_DAYS = 7;
	
	private final Player _player;
	private final Collection<RankingHistoryDataHolder> _data = new ArrayList<>();
	private long _nextUpdate = 0;
	
	public RankingHistory(Player player)
	{
		_player = player;
	}
	
	public void store()
	{
		final int ranking = RankManager.getInstance().getPlayerGlobalRank(_player);
		final long exp = _player.getExp();
		final int today = (int) (System.currentTimeMillis() / 86400000L);
		final int oldestDay = (today - NUM_HISTORY_DAYS) + 1;
		
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement statement = con.prepareStatement("INSERT INTO character_ranking_history (charId, day, ranking, exp) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE ranking = ?, exp = ?");
			PreparedStatement deleteSt = con.prepareStatement("DELETE FROM character_ranking_history WHERE charId = ? AND day < ?"))
		{
			statement.setInt(1, _player.getObjectId());
			statement.setInt(2, today);
			statement.setInt(3, ranking);
			statement.setLong(4, exp);
			statement.setInt(5, ranking); // update
			statement.setLong(6, exp); // update
			statement.execute();
			
			// Delete old records
			deleteSt.setInt(1, _player.getObjectId());
			deleteSt.setInt(2, oldestDay);
			deleteSt.execute();
		}
		catch (Exception e)
		{
			LOGGER.log(Level.WARNING, "Could not insert RankingCharHistory data: " + e.getMessage(), e);
		}
	}
	
	public Collection<RankingHistoryDataHolder> getData()
	{
		final long currentTime = System.currentTimeMillis();
		if (currentTime > _nextUpdate)
		{
			_data.clear();
			if (_nextUpdate == 0)
			{
				store(); // to update
			}
			_nextUpdate = currentTime + Config.CHAR_DATA_STORE_INTERVAL;
			try (Connection con = DatabaseFactory.getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT * FROM character_ranking_history WHERE charId = ? ORDER BY day DESC"))
			{
				statement.setInt(1, _player.getObjectId());
				try (ResultSet rset = statement.executeQuery())
				{
					while (rset.next())
					{
						final int day = rset.getInt("day");
						final long timestamp = (day * 86400000L) + 86400000L;
						final int ranking = rset.getInt("ranking");
						final long exp = rset.getLong("exp");
						_data.add(new RankingHistoryDataHolder(timestamp / 1000, ranking, exp));
					}
				}
			}
			catch (Exception e)
			{
				LOGGER.log(Level.WARNING, "Could not get RankingCharHistory data: " + e.getMessage(), e);
			}
		}
		return _data;
	}
}
