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
package org.l2j.gameserver.network.serverpackets.gacha;

import java.util.List;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.instancemanager.events.UniqueGachaManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.GachaItemHolder;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

public class UniqueGachaGame extends ServerPacket
{
	public static final int FAILURE = 0;
	public static final int SUCCESS = 1;
	
	private final int _success;
	private final Player _player;
	private final List<GachaItemHolder> _rewards;
	private final boolean _rare;
	
	public UniqueGachaGame(int success, Player player, List<GachaItemHolder> rewards, boolean rare)
	{
		_success = success;
		_player = player;
		_rewards = rewards;
		_rare = rare;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		final UniqueGachaManager manager = UniqueGachaManager.getInstance();
		ServerPackets.EX_UNIQUE_GACHA_GAME.writeId(this, buffer);
		buffer.writeByte(_success); // result // char
		buffer.writeInt(manager.getCurrencyCount(_player)); // amount // int
		buffer.writeInt(manager.getStepsToGuaranteedReward(_player)); // guaruant // int
		// 0 - yellow
		// 1 - purple
		buffer.writeByte(_rare ? 1 : 0); // rank // char
		buffer.writeInt(_rewards.size()); // size // int
		for (GachaItemHolder item : _rewards)
		{
			buffer.writeByte(item.getRank().getClientId()); // rank // char
			buffer.writeInt(item.getId()); // itemId // int
			buffer.writeLong(item.getCount()); // count // long
		}
	}
}
