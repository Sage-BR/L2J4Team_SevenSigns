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

import java.util.List;

import org.l2j.gameserver.instancemanager.CastleManorManager;
import org.l2j.gameserver.model.Seed;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author l3x
 */
public class ExShowManorDefaultInfo extends ServerPacket
{
	private final List<Seed> _crops;
	private final boolean _hideButtons;
	
	public ExShowManorDefaultInfo(boolean hideButtons)
	{
		_crops = CastleManorManager.getInstance().getCrops();
		_hideButtons = hideButtons;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_SHOW_MANOR_DEFAULT_INFO.writeId(this);
		writeByte(_hideButtons); // Hide "Seed Purchase" and "Crop Sales" buttons
		writeInt(_crops.size());
		for (Seed crop : _crops)
		{
			writeInt(crop.getCropId()); // crop Id
			writeInt(crop.getLevel()); // level
			writeInt(crop.getSeedReferencePrice()); // seed price
			writeInt(crop.getCropReferencePrice()); // crop price
			writeByte(1); // Reward 1 type
			writeInt(crop.getReward(1)); // Reward 1 itemId
			writeByte(1); // Reward 2 type
			writeInt(crop.getReward(2)); // Reward 2 itemId
		}
	}
}