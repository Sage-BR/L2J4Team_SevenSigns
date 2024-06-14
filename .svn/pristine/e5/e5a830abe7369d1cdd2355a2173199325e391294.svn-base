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
package org.l2jmobius.gameserver.network.clientpackets.stats;

import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.stats.Stat;
import org.l2jmobius.gameserver.model.variables.PlayerVariables;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;

/**
 * @author Mobius
 */
public class ExSetStatusBonus extends ClientPacket
{
	private int _str;
	private int _dex;
	private int _con;
	private int _int;
	private int _wit;
	private int _men;
	
	@Override
	protected void readImpl()
	{
		readShort(); // unk
		readShort(); // totalBonus
		_str = readShort();
		_dex = readShort();
		_con = readShort();
		_int = readShort();
		_wit = readShort();
		_men = readShort();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		if ((_str < 0) || (_dex < 0) || (_con < 0) || (_int < 0) || (_wit < 0) || (_men < 0))
		{
			return;
		}
		
		final int usedPoints = player.getVariables().getInt(PlayerVariables.STAT_POINTS, 0);
		final int effectBonus = (int) player.getStat().getValue(Stat.ELIXIR_USAGE_LIMIT, 0);
		final int elixirsAvailable = player.getVariables().getInt(PlayerVariables.ELIXIRS_AVAILABLE, 0) + effectBonus;
		final int currentPoints = _str + _dex + _con + _int + _wit + _men;
		final int possiblePoints = player.getLevel() < 76 ? 0 : ((player.getLevel() - 75) + elixirsAvailable) - usedPoints;
		if ((possiblePoints <= 0) || (currentPoints > possiblePoints))
		{
			return;
		}
		
		if (_str > 0)
		{
			player.getVariables().set(PlayerVariables.STAT_STR, player.getVariables().getInt(PlayerVariables.STAT_STR, 0) + _str);
		}
		if (_dex > 0)
		{
			player.getVariables().set(PlayerVariables.STAT_DEX, player.getVariables().getInt(PlayerVariables.STAT_DEX, 0) + _dex);
		}
		if (_con > 0)
		{
			player.getVariables().set(PlayerVariables.STAT_CON, player.getVariables().getInt(PlayerVariables.STAT_CON, 0) + _con);
		}
		if (_int > 0)
		{
			player.getVariables().set(PlayerVariables.STAT_INT, player.getVariables().getInt(PlayerVariables.STAT_INT, 0) + _int);
		}
		if (_wit > 0)
		{
			player.getVariables().set(PlayerVariables.STAT_WIT, player.getVariables().getInt(PlayerVariables.STAT_WIT, 0) + _wit);
		}
		if (_men > 0)
		{
			player.getVariables().set(PlayerVariables.STAT_MEN, player.getVariables().getInt(PlayerVariables.STAT_MEN, 0) + _men);
		}
		
		player.getStat().recalculateStats(true);
		
		// Calculate stat increase skills.
		player.calculateStatIncreaseSkills();
		player.updateUserInfo();
	}
}
