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
package org.l2j.gameserver.network.serverpackets.worldexchange;

import java.util.Collections;
import java.util.List;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.enums.AttributeType;
import org.l2j.gameserver.enums.WorldExchangeItemSubType;
import org.l2j.gameserver.model.VariationInstance;
import org.l2j.gameserver.model.ensoul.EnsoulOption;
import org.l2j.gameserver.model.holders.WorldExchangeHolder;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Index
 */
public class WorldExchangeItemList extends ServerPacket
{
	public static final WorldExchangeItemList EMPTY_LIST = new WorldExchangeItemList(Collections.emptyList(), null);
	
	private final List<WorldExchangeHolder> _holders;
	private final WorldExchangeItemSubType _type;
	
	public WorldExchangeItemList(List<WorldExchangeHolder> holders, WorldExchangeItemSubType type)
	{
		_holders = holders;
		_type = type;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		if (_holders.isEmpty())
		{
			buffer.writeShort(0); // Category
			buffer.writeByte(0); // SortType
			buffer.writeInt(0); // Page
			buffer.writeInt(0); // ItemIDList
			return;
		}
		
		ServerPackets.EX_WORLD_EXCHANGE_ITEM_LIST.writeId(this, buffer);
		buffer.writeShort(_type.getId());
		buffer.writeByte(0);
		buffer.writeInt(0);
		buffer.writeInt(_holders.size());
		for (WorldExchangeHolder holder : _holders)
		{
			getItemInfo(buffer, holder);
		}
	}
	
	private void getItemInfo(WritableBuffer buffer, WorldExchangeHolder holder)
	{
		buffer.writeLong(holder.getWorldExchangeId());
		buffer.writeLong(holder.getPrice());
		buffer.writeInt((int) (holder.getEndTime() / 1000L));
		Item item = holder.getItemInstance();
		buffer.writeInt(item.getId());
		buffer.writeLong(item.getCount());
		buffer.writeInt(item.getEnchantLevel() < 1 ? 0 : item.getEnchantLevel());
		VariationInstance iv = item.getAugmentation();
		buffer.writeInt(iv != null ? iv.getOption1Id() : 0);
		buffer.writeInt(iv != null ? iv.getOption2Id() : 0);
		buffer.writeInt(-1);
		buffer.writeShort(item.getAttackAttribute() != null ? item.getAttackAttribute().getType().getClientId() : 0);
		buffer.writeShort(item.getAttackAttribute() != null ? item.getAttackAttribute().getValue() : 0);
		buffer.writeShort(item.getDefenceAttribute(AttributeType.FIRE));
		buffer.writeShort(item.getDefenceAttribute(AttributeType.WATER));
		buffer.writeShort(item.getDefenceAttribute(AttributeType.WIND));
		buffer.writeShort(item.getDefenceAttribute(AttributeType.EARTH));
		buffer.writeShort(item.getDefenceAttribute(AttributeType.HOLY));
		buffer.writeShort(item.getDefenceAttribute(AttributeType.DARK));
		buffer.writeInt(item.getVisualId());
		
		final List<EnsoulOption> soul = (List<EnsoulOption>) holder.getItemInfo().getSoulCrystalOptions();
		try
		{
			buffer.writeInt(soul != null ? soul.get(0).getId() : 0);
		}
		catch (IndexOutOfBoundsException ignored)
		{
			buffer.writeInt(0);
		}
		
		try
		{
			buffer.writeInt(soul != null ? soul.get(1).getId() : 0);
		}
		catch (IndexOutOfBoundsException ignored)
		{
			buffer.writeInt(0);
		}
		
		final List<EnsoulOption> specialSoul = (List<EnsoulOption>) holder.getItemInfo().getSoulCrystalSpecialOptions();
		try
		{
			buffer.writeInt(specialSoul != null ? specialSoul.get(0).getId() : 0);
		}
		catch (IndexOutOfBoundsException ignored)
		{
			buffer.writeInt(0);
		}
		
		buffer.writeShort(item.isBlessed());
	}
}
