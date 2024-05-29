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
package org.l2j.gameserver.network.serverpackets.castlewar;

import org.l2j.gameserver.enums.TaxType;
import org.l2j.gameserver.instancemanager.CastleManager;
import org.l2j.gameserver.model.siege.Castle;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Serenitty, Mobius
 */
public class MercenaryCastleWarCastleInfo extends ServerPacket
{
	private final int _castleId;
	
	public MercenaryCastleWarCastleInfo(int castleId)
	{
		_castleId = castleId;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_MERCENARY_CASTLEWAR_CASTLE_INFO.writeId(this);
		
		final Castle castle = CastleManager.getInstance().getCastleById(_castleId);
		if (castle == null)
		{
			writeInt(_castleId);
			writeInt(0);
			writeInt(0);
			writeSizedString("");
			writeSizedString("");
			writeInt(0);
			writeLong(0);
			writeLong(0);
			writeInt(0);
			return;
		}
		
		writeInt(castle.getResidenceId());
		writeInt(castle.getOwner() != null ? castle.getOwner().getCrestId() : 0); // CastleOwnerPledgeSID
		writeInt(castle.getOwner() != null ? castle.getOwner().getCrestLargeId() : 0); // CastleOwnerPledgeCrestDBID
		writeSizedString(castle.getOwner() != null ? castle.getOwner().getName() : "-"); // CastleOwnerPledgeName
		writeSizedString(castle.getOwner() != null ? castle.getOwner().getLeaderName() : "-"); // CastleOwnerPledgeMasterName
		writeInt(castle.getTaxPercent(TaxType.BUY)); // CastleTaxRate
		writeLong((long) (castle.getTreasury() * castle.getTaxRate(TaxType.BUY))); // CurrentIncome
		writeLong((long) (castle.getTreasury() + (castle.getTreasury() * castle.getTaxRate(TaxType.BUY)))); // TotalIncome
		writeInt(castle.getSiegeDate() != null ? (int) (castle.getSiegeDate().getTimeInMillis() / 1000) : 0); // NextSiegeTime
	}
}
