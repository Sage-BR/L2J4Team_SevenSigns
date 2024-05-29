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
package handlers.itemhandlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.l2j.Config;

import org.l2j.commons.util.Rnd;
import org.l2j.gameserver.data.xml.ItemData;
import org.l2j.gameserver.enums.SpecialItemType;
import org.l2j.gameserver.handler.IItemHandler;
import org.l2j.gameserver.instancemanager.DailyTaskManager;
import org.l2j.gameserver.model.ExtractableProduct;
import org.l2j.gameserver.model.actor.Playable;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.request.AutoPeelRequest;
import org.l2j.gameserver.model.holders.ItemHolder;
import org.l2j.gameserver.model.item.EtcItem;
import org.l2j.gameserver.model.item.ItemTemplate;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.serverpackets.ExPCCafePointInfo;
import org.l2j.gameserver.network.serverpackets.InventoryUpdate;
import org.l2j.gameserver.network.serverpackets.SystemMessage;
import org.l2j.gameserver.network.serverpackets.autopeel.ExResultItemAutoPeel;
import org.l2j.gameserver.network.serverpackets.autopeel.ExStopItemAutoPeel;

/**
 * Extractable Items handler.
 * @author HorridoJoho, Mobius
 */
public class ExtractableItems implements IItemHandler
{
	@Override
	public boolean useItem(Playable playable, Item item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.YOUR_PET_CANNOT_CARRY_THIS_ITEM);
			return false;
		}
		
		final Player player = playable.getActingPlayer();
		final EtcItem etcitem = (EtcItem) item.getTemplate();
		final List<ExtractableProduct> exitems = etcitem.getExtractableItems();
		if (exitems == null)
		{
			LOGGER.info("No extractable data defined for " + etcitem);
			return false;
		}
		
		if (!player.isInventoryUnder80(false))
		{
			player.sendPacket(SystemMessageId.NOT_ENOUGH_SPACE_IN_INVENTORY_UNABLE_TO_PROCESS_THIS_REQUEST_UNTIL_YOUR_INVENTORY_S_WEIGHT_IS_LESS_THAN_80_AND_SLOT_COUNT_IS_LESS_THAN_90_OF_CAPACITY);
			return false;
		}
		
		// destroy item
		if (!DailyTaskManager.RESET_ITEMS.contains(item.getId()) && !player.destroyItem("Extract", item.getObjectId(), 1, player, true))
		{
			return false;
		}
		
		boolean specialReward = false;
		final Map<Item, Long> extractedItems = new HashMap<>();
		final List<Item> enchantedItems = new ArrayList<>();
		if (etcitem.getExtractableCountMin() > 0)
		{
			while (extractedItems.size() < etcitem.getExtractableCountMin())
			{
				for (ExtractableProduct expi : exitems)
				{
					if ((etcitem.getExtractableCountMax() > 0) && (extractedItems.size() == etcitem.getExtractableCountMax()))
					{
						break;
					}
					
					if (Rnd.get(100000) <= expi.getChance())
					{
						final long min = (long) (expi.getMin() * Config.RATE_EXTRACTABLE);
						final long max = (long) (expi.getMax() * Config.RATE_EXTRACTABLE);
						long createItemAmount = (max == min) ? min : (Rnd.get((max - min) + 1) + min);
						if (createItemAmount == 0)
						{
							continue;
						}
						
						// Do not extract the same item.
						boolean alreadyExtracted = false;
						for (Item i : extractedItems.keySet())
						{
							if (i.getTemplate().getId() == expi.getId())
							{
								alreadyExtracted = true;
								break;
							}
						}
						if (alreadyExtracted && (exitems.size() >= etcitem.getExtractableCountMax()))
						{
							continue;
						}
						
						if (expi.getId() == -1) // Prime points
						{
							player.setPrimePoints(player.getPrimePoints() + (int) createItemAmount);
							player.sendMessage("You have obtained " + (createItemAmount / 100) + " Euro!");
							specialReward = true;
							continue;
						}
						else if (expi.getId() == SpecialItemType.PC_CAFE_POINTS.getClientId())
						{
							final int currentPoints = player.getPcCafePoints();
							final int upgradePoints = player.getPcCafePoints() + (int) createItemAmount;
							player.setPcCafePoints(upgradePoints);
							final SystemMessage message = new SystemMessage(SystemMessageId.YOU_EARNED_S1_PA_POINT_S).addInt((int) createItemAmount);
							player.sendPacket(message);
							player.sendPacket(new ExPCCafePointInfo(currentPoints, upgradePoints, 1));
							specialReward = true;
							continue;
						}
						else if (expi.getId() == SpecialItemType.HONOR_COINS.getClientId())
						{
							player.setHonorCoins(player.getHonorCoins() + (int) createItemAmount);
							player.sendMessage("You have obtained " + (createItemAmount) + " Honor Coin.");
							specialReward = true;
							continue;
						}
						
						final ItemTemplate template = ItemData.getInstance().getTemplate(expi.getId());
						if (template == null)
						{
							LOGGER.warning("ExtractableItems: Could not find " + item + " product template with id " + expi.getId() + "!");
							continue;
						}
						
						if (template.isStackable() || (createItemAmount == 1))
						{
							final Item newItem = player.addItem("Extract", expi.getId(), createItemAmount, player, false);
							if (expi.getMaxEnchant() > 0)
							{
								newItem.setEnchantLevel(Rnd.get(expi.getMinEnchant(), expi.getMaxEnchant()));
								enchantedItems.add(newItem);
							}
							addItem(extractedItems, newItem, createItemAmount);
						}
						else
						{
							while (createItemAmount > 0)
							{
								final Item newItem = player.addItem("Extract", expi.getId(), 1, player, false);
								if (expi.getMaxEnchant() > 0)
								{
									newItem.setEnchantLevel(Rnd.get(expi.getMinEnchant(), expi.getMaxEnchant()));
									enchantedItems.add(newItem);
								}
								addItem(extractedItems, newItem, 1);
								createItemAmount--;
							}
						}
					}
				}
			}
		}
		else
		{
			for (ExtractableProduct expi : exitems)
			{
				if ((etcitem.getExtractableCountMax() > 0) && (extractedItems.size() == etcitem.getExtractableCountMax()))
				{
					break;
				}
				
				if (Rnd.get(100000) <= expi.getChance())
				{
					final long min = (long) (expi.getMin() * Config.RATE_EXTRACTABLE);
					final long max = (long) (expi.getMax() * Config.RATE_EXTRACTABLE);
					long createItemAmount = (max == min) ? min : (Rnd.get((max - min) + 1) + min);
					if (createItemAmount == 0)
					{
						continue;
					}
					
					if (expi.getId() == -1) // Prime points
					{
						player.setPrimePoints(player.getPrimePoints() + (int) createItemAmount);
						player.sendMessage("You have obtained " + (createItemAmount / 100) + " Euro!");
						specialReward = true;
						continue;
					}
					else if (expi.getId() == SpecialItemType.PC_CAFE_POINTS.getClientId())
					{
						final int currentPoints = player.getPcCafePoints();
						final int upgradePoints = player.getPcCafePoints() + (int) createItemAmount;
						player.setPcCafePoints(upgradePoints);
						final SystemMessage message = new SystemMessage(SystemMessageId.YOU_EARNED_S1_PA_POINT_S).addInt((int) createItemAmount);
						player.sendPacket(message);
						player.sendPacket(new ExPCCafePointInfo(currentPoints, upgradePoints, 1));
						specialReward = true;
						continue;
					}
					else if (expi.getId() == SpecialItemType.HONOR_COINS.getClientId())
					{
						player.setHonorCoins(player.getHonorCoins() + (int) createItemAmount);
						player.sendMessage("You have obtained " + (createItemAmount) + " Honor Coin.");
						specialReward = true;
						continue;
					}
					
					final ItemTemplate template = ItemData.getInstance().getTemplate(expi.getId());
					if (template == null)
					{
						LOGGER.warning("ExtractableItems: Could not find " + item + " product template with id " + expi.getId() + "!");
						continue;
					}
					
					if (template.isStackable() || (createItemAmount == 1))
					{
						final Item newItem = player.addItem("Extract", expi.getId(), createItemAmount, player, false);
						if (expi.getMaxEnchant() > 0)
						{
							newItem.setEnchantLevel(Rnd.get(expi.getMinEnchant(), expi.getMaxEnchant()));
							enchantedItems.add(newItem);
						}
						addItem(extractedItems, newItem, createItemAmount);
					}
					else
					{
						while (createItemAmount > 0)
						{
							final Item newItem = player.addItem("Extract", expi.getId(), 1, player, false);
							if (expi.getMaxEnchant() > 0)
							{
								newItem.setEnchantLevel(Rnd.get(expi.getMinEnchant(), expi.getMaxEnchant()));
								enchantedItems.add(newItem);
							}
							addItem(extractedItems, newItem, 1);
							createItemAmount--;
						}
					}
				}
			}
		}
		
		if (extractedItems.isEmpty() && !specialReward)
		{
			player.sendPacket(SystemMessageId.FAILED_TO_CHANGE_THE_ITEM);
		}
		if (!enchantedItems.isEmpty())
		{
			final InventoryUpdate playerIU = new InventoryUpdate();
			for (Item i : enchantedItems)
			{
				playerIU.addModifiedItem(i);
			}
			player.sendPacket(playerIU);
		}
		
		for (Entry<Item, Long> entry : extractedItems.entrySet())
		{
			sendMessage(player, entry.getKey(), entry.getValue());
		}
		
		final AutoPeelRequest request = player.getRequest(AutoPeelRequest.class);
		if (request != null)
		{
			if (request.isProcessing())
			{
				request.setProcessing(false);
				final List<ItemHolder> rewards = new LinkedList<>();
				for (Entry<Item, Long> entry : extractedItems.entrySet())
				{
					rewards.add(new ItemHolder(entry.getKey().getId(), entry.getValue()));
				}
				player.sendPacket(new ExResultItemAutoPeel(true, request.getTotalPeelCount(), request.getRemainingPeelCount() - 1, rewards));
			}
			else
			{
				player.sendPacket(new ExStopItemAutoPeel(false));
			}
		}
		
		return true;
	}
	
	private void addItem(Map<Item, Long> extractedItems, Item newItem, long count)
	{
		if (extractedItems.containsKey(newItem))
		{
			extractedItems.put(newItem, extractedItems.get(newItem) + count);
		}
		else
		{
			extractedItems.put(newItem, count);
		}
	}
	
	private void sendMessage(Player player, Item item, long count)
	{
		final SystemMessage sm;
		if (count > 1)
		{
			sm = new SystemMessage(SystemMessageId.YOU_VE_OBTAINED_S1_X_S2);
			sm.addItemName(item);
			sm.addLong(count);
		}
		else if (item.getEnchantLevel() > 0)
		{
			sm = new SystemMessage(SystemMessageId.YOU_VE_OBTAINED_S1_S2);
			sm.addInt(item.getEnchantLevel());
			sm.addItemName(item);
		}
		else
		{
			sm = new SystemMessage(SystemMessageId.YOU_HAVE_OBTAINED_S1);
			sm.addItemName(item);
		}
		player.sendPacket(sm);
	}
}
