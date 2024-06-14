/*
 * This file is part of the L2J Mobius project.
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
package org.l2jmobius.gameserver.network.serverpackets.enchant.single;

import static org.l2jmobius.gameserver.model.stats.Stat.ENCHANT_RATE;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.data.xml.EnchantItemData;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.actor.request.EnchantItemRequest;
import org.l2jmobius.gameserver.model.item.enchant.EnchantScroll;
import org.l2jmobius.gameserver.model.item.type.CrystalType;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Index
 */
public class ChangedEnchantTargetItemProbabilityList extends ServerPacket
{
	private final Player _player;
	private final boolean _isMulti;
	
	public ChangedEnchantTargetItemProbabilityList(Player player, Boolean isMulti)
	{
		_player = player;
		_isMulti = isMulti;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		if (_player.getRequest(EnchantItemRequest.class) == null)
		{
			return;
		}
		
		final EnchantItemRequest request = _player.getRequest(EnchantItemRequest.class);
		if ((!_isMulti && (request.getEnchantingItem() == null)) || request.isProcessing() || (request.getEnchantingScroll() == null))
		{
			return;
		}
		
		int count = 1;
		if (_isMulti)
		{
			count = request.getMultiEnchantingItemsCount();
		}
		
		ServerPackets.EX_CHANGED_ENCHANT_TARGET_ITEM_PROB_LIST.writeId(this, buffer);
		buffer.writeInt(count);
		for (int i = 1; i <= count; i++)
		{
			// 100,00 % = 10000, because last 2 numbers going after float comma.
			double baseRate;
			double passiveRate;
			if (!_isMulti || (request.getMultiEnchantingItemsBySlot(i) != 0))
			{
				baseRate = getBaseRate(request, i);
				passiveRate = getPassiveRate(request, i);
			}
			else
			{
				baseRate = 0;
				passiveRate = 0;
			}
			double passiveBaseRate = 0;
			final double supportRate = getSupportRate(request);
			if (passiveRate != 0)
			{
				passiveBaseRate = (baseRate * passiveRate) / 10000;
			}
			double totalRate = baseRate + supportRate + passiveBaseRate;
			if (totalRate >= 10000)
			{
				totalRate = 10000;
			}
			if (!_isMulti)
			{
				buffer.writeInt(request.getEnchantingItem().getObjectId());
			}
			else
			{
				buffer.writeInt(request.getMultiEnchantingItemsBySlot(i));
			}
			buffer.writeInt((int) totalRate); // Total success.
			buffer.writeInt((int) baseRate); // Base success.
			buffer.writeInt((int) supportRate); // Support success.
			buffer.writeInt((int) passiveBaseRate); // Passive success (items, skills).
		}
	}
	
	private int getBaseRate(EnchantItemRequest request, int iteration)
	{
		final EnchantScroll enchantScroll = EnchantItemData.getInstance().getEnchantScroll(request.getEnchantingScroll());
		if (enchantScroll == null)
		{
			return 0;
		}
		
		return (int) Math.min(100, enchantScroll.getChance(_player, _isMulti ? _player.getInventory().getItemByObjectId(request.getMultiEnchantingItemsBySlot(iteration)) : request.getEnchantingItem()) + enchantScroll.getBonusRate()) * 100;
	}
	
	private int getSupportRate(EnchantItemRequest request)
	{
		double supportRate = 0;
		if (!_isMulti && (request.getSupportItem() != null))
		{
			supportRate = EnchantItemData.getInstance().getSupportItem(request.getSupportItem()).getBonusRate();
			supportRate = supportRate * 100;
		}
		return (int) supportRate;
	}
	
	private int getPassiveRate(EnchantItemRequest request, int iteration)
	{
		double passiveRate = 0;
		if (_player.getStat().getValue(ENCHANT_RATE) != 0)
		{
			if (!_isMulti)
			{
				final int crystalLevel = request.getEnchantingItem().getTemplate().getCrystalType().getLevel();
				if ((crystalLevel == CrystalType.NONE.getLevel()) || (crystalLevel == CrystalType.EVENT.getLevel()))
				{
					passiveRate = 0;
				}
				else
				{
					passiveRate = _player.getStat().getValue(ENCHANT_RATE);
					passiveRate = passiveRate * 100;
				}
			}
			else
			{
				final int crystalLevel = _player.getInventory().getItemByObjectId(request.getMultiEnchantingItemsBySlot(iteration)).getTemplate().getCrystalType().getLevel();
				if ((crystalLevel == CrystalType.NONE.getLevel()) || (crystalLevel == CrystalType.EVENT.getLevel()))
				{
					passiveRate = 0;
				}
				else
				{
					passiveRate = _player.getStat().getValue(ENCHANT_RATE);
					passiveRate = passiveRate * 100;
				}
			}
		}
		return (int) passiveRate;
	}
}