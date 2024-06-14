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
package org.l2j.gameserver.enums;

import java.util.HashMap;
import java.util.Map;

public enum UniqueGachaRank
{
	RANK_UR(1),
	RANK_SR(2),
	RANK_R(3);
	
	private final int _clientId;
	
	private static final Map<Integer, UniqueGachaRank> VALUES = new HashMap<>(3);
	static
	{
		for (UniqueGachaRank rank : values())
		{
			VALUES.put(rank.getClientId(), rank);
		}
	}
	
	UniqueGachaRank(int clientId)
	{
		_clientId = clientId;
	}
	
	public static UniqueGachaRank getRankByClientId(int clientId)
	{
		return VALUES.getOrDefault(clientId, null);
	}
	
	public int getClientId()
	{
		return _clientId;
	}
}
