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
package org.l2jmobius.gameserver.network.serverpackets.balok;

import java.util.concurrent.TimeUnit;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.instancemanager.GlobalVariablesManager;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Mobius, Serenitty
 */
public class BalrogWarHud extends ServerPacket
{
	private final int _state;
	private final int _stage;
	
	public BalrogWarHud(int state, int stage)
	{
		_state = state;
		_stage = stage;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_BALROGWAR_HUD.writeId(this, buffer);
		final long remainTime = GlobalVariablesManager.getInstance().getLong(GlobalVariablesManager.BALOK_REMAIN_TIME, 0);
		final long currentTime = System.currentTimeMillis();
		buffer.writeInt(_state); // State
		buffer.writeInt(_stage); // Progress Step
		buffer.writeInt((int) TimeUnit.MILLISECONDS.toSeconds(remainTime - currentTime)); // Time (in seconds)
	}
}
