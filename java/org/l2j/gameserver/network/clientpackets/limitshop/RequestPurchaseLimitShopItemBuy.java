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
package org.l2j.gameserver.network.clientpackets.limitshop;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.l2j.Config;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.commons.util.Rnd;
import org.l2j.gameserver.data.xml.LimitShopClanData;
import org.l2j.gameserver.data.xml.LimitShopCraftData;
import org.l2j.gameserver.data.xml.LimitShopData;
import org.l2j.gameserver.enums.ExBrProductReplyType;
import org.l2j.gameserver.enums.SpecialItemType;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.request.PrimeShopRequest;
import org.l2j.gameserver.model.holders.LimitShopProductHolder;
import org.l2j.gameserver.model.holders.LimitShopRandomCraftReward;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.itemcontainer.Inventory;
import org.l2j.gameserver.model.variables.AccountVariables;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.ExItemAnnounce;
import org.l2j.gameserver.network.serverpackets.ExPCCafePointInfo;
import org.l2j.gameserver.network.serverpackets.limitshop.ExPurchaseLimitShopItemResult;
import org.l2j.gameserver.network.serverpackets.primeshop.ExBRBuyProduct;
import org.l2j.gameserver.util.Broadcast;

/**
 * @author Mobius
 */
public class RequestPurchaseLimitShopItemBuy implements ClientPacket
{
	private int _shopIndex;
	private int _productId;
	private int _amount;
	private LimitShopProductHolder _product;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_shopIndex = packet.readByte(); // 3 Lcoin Store, 4 Special Craft, 100 Clan Shop
		_productId = packet.readInt();
		_amount = packet.readInt();
		
		switch (_shopIndex)
		{
			case 3: // Normal Lcoin Shop
			{
				_product = LimitShopData.getInstance().getProduct(_productId);
				break;
			}
			case 4: // Lcoin Special Craft
			{
				_product = LimitShopCraftData.getInstance().getProduct(_productId);
				break;
			}
			case 100: // Clan Shop
			{
				_product = LimitShopClanData.getInstance().getProduct(_productId);
				break;
			}
			default:
			{
				_product = null;
			}
		}
		
		packet.readInt(); // SuccessionItemSID
		packet.readInt(); // MaterialItemSID
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (_product == null)
		{
			return;
		}
		
		if ((_amount < 1) || (_amount > 10000))
		{
			player.sendPacket(new ExBRBuyProduct(ExBrProductReplyType.INVENTORY_OVERFLOW));
			player.sendPacket(new ExPurchaseLimitShopItemResult(false, _shopIndex, _productId, 0, Collections.emptyList()));
			return;
		}
		
		if (!player.isInventoryUnder80(false))
		{
			player.sendPacket(new ExBRBuyProduct(ExBrProductReplyType.INVENTORY_OVERFLOW));
			player.sendPacket(new ExPurchaseLimitShopItemResult(false, _shopIndex, _productId, 0, Collections.emptyList()));
			return;
		}
		
		if ((player.getLevel() < _product.getMinLevel()) || (player.getLevel() > _product.getMaxLevel()))
		{
			player.sendPacket(SystemMessageId.YOUR_LEVEL_CANNOT_PURCHASE_THIS_ITEM);
			player.sendPacket(new ExPurchaseLimitShopItemResult(false, _shopIndex, _productId, 0, Collections.emptyList()));
			return;
		}
		
		if (player.hasItemRequest() || player.hasRequest(PrimeShopRequest.class))
		{
			player.sendPacket(new ExBRBuyProduct(ExBrProductReplyType.INVALID_USER_STATE));
			player.sendPacket(new ExPurchaseLimitShopItemResult(false, _shopIndex, _productId, 0, Collections.emptyList()));
			return;
		}
		
		// Add request.
		player.addRequest(new PrimeShopRequest(player));
		
		// Check limits.
		if (_product.getAccountDailyLimit() > 0) // Sale period.
		{
			final long amount = _product.getAccountDailyLimit() * _amount;
			if (amount < 1)
			{
				player.sendPacket(SystemMessageId.INCORRECT_ITEM_COUNT_2);
				player.removeRequest(PrimeShopRequest.class);
				player.sendPacket(new ExPurchaseLimitShopItemResult(false, _shopIndex, _productId, 0, Collections.emptyList()));
				return;
			}
			
			if (player.getAccountVariables().getInt(AccountVariables.LCOIN_SHOP_PRODUCT_DAILY_COUNT + _product.getProductionId(), 0) >= amount)
			{
				player.sendMessage("You have reached your daily limit."); // TODO: Retail system message?
				player.removeRequest(PrimeShopRequest.class);
				player.sendPacket(new ExPurchaseLimitShopItemResult(false, _shopIndex, _productId, 0, Collections.emptyList()));
				return;
			}
		}
		else if (_product.getAccountMontlyLimit() > 0)
		{
			final long amount = _product.getAccountMontlyLimit() * _amount;
			if (amount < 1)
			{
				player.sendPacket(SystemMessageId.INCORRECT_ITEM_COUNT_2);
				player.removeRequest(PrimeShopRequest.class);
				player.sendPacket(new ExPurchaseLimitShopItemResult(false, _shopIndex, _productId, 0, Collections.emptyList()));
				return;
			}
			if (player.getAccountVariables().getInt(AccountVariables.LCOIN_SHOP_PRODUCT_MONTLY_COUNT + _product.getProductionId(), 0) >= amount)
			{
				player.sendMessage("You have reached your montly limit.");
				player.removeRequest(PrimeShopRequest.class);
				player.sendPacket(new ExPurchaseLimitShopItemResult(false, _shopIndex, _productId, 0, Collections.emptyList()));
				return;
			}
			
		}
		else if (_product.getAccountBuyLimit() > 0) // Count limit.
		{
			final long amount = _product.getAccountBuyLimit() * _amount;
			if (amount < 1)
			{
				player.sendPacket(SystemMessageId.INCORRECT_ITEM_COUNT_2);
				player.removeRequest(PrimeShopRequest.class);
				player.sendPacket(new ExPurchaseLimitShopItemResult(false, _shopIndex, _productId, 0, Collections.emptyList()));
				return;
			}
			
			if (player.getAccountVariables().getInt(AccountVariables.LCOIN_SHOP_PRODUCT_COUNT + _product.getProductionId(), 0) >= amount)
			{
				player.sendMessage("You cannot buy any more of this item."); // TODO: Retail system message?
				player.removeRequest(PrimeShopRequest.class);
				player.sendPacket(new ExPurchaseLimitShopItemResult(false, _shopIndex, _productId, 0, Collections.emptyList()));
				return;
			}
		}
		
		// Check existing items.
		final int remainingInfo = Math.max(0, Math.max(_product.getAccountBuyLimit(), Math.max(_product.getAccountDailyLimit(), _product.getAccountMontlyLimit())));
		for (int i = 0; i < _product.getIngredientIds().length; i++)
		{
			if (_product.getIngredientIds()[i] == 0)
			{
				continue;
			}
			if (_product.getIngredientIds()[i] == Inventory.ADENA_ID)
			{
				final long amount = _product.getIngredientQuantities()[i] * _amount;
				if (amount < 1)
				{
					player.sendPacket(SystemMessageId.INCORRECT_ITEM_COUNT_2);
					player.removeRequest(PrimeShopRequest.class);
					player.sendPacket(new ExPurchaseLimitShopItemResult(false, _shopIndex, _productId, remainingInfo, Collections.emptyList()));
					return;
				}
				
				if (player.getAdena() < amount)
				{
					player.sendPacket(SystemMessageId.INCORRECT_ITEM_COUNT_2);
					player.removeRequest(PrimeShopRequest.class);
					player.sendPacket(new ExPurchaseLimitShopItemResult(false, _shopIndex, _productId, remainingInfo, Collections.emptyList()));
					return;
				}
			}
			else if (_product.getIngredientIds()[i] == SpecialItemType.HONOR_COINS.getClientId())
			{
				final long amount = _product.getIngredientQuantities()[i] * _amount;
				if (amount < 1)
				{
					player.sendPacket(SystemMessageId.INCORRECT_ITEM_COUNT_2);
					player.removeRequest(PrimeShopRequest.class);
					player.sendPacket(new ExPurchaseLimitShopItemResult(false, _shopIndex, _productId, remainingInfo, Collections.emptyList()));
					return;
				}
				
				if (player.getHonorCoins() < amount)
				{
					player.sendPacket(SystemMessageId.INCORRECT_ITEM_COUNT_2);
					player.removeRequest(PrimeShopRequest.class);
					player.sendPacket(new ExPurchaseLimitShopItemResult(false, _shopIndex, _productId, remainingInfo, Collections.emptyList()));
					return;
				}
			}
			else if (_product.getIngredientIds()[i] == SpecialItemType.PC_CAFE_POINTS.getClientId())
			{
				final long amount = _product.getIngredientQuantities()[i] * _amount;
				if (amount < 1)
				{
					player.sendPacket(SystemMessageId.INCORRECT_ITEM_COUNT_2);
					player.removeRequest(PrimeShopRequest.class);
					player.sendPacket(new ExPurchaseLimitShopItemResult(false, _shopIndex, _productId, remainingInfo, Collections.emptyList()));
					return;
				}
				
				if (player.getPcCafePoints() < amount)
				{
					player.sendPacket(SystemMessageId.INCORRECT_ITEM_COUNT_2);
					player.removeRequest(PrimeShopRequest.class);
					player.sendPacket(new ExPurchaseLimitShopItemResult(false, _shopIndex, _productId, remainingInfo, Collections.emptyList()));
					return;
				}
			}
			else
			{
				final long amount = _product.getIngredientQuantities()[i] * _amount;
				if (amount < 1)
				{
					player.sendPacket(SystemMessageId.INCORRECT_ITEM_COUNT_2);
					player.removeRequest(PrimeShopRequest.class);
					player.sendPacket(new ExPurchaseLimitShopItemResult(false, _shopIndex, _productId, remainingInfo, Collections.emptyList()));
					return;
				}
				
				if (player.getInventory().getInventoryItemCount(_product.getIngredientIds()[i], _product.getIngredientEnchants()[i] == 0 ? -1 : _product.getIngredientEnchants()[i], true) < amount)
				{
					player.sendPacket(SystemMessageId.INCORRECT_ITEM_COUNT_2);
					player.removeRequest(PrimeShopRequest.class);
					player.sendPacket(new ExPurchaseLimitShopItemResult(false, _shopIndex, _productId, remainingInfo, Collections.emptyList()));
					return;
				}
			}
		}
		
		// Remove items.
		for (int i = 0; i < _product.getIngredientIds().length; i++)
		{
			if (_product.getIngredientIds()[i] == 0)
			{
				continue;
			}
			if (_product.getIngredientIds()[i] == Inventory.ADENA_ID)
			{
				player.reduceAdena("LCoinShop", _product.getIngredientQuantities()[i] * _amount, player, true);
			}
			else if (_product.getIngredientIds()[i] == SpecialItemType.HONOR_COINS.getClientId())
			{
				player.setHonorCoins(player.getHonorCoins() - (_product.getIngredientQuantities()[i] * _amount));
			}
			else if (_product.getIngredientIds()[i] == SpecialItemType.PC_CAFE_POINTS.getClientId())
			{
				final int newPoints = (int) (player.getPcCafePoints() - (_product.getIngredientQuantities()[i] * _amount));
				player.setPcCafePoints(newPoints);
				player.sendPacket(new ExPCCafePointInfo(player.getPcCafePoints(), (int) (-(_product.getIngredientQuantities()[i] * _amount)), 1));
			}
			else
			{
				if (_product.getIngredientEnchants()[i] > 0)
				{
					int count = 0;
					final Collection<Item> items = player.getInventory().getAllItemsByItemId(_product.getIngredientIds()[i], _product.getIngredientEnchants()[i]);
					for (Item item : items)
					{
						if (count == _amount)
						{
							break;
						}
						count++;
						player.destroyItem("LCoinShop", item, player, true);
					}
				}
				else
				{
					final long amount = _product.getIngredientQuantities()[i] * _amount;
					if (amount < 1)
					{
						player.sendPacket(SystemMessageId.INCORRECT_ITEM_COUNT_2);
						player.removeRequest(PrimeShopRequest.class);
						player.sendPacket(new ExPurchaseLimitShopItemResult(false, _shopIndex, _productId, remainingInfo, Collections.emptyList()));
						return;
					}
					
					player.destroyItemByItemId("LCoinShop", _product.getIngredientIds()[i], amount, player, true);
				}
			}
			if (Config.VIP_SYSTEM_L_SHOP_AFFECT)
			{
				player.updateVipPoints(_amount);
			}
		}
		
		// Reward.
		final Map<Integer, LimitShopRandomCraftReward> rewards = new HashMap<>();
		if (_product.getProductionId2() > 0)
		{
			for (int i = 0; i < _amount; i++)
			{
				if (Rnd.get(100) < _product.getChance())
				{
					rewards.computeIfAbsent(0, k -> new LimitShopRandomCraftReward(_product.getProductionId(), 0, 0)).getCount().addAndGet((int) _product.getCount());
					final Item item = player.addItem("LCoinShop", _product.getProductionId(), _product.getCount(), _product.getEnchant(), player, true);
					if (_product.isAnnounce())
					{
						Broadcast.toAllOnlinePlayers(new ExItemAnnounce(player, item, ExItemAnnounce.SPECIAL_CREATION));
					}
				}
				else if ((Rnd.get(100) < _product.getChance2()) || (_product.getProductionId3() == 0))
				{
					rewards.computeIfAbsent(1, k -> new LimitShopRandomCraftReward(_product.getProductionId2(), 0, 1)).getCount().addAndGet((int) _product.getCount2());
					final Item item = player.addItem("LCoinShop", _product.getProductionId2(), _product.getCount2(), player, true);
					if (_product.isAnnounce2())
					{
						Broadcast.toAllOnlinePlayers(new ExItemAnnounce(player, item, ExItemAnnounce.SPECIAL_CREATION));
					}
				}
				else if ((Rnd.get(100) < _product.getChance3()) || (_product.getProductionId4() == 0))
				{
					rewards.computeIfAbsent(2, k -> new LimitShopRandomCraftReward(_product.getProductionId3(), 0, 2)).getCount().addAndGet((int) _product.getCount3());
					final Item item = player.addItem("LCoinShop", _product.getProductionId3(), _product.getCount3(), player, true);
					if (_product.isAnnounce3())
					{
						Broadcast.toAllOnlinePlayers(new ExItemAnnounce(player, item, ExItemAnnounce.SPECIAL_CREATION));
					}
				}
				else if ((Rnd.get(100) < _product.getChance4()) || (_product.getProductionId5() == 0))
				{
					rewards.computeIfAbsent(3, k -> new LimitShopRandomCraftReward(_product.getProductionId4(), 0, 3)).getCount().addAndGet((int) _product.getCount4());
					final Item item = player.addItem("LCoinShop", _product.getProductionId4(), _product.getCount4(), player, true);
					if (_product.isAnnounce4())
					{
						Broadcast.toAllOnlinePlayers(new ExItemAnnounce(player, item, ExItemAnnounce.SPECIAL_CREATION));
					}
				}
				else if (_product.getProductionId5() > 0)
				{
					rewards.computeIfAbsent(4, k -> new LimitShopRandomCraftReward(_product.getProductionId5(), 0, 4)).getCount().addAndGet((int) _product.getCount5());
					final Item item = player.addItem("LCoinShop", _product.getProductionId5(), _product.getCount5(), player, true);
					if (_product.isAnnounce5())
					{
						Broadcast.toAllOnlinePlayers(new ExItemAnnounce(player, item, ExItemAnnounce.SPECIAL_CREATION));
					}
				}
			}
		}
		else if (Rnd.get(100) < _product.getChance())
		{
			rewards.put(0, new LimitShopRandomCraftReward(_product.getProductionId(), (int) (_product.getCount() * _amount), 0));
			final Item item = player.addItem("LCoinShop", _product.getProductionId(), _product.getCount() * _amount, _product.getEnchant(), player, true);
			if (_product.isAnnounce())
			{
				Broadcast.toAllOnlinePlayers(new ExItemAnnounce(player, item, ExItemAnnounce.SPECIAL_CREATION));
			}
		}
		
		// Update account variables.
		if (_product.getAccountDailyLimit() > 0)
		{
			player.getAccountVariables().set(AccountVariables.LCOIN_SHOP_PRODUCT_DAILY_COUNT + _product.getProductionId(), player.getAccountVariables().getInt(AccountVariables.LCOIN_SHOP_PRODUCT_DAILY_COUNT + _product.getProductionId(), 0) + _amount);
		}
		if (_product.getAccountMontlyLimit() > 0)
		{
			player.getAccountVariables().set(AccountVariables.LCOIN_SHOP_PRODUCT_MONTLY_COUNT + _product.getProductionId(), player.getAccountVariables().getInt(AccountVariables.LCOIN_SHOP_PRODUCT_MONTLY_COUNT + _product.getProductionId(), 0) + _amount);
		}
		else if (_product.getAccountBuyLimit() > 0)
		{
			player.getAccountVariables().set(AccountVariables.LCOIN_SHOP_PRODUCT_COUNT + _product.getProductionId(), player.getAccountVariables().getInt(AccountVariables.LCOIN_SHOP_PRODUCT_COUNT + _product.getProductionId(), 0) + _amount);
		}
		
		player.sendPacket(new ExPurchaseLimitShopItemResult(true, _shopIndex, _productId, Math.max(remainingInfo - _amount, 0), rewards.values()));
		player.sendItemList();
		
		// Remove request.
		player.removeRequest(PrimeShopRequest.class);
	}
}
