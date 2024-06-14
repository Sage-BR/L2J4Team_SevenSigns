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
package org.l2j.gameserver.network.clientpackets.castlewar;

import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.network.clientpackets.ClientPacket;

public class ExPledgeMercenaryRecruitInfoSet extends ClientPacket
{
	private boolean _isRecruit;
	private int _mercenaryReaward;
	
	@Override
	protected void readImpl()
	{
		readInt(); // castleId
		readInt(); // type
		_isRecruit = readInt() == 1;
		_mercenaryReaward = readInt();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		final Clan clan = player.getClan();
		if (clan == null)
		{
			return;
		}
		
		if (clan.isRecruitMercenary() && (clan.getMapMercenary().size() > 0))
		{
			return;
		}
		
		clan.setRecruitMercenary(_isRecruit);
		clan.setRewardMercenary(_mercenaryReaward);
	}
}
