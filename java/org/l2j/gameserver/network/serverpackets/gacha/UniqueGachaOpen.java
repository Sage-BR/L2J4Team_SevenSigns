/*
 * This file is part of the L2J 4Team Project.
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
package org.l2j.gameserver.network.serverpackets.gacha;

import java.util.Map.Entry;
import java.util.Set;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.instancemanager.events.UniqueGachaManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.GachaItemHolder;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

public class UniqueGachaOpen extends ServerPacket
{
	private final static int SHORT_PACKET_INFO = 2 + 1 + 4 + 8;
	
	private final Player _player;
	private final int _fullInfo;
	private final int _openMode;
	
	public UniqueGachaOpen(Player player, int fullInfo, int openMode)
	{
		_player = player;
		_fullInfo = fullInfo;
		_openMode = openMode;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		final UniqueGachaManager manager = UniqueGachaManager.getInstance();
		ServerPackets.EX_UNIQUE_GACHA_OPEN.writeId(this, buffer);
		/**
		 * open mode 1 = const GACHA_TITLE_FORM = 1; = will open main window?
		 */
		buffer.writeByte(_openMode); // open mode // char // 1 = main menu
		buffer.writeByte(0); // result // char // not used?
		buffer.writeInt(manager.getCurrencyCount(_player)); // my cost item amount // int // current item count for roll?
		buffer.writeInt(manager.getStepsToGuaranteedReward(_player)); // my confirm count // int // left to guarant
		buffer.writeInt((int) ((manager.getActiveUntilPeriod() - System.currentTimeMillis()) / 1000L)); // remain time in seconds // int
		buffer.writeByte(_fullInfo); // full info // char
		buffer.writeByte(manager.isShowProbability() ? 1 : 0); // show probability // char
		if (_fullInfo == 1)
		{
			writeFullInfo(manager, buffer);
		}
		else
		{
			writeDummyInfo(buffer);
		}
	}
	
	private void writeFullInfo(UniqueGachaManager manager, WritableBuffer buffer)
	{
		buffer.writeInt(manager.getVisibleItems().size()); // show items size // int
		for (GachaItemHolder item : manager.getVisibleItems())
		{
			buffer.writeShort(SHORT_PACKET_INFO); // current size // short
			buffer.writeByte(item.getRank().getClientId()); // rank type // char
			buffer.writeInt(item.getId()); // item type // int
			buffer.writeLong(item.getCount()); // amount // long
			buffer.writeDouble(getChance(manager.isShowProbability(), item.getItemChance())); // probability // double
		}
		buffer.writeInt(manager.getTotalRewardCount()); // reward item size // int
		for (Set<GachaItemHolder> items : manager.getRewardItems().values())
		{
			for (GachaItemHolder item : items)
			{
				buffer.writeShort(SHORT_PACKET_INFO); // current size // short
				buffer.writeByte(item.getRank().getClientId()); // rank type // char
				buffer.writeInt(item.getId()); // item type // int
				buffer.writeLong(item.getCount()); // amount // long
				buffer.writeDouble(getChance(manager.isShowProbability(), item.getItemChance())); // probability // double
			}
		}
		buffer.writeByte(1); // cost type // char // bool?
		buffer.writeInt(manager.getCurrencyItemId()); // cost item type // int // item id
		buffer.writeInt(manager.getGameCosts().size()); // cost amount item info // int
		for (Entry<Integer, Long> entry : manager.getGameCosts().entrySet())
		{
			buffer.writeInt(entry.getKey()); // game count // int
			buffer.writeLong(entry.getValue()); // amount // long
		}
	}
	
	private void writeDummyInfo(WritableBuffer buffer)
	{
		buffer.writeInt(0); // show items size // int
		buffer.writeInt(0); // reward item size // int
		buffer.writeByte(0); // cost type // char // bool?
		buffer.writeInt(0); // cost item type // int // item id
		buffer.writeInt(0); // cost amount item info // int
	}
	
	private double getChance(boolean showChance, int itemChance)
	{
		return showChance ? (double) ((double) itemChance / (double) UniqueGachaManager.MINIMUM_CHANCE) : 0;
	}
}
