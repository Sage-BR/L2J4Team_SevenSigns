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

import java.util.Collection;

import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.model.holders.DamageTakenHolder;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.network.ServerPackets;

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
	public void write()
	{
		ServerPackets.EX_DIE_INFO.writeId(this);
		writeShort(_droppedItems.size());
		for (Item item : _droppedItems)
		{
			writeInt(item.getId());
			writeInt(item.getEnchantLevel());
			writeInt((int) item.getCount());
		}
		writeShort(_lastDamageTaken.size());
		for (DamageTakenHolder damageHolder : _lastDamageTaken)
		{
			if (damageHolder.getCreature().isNpc())
			{
				writeShort(1);
				writeInt(damageHolder.getCreature().getId());
				writeString("");
			}
			else
			{
				final Clan clan = damageHolder.getCreature().getClan();
				writeShort(0);
				writeString(damageHolder.getCreature().getName());
				writeString(clan == null ? "" : clan.getName());
			}
			writeInt(damageHolder.getSkillId());
			writeDouble(damageHolder.getDamage());
			writeShort(0); // damage type
		}
	}
}
