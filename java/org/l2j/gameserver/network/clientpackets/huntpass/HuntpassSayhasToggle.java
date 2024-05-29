/*
 * This file is part of the L2J 4Team project.
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
package org.l2j.gameserver.network.clientpackets.huntpass;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.model.HuntPass;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.huntpass.HuntPassSayhasSupportInfo;

/**
 * @author Serenitty
 */
public class HuntpassSayhasToggle implements ClientPacket
{
	private boolean _sayhaToggle;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_sayhaToggle = packet.readByte() != 0;
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final HuntPass huntPass = player.getHuntPass();
		if (huntPass == null)
		{
			return;
		}
		
		int timeEarned = huntPass.getAvailableSayhaTime();
		int timeUsed = huntPass.getUsedSayhaTime();
		if (player.getVitalityPoints() < 35000)
		{
			player.sendPacket(SystemMessageId.UNABLE_TO_ACTIVATE_YOU_CAN_USE_SAYHA_S_GRACE_SUSTENTION_EFFECT_OF_THE_SEASON_PASS_ONLY_IF_YOU_HAVE_AT_LEAST_35_000_SAYHA_S_GRACE_POINTS);
			return;
		}
		
		if (_sayhaToggle && (timeEarned > 0) && (timeEarned > timeUsed))
		{
			huntPass.setSayhasSustention(true);
		}
		else
		{
			huntPass.setSayhasSustention(false);
		}
		player.sendPacket(new HuntPassSayhasSupportInfo(player));
	}
}
