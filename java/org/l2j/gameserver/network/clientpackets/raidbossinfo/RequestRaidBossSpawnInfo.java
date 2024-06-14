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
package org.l2j.gameserver.network.clientpackets.raidbossinfo;

import java.util.HashMap;
import java.util.Map;

import org.l2j.gameserver.enums.RaidBossStatus;
import org.l2j.gameserver.instancemanager.DBSpawnManager;
import org.l2j.gameserver.instancemanager.GrandBossManager;
import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.instance.GrandBoss;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.raidbossinfo.ExRaidBossSpawnInfo;

/**
 * @author Mobius
 */
public class RequestRaidBossSpawnInfo extends ClientPacket
{
	private static final int BAIUM = 29020;
	
	private final Map<Integer, RaidBossStatus> _statuses = new HashMap<>();
	
	@Override
	protected void readImpl()
	{
		final int count = readInt();
		for (int i = 0; i < count; i++)
		{
			final int bossId = readInt();
			final GrandBoss boss = GrandBossManager.getInstance().getBoss(bossId);
			if (boss == null)
			{
				final RaidBossStatus status = DBSpawnManager.getInstance().getStatus(bossId);
				if (status != RaidBossStatus.UNDEFINED)
				{
					final Npc npc = DBSpawnManager.getInstance().getNpc(bossId);
					if ((npc != null) && npc.isInCombat())
					{
						_statuses.put(bossId, RaidBossStatus.COMBAT);
					}
					else
					{
						_statuses.put(bossId, status);
					}
				}
				else
				{
					_statuses.put(bossId, RaidBossStatus.DEAD);
					// PacketLogger.warning("Could not find spawn info for boss " + bossId + ".");
				}
			}
			else
			{
				if (boss.isDead() || !boss.isSpawned())
				{
					if ((bossId == BAIUM) && (GrandBossManager.getInstance().getStatus(BAIUM) == 0))
					{
						_statuses.put(bossId, RaidBossStatus.ALIVE);
					}
					else
					{
						_statuses.put(bossId, RaidBossStatus.DEAD);
					}
				}
				else if (boss.isInCombat())
				{
					_statuses.put(bossId, RaidBossStatus.COMBAT);
				}
				else
				{
					_statuses.put(bossId, RaidBossStatus.ALIVE);
				}
			}
		}
	}
	
	@Override
	protected void runImpl()
	{
		getClient().sendPacket(new ExRaidBossSpawnInfo(_statuses));
	}
}
