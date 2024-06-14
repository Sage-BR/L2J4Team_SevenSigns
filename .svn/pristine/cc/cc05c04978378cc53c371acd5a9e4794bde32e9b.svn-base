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
package org.l2jmobius.gameserver.network.serverpackets.olympiad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

import org.l2jmobius.commons.database.DatabaseFactory;
import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.instancemanager.RankManager;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.olympiad.Olympiad;
import org.l2jmobius.gameserver.model.olympiad.OlympiadManager;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.PacketLogger;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

public class ExOlympiadRecord extends ServerPacket
{
	private static final String GET_PREVIOUS_CYCLE_DATA = "SELECT charId, class_id, olympiad_points, competitions_won, competitions_lost, " + "(SELECT COUNT(*) FROM olympiad_nobles_eom WHERE olympiad_points > t.olympiad_points) AS previousPlaceTotal " + "FROM olympiad_nobles_eom t WHERE class_id = ? ORDER BY olympiad_points DESC LIMIT " + RankManager.PLAYER_LIMIT;
	
	private final Player _player;
	private final int _gameRuleType;
	private final int _type;
	
	public ExOlympiadRecord(Player player, int gameRuleType)
	{
		_player = player;
		_gameRuleType = gameRuleType;
		_type = OlympiadManager.getInstance().isRegistered(player) ? 1 : 0;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_OLYMPIAD_RECORD.writeId(this, buffer);
		buffer.writeInt(Olympiad.getInstance().getNoblePoints(_player)); // nPoint
		buffer.writeInt(Olympiad.getInstance().getCompetitionWon(_player.getObjectId())); // nWinCount
		buffer.writeInt(Olympiad.getInstance().getCompetitionLost(_player.getObjectId())); // nLoseCount
		buffer.writeInt(Olympiad.getInstance().getRemainingWeeklyMatches(_player.getObjectId())); // nMatchCount
		// Previous Cycle
		int previousPlace = 0;
		int previousWins = 0;
		int previousLoses = 0;
		int previousPoints = 0;
		int previousClass = 0;
		int previousPlaceTotal = 0;
		
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement statement = con.prepareStatement(GET_PREVIOUS_CYCLE_DATA))
		{
			statement.setInt(1, _player.getBaseClass());
			try (ResultSet rset = statement.executeQuery())
			{
				if (rset.next())
				{
					previousPlace = rset.getRow();
					previousWins = rset.getInt("competitions_won");
					previousLoses = rset.getInt("competitions_lost");
					previousPoints = rset.getInt("olympiad_points");
					previousClass = rset.getInt("class_id");
					previousPlaceTotal = rset.getInt("previousPlaceTotal") + 1;
				}
			}
		}
		catch (Exception e)
		{
			PacketLogger.warning("ExOlympiadRecord: Could not load data: " + e.getMessage());
		}
		
		buffer.writeInt(previousClass); // nPrevClassType
		buffer.writeInt(previousPlaceTotal); // nPrevRank in all servers
		buffer.writeInt(2); // nPrevRankCount number of participants with 25+ matches
		buffer.writeInt(previousPlace); // nPrevClassRank in all servers
		buffer.writeInt(4); // nPrevClassRankCount number of participants with 25+ matches
		buffer.writeInt(5); // nPrevClassRankByServer in current server
		buffer.writeInt(6); // nPrevClassRankByServerCount number of participants with 25+ matches
		buffer.writeInt(previousPoints); // nPrevPoint
		buffer.writeInt(previousWins); // nPrevWinCount
		buffer.writeInt(previousLoses); // nPrevLoseCount
		buffer.writeInt(previousPlace); // nPrevGrade
		buffer.writeInt(Calendar.getInstance().get(Calendar.YEAR)); // nSeasonYear
		buffer.writeInt(Calendar.getInstance().get(Calendar.MONTH) + 1); // nSeasonMonth
		buffer.writeByte(Olympiad.getInstance().inCompPeriod()); // bMatchOpen
		buffer.writeInt(Olympiad.getInstance().getCurrentCycle()); // nSeason
		buffer.writeByte(_type); // bRegistered
		buffer.writeInt(_gameRuleType); // cGameRuleType
	}
}
