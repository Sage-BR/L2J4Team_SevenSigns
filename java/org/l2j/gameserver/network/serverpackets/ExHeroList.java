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
package org.l2j.gameserver.network.serverpackets;

import java.util.Map;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.Config;
import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.olympiad.Hero;
import org.l2j.gameserver.model.olympiad.Olympiad;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author -Wooden-, KenM, godson
 */
public class ExHeroList extends ServerPacket
{
	private final Map<Integer, StatSet> _heroList;
	
	public ExHeroList()
	{
		_heroList = Hero.getInstance().getHeroes();
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_HERO_LIST.writeId(this, buffer);
		buffer.writeInt(_heroList.size());
		for (StatSet hero : _heroList.values())
		{
			buffer.writeString(hero.getString(Olympiad.CHAR_NAME));
			buffer.writeInt(hero.getInt(Olympiad.CLASS_ID));
			buffer.writeString(hero.getString(Hero.CLAN_NAME, ""));
			buffer.writeInt(0); // hero.getInt(Hero.CLAN_CREST, 0)
			buffer.writeString(hero.getString(Hero.ALLY_NAME, ""));
			buffer.writeInt(0); // hero.getInt(Hero.ALLY_CREST, 0)
			buffer.writeInt(hero.getInt(Hero.COUNT));
			buffer.writeInt(Config.SERVER_ID);
			buffer.writeByte(0); // 272
		}
	}
}
