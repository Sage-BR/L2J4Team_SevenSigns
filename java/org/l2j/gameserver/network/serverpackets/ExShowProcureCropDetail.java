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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.l2j.gameserver.instancemanager.CastleManager;
import org.l2j.gameserver.instancemanager.CastleManorManager;
import org.l2j.gameserver.model.CropProcure;
import org.l2j.gameserver.model.siege.Castle;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author l3x
 */
public class ExShowProcureCropDetail extends ServerPacket
{
	private final int _cropId;
	private final Map<Integer, CropProcure> _castleCrops = new HashMap<>();
	
	public ExShowProcureCropDetail(int cropId)
	{
		_cropId = cropId;
		for (Castle c : CastleManager.getInstance().getCastles())
		{
			final CropProcure cropItem = CastleManorManager.getInstance().getCropProcure(c.getResidenceId(), cropId, false);
			if ((cropItem != null) && (cropItem.getAmount() > 0))
			{
				_castleCrops.put(c.getResidenceId(), cropItem);
			}
		}
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_SHOW_PROCURE_CROP_DETAIL.writeId(this);
		writeInt(_cropId); // crop id
		writeInt(_castleCrops.size()); // size
		for (Entry<Integer, CropProcure> entry : _castleCrops.entrySet())
		{
			final CropProcure crop = entry.getValue();
			writeInt(entry.getKey()); // manor name
			writeLong(crop.getAmount()); // buy residual
			writeLong(crop.getPrice()); // buy price
			writeByte(crop.getReward()); // reward type
		}
	}
}
