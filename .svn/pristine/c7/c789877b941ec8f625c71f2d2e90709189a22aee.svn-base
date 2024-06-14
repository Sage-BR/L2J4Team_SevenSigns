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

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.model.ElementalSpirit;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

/**
 * @author JoeAlisson
 */
abstract class AbstractElementalSpiritPacket extends ServerPacket
{
	void writeSpiritInfo(WritableBuffer buffer, ElementalSpirit spirit)
	{
		buffer.writeByte(spirit.getStage());
		buffer.writeInt(spirit.getNpcId());
		buffer.writeLong(spirit.getExperience());
		buffer.writeLong(spirit.getExperienceToNextLevel());
		buffer.writeLong(spirit.getExperienceToNextLevel());
		buffer.writeInt(spirit.getLevel());
		buffer.writeInt(spirit.getMaxLevel());
		buffer.writeInt(spirit.getAvailableCharacteristicsPoints());
		buffer.writeInt(spirit.getAttackPoints());
		buffer.writeInt(spirit.getDefensePoints());
		buffer.writeInt(spirit.getCriticalRatePoints());
		buffer.writeInt(spirit.getCriticalDamagePoints());
		buffer.writeInt(spirit.getMaxCharacteristics());
		buffer.writeInt(spirit.getMaxCharacteristics());
		buffer.writeInt(spirit.getMaxCharacteristics());
		buffer.writeInt(spirit.getMaxCharacteristics());
		buffer.writeByte(1); // unk
		for (int j = 0; j < 1; j++)
		{
			buffer.writeShort(2);
			buffer.writeLong(100);
		}
	}
}
