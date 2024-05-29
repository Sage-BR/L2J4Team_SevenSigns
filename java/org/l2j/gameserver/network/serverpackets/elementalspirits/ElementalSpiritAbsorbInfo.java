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
package org.l2j.gameserver.network.serverpackets.elementalspirits;

import java.util.List;

import org.l2j.commons.util.CommonUtil;
import org.l2j.gameserver.enums.ElementalType;
import org.l2j.gameserver.model.ElementalSpirit;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.ElementalSpiritAbsorbItemHolder;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

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
	public void write()
	{
		ServerPackets.EX_ELEMENTAL_SPIRIT_ABSORB_INFO.writeId(this);
		final ElementalSpirit spirit = _player.getElementalSpirit(ElementalType.of(_type));
		if (spirit == null)
		{
			writeByte(0);
			writeByte(0);
			return;
		}
		writeByte(1);
		writeByte(_type);
		writeByte(spirit.getStage());
		writeLong(spirit.getExperience());
		writeLong(spirit.getExperienceToNextLevel()); // NextExp
		writeLong(spirit.getExperienceToNextLevel()); // MaxExp
		writeInt(spirit.getLevel());
		writeInt(spirit.getMaxLevel());
		final List<ElementalSpiritAbsorbItemHolder> absorbItems = spirit.getAbsorbItems();
		writeInt(absorbItems.size()); // AbsorbCount
		for (ElementalSpiritAbsorbItemHolder absorbItem : absorbItems)
		{
			writeInt(absorbItem.getId());
			writeInt(CommonUtil.zeroIfNullOrElse(_player.getInventory().getItemByItemId(absorbItem.getId()), item -> (int) item.getCount()));
			writeInt(absorbItem.getExperience());
		}
	}
}
