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
package org.l2j.gameserver.network.clientpackets.gacha;

import java.util.List;
import java.util.Map.Entry;

import org.l2j.gameserver.instancemanager.events.UniqueGachaManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.GachaItemHolder;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.gacha.UniqueGachaGame;
import org.l2j.gameserver.network.serverpackets.gacha.UniqueGachaInvenAddItem;

public class ExUniqueGachaGame extends ClientPacket
{
	private int _gameCount;
	
	@Override
	protected void readImpl()
	{
		_gameCount = readInt();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		final Entry<List<GachaItemHolder>, Boolean> pair = UniqueGachaManager.getInstance().tryToRoll(player, _gameCount);
		final List<GachaItemHolder> rewards = pair.getKey();
		final boolean rare = pair.getValue().booleanValue();
		player.sendPacket(new UniqueGachaGame(rewards.isEmpty() ? UniqueGachaGame.FAILURE : UniqueGachaGame.SUCCESS, player, rewards, rare));
		player.sendPacket(new UniqueGachaInvenAddItem(rewards));
	}
}