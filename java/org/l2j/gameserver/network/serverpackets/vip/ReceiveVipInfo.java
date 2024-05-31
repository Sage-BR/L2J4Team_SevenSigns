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
package org.l2j.gameserver.network.serverpackets.vip;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.l2j.Config;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.vip.VipManager;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

public class ReceiveVipInfo extends ServerPacket
{
	private final Player _player;
	
	public ReceiveVipInfo(Player player)
	{
		_player = player;
	}
	
	@Override
	public void write()
	{
		if (!Config.VIP_SYSTEM_ENABLED)
		{
			return;
		}
		
		final VipManager vipManager = VipManager.getInstance();
		final byte vipTier = _player.getVipTier();
		final int vipDuration = (int) ChronoUnit.SECONDS.between(Instant.now(), Instant.ofEpochMilli(_player.getVipTierExpiration()));
		
		ServerPackets.RECIVE_VIP_INFO.writeId(this);
		writeByte(vipTier);
		writeLong(_player.getVipPoints());
		writeInt(vipDuration);
		writeLong(vipManager.getPointsToLevel((byte) (vipTier + 1)));
		writeLong(vipManager.getPointsDepreciatedOnLevel(vipTier));
		writeByte(vipTier);
		writeLong(vipManager.getPointsToLevel(vipTier));
	}
}