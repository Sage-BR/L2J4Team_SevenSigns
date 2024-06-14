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
import org.l2jmobius.commons.util.CommonUtil;
import org.l2jmobius.gameserver.enums.ElementalType;
import org.l2jmobius.gameserver.model.ElementalSpirit;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.ElementalSpiritAbsorbItemHolder;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

/**
 * @author JoeAlisson
 */
public class ElementalSpiritAbsorbInfo extends ServerPacket
{
	private final Player _player;
	private final byte _type;
	
	public ElementalSpiritAbsorbInfo(Player player, byte type)
	{
		_player = player;
		_type = type;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_ELEMENTAL_SPIRIT_ABSORB_INFO.writeId(this, buffer);
		final ElementalSpirit spirit = _player.getElementalSpirit(ElementalType.of(_type));
		if (spirit == null)
		{
			buffer.writeByte(0);
			buffer.writeByte(0);
			return;
		}
		buffer.writeByte(1);
		buffer.writeByte(_type);
		buffer.writeByte(spirit.getStage());
		buffer.writeLong(spirit.getExperience());
		buffer.writeLong(spirit.getExperienceToNextLevel()); // NextExp
		buffer.writeLong(spirit.getExperienceToNextLevel()); // MaxExp
		buffer.writeInt(spirit.getLevel());
		buffer.writeInt(spirit.getMaxLevel());
		final List<ElementalSpiritAbsorbItemHolder> absorbItems = spirit.getAbsorbItems();
		buffer.writeInt(absorbItems.size()); // AbsorbCount
		for (ElementalSpiritAbsorbItemHolder absorbItem : absorbItems)
		{
			buffer.writeInt(absorbItem.getId());
			buffer.writeInt(CommonUtil.zeroIfNullOrElse(_player.getInventory().getItemByItemId(absorbItem.getId()), item -> (int) item.getCount()));
			buffer.writeInt(absorbItem.getExperience());
		}
	}
}
