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
package org.l2jmobius.gameserver.network.clientpackets.ranking;

import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;
import org.l2jmobius.gameserver.network.serverpackets.ranking.ExOlympiadRankingInfo;

/**
 * @author NviX
 */
public class RequestOlympiadRankingInfo extends ClientPacket
{
	private int _tabId;
	private int _rankingType;
	private int _unk;
	private int _classId;
	private int _serverId;
	
	@Override
	protected void readImpl()
	{
		_tabId = readByte();
		_rankingType = readByte();
		_unk = readByte();
		_classId = readInt();
		_serverId = readInt();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		player.sendPacket(new ExOlympiadRankingInfo(player, _tabId, _rankingType, _unk, _classId, _serverId));
	}
}
