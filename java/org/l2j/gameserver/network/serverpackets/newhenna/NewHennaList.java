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

package org.l2j.gameserver.network.serverpackets.newhenna;

import java.util.ArrayList;
import java.util.List;

import org.l2j.gameserver.data.xml.HennaPatternPotentialData;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.ItemHolder;
import org.l2j.gameserver.model.item.henna.Henna;
import org.l2j.gameserver.model.item.henna.HennaPoten;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Index, Serenitty
 */
public class NewHennaList extends ServerPacket
{
	private final HennaPoten[] _hennaId;
	private final int _dailyStep;
	private final int _dailyCount;
	private final int _availableSlots;
	private final int _resetCount;
	private final int _sendType;
	private List<ItemHolder> _resetData = new ArrayList<>();
	
	public NewHennaList(Player player, int sendType)
	{
		_dailyStep = player.getDyePotentialDailyStep();
		_dailyCount = player.getDyePotentialDailyCount();
		_hennaId = player.getHennaPotenList();
		_availableSlots = player.getAvailableHennaSlots();
		_resetCount = player.getDyePotentialDailyEnchantReset();
		_resetData = HennaPatternPotentialData.getInstance().getEnchantReset();
		_sendType = sendType;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_NEW_HENNA_LIST.writeId(this);
		writeByte(_sendType); // SendType
		writeShort(_dailyStep);
		writeShort(_dailyCount);
		writeShort(_resetCount + 1); // ResetCount
		writeShort(_resetData.size()); // ResetMaxCount
		
		final ItemHolder resetInfo = _resetData.get(_resetCount);
		if (resetInfo != null)
		{
			writeInt(1);
			writeInt(resetInfo.getId());
			writeLong(resetInfo.getCount());
		}
		else
		{
			writeInt(0);
		}
		
		// hennaInfoList
		writeInt(_hennaId.length);
		for (int i = 1; i <= _hennaId.length; i++)
		{
			final HennaPoten hennaPoten = _hennaId[i - 1];
			final Henna henna = _hennaId[i - 1].getHenna();
			writeInt(henna != null ? henna.getDyeId() : 0);
			writeInt(hennaPoten.getPotenId());
			writeByte(i != _availableSlots);
			writeShort(hennaPoten.getEnchantLevel());
			writeInt(hennaPoten.getEnchantExp());
			writeShort(hennaPoten.getActiveStep());
			writeShort(_dailyStep);
			writeShort(_dailyCount);
		}
	}
}
