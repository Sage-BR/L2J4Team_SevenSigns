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
package org.l2j.gameserver.taskmanager;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.l2j.Config;
import org.l2j.commons.threads.ThreadPool;
import org.l2j.gameserver.data.xml.ActionData;
import org.l2j.gameserver.handler.IItemHandler;
import org.l2j.gameserver.handler.IPlayerActionHandler;
import org.l2j.gameserver.handler.ItemHandler;
import org.l2j.gameserver.handler.PlayerActionHandler;
import org.l2j.gameserver.model.ActionDataHolder;
import org.l2j.gameserver.model.WorldObject;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.actor.Playable;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.Summon;
import org.l2j.gameserver.model.actor.instance.Guard;
import org.l2j.gameserver.model.actor.instance.Pet;
import org.l2j.gameserver.model.actor.transform.TransformTemplate;
import org.l2j.gameserver.model.effects.AbstractEffect;
import org.l2j.gameserver.model.effects.EffectType;
import org.l2j.gameserver.model.events.EventDispatcher;
import org.l2j.gameserver.model.events.EventType;
import org.l2j.gameserver.model.events.impl.item.OnItemUse;
import org.l2j.gameserver.model.holders.AttachSkillHolder;
import org.l2j.gameserver.model.holders.ItemSkillHolder;
import org.l2j.gameserver.model.item.EtcItem;
import org.l2j.gameserver.model.item.ItemTemplate;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.skill.AbnormalType;
import org.l2j.gameserver.model.skill.BuffInfo;
import org.l2j.gameserver.model.skill.EffectScope;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.model.skill.targets.AffectScope;
import org.l2j.gameserver.model.skill.targets.TargetType;
import org.l2j.gameserver.model.zone.ZoneId;
import org.l2j.gameserver.network.SystemMessageId;

/**
 * @author Mobius
 */
public class AutoUseTaskManager
{
	private static final Set<Set<Player>> POOLS = ConcurrentHashMap.newKeySet();
	private static final int POOL_SIZE = 300;
	private static final int TASK_DELAY = 300;
	private static final int REUSE_MARGIN_TIME = 3;
	
	protected AutoUseTaskManager()
	{
	}
	
	private class AutoUse implements Runnable
	{
		private final Set<Player> _players;
		
		public AutoUse(Set<Player> players)
		{
			_players = players;
		}
		
		@Override
		public void run()
		{
			if (_players.isEmpty())
			{
				return;
			}
			
			for (Player player : _players)
			{
				if (player.getAutoUseSettings().isEmpty() || !player.isOnline() || (player.isInOfflineMode() && !player.isOfflinePlay()))
				{
					stopAutoUseTask(player);
					continue;
				}
				
				if (player.isSitting() || player.hasBlockActions() || player.isControlBlocked() || player.isAlikeDead() || player.isMounted() || (player.isTransformed() && player.getTransformation().get().isRiding()))
				{
					continue;
				}
				
				final boolean isInPeaceZone = player.isInsideZone(ZoneId.PEACE) || player.isInsideZone(ZoneId.SAYUNE);
				
				if (Config.ENABLE_AUTO_ITEM && !isInPeaceZone)
				{
					final Pet pet = player.getPet();
					ITEMS: for (Integer itemId : player.getAutoUseSettings().getAutoSupplyItems())
					{
						if (player.isTeleporting())
						{
							break ITEMS;
						}
						
						final Item item = player.getInventory().getItemByItemId(itemId.intValue());
						if (item == null)
						{
							player.getAutoUseSettings().getAutoSupplyItems().remove(itemId);
							continue ITEMS;
						}
						
						// Pet food is managed by Pet FeedTask.
						if ((pet != null) && pet.getPetData().getFood().contains(itemId))
						{
							continue ITEMS;
						}
						
						final ItemTemplate it = item.getTemplate();
						if (it != null)
						{
							if (!it.checkCondition(player, player, false))
							{
								continue ITEMS;
							}
							
							final List<ItemSkillHolder> skills = it.getAllSkills();
							if (skills != null)
							{
								for (ItemSkillHolder itemSkillHolder : skills)
								{
									final Skill skill = itemSkillHolder.getSkill();
									if (player.isAffectedBySkill(skill.getId()) || player.hasSkillReuse(skill.getReuseHashCode()) || !skill.checkCondition(player, player, false))
									{
										continue ITEMS;
									}
									
									// Check item skills that affect pets.
									if ((pet != null) && !pet.isDead() && (pet.isAffectedBySkill(skill.getId()) || pet.hasSkillReuse(skill.getReuseHashCode()) || !skill.checkCondition(pet, pet, false)))
									{
										continue ITEMS;
									}
								}
							}
						}
						
						final int reuseDelay = item.getReuseDelay();
						if ((reuseDelay <= 0) || (player.getItemRemainingReuseTime(item.getObjectId()) <= 0))
						{
							final EtcItem etcItem = item.getEtcItem();
							final IItemHandler handler = ItemHandler.getInstance().getHandler(etcItem);
							if ((handler != null) && handler.useItem(player, item, false))
							{
								if (reuseDelay > 0)
								{
									player.addTimeStampItem(item, reuseDelay);
								}
								
								// Notify events.
								if (EventDispatcher.getInstance().hasListener(EventType.ON_ITEM_USE, item.getTemplate()))
								{
									EventDispatcher.getInstance().notifyEventAsync(new OnItemUse(player, item), item.getTemplate());
								}
							}
						}
					}
				}
				
				if (Config.ENABLE_AUTO_POTION && !isInPeaceZone && (player.getCurrentHpPercent() < player.getAutoPlaySettings().getAutoPotionPercent()))
				{
					final int itemId = player.getAutoUseSettings().getAutoPotionItem();
					if (itemId > 0)
					{
						final Item item = player.getInventory().getItemByItemId(itemId);
						if (item == null)
						{
							player.getAutoUseSettings().setAutoPotionItem(0);
						}
						else
						{
							final int reuseDelay = item.getReuseDelay();
							if ((reuseDelay <= 0) || (player.getItemRemainingReuseTime(item.getObjectId()) <= 0))
							{
								final EtcItem etcItem = item.getEtcItem();
								final IItemHandler handler = ItemHandler.getInstance().getHandler(etcItem);
								if ((handler != null) && handler.useItem(player, item, false))
								{
									if (reuseDelay > 0)
									{
										player.addTimeStampItem(item, reuseDelay);
									}
									
									// Notify events.
									if (EventDispatcher.getInstance().hasListener(EventType.ON_ITEM_USE, item.getTemplate()))
									{
										EventDispatcher.getInstance().notifyEventAsync(new OnItemUse(player, item), item.getTemplate());
									}
								}
							}
						}
					}
				}
				
				if (Config.ENABLE_AUTO_PET_POTION && !isInPeaceZone)
				{
					final Pet pet = player.getPet();
					if ((pet != null) && !pet.isDead())
					{
						final int percent = pet.getCurrentHpPercent();
						if ((percent < 100) && (percent <= player.getAutoPlaySettings().getAutoPetPotionPercent()))
						{
							final int itemId = player.getAutoUseSettings().getAutoPetPotionItem();
							if (itemId > 0)
							{
								final Item item = player.getInventory().getItemByItemId(itemId);
								if (item == null)
								{
									player.getAutoUseSettings().setAutoPetPotionItem(0);
								}
								else
								{
									final int reuseDelay = item.getReuseDelay();
									if ((reuseDelay <= 0) || (player.getItemRemainingReuseTime(item.getObjectId()) <= 0))
									{
										final EtcItem etcItem = item.getEtcItem();
										final IItemHandler handler = ItemHandler.getInstance().getHandler(etcItem);
										if ((handler != null) && handler.useItem(player, item, false) && (reuseDelay > 0))
										{
											player.addTimeStampItem(item, reuseDelay);
										}
									}
								}
							}
						}
					}
				}
				
				if (Config.ENABLE_AUTO_SKILL)
				{
					BUFFS: for (Integer skillId : player.getAutoUseSettings().getAutoBuffs())
					{
						// Fixes start area issue.
						
						// Already casting.
						// Player is teleporting.
						if (isInPeaceZone || player.isCastingNow() || player.isTeleporting())
						{
							break BUFFS;
						}
						
						Playable pet = null;
						Skill skill = player.getKnownSkill(skillId.intValue());
						if (skill == null)
						{
							if (player.hasServitors())
							{
								SUMMON_SEARCH: for (Summon summon : player.getServitors().values())
								{
									skill = summon.getKnownSkill(skillId.intValue());
									if (skill != null)
									{
										pet = summon;
										break SUMMON_SEARCH;
									}
								}
							}
							if ((skill == null) && player.hasPet())
							{
								pet = player.getPet();
								skill = pet.getKnownSkill(skillId.intValue());
							}
							if (skill == null)
							{
								player.getAutoUseSettings().getAutoBuffs().remove(skillId);
								continue BUFFS;
							}
						}
						
						final WorldObject target = player.getTarget();
						if (canCastBuff(player, target, skill))
						{
							ATTACH_SEARCH: for (AttachSkillHolder holder : skill.getAttachSkills())
							{
								if (player.isAffectedBySkill(holder.getRequiredSkillId()))
								{
									skill = holder.getSkill();
									break ATTACH_SEARCH;
								}
							}
							
							// Playable target cast.
							final Playable caster = pet != null ? pet : player;
							if ((target != null) && target.isPlayable() && (target.getActingPlayer().getPvpFlag() == 0) && (target.getActingPlayer().getReputation() >= 0))
							{
								caster.doCast(skill);
							}
							else // Target self, cast and re-target.
							{
								final WorldObject savedTarget = target;
								caster.setTarget(caster);
								caster.doCast(skill);
								caster.setTarget(savedTarget);
							}
						}
					}
					
					// Continue when auto play is not enabled.
					if (!player.isAutoPlaying())
					{
						continue;
					}
					
					SKILLS:
					{
						// Already casting.
						// Player is teleporting.
						if (player.isCastingNow() || player.isTeleporting())
						{
							break SKILLS;
						}
						
						// Acquire next skill.
						Playable pet = null;
						final Integer skillId = player.getAutoUseSettings().getNextSkillId();
						Skill skill = player.getKnownSkill(skillId.intValue());
						if (skill == null)
						{
							if (player.hasServitors())
							{
								SUMMON_SEARCH: for (Summon summon : player.getServitors().values())
								{
									skill = summon.getKnownSkill(skillId.intValue());
									if (skill != null)
									{
										pet = summon;
										break SUMMON_SEARCH;
									}
								}
							}
							if ((skill == null) && player.hasPet())
							{
								pet = player.getPet();
								skill = pet.getKnownSkill(skillId.intValue());
								if (pet.isSkillDisabled(skill))
								{
									player.getAutoUseSettings().incrementSkillOrder();
									break SKILLS;
								}
							}
							if (skill == null)
							{
								player.getAutoUseSettings().getAutoSkills().remove(skillId);
								player.getAutoUseSettings().resetSkillOrder();
								break SKILLS;
							}
						}
						
						// Casting on self stops movement.
						final WorldObject target = player.getTarget();
						if (target == player)
						{
							break SKILLS;
						}
						
						// Check bad skill target.
						if ((target == null) || ((Creature) target).isDead())
						{
							break SKILLS;
						}
						
						// Peace zone and auto attackable checks.
						if (target.isInsideZone(ZoneId.PEACE) || !target.isAutoAttackable(player))
						{
							break SKILLS;
						}
						
						// Do not attack guards.
						if (target instanceof Guard)
						{
							final int targetMode = player.getAutoPlaySettings().getNextTargetMode();
							if ((targetMode != 3 /* NPC */) && (targetMode != 0 /* Any Target */))
							{
								break SKILLS;
							}
						}
						
						if (!canUseMagic(player, target, skill) || (pet != null ? pet : player).useMagic(skill, null, true, false))
						{
							player.getAutoUseSettings().incrementSkillOrder();
						}
					}
					
					ACTIONS: for (Integer actionId : player.getAutoUseSettings().getAutoActions())
					{
						final BuffInfo info = player.getEffectList().getFirstBuffInfoByAbnormalType(AbnormalType.BOT_PENALTY);
						if (info != null)
						{
							for (AbstractEffect effect : info.getEffects())
							{
								if (!effect.checkCondition(actionId))
								{
									player.sendPacket(SystemMessageId.YOU_HAVE_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_SO_YOUR_ACTIONS_HAVE_BEEN_RESTRICTED);
									break ACTIONS;
								}
							}
						}
						
						// Do not allow to do some action if player is transformed.
						if (player.isTransformed())
						{
							final TransformTemplate transformTemplate = player.getTransformation().get().getTemplate(player);
							final int[] allowedActions = transformTemplate.getBasicActionList();
							if ((allowedActions == null) || (Arrays.binarySearch(allowedActions, actionId) < 0))
							{
								continue ACTIONS;
							}
						}
						
						final ActionDataHolder actionHolder = ActionData.getInstance().getActionData(actionId);
						if (actionHolder != null)
						{
							final IPlayerActionHandler actionHandler = PlayerActionHandler.getInstance().getHandler(actionHolder.getHandler());
							if (actionHandler != null)
							{
								if (!actionHandler.isPetAction())
								{
									actionHandler.useAction(player, actionHolder, false, false);
								}
								else
								{
									final Summon summon = player.getAnyServitor();
									if ((summon != null) && !summon.isAlikeDead())
									{
										final Skill skill = summon.getKnownSkill(actionHolder.getOptionId());
										if ((skill != null) && !canSummonCastSkill(player, summon, skill))
										{
											continue ACTIONS;
										}
										
										actionHandler.useAction(player, actionHolder, false, false);
									}
								}
							}
						}
					}
				}
			}
		}
		
		private boolean canCastBuff(Player player, WorldObject target, Skill skill)
		{
			// Summon check.
			if ((skill.getAffectScope() == AffectScope.SUMMON_EXCEPT_MASTER) || (skill.getTargetType() == TargetType.SUMMON))
			{
				if (!player.hasServitors())
				{
					return false;
				}
				int occurrences = 0;
				for (Summon servitor : player.getServitors().values())
				{
					if (servitor.isAffectedBySkill(skill.getId()))
					{
						occurrences++;
					}
				}
				if (occurrences == player.getServitors().size())
				{
					return false;
				}
			}
			
			if ((target != null) && target.isCreature() && ((Creature) target).isAlikeDead() && (skill.getTargetType() != TargetType.SELF) && (skill.getTargetType() != TargetType.NPC_BODY) && (skill.getTargetType() != TargetType.PC_BODY))
			{
				return false;
			}
			
			final Playable playableTarget = (target == null) || !target.isPlayable() || (skill.getTargetType() == TargetType.SELF) ? player : (Playable) target;
			if (((player != playableTarget) && (player.calculateDistance3D(playableTarget) > skill.getCastRange())) || !canUseMagic(player, playableTarget, skill))
			{
				return false;
			}
			
			final BuffInfo buffInfo = playableTarget.getEffectList().getBuffInfoBySkillId(skill.getId());
			final BuffInfo abnormalBuffInfo = playableTarget.getEffectList().getFirstBuffInfoByAbnormalType(skill.getAbnormalType());
			if (abnormalBuffInfo != null)
			{
				if (buffInfo != null)
				{
					return (abnormalBuffInfo.getSkill().getId() == buffInfo.getSkill().getId()) && ((buffInfo.getTime() <= REUSE_MARGIN_TIME) || (buffInfo.getSkill().getLevel() < skill.getLevel()));
				}
				return (abnormalBuffInfo.getSkill().getAbnormalLevel() < skill.getAbnormalLevel()) || abnormalBuffInfo.isAbnormalType(AbnormalType.NONE);
			}
			return buffInfo == null;
		}
		
		private boolean canUseMagic(Player player, WorldObject target, Skill skill)
		{
			if (((skill.getItemConsumeCount() > 0) && (player.getInventory().getInventoryItemCount(skill.getItemConsumeId(), -1) < skill.getItemConsumeCount())) || ((skill.getMpConsume() > 0) && (player.getCurrentMp() < skill.getMpConsume())))
			{
				return false;
			}
			
			for (AttachSkillHolder holder : skill.getAttachSkills())
			{
				if (player.isAffectedBySkill(holder.getRequiredSkillId()) //
					&& (player.hasSkillReuse(holder.getSkill().getReuseHashCode()) || player.isAffectedBySkill(holder)))
				{
					return false;
				}
			}
			
			return !player.isSkillDisabled(skill) && skill.checkCondition(player, target, false);
		}
		
		private boolean canSummonCastSkill(Player player, Summon summon, Skill skill)
		{
			if (skill.isBad() && (player.getTarget() == null))
			{
				return false;
			}
			
			final int mpConsume = skill.getMpConsume() + skill.getMpInitialConsume();
			if ((((mpConsume != 0) && (mpConsume > (int) Math.floor(summon.getCurrentMp()))) || ((skill.getHpConsume() != 0) && (skill.getHpConsume() > (int) Math.floor(summon.getCurrentHp())))))
			{
				return false;
			}
			
			if (summon.isSkillDisabled(skill))
			{
				return false;
			}
			
			if (((player.getTarget() != null) && !skill.checkCondition(summon, player.getTarget(), false)) || ((player.getTarget() == null) && !skill.checkCondition(summon, player, false)))
			{
				return false;
			}
			
			if ((skill.getItemConsumeCount() > 0) && (summon.getInventory().getInventoryItemCount(skill.getItemConsumeId(), -1) < skill.getItemConsumeCount()))
			{
				return false;
			}
			
			if (skill.getTargetType().equals(TargetType.SELF) || skill.getTargetType().equals(TargetType.SUMMON))
			{
				final BuffInfo summonInfo = summon.getEffectList().getBuffInfoBySkillId(skill.getId());
				return (summonInfo != null) && (summonInfo.getTime() >= REUSE_MARGIN_TIME);
			}
			
			if ((skill.getEffects(EffectScope.GENERAL) != null) && skill.getEffects(EffectScope.GENERAL).stream().anyMatch(a -> a.getEffectType().equals(EffectType.MANAHEAL_BY_LEVEL)) && (player.getCurrentMpPercent() > 80))
			{
				return false;
			}
			
			final BuffInfo buffInfo = player.getEffectList().getBuffInfoBySkillId(skill.getId());
			final BuffInfo abnormalBuffInfo = player.getEffectList().getFirstBuffInfoByAbnormalType(skill.getAbnormalType());
			if (abnormalBuffInfo != null)
			{
				if (buffInfo != null)
				{
					return (abnormalBuffInfo.getSkill().getId() == buffInfo.getSkill().getId()) && ((buffInfo.getTime() <= REUSE_MARGIN_TIME) || (buffInfo.getSkill().getLevel() < skill.getLevel()));
				}
				return (abnormalBuffInfo.getSkill().getAbnormalLevel() < skill.getAbnormalLevel()) || abnormalBuffInfo.isAbnormalType(AbnormalType.NONE);
			}
			
			return true;
		}
	}
	
	public synchronized void startAutoUseTask(Player player)
	{
		for (Set<Player> pool : POOLS)
		{
			if (pool.contains(player))
			{
				return;
			}
		}
		
		for (Set<Player> pool : POOLS)
		{
			if (pool.size() < POOL_SIZE)
			{
				pool.add(player);
				return;
			}
		}
		
		final Set<Player> pool = ConcurrentHashMap.newKeySet(POOL_SIZE);
		pool.add(player);
		ThreadPool.scheduleAtFixedRate(new AutoUse(pool), TASK_DELAY, TASK_DELAY);
		POOLS.add(pool);
	}
	
	public void stopAutoUseTask(Player player)
	{
		player.getAutoUseSettings().resetSkillOrder();
		if (player.getAutoUseSettings().isEmpty() || !player.isOnline() || (player.isInOfflineMode() && !player.isOfflinePlay()))
		{
			for (Set<Player> pool : POOLS)
			{
				if (pool.remove(player))
				{
					return;
				}
			}
		}
	}
	
	public void addAutoSupplyItem(Player player, int itemId)
	{
		player.getAutoUseSettings().getAutoSupplyItems().add(itemId);
		startAutoUseTask(player);
	}
	
	public void removeAutoSupplyItem(Player player, int itemId)
	{
		player.getAutoUseSettings().getAutoSupplyItems().remove(itemId);
		stopAutoUseTask(player);
	}
	
	public void setAutoPotionItem(Player player, int itemId)
	{
		player.getAutoUseSettings().setAutoPotionItem(itemId);
		startAutoUseTask(player);
	}
	
	public void removeAutoPotionItem(Player player)
	{
		player.getAutoUseSettings().setAutoPotionItem(0);
		stopAutoUseTask(player);
	}
	
	public void setAutoPetPotionItem(Player player, int itemId)
	{
		player.getAutoUseSettings().setAutoPetPotionItem(itemId);
		startAutoUseTask(player);
	}
	
	public void removeAutoPetPotionItem(Player player)
	{
		player.getAutoUseSettings().setAutoPetPotionItem(0);
		stopAutoUseTask(player);
	}
	
	public void addAutoBuff(Player player, int skillId)
	{
		player.getAutoUseSettings().getAutoBuffs().add(skillId);
		startAutoUseTask(player);
	}
	
	public void removeAutoBuff(Player player, int skillId)
	{
		player.getAutoUseSettings().getAutoBuffs().remove(skillId);
		stopAutoUseTask(player);
	}
	
	public void addAutoSkill(Player player, Integer skillId)
	{
		player.getAutoUseSettings().getAutoSkills().add(skillId);
		startAutoUseTask(player);
	}
	
	public void removeAutoSkill(Player player, Integer skillId)
	{
		player.getAutoUseSettings().getAutoSkills().remove(skillId);
		stopAutoUseTask(player);
	}
	
	public void addAutoAction(Player player, int actionId)
	{
		player.getAutoUseSettings().getAutoActions().add(actionId);
		startAutoUseTask(player);
	}
	
	public void removeAutoAction(Player player, int actionId)
	{
		player.getAutoUseSettings().getAutoActions().remove(actionId);
		stopAutoUseTask(player);
	}
	
	public static AutoUseTaskManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final AutoUseTaskManager INSTANCE = new AutoUseTaskManager();
	}
}
