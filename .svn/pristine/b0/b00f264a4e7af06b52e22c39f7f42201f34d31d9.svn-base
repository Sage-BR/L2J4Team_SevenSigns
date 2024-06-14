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
package org.l2jmobius.gameserver.network.serverpackets.elementalspirits;

import java.util.List;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.enums.ElementalType;
import org.l2jmobius.gameserver.model.ElementalSpirit;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.ItemHolder;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

/**
 * @author JoeAlisson
 */
public class ElementalSpiritEvolutionInfo extends ServerPacket
{
	private final Player _player;
	private final byte _type;
	
	public ElementalSpiritEvolutionInfo(Player player, byte type)
	{
		_player = player;
		_type = type;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_ELEMENTAL_SPIRIT_EVOLUTION_INFO.writeId(this, buffer);
		final ElementalSpirit spirit = _player.getElementalSpirit(ElementalType.of(_type));
		if (spirit == null)
		{
			buffer.writeByte(0);
			buffer.writeInt(0);
			return;
		}
		buffer.writeByte(_type);
		buffer.writeInt(spirit.getNpcId());
		buffer.writeInt(1); // unk
		buffer.writeInt(spirit.getStage());
		buffer.writeDouble(100); // chance ??
		final List<ItemHolder> items = spirit.getItemsToEvolve();
		buffer.writeInt(items.size());
		for (ItemHolder item : items)
		{
			buffer.writeInt(item.getId());
			buffer.writeLong(item.getCount());
		}
	}
}
