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
package org.l2j.gameserver.network.serverpackets;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.l2j.gameserver.instancemanager.InstanceManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.instancezone.Instance;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author UnAfraid
 */
public class ExInzoneWaiting extends ServerPacket
{
	private final int _currentTemplateId;
	private final Map<Integer, Long> _instanceTimes;
	private final boolean _hide;
	
	public ExInzoneWaiting(Player player, boolean hide)
	{
		final Instance instance = InstanceManager.getInstance().getPlayerInstance(player, false);
		_currentTemplateId = ((instance != null) && (instance.getTemplateId() >= 0)) ? instance.getTemplateId() : -1;
		_instanceTimes = InstanceManager.getInstance().getAllInstanceTimes(player);
		_hide = hide;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_INZONE_WAITING_INFO.writeId(this);
		writeByte(!_hide); // Grand Crusade
		writeInt(_currentTemplateId);
		writeInt(_instanceTimes.size());
		for (Entry<Integer, Long> entry : _instanceTimes.entrySet())
		{
			final long instanceTime = TimeUnit.MILLISECONDS.toSeconds(entry.getValue() - System.currentTimeMillis());
			writeInt(entry.getKey());
			writeInt((int) instanceTime);
		}
	}
}
