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
package org.l2j.gameserver.network.clientpackets.enchant;

import java.util.logging.Logger;

import org.l2j.Config;
import org.l2j.commons.util.Rnd;
import org.l2j.gameserver.data.xml.EnchantChallengePointData;
import org.l2j.gameserver.data.xml.EnchantChallengePointData.EnchantChallengePointsItemInfo;
import org.l2j.gameserver.data.xml.EnchantChallengePointData.EnchantChallengePointsOptionInfo;
import org.l2j.gameserver.data.xml.EnchantItemData;
import org.l2j.gameserver.data.xml.ItemCrystallizationData;
import org.l2j.gameserver.enums.ItemSkillType;
import org.l2j.gameserver.model.World;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.request.EnchantItemRequest;
import org.l2j.gameserver.model.holders.ItemChanceHolder;
import org.l2j.gameserver.model.holders.ItemHolder;
import org.l2j.gameserver.model.item.ItemTemplate;
import org.l2j.gameserver.model.item.enchant.EnchantResultType;
import org.l2j.gameserver.model.item.enchant.EnchantScroll;
import org.l2j.gameserver.model.item.enchant.EnchantSupportItem;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.skill.CommonSkill;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.ExItemAnnounce;
import org.l2j.gameserver.network.serverpackets.InventoryUpdate;
import org.l2j.gameserver.network.serverpackets.MagicSkillUse;
import org.l2j.gameserver.network.serverpackets.SystemMessage;
import org.l2j.gameserver.network.serverpackets.enchant.EnchantResult;
import org.l2j.gameserver.network.serverpackets.enchant.challengepoint.ExEnchantChallengePointInfo;
import org.l2j.gameserver.util.Broadcast;
import org.l2j.gameserver.util.Util;

public class RequestEnchantItem extends ClientPacket
{
	protected static final Logger LOGGER_ENCHANT = Logger.getLogger("enchant.items");
	
	private int _objectId;
	
	@Override
	protected void readImpl()
	{
		_objectId = readInt();
		readByte(); // Unknown.
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		final EnchantItemRequest request = player.getRequest(EnchantItemRequest.class);
		if ((request == null) || request.isProcessing())
		{
			return;
		}
		
		request.setEnchantingItem(_objectId);
		request.setProcessing(true);
		
		if (!player.isOnline() || getClient().isDetached())
		{
			player.removeRequest(request.getClass());
			return;
		}
		
		if (player.isProcessingTransaction() || player.isInStoreMode())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_ENCHANT_WHILE_OPERATING_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP);
			player.removeRequest(request.getClass());
			return;
		}
		
		final Item item = request.getEnchantingItem();
		final Item scroll = request.getEnchantingScroll();
		final Item support = request.getSupportItem();
		if ((item == null) || (scroll == null))
		{
			player.removeRequest(request.getClass());
			return;
		}
		
		// Template for scroll.
		final EnchantScroll scrollTemplate = EnchantItemData.getInstance().getEnchantScroll(scroll);
		if (scrollTemplate == null)
		{
			return;
		}
		
		// Template for support item, if exist.
		EnchantSupportItem supportTemplate = null;
		if (support != null)
		{
			supportTemplate = EnchantItemData.getInstance().getSupportItem(support);
			if (supportTemplate == null)
			{
				player.removeRequest(request.getClass());
				return;
			}
		}
		
		// First validation check, also over enchant check.
		if (!scrollTemplate.isValid(item, supportTemplate) || (Config.DISABLE_OVER_ENCHANTING && ((item.getEnchantLevel() == scrollTemplate.getMaxEnchantLevel()) || ((item.getTemplate().getEnchantLimit() != 0) && (item.getEnchantLevel() == item.getTemplate().getEnchantLimit())))))
		{
			player.sendPacket(SystemMessageId.AUGMENTATION_REQUIREMENTS_ARE_NOT_FULFILLED);
			player.removeRequest(request.getClass());
			player.sendPacket(new EnchantResult(EnchantResult.ERROR, null, null, 0));
			return;
		}
		
		// Fast auto-enchant cheat check.
		// if ((request.getTimestamp() == 0) || ((System.currentTimeMillis() - request.getTimestamp()) < 600))
		// {
		// Util.handleIllegalPlayerAction(player, player + " use autoenchant program ", Config.DEFAULT_PUNISH);
		// player.removeRequest(request.getClass());
		// player.sendPacket(new EnchantResult(EnchantResult.ERROR, null, null, 0));
		// return;
		// }
		
		// Attempting to destroy scroll.
		if (player.getInventory().destroyItem("Enchant", scroll.getObjectId(), 1, player, item) == null)
		{
			player.sendPacket(SystemMessageId.INCORRECT_ITEM_COUNT_2);
			Util.handleIllegalPlayerAction(player, player + " tried to enchant with a scroll he doesn't have", Config.DEFAULT_PUNISH);
			player.removeRequest(request.getClass());
			player.sendPacket(new EnchantResult(EnchantResult.ERROR, null, null, 0));
			return;
		}
		
		// Attempting to destroy support if exists.
		if ((support != null) && (player.getInventory().destroyItem("Enchant", support.getObjectId(), 1, player, item) == null))
		{
			player.sendPacket(SystemMessageId.INCORRECT_ITEM_COUNT_2);
			Util.handleIllegalPlayerAction(player, player + " tried to enchant with a support item he doesn't have", Config.DEFAULT_PUNISH);
			player.removeRequest(request.getClass());
			player.sendPacket(new EnchantResult(EnchantResult.ERROR, null, null, 0));
			return;
		}
		
		final InventoryUpdate iu = new InventoryUpdate();
		synchronized (item)
		{
			// Last validation check.
			if ((item.getOwnerId() != player.getObjectId()) || !item.isEnchantable())
			{
				player.sendPacket(SystemMessageId.AUGMENTATION_REQUIREMENTS_ARE_NOT_FULFILLED);
				player.removeRequest(request.getClass());
				player.sendPacket(new EnchantResult(EnchantResult.ERROR, null, null, 0));
				return;
			}
			
			final EnchantResultType resultType = scrollTemplate.calculateSuccess(player, item, supportTemplate);
			final EnchantChallengePointsItemInfo info = EnchantChallengePointData.getInstance().getInfoByItemId(item.getId());
			int challengePointsGroupId = -1;
			int challengePointsOptionIndex = -1;
			if (info != null)
			{
				final int groupId = info.groupId();
				if (groupId == player.getChallengeInfo().getChallengePointsPendingRecharge()[0])
				{
					challengePointsGroupId = player.getChallengeInfo().getChallengePointsPendingRecharge()[0];
					challengePointsOptionIndex = player.getChallengeInfo().getChallengePointsPendingRecharge()[1];
				}
			}
			
			switch (resultType)
			{
				case ERROR:
				{
					player.sendPacket(SystemMessageId.AUGMENTATION_REQUIREMENTS_ARE_NOT_FULFILLED);
					player.removeRequest(request.getClass());
					player.sendPacket(new EnchantResult(EnchantResult.ERROR, null, null, 0));
					break;
				}
				case SUCCESS:
				{
					final ItemTemplate it = item.getTemplate();
					if (scrollTemplate.isCursed())
					{
						// Blessed enchant: Enchant value down by 1.
						player.sendPacket(SystemMessageId.THE_ENCHANT_VALUE_IS_DECREASED_BY_1);
						item.setEnchantLevel(item.getEnchantLevel() - 1);
					}
					// Increase enchant level only if scroll's base template has chance, some armors can success over +20 but they shouldn't have increased.
					else if (scrollTemplate.getChance(player, item) > 0)
					{
						if (item.isEquipped())
						{
							item.clearSpecialAbilities();
							item.clearEnchantStats();
						}
						
						if (supportTemplate != null)
						{
							item.setEnchantLevel(Math.min(item.getEnchantLevel() + Rnd.get(supportTemplate.getRandomEnchantMin(), supportTemplate.getRandomEnchantMax()), supportTemplate.getMaxEnchantLevel()));
						}
						if (supportTemplate == null)
						{
							item.setEnchantLevel(Math.min(item.getEnchantLevel() + Rnd.get(scrollTemplate.getRandomEnchantMin(), scrollTemplate.getRandomEnchantMax()), scrollTemplate.getMaxEnchantLevel()));
						}
						else
						{
							int enchantValue = 1;
							if ((challengePointsGroupId > 0) && (challengePointsOptionIndex == EnchantChallengePointData.OPTION_OVER_UP_PROB))
							{
								final EnchantChallengePointsOptionInfo optionInfo = EnchantChallengePointData.getInstance().getOptionInfo(challengePointsGroupId, challengePointsOptionIndex);
								if ((optionInfo != null) && (item.getEnchantLevel() >= optionInfo.minEnchant()) && (item.getEnchantLevel() <= optionInfo.maxEnchant()) && (Rnd.get(100) < optionInfo.chance()))
								{
									enchantValue = 2;
								}
							}
							item.setEnchantLevel(item.getEnchantLevel() + enchantValue);
						}
						
						if (item.isEquipped())
						{
							item.applySpecialAbilities();
							item.applyEnchantStats();
						}
						
						item.updateDatabase();
						
						iu.addModifiedItem(item);
						if (scroll.getCount() > 0)
						{
							iu.addModifiedItem(scroll);
						}
						else
						{
							iu.addRemovedItem(scroll);
						}
						if (support != null)
						{
							if (support.getCount() > 0)
							{
								iu.addModifiedItem(support);
							}
							else
							{
								iu.addRemovedItem(support);
							}
						}
					}
					player.sendPacket(new EnchantResult(EnchantResult.SUCCESS, new ItemHolder(item.getId(), 1), null, item.getEnchantLevel()));
					if (Config.LOG_ITEM_ENCHANTS)
					{
						final StringBuilder sb = new StringBuilder();
						if (item.getEnchantLevel() > 0)
						{
							if (support == null)
							{
								LOGGER_ENCHANT.info(sb.append("Success, Character:").append(player.getName()).append(" [").append(player.getObjectId()).append("] Account:").append(player.getAccountName()).append(" IP:").append(player.getIPAddress()).append(", +").append(item.getEnchantLevel()).append(" ").append(item.getName()).append("(").append(item.getCount()).append(") [").append(item.getObjectId()).append("], ").append(scroll.getName()).append("(").append(scroll.getCount()).append(") [").append(scroll.getObjectId()).append("]").toString());
							}
							else
							{
								LOGGER_ENCHANT.info(sb.append("Success, Character:").append(player.getName()).append(" [").append(player.getObjectId()).append("] Account:").append(player.getAccountName()).append(" IP:").append(player.getIPAddress()).append(", +").append(item.getEnchantLevel()).append(" ").append(item.getName()).append("(").append(item.getCount()).append(") [").append(item.getObjectId()).append("], ").append(scroll.getName()).append("(").append(scroll.getCount()).append(") [").append(scroll.getObjectId()).append("], ").append(support.getName()).append("(").append(support.getCount()).append(") [").append(support.getObjectId()).append("]").toString());
							}
						}
						else if (support == null)
						{
							LOGGER_ENCHANT.info(sb.append("Success, Character:").append(player.getName()).append(" [").append(player.getObjectId()).append("] Account:").append(player.getAccountName()).append(" IP:").append(player.getIPAddress()).append(", ").append(item.getName()).append("(").append(item.getCount()).append(") [").append(item.getObjectId()).append("], ").append(scroll.getName()).append("(").append(scroll.getCount()).append(") [").append(scroll.getObjectId()).append("]").toString());
						}
						else
						{
							LOGGER_ENCHANT.info(sb.append("Success, Character:").append(player.getName()).append(" [").append(player.getObjectId()).append("] Account:").append(player.getAccountName()).append(" IP:").append(player.getIPAddress()).append(", ").append(item.getName()).append("(").append(item.getCount()).append(") [").append(item.getObjectId()).append("], ").append(scroll.getName()).append("(").append(scroll.getCount()).append(") [").append(scroll.getObjectId()).append("], ").append(support.getName()).append("(").append(support.getCount()).append(") [").append(support.getObjectId()).append("]").toString());
						}
					}
					
					// Announce the success.
					if ((item.getEnchantLevel() >= (item.isArmor() ? Config.MIN_ARMOR_ENCHANT_ANNOUNCE : Config.MIN_WEAPON_ENCHANT_ANNOUNCE)) //
						&& (item.getEnchantLevel() <= (item.isArmor() ? Config.MAX_ARMOR_ENCHANT_ANNOUNCE : Config.MAX_WEAPON_ENCHANT_ANNOUNCE)))
					{
						final SystemMessage sm = new SystemMessage(SystemMessageId.C1_HAS_ENCHANTED_S3_UP_TO_S2);
						sm.addString(player.getName());
						sm.addInt(item.getEnchantLevel());
						sm.addItemName(item);
						player.broadcastPacket(sm);
						Broadcast.toAllOnlinePlayers(new ExItemAnnounce(player, item, ExItemAnnounce.ENCHANT));
						
						final Skill skill = CommonSkill.FIREWORK.getSkill();
						if (skill != null)
						{
							player.broadcastPacket(new MagicSkillUse(player, player, skill.getId(), skill.getLevel(), skill.getHitTime(), skill.getReuseDelay()));
						}
					}
					
					if (item.isEquipped())
					{
						if (item.isArmor())
						{
							it.forEachSkill(ItemSkillType.ON_ENCHANT, holder ->
							{
								// Add skills bestowed from +4 armor.
								if (item.getEnchantLevel() >= holder.getValue())
								{
									player.addSkill(holder.getSkill(), false);
									player.sendSkillList();
								}
							});
						}
						player.broadcastUserInfo(); // Update user info.
					}
					break;
				}
				case FAILURE:
				{
					boolean challengePointsSafe = false;
					if ((challengePointsGroupId > 0) && (challengePointsOptionIndex == EnchantChallengePointData.OPTION_NUM_PROTECT_PROB))
					{
						final EnchantChallengePointsOptionInfo optionInfo = EnchantChallengePointData.getInstance().getOptionInfo(challengePointsGroupId, challengePointsOptionIndex);
						if ((optionInfo != null) && (item.getEnchantLevel() >= optionInfo.minEnchant()) && (item.getEnchantLevel() <= optionInfo.maxEnchant()) && (Rnd.get(100) < optionInfo.chance()))
						{
							challengePointsSafe = true;
						}
					}
					
					if (challengePointsSafe || scrollTemplate.isSafe())
					{
						// Safe enchant: Remain old value.
						player.sendPacket(SystemMessageId.ENCHANT_FAILED_THE_ENCHANT_SKILL_FOR_THE_CORRESPONDING_ITEM_WILL_BE_EXACTLY_RETAINED);
						player.sendPacket(new EnchantResult(EnchantResult.SAFE_FAIL_02, new ItemHolder(item.getId(), 1), null, item.getEnchantLevel()));
						if (Config.LOG_ITEM_ENCHANTS)
						{
							final StringBuilder sb = new StringBuilder();
							if (item.getEnchantLevel() > 0)
							{
								if (support == null)
								{
									LOGGER_ENCHANT.info(sb.append("Safe Fail, Character:").append(player.getName()).append(" [").append(player.getObjectId()).append("] Account:").append(player.getAccountName()).append(" IP:").append(player.getIPAddress()).append(", +").append(item.getEnchantLevel()).append(" ").append(item.getName()).append("(").append(item.getCount()).append(") [").append(item.getObjectId()).append("], ").append(scroll.getName()).append("(").append(scroll.getCount()).append(") [").append(scroll.getObjectId()).append("]").toString());
								}
								else
								{
									LOGGER_ENCHANT.info(sb.append("Safe Fail, Character:").append(player.getName()).append(" [").append(player.getObjectId()).append("] Account:").append(player.getAccountName()).append(" IP:").append(player.getIPAddress()).append(", +").append(item.getEnchantLevel()).append(" ").append(item.getName()).append("(").append(item.getCount()).append(") [").append(item.getObjectId()).append("], ").append(scroll.getName()).append("(").append(scroll.getCount()).append(") [").append(scroll.getObjectId()).append("], ").append(support.getName()).append("(").append(support.getCount()).append(") [").append(support.getObjectId()).append("]").toString());
								}
							}
							else if (support == null)
							{
								LOGGER_ENCHANT.info(sb.append("Safe Fail, Character:").append(player.getName()).append(" [").append(player.getObjectId()).append("] Account:").append(player.getAccountName()).append(" IP:").append(player.getIPAddress()).append(", ").append(item.getName()).append("(").append(item.getCount()).append(") [").append(item.getObjectId()).append("], ").append(scroll.getName()).append("(").append(scroll.getCount()).append(") [").append(scroll.getObjectId()).append("]").toString());
							}
							else
							{
								LOGGER_ENCHANT.info(sb.append("Safe Fail, Character:").append(player.getName()).append(" [").append(player.getObjectId()).append("] Account:").append(player.getAccountName()).append(" IP:").append(player.getIPAddress()).append(", ").append(item.getName()).append("(").append(item.getCount()).append(") [").append(item.getObjectId()).append("], ").append(scroll.getName()).append("(").append(scroll.getCount()).append(") [").append(scroll.getObjectId()).append("], ").append(support.getName()).append("(").append(support.getCount()).append(") [").append(support.getObjectId()).append("]").toString());
							}
						}
					}
					else
					{
						// Unequip item on enchant failure to avoid item skills stack.
						if (item.isEquipped())
						{
							if (item.getEnchantLevel() > 0)
							{
								final SystemMessage sm = new SystemMessage(SystemMessageId.S1_S2_UNEQUIPPED);
								sm.addInt(item.getEnchantLevel());
								sm.addItemName(item);
								player.sendPacket(sm);
							}
							else
							{
								final SystemMessage sm = new SystemMessage(SystemMessageId.S1_UNEQUIPPED);
								sm.addItemName(item);
								player.sendPacket(sm);
							}
							
							for (Item itm : player.getInventory().unEquipItemInSlotAndRecord(item.getLocationSlot()))
							{
								iu.addModifiedItem(itm);
							}
							player.sendInventoryUpdate(iu);
							player.broadcastUserInfo();
						}
						
						boolean challengePointsBlessed = false;
						boolean challengePointsBlessedDown = false;
						if (challengePointsGroupId > 0)
						{
							if (challengePointsOptionIndex == EnchantChallengePointData.OPTION_NUM_RESET_PROB)
							{
								final EnchantChallengePointsOptionInfo optionInfo = EnchantChallengePointData.getInstance().getOptionInfo(challengePointsGroupId, challengePointsOptionIndex);
								if ((optionInfo != null) && (item.getEnchantLevel() >= optionInfo.minEnchant()) && (item.getEnchantLevel() <= optionInfo.maxEnchant()) && (Rnd.get(100) < optionInfo.chance()))
								{
									challengePointsBlessed = true;
								}
							}
							else if (challengePointsOptionIndex == EnchantChallengePointData.OPTION_NUM_DOWN_PROB)
							{
								final EnchantChallengePointsOptionInfo optionInfo = EnchantChallengePointData.getInstance().getOptionInfo(challengePointsGroupId, challengePointsOptionIndex);
								if ((optionInfo != null) && (item.getEnchantLevel() >= optionInfo.minEnchant()) && (item.getEnchantLevel() <= optionInfo.maxEnchant()) && (Rnd.get(100) < optionInfo.chance()))
								{
									challengePointsBlessedDown = true;
								}
							}
						}
						
						if (challengePointsBlessed || challengePointsBlessedDown || scrollTemplate.isBlessed() || scrollTemplate.isBlessedDown() || scrollTemplate.isCursed() /* || ((supportTemplate != null) && supportTemplate.isDown()) */ || ((supportTemplate != null) && supportTemplate.isBlessed()))
						{
							// Blessed enchant: Enchant value down by 1.
							if (scrollTemplate.isBlessedDown() || challengePointsBlessedDown || scrollTemplate.isCursed())
							{
								player.sendPacket(SystemMessageId.THE_ENCHANT_VALUE_IS_DECREASED_BY_1);
								item.setEnchantLevel(Math.max(0, item.getEnchantLevel() - 1));
							}
							else // Blessed enchant: Clear enchant value.
							{
								player.sendPacket(SystemMessageId.THE_BLESSED_ENCHANT_FAILED_THE_ENCHANT_VALUE_OF_THE_ITEM_BECAME_0);
								item.setEnchantLevel(0);
							}
							player.sendPacket(new EnchantResult(EnchantResult.FAIL, new ItemHolder(item.getId(), 1), null, item.getEnchantLevel()));
							item.updateDatabase();
							if (Config.LOG_ITEM_ENCHANTS)
							{
								final StringBuilder sb = new StringBuilder();
								if (item.getEnchantLevel() > 0)
								{
									if (support == null)
									{
										LOGGER_ENCHANT.info(sb.append("Blessed Fail, Character:").append(player.getName()).append(" [").append(player.getObjectId()).append("] Account:").append(player.getAccountName()).append(" IP:").append(player.getIPAddress()).append(", +").append(item.getEnchantLevel()).append(" ").append(item.getName()).append("(").append(item.getCount()).append(") [").append(item.getObjectId()).append("], ").append(scroll.getName()).append("(").append(scroll.getCount()).append(") [").append(scroll.getObjectId()).append("]").toString());
									}
									else
									{
										LOGGER_ENCHANT.info(sb.append("Blessed Fail, Character:").append(player.getName()).append(" [").append(player.getObjectId()).append("] Account:").append(player.getAccountName()).append(" IP:").append(player.getIPAddress()).append(", +").append(item.getEnchantLevel()).append(" ").append(item.getName()).append("(").append(item.getCount()).append(") [").append(item.getObjectId()).append("], ").append(scroll.getName()).append("(").append(scroll.getCount()).append(") [").append(scroll.getObjectId()).append("], ").append(support.getName()).append("(").append(support.getCount()).append(") [").append(support.getObjectId()).append("]").toString());
									}
								}
								else if (support == null)
								{
									LOGGER_ENCHANT.info(sb.append("Blessed Fail, Character:").append(player.getName()).append(" [").append(player.getObjectId()).append("] Account:").append(player.getAccountName()).append(" IP:").append(player.getIPAddress()).append(", ").append(item.getName()).append("(").append(item.getCount()).append(") [").append(item.getObjectId()).append("], ").append(scroll.getName()).append("(").append(scroll.getCount()).append(") [").append(scroll.getObjectId()).append("]").toString());
								}
								else
								{
									LOGGER_ENCHANT.info(sb.append("Blessed Fail, Character:").append(player.getName()).append(" [").append(player.getObjectId()).append("] Account:").append(player.getAccountName()).append(" IP:").append(player.getIPAddress()).append(", ").append(item.getName()).append("(").append(item.getCount()).append(") [").append(item.getObjectId()).append("], ").append(scroll.getName()).append("(").append(scroll.getCount()).append(") [").append(scroll.getObjectId()).append("], ").append(support.getName()).append("(").append(support.getCount()).append(") [").append(support.getObjectId()).append("]").toString());
								}
							}
						}
						else
						{
							// add challenge point
							EnchantChallengePointData.getInstance().handleFailure(player, item);
							player.sendPacket(new ExEnchantChallengePointInfo(player));
							// Enchant failed, destroy item.
							if (player.getInventory().destroyItem("Enchant", item, player, null) == null)
							{
								// Unable to destroy item, cheater?
								Util.handleIllegalPlayerAction(player, "Unable to delete item on enchant failure from " + player + ", possible cheater !", Config.DEFAULT_PUNISH);
								player.removeRequest(request.getClass());
								player.sendPacket(new EnchantResult(EnchantResult.ERROR, null, null, 0));
								if (Config.LOG_ITEM_ENCHANTS)
								{
									final StringBuilder sb = new StringBuilder();
									if (item.getEnchantLevel() > 0)
									{
										if (support == null)
										{
											LOGGER_ENCHANT.info(sb.append("Unable to destroy, Character:").append(player.getName()).append(" [").append(player.getObjectId()).append("] Account:").append(player.getAccountName()).append(" IP:").append(player.getIPAddress()).append(", +").append(item.getEnchantLevel()).append(" ").append(item.getName()).append("(").append(item.getCount()).append(") [").append(item.getObjectId()).append("], ").append(scroll.getName()).append("(").append(scroll.getCount()).append(") [").append(scroll.getObjectId()).append("]").toString());
										}
										else
										{
											LOGGER_ENCHANT.info(sb.append("Unable to destroy, Character:").append(player.getName()).append(" [").append(player.getObjectId()).append("] Account:").append(player.getAccountName()).append(" IP:").append(player.getIPAddress()).append(", +").append(item.getEnchantLevel()).append(" ").append(item.getName()).append("(").append(item.getCount()).append(") [").append(item.getObjectId()).append("], ").append(scroll.getName()).append("(").append(scroll.getCount()).append(") [").append(scroll.getObjectId()).append("], ").append(support.getName()).append("(").append(support.getCount()).append(") [").append(support.getObjectId()).append("]").toString());
										}
									}
									else if (support == null)
									{
										LOGGER_ENCHANT.info(sb.append("Unable to destroy, Character:").append(player.getName()).append(" [").append(player.getObjectId()).append("] Account:").append(player.getAccountName()).append(" IP:").append(player.getIPAddress()).append(", ").append(item.getName()).append("(").append(item.getCount()).append(") [").append(item.getObjectId()).append("], ").append(scroll.getName()).append("(").append(scroll.getCount()).append(") [").append(scroll.getObjectId()).append("]").toString());
									}
									else
									{
										LOGGER_ENCHANT.info(sb.append("Unable to destroy, Character:").append(player.getName()).append(" [").append(player.getObjectId()).append("] Account:").append(player.getAccountName()).append(" IP:").append(player.getIPAddress()).append(", ").append(item.getName()).append("(").append(item.getCount()).append(") [").append(item.getObjectId()).append("], ").append(scroll.getName()).append("(").append(scroll.getCount()).append(") [").append(scroll.getObjectId()).append("], ").append(support.getName()).append("(").append(support.getCount()).append(") [").append(support.getObjectId()).append("]").toString());
									}
								}
								return;
							}
							
							World.getInstance().removeObject(item);
							
							int count = 0;
							if (item.getTemplate().isCrystallizable())
							{
								count = Math.max(0, item.getCrystalCount() - ((item.getTemplate().getCrystalCount() + 1) / 2));
							}
							
							Item crystals = null;
							final int crystalId = item.getTemplate().getCrystalItemId();
							if (count > 0)
							{
								crystals = player.getInventory().addItem("Enchant", crystalId, count, player, item);
								final SystemMessage sm = new SystemMessage(SystemMessageId.YOU_HAVE_OBTAINED_S1_X_S2);
								sm.addItemName(crystals);
								sm.addLong(count);
								player.sendPacket(sm);
							}
							
							// if (crystals != null)
							// {
							// iu.addItem(crystals); // FIXME: Packet never sent?
							// }
							
							if ((crystalId == 0) || (count == 0))
							{
								player.sendPacket(new EnchantResult(EnchantResult.NO_CRYSTAL, null, null, 0));
							}
							else
							{
								final ItemChanceHolder destroyReward = ItemCrystallizationData.getInstance().getItemOnDestroy(player, item);
								if ((destroyReward != null) && (Rnd.get(100) < destroyReward.getChance()))
								{
									player.addItem("Enchant", destroyReward, player, true);
									player.sendPacket(new EnchantResult(EnchantResult.FAIL, new ItemHolder(crystalId, count), destroyReward, 0));
								}
								else
								{
									player.sendPacket(new EnchantResult(EnchantResult.FAIL, new ItemHolder(crystalId, count), null, 0));
								}
							}
							player.sendPacket(new ExEnchantChallengePointInfo(player));
							if (Config.LOG_ITEM_ENCHANTS)
							{
								final StringBuilder sb = new StringBuilder();
								if (item.getEnchantLevel() > 0)
								{
									if (support == null)
									{
										LOGGER_ENCHANT.info(sb.append("Fail, Character:").append(player.getName()).append(" [").append(player.getObjectId()).append("] Account:").append(player.getAccountName()).append(" IP:").append(player.getIPAddress()).append(", +").append(item.getEnchantLevel()).append(" ").append(item.getName()).append("(").append(item.getCount()).append(") [").append(item.getObjectId()).append("], ").append(scroll.getName()).append("(").append(scroll.getCount()).append(") [").append(scroll.getObjectId()).append("]").toString());
									}
									else
									{
										LOGGER_ENCHANT.info(sb.append("Fail, Character:").append(player.getName()).append(" [").append(player.getObjectId()).append("] Account:").append(player.getAccountName()).append(" IP:").append(player.getIPAddress()).append(", +").append(item.getEnchantLevel()).append(" ").append(item.getName()).append("(").append(item.getCount()).append(") [").append(item.getObjectId()).append("], ").append(scroll.getName()).append("(").append(scroll.getCount()).append(") [").append(scroll.getObjectId()).append("], ").append(support.getName()).append("(").append(support.getCount()).append(") [").append(support.getObjectId()).append("]").toString());
									}
								}
								else if (support == null)
								{
									LOGGER_ENCHANT.info(sb.append("Fail, Character:").append(player.getName()).append(" [").append(player.getObjectId()).append("] Account:").append(player.getAccountName()).append(" IP:").append(player.getIPAddress()).append(", ").append(item.getName()).append("(").append(item.getCount()).append(") [").append(item.getObjectId()).append("], ").append(scroll.getName()).append("(").append(scroll.getCount()).append(") [").append(scroll.getObjectId()).append("]").toString());
								}
								else
								{
									LOGGER_ENCHANT.info(sb.append("Fail, Character:").append(player.getName()).append(" [").append(player.getObjectId()).append("] Account:").append(player.getAccountName()).append(" IP:").append(player.getIPAddress()).append(", ").append(item.getName()).append("(").append(item.getCount()).append(") [").append(item.getObjectId()).append("], ").append(scroll.getName()).append("(").append(scroll.getCount()).append(") [").append(scroll.getObjectId()).append("], ").append(support.getName()).append("(").append(support.getCount()).append(") [").append(support.getObjectId()).append("]").toString());
								}
							}
						}
					}
					break;
				}
			}
			
			if (challengePointsGroupId >= 0)
			{
				player.getChallengeInfo().setChallengePointsPendingRecharge(-1, -1);
				player.getChallengeInfo().addChallengePointsRecharge(challengePointsGroupId, challengePointsOptionIndex, -1);
				player.sendPacket(new ExEnchantChallengePointInfo(player));
			}
			
			player.sendItemList();
			player.broadcastUserInfo();
			
			request.setProcessing(false);
		}
	}
}
