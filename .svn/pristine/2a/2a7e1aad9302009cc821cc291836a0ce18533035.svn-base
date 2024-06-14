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
package org.l2jmobius.gameserver.network.serverpackets.vip;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.Config;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.vip.VipManager;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

public class ReceiveVipInfo extends ServerPacket
{
	private final Player _player;
	
	public ReceiveVipInfo(Player player)
	{
		_player = player;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		if (!Config.VIP_SYSTEM_ENABLED)
		{
			return;
		}
		
		final VipManager vipManager = VipManager.getInstance();
		final byte vipTier = _player.getVipTier();
		final int vipDuration = (int) ChronoUnit.SECONDS.between(Instant.now(), Instant.ofEpochMilli(_player.getVipTierExpiration()));
		
		ServerPackets.RECIVE_VIP_INFO.writeId(this, buffer);
		buffer.writeByte(vipTier);
		buffer.writeLong(_player.getVipPoints());
		buffer.writeInt(vipDuration);
		buffer.writeLong(vipManager.getPointsToLevel((byte) (vipTier + 1)));
		buffer.writeLong(vipManager.getPointsDepreciatedOnLevel(vipTier));
		buffer.writeByte(vipTier);
		buffer.writeLong(vipManager.getPointsToLevel(vipTier));
	}
}