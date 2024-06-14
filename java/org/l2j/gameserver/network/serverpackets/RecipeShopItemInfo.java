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
package org.l2j.gameserver.network.serverpackets;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.stats.Stat;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;

public class RecipeShopItemInfo extends ServerPacket
{
	private final Player _player;
	private final int _recipeId;
	private final double _craftRate;
	private final double _craftCritical;
	
	public RecipeShopItemInfo(Player player, int recipeId)
	{
		_player = player;
		_recipeId = recipeId;
		_craftRate = _player.getStat().getValue(Stat.CRAFT_RATE, 0);
		_craftCritical = _player.getStat().getValue(Stat.CRAFTING_CRITICAL, 0);
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.RECIPE_SHOP_ITEM_INFO.writeId(this, buffer);
		buffer.writeInt(_player.getObjectId());
		buffer.writeInt(_recipeId);
		buffer.writeInt((int) _player.getCurrentMp());
		buffer.writeInt(_player.getMaxMp());
		buffer.writeInt(0xffffffff); // item creation none/success/failed
		buffer.writeLong(0); // manufacturePrice
		buffer.writeByte(0); // Trigger offering window if 1
		buffer.writeLong(0); // Adena worth of items for maximum offering.
		buffer.writeDouble(Math.min(_craftRate, 100.0));
		buffer.writeByte(_craftCritical > 0);
		buffer.writeDouble(Math.min(_craftCritical, 100.0));
		buffer.writeByte(0); // find me
	}
}
