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
import org.l2j.gameserver.model.SeedProduction;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author l3x
 */
public class ExShowSeedInfo extends ServerPacket
{
	private final List<SeedProduction> _seeds;
	private final int _manorId;
	private final boolean _hideButtons;
	
	public ExShowSeedInfo(int manorId, boolean nextPeriod, boolean hideButtons)
	{
		_manorId = manorId;
		_hideButtons = hideButtons;
		final CastleManorManager manor = CastleManorManager.getInstance();
		_seeds = (nextPeriod && !manor.isManorApproved()) ? null : manor.getSeedProduction(manorId, nextPeriod);
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_SHOW_SEED_INFO.writeId(this);
		writeByte(_hideButtons); // Hide "Seed Purchase" button
		writeInt(_manorId); // Manor ID
		writeInt(0); // Unknown
		if (_seeds == null)
		{
			writeInt(0);
			return;
		}
		writeInt(_seeds.size());
		for (SeedProduction seed : _seeds)
		{
			writeInt(seed.getId()); // Seed id
			writeLong(seed.getAmount()); // Left to buy
			writeLong(seed.getStartAmount()); // Started amount
			writeLong(seed.getPrice()); // Sell Price
			final Seed s = CastleManorManager.getInstance().getSeed(seed.getId());
			if (s == null)
			{
				writeInt(0); // Seed level
				writeByte(1); // Reward 1
				writeInt(0); // Reward 1 - item id
				writeByte(1); // Reward 2
				writeInt(0); // Reward 2 - item id
			}
			else
			{
				writeInt(s.getLevel()); // Seed level
				writeByte(1); // Reward 1
				writeInt(s.getReward(1)); // Reward 1 - item id
				writeByte(1); // Reward 2
				writeInt(s.getReward(2)); // Reward 2 - item id
			}
		}
	}
}