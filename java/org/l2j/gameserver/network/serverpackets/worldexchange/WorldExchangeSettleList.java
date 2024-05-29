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
package org.l2j.gameserver.network.serverpackets.worldexchange;

import java.util.List;
import java.util.Map;

import org.l2j.gameserver.enums.AttributeType;
import org.l2j.gameserver.enums.WorldExchangeItemStatusType;
import org.l2j.gameserver.instancemanager.WorldExchangeManager;
import org.l2j.gameserver.model.VariationInstance;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.ensoul.EnsoulOption;
import org.l2j.gameserver.model.holders.WorldExchangeHolder;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Index
 */
public class WorldExchangeSettleList extends ServerPacket
{
	private final Player _player;
	
	public WorldExchangeSettleList(Player player)
	{
		_player = player;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_WORLD_EXCHANGE_SETTLE_LIST.writeId(this);
		
		final Map<WorldExchangeItemStatusType, List<WorldExchangeHolder>> holders = WorldExchangeManager.getInstance().getPlayerBids(_player.getObjectId());
		if (holders.isEmpty())
		{
			writeInt(0); // RegiItemDataList
			writeInt(0); // RecvItemDataList
			writeInt(0); // TimeOutItemDataList
			return;
		}
		
		writeInt(holders.get(WorldExchangeItemStatusType.WORLD_EXCHANGE_REGISTERED).size());
		for (WorldExchangeHolder holder : holders.get(WorldExchangeItemStatusType.WORLD_EXCHANGE_REGISTERED))
		{
			getItemInfo(holder);
		}
		
		writeInt(holders.get(WorldExchangeItemStatusType.WORLD_EXCHANGE_SOLD).size());
		for (WorldExchangeHolder holder : holders.get(WorldExchangeItemStatusType.WORLD_EXCHANGE_SOLD))
		{
			getItemInfo(holder);
		}
		
		writeInt(holders.get(WorldExchangeItemStatusType.WORLD_EXCHANGE_OUT_TIME).size());
		for (WorldExchangeHolder holder : holders.get(WorldExchangeItemStatusType.WORLD_EXCHANGE_OUT_TIME))
		{
			getItemInfo(holder);
		}
	}
	
	private void getItemInfo(WorldExchangeHolder holder)
	{
		writeLong(holder.getWorldExchangeId());
		writeLong(holder.getPrice());
		writeInt((int) (holder.getEndTime() / 1000L));
		Item item = holder.getItemInstance();
		writeInt(item.getId());
		writeLong(item.getCount());
		writeInt(item.getEnchantLevel() < 1 ? 0 : item.getEnchantLevel());
		VariationInstance iv = item.getAugmentation();
		writeInt(iv != null ? iv.getOption1Id() : 0);
		writeInt(iv != null ? iv.getOption2Id() : 0);
		writeInt(-1); // IntensiveItemClassID
		writeShort(item.getAttackAttribute() != null ? item.getAttackAttribute().getType().getClientId() : 0);
		writeShort(item.getAttackAttribute() != null ? item.getAttackAttribute().getValue() : 0);
		writeShort(item.getDefenceAttribute(AttributeType.FIRE));
		writeShort(item.getDefenceAttribute(AttributeType.WATER));
		writeShort(item.getDefenceAttribute(AttributeType.WIND));
		writeShort(item.getDefenceAttribute(AttributeType.EARTH));
		writeShort(item.getDefenceAttribute(AttributeType.HOLY));
		writeShort(item.getDefenceAttribute(AttributeType.DARK));
		writeInt(item.getVisualId());
		
		final List<EnsoulOption> soul = (List<EnsoulOption>) holder.getItemInfo().getSoulCrystalOptions();
		try
		{
			writeInt(soul.get(0).getId());
		}
		catch (IndexOutOfBoundsException ignored)
		{
			writeInt(0);
		}
		
		try
		{
			writeInt(soul.get(1).getId());
		}
		catch (IndexOutOfBoundsException ignored)
		{
			writeInt(0);
		}
		
		final List<EnsoulOption> specialSoul = (List<EnsoulOption>) holder.getItemInfo().getSoulCrystalSpecialOptions();
		try
		{
			writeInt(specialSoul.get(0).getId());
		}
		catch (IndexOutOfBoundsException ignored)
		{
			writeInt(0);
		}
		
		writeShort(item.isBlessed());
	}
}
