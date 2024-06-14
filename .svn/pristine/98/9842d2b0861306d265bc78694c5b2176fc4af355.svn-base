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
package org.l2jmobius.gameserver.network.serverpackets.limitshop;

import java.util.Collection;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.model.holders.LimitShopRandomCraftReward;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Gustavo Fonseca
 */
public class ExPurchaseLimitShopItemResult extends ServerPacket
{
	private final int _category, _productId;
	private final boolean _isSuccess;
	private final int _remainingInfo;
	private final Collection<LimitShopRandomCraftReward> _rewards;
	
	public ExPurchaseLimitShopItemResult(boolean isSuccess, int category, int productId, int remainingInfo, Collection<LimitShopRandomCraftReward> rewards)
	{
		_isSuccess = isSuccess;
		_category = category;
		_productId = productId;
		_remainingInfo = remainingInfo;
		_rewards = rewards;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_PURCHASE_LIMIT_SHOP_ITEM_BUY.writeId(this, buffer);
		buffer.writeByte(_isSuccess ? 0 : 1);
		buffer.writeByte(_category);
		buffer.writeInt(_productId);
		buffer.writeInt(_rewards.size());
		for (LimitShopRandomCraftReward entry : _rewards)
		{
			buffer.writeByte(entry.getRewardIndex());
			buffer.writeInt(entry.getItemId());
			buffer.writeInt(entry.getCount().get());
		}
		buffer.writeInt(_remainingInfo);
	}
}