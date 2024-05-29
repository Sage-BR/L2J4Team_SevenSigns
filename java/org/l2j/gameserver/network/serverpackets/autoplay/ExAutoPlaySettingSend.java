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
package org.l2j.gameserver.network.serverpackets.autoplay;

import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author JoeAlisson
 */
public class ExAutoPlaySettingSend extends ServerPacket
{
	private final int _options;
	private final boolean _active;
	private final boolean _pickUp;
	private final int _nextTargetMode;
	private final boolean _shortRange;
	private final int _potionPercent;
	private final boolean _respectfulHunting;
	private final int _petPotionPercent;
	
	public ExAutoPlaySettingSend(int options, boolean active, boolean pickUp, int nextTargetMode, boolean shortRange, int potionPercent, boolean respectfulHunting, int petPotionPercent)
	{
		_options = options;
		_active = active;
		_pickUp = pickUp;
		_nextTargetMode = nextTargetMode;
		_shortRange = shortRange;
		_potionPercent = potionPercent;
		_respectfulHunting = respectfulHunting;
		_petPotionPercent = petPotionPercent;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_AUTOPLAY_SETTING.writeId(this);
		writeShort(_options);
		writeByte(_active);
		writeByte(_pickUp);
		writeShort(_nextTargetMode);
		writeByte(_shortRange);
		writeInt(_potionPercent);
		writeInt(_petPotionPercent); // 272
		writeByte(_respectfulHunting);
	}
}
