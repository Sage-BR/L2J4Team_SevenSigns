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
package org.l2jmobius.gameserver.network.serverpackets.ranking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.l2jmobius.Config;
import org.l2jmobius.commons.database.DatabaseFactory;
import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.data.sql.ClanTable;
import org.l2jmobius.gameserver.instancemanager.RankManager;
import org.l2jmobius.gameserver.model.olympiad.Hero;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.PacketLogger;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

/**
 * @author NviX
 */
public class ExOlympiadHeroAndLegendInfo extends ServerPacket
{
	// TODO: Move query and store data at RankManager.
	private static final String GET_HEROES = "SELECT characters.charId, characters.char_name, characters.race, characters.sex, characters.base_class, characters.level, characters.clanid, olympiad_nobles_eom.competitions_won, olympiad_nobles_eom.competitions_lost, olympiad_nobles_eom.olympiad_points, heroes.legend_count, heroes.count FROM heroes, characters, olympiad_nobles_eom WHERE characters.charId = heroes.charId AND characters.charId = olympiad_nobles_eom.charId AND heroes.played = 1 ORDER BY olympiad_nobles_eom.olympiad_points DESC, characters.base_class ASC LIMIT " + RankManager.PLAYER_LIMIT;
	
	public ExOlympiadHeroAndLegendInfo()
	{
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_OLYMPIAD_HERO_AND_LEGEND_INFO.writeId(this, buffer);
		if (!Hero.getInstance().getHeroes().isEmpty())
		{
			try (Connection con = DatabaseFactory.getConnection();
				PreparedStatement statement = con.prepareStatement(GET_HEROES))
			{
				try (ResultSet rset = statement.executeQuery())
				{
					int i = 1;
					boolean wroteCount = false;
					while (rset.next())
					{
						if (i == 1)
						{
							buffer.writeByte(1); // ?? shows 78 on JP
							buffer.writeByte(1); // ?? shows 0 on JP
							buffer.writeSizedString(rset.getString("char_name"));
							final int clanId = rset.getInt("clanid");
							if (clanId > 0)
							{
								buffer.writeSizedString(ClanTable.getInstance().getClan(clanId).getName());
							}
							else
							{
								buffer.writeSizedString("");
							}
							buffer.writeInt(Config.SERVER_ID);
							buffer.writeInt(rset.getInt("race"));
							buffer.writeInt(rset.getInt("sex") != 1);
							buffer.writeInt(rset.getInt("base_class"));
							buffer.writeInt(rset.getInt("level"));
							buffer.writeInt(rset.getInt("legend_count"));
							buffer.writeInt(rset.getInt("competitions_won"));
							buffer.writeInt(rset.getInt("competitions_lost"));
							buffer.writeInt(rset.getInt("olympiad_points"));
							if (clanId > 0)
							{
								buffer.writeInt(ClanTable.getInstance().getClan(clanId).getLevel());
							}
							else
							{
								buffer.writeInt(0);
							}
							i++;
						}
						else
						{
							if (!wroteCount)
							{
								buffer.writeInt(Hero.getInstance().getHeroes().size() - 1);
								wroteCount = true;
							}
							if (Hero.getInstance().getHeroes().size() > 1)
							{
								buffer.writeSizedString(rset.getString("char_name"));
								final int clanId = rset.getInt("clanid");
								if (clanId > 0)
								{
									buffer.writeSizedString(ClanTable.getInstance().getClan(clanId).getName());
								}
								else
								{
									buffer.writeSizedString("");
								}
								buffer.writeInt(Config.SERVER_ID);
								buffer.writeInt(rset.getInt("race"));
								buffer.writeInt(rset.getInt("sex") != 1);
								buffer.writeInt(rset.getInt("base_class"));
								buffer.writeInt(rset.getInt("level"));
								buffer.writeInt(rset.getInt("count"));
								buffer.writeInt(rset.getInt("competitions_won"));
								buffer.writeInt(rset.getInt("competitions_lost"));
								buffer.writeInt(rset.getInt("olympiad_points"));
								if (clanId > 0)
								{
									buffer.writeInt(ClanTable.getInstance().getClan(clanId).getLevel());
								}
								else
								{
									buffer.writeInt(0);
								}
							}
						}
					}
				}
			}
			catch (SQLException e)
			{
				PacketLogger.warning("Hero and Legend Info: Couldnt load data: " + e.getMessage());
			}
		}
	}
}
