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
package org.l2jmobius.gameserver.network.serverpackets;

import java.util.Collection;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.model.clan.Clan;
import org.l2jmobius.gameserver.model.holders.DamageTakenHolder;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;

/**
 * @author Mobius
 */
public class ExDieInfo extends ServerPacket
{
	private final Collection<Item> _droppedItems;
	private final Collection<DamageTakenHolder> _lastDamageTaken;
	
	public ExDieInfo(Collection<Item> droppedItems, Collection<DamageTakenHolder> lastDamageTaken)
	{
		_droppedItems = droppedItems;
		_lastDamageTaken = lastDamageTaken;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_DIE_INFO.writeId(this, buffer);
		buffer.writeShort(_droppedItems.size());
		for (Item item : _droppedItems)
		{
			buffer.writeInt(item.getId());
			buffer.writeInt(item.getEnchantLevel());
			buffer.writeInt((int) item.getCount());
		}
		buffer.writeShort(_lastDamageTaken.size());
		for (DamageTakenHolder damageHolder : _lastDamageTaken)
		{
			if (damageHolder.getCreature().isNpc())
			{
				buffer.writeShort(1);
				buffer.writeInt(damageHolder.getCreature().getId());
				buffer.writeShort(0);
			}
			else
			{
				final Clan clan = damageHolder.getCreature().getClan();
				buffer.writeShort(2);
				buffer.writeString(damageHolder.getCreature().getName());
				buffer.writeString(clan == null ? "" : clan.getName());
			}
			buffer.writeInt(damageHolder.getSkillId());
			buffer.writeDouble(damageHolder.getDamage());
			buffer.writeShort(damageHolder.getClientId());
		}
	}
}
