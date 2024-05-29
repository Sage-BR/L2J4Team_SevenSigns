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

import org.l2j.gameserver.model.ElementalSpirit;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author JoeAlisson
 */
abstract class AbstractElementalSpiritPacket extends ServerPacket
{
	void writeSpiritInfo(ElementalSpirit spirit)
	{
		writeByte(spirit.getStage());
		writeInt(spirit.getNpcId());
		writeLong(spirit.getExperience());
		writeLong(spirit.getExperienceToNextLevel());
		writeLong(spirit.getExperienceToNextLevel());
		writeInt(spirit.getLevel());
		writeInt(spirit.getMaxLevel());
		writeInt(spirit.getAvailableCharacteristicsPoints());
		writeInt(spirit.getAttackPoints());
		writeInt(spirit.getDefensePoints());
		writeInt(spirit.getCriticalRatePoints());
		writeInt(spirit.getCriticalDamagePoints());
		writeInt(spirit.getMaxCharacteristics());
		writeInt(spirit.getMaxCharacteristics());
		writeInt(spirit.getMaxCharacteristics());
		writeInt(spirit.getMaxCharacteristics());
		writeByte(1); // unk
		for (int j = 0; j < 1; j++)
		{
			writeShort(2);
			writeLong(100);
		}
	}
}
