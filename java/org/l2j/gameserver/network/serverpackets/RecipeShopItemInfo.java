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

import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.stats.Stat;
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
	public void write()
	{
		ServerPackets.RECIPE_SHOP_ITEM_INFO.writeId(this);
		writeInt(_player.getObjectId());
		writeInt(_recipeId);
		writeInt((int) _player.getCurrentMp());
		writeInt(_player.getMaxMp());
		writeInt(0xffffffff); // item creation none/success/failed
		writeLong(0); // manufacturePrice
		writeByte(0); // Trigger offering window if 1
		writeLong(0); // Adena worth of items for maximum offering.
		writeDouble(Math.min(_craftRate, 100.0));
		writeByte(_craftCritical > 0);
		writeDouble(Math.min(_craftCritical, 100.0));
		writeByte(0); // find me
	}
}
