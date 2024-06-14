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

import org.l2j.gameserver.instancemanager.SiegeManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.model.siege.Siege;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.castlewar.MercenaryCastleWarCastleSiegeAttackerList;
import org.l2j.gameserver.network.serverpackets.castlewar.MercenaryCastleWarCastleSiegeDefenderList;

public class ExPledgeMercenaryMemberJoin extends ClientPacket
{
	private int _castleId;
	private boolean _type;
	private int _pledgeId;
	
	@Override
	protected void readImpl()
	{
		readInt(); // objectId
		_type = readInt() == 1;
		_castleId = readInt();
		_pledgeId = readInt();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		final Siege siege = SiegeManager.getInstance().getSiege(_castleId);
		if ((siege != null) && siege.isInProgress())
		{
			return;
		}
		
		if (_type)
		{
			if (player.getParty() != null)
			{
				player.sendPacket(SystemMessageId.A_CHARACTER_WHICH_IS_A_MEMBER_OF_A_PARTY_CANNOT_FILE_A_MERCENARY_REQUEST);
				return;
			}
			else if (player.isMercenary())
			{
				player.sendPacket(SystemMessageId.THE_CHARACTER_IS_PARTICIPATING_AS_A_MERCENARY);
				return;
			}
			else if (player.getLevel() < 40)
			{
				player.sendPacket(SystemMessageId.YOUR_CHARACTER_DOES_NOT_MEET_THE_LEVEL_REQUIREMENTS_TO_BE_A_MERCENARY);
				return;
			}
			else if (player.getClan() != null)
			{
				final Clan clan = player.getClan();
				if (clan.getId() == _pledgeId)
				{
					player.sendPacket(SystemMessageId.YOU_CANNOT_BE_A_MERCENARY_AT_THE_CLAN_YOU_ARE_A_MEMBER_OF);
					return;
				}
				if ((siege != null) && (siege.checkIsAttacker(clan) || siege.checkIsDefender(clan)))
				{
					player.sendPacket(SystemMessageId.ATTACKERS_AND_DEFENDERS_CANNOT_BE_RECRUITED_AS_MERCENARIES);
					return;
				}
			}
		}
		
		player.setMercenary(_type, _pledgeId);
		player.sendPacket(new MercenaryCastleWarCastleSiegeAttackerList(_castleId));
		player.sendPacket(new MercenaryCastleWarCastleSiegeDefenderList(_castleId));
	}
}
