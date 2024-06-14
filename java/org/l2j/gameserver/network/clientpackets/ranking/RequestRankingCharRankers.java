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
import org.l2j.gameserver.network.serverpackets.ranking.ExRankingCharRankers;

/**
 * @author JoeAlisson
 */
public class RequestRankingCharRankers extends ClientPacket
{
	private int _group;
	private int _scope;
	private int _ordinal;
	private int _baseclass;
	
	@Override
	protected void readImpl()
	{
		_group = readByte(); // Tab Id
		_scope = readByte(); // All or personal
		_ordinal = readInt();
		_baseclass = readInt();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		player.sendPacket(new ExRankingCharRankers(player, _group, _scope, _ordinal, _baseclass));
	}
}
