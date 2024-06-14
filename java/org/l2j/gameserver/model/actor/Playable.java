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
package org.l2j.gameserver.model.actor;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.l2j.gameserver.ai.CtrlEvent;
import org.l2j.gameserver.enums.ClanWarState;
import org.l2j.gameserver.enums.InstanceType;
import org.l2j.gameserver.instancemanager.ZoneManager;
import org.l2j.gameserver.model.WorldObject;
import org.l2j.gameserver.model.actor.stat.PlayableStat;
import org.l2j.gameserver.model.actor.status.PlayableStatus;
import org.l2j.gameserver.model.actor.templates.CreatureTemplate;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.model.clan.ClanWar;
import org.l2j.gameserver.model.effects.EffectFlag;
import org.l2j.gameserver.model.events.EventDispatcher;
import org.l2j.gameserver.model.events.EventType;
import org.l2j.gameserver.model.events.impl.creature.OnCreatureDeath;
import org.l2j.gameserver.model.events.returns.TerminateReturn;
import org.l2j.gameserver.model.instancezone.Instance;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.quest.QuestState;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.network.serverpackets.EtcStatusUpdate;

/**
 * This class represents all Playable characters in the world.<br>
 * Playable:
 * <ul>
 * <li>Player</li>
 * <li>Summon</li>
 * </ul>
 */
public abstract class Playable extends Creature
{
	private Creature _lockedTarget = null;
	private Player transferDmgTo = null;
	
	private final Map<Integer, Integer> _replacedSkills = new ConcurrentHashMap<>(1);
	private final Map<Integer, Integer> _originalSkills = new ConcurrentHashMap<>(1);
	
	/**
	 * Constructor of Playable.<br>
	 * <br>
	 * <b><u>Actions</u>:</b>
	 * <ul>
	 * <li>Call the Creature constructor to create an empty _skills slot and link copy basic Calculator set to this Playable</li>
	 * </ul>
	 * @param objectId the object id
	 * @param template The CreatureTemplate to apply to the Playable
	 */
	public Playable(int objectId, CreatureTemplate template)
	{
		super(objectId, template);
		setInstanceType(InstanceType.Playable);
		setInvul(false);
	}
	
	public Playable(CreatureTemplate template)
	{
		super(template);
		setInstanceType(InstanceType.Playable);
		setInvul(false);
	}
	
	@Override
	public PlayableStat getStat()
	{
		return (PlayableStat) super.getStat();
	}
	
	@Override
	public void initCharStat()
	{
		setStat(new PlayableStat(this));
	}
	
	@Override
	public PlayableStatus getStatus()
	{
		return (PlayableStatus) super.getStatus();
	}
	
	@Override
	public void initCharStatus()
	{
		setStatus(new PlayableStatus(this));
	}
	
	@Override
	public boolean doDie(Creature killer)
	{
		if (EventDispatcher.getInstance().hasListener(EventType.ON_CREATURE_DEATH, this))
		{
			final TerminateReturn returnBack = EventDispatcher.getInstance().notifyEvent(new OnCreatureDeath(killer, this), this, TerminateReturn.class);
			if ((returnBack != null) && returnBack.terminate())
			{
				return false;
			}
		}
		
		// killing is only possible one time
		synchronized (this)
		{
			if (isDead())
			{
				return false;
			}
			// now reset currentHp to zero
			setCurrentHp(0);
			setDead(true);
		}
		
		// Set target to null and cancel Attack or Cast
		setTarget(null);
		
		// Abort casting after target has been cancelled.
		abortAttack();
		abortCast();
		
		// Stop movement
		stopMove(null);
		
		// Stop HP/MP/CP Regeneration task
		getStatus().stopHpMpRegeneration();
		
		boolean deleteBuffs = true;
		if (isNoblesseBlessedAffected())
		{
			stopEffects(EffectFlag.NOBLESS_BLESSING);
			deleteBuffs = false;
		}
		if (isResurrectSpecialAffected())
		{
			stopEffects(EffectFlag.RESURRECTION_SPECIAL);
			deleteBuffs = false;
		}
		if (isPlayer())
		{
			final Player player = getActingPlayer();
			if (player.hasCharmOfCourage())
			{
				if (player.isInSiege())
				{
					getActingPlayer().reviveRequest(getActingPlayer(), false, 0, 0, 0, 0);
				}
				player.setCharmOfCourage(false);
				player.sendPacket(new EtcStatusUpdate(player));
			}
		}
		
		if (deleteBuffs)
		{
			stopAllEffectsExceptThoseThatLastThroughDeath();
		}
		
		// Send the Server->Client packet StatusUpdate with current HP and MP to all other Player to inform
		broadcastStatusUpdate();
		
		ZoneManager.getInstance().getRegion(this).onDeath(this);
		
		// Notify Quest of Playable's death
		final Player actingPlayer = getActingPlayer();
		if (!actingPlayer.isNotifyQuestOfDeathEmpty())
		{
			for (QuestState qs : actingPlayer.getNotifyQuestOfDeath())
			{
				qs.getQuest().notifyDeath((killer == null ? this : killer), this, qs);
			}
		}
		
		// Notify instance
		if (isPlayer())
		{
			final Instance instance = getInstanceWorld();
			if (instance != null)
			{
				instance.onDeath(getActingPlayer());
			}
		}
		
		if (killer != null)
		{
			final Player killerPlayer = killer.getActingPlayer();
			if (killerPlayer != null)
			{
				killerPlayer.onPlayerKill(this);
			}
		}
		
		// Notify Creature AI
		getAI().notifyEvent(CtrlEvent.EVT_DEAD);
		return true;
	}
	
	public boolean checkIfPvP(Player target)
	{
		final Player player = getActingPlayer();
		if ((player == null) //
			|| (target == null) //
			|| (player == target) //
			|| (target.getReputation() < 0) //
			|| (target.getPvpFlag() > 0) //
			|| target.isOnDarkSide())
		{
			return true;
		}
		else if (player.isInParty() && player.getParty().containsPlayer(target))
		{
			return false;
		}
		
		final Clan playerClan = player.getClan();
		if ((playerClan != null) && !player.isAcademyMember() && !target.isAcademyMember())
		{
			final ClanWar war = playerClan.getWarWith(target.getClanId());
			return (war != null) && (war.getState() == ClanWarState.MUTUAL);
		}
		return false;
	}
	
	/**
	 * Return True.
	 */
	@Override
	public boolean canBeAttacked()
	{
		return true;
	}
	
	// Support for Noblesse Blessing skill, where buffs are retained after resurrect
	public boolean isNoblesseBlessedAffected()
	{
		return isAffected(EffectFlag.NOBLESS_BLESSING);
	}
	
	/**
	 * @return {@code true} if char can resurrect by himself, {@code false} otherwise
	 */
	public boolean isResurrectSpecialAffected()
	{
		return isAffected(EffectFlag.RESURRECTION_SPECIAL);
	}
	
	/**
	 * @return {@code true} if the Silent Moving mode is active, {@code false} otherwise
	 */
	public boolean isSilentMovingAffected()
	{
		return isAffected(EffectFlag.SILENT_MOVE);
	}
	
	/**
	 * For Newbie Protection Blessing skill, keeps you safe from an attack by a chaotic character >= 10 levels apart from you.
	 * @return
	 */
	public boolean isProtectionBlessingAffected()
	{
		return isAffected(EffectFlag.PROTECTION_BLESSING);
	}
	
	@Override
	public void updateEffectIcons(boolean partyOnly)
	{
		getEffectList().updateEffectIcons(partyOnly);
	}
	
	public boolean isLockedTarget()
	{
		return _lockedTarget != null;
	}
	
	public Creature getLockedTarget()
	{
		return _lockedTarget;
	}
	
	public void setLockedTarget(Creature creature)
	{
		_lockedTarget = creature;
	}
	
	public void setTransferDamageTo(Player val)
	{
		transferDmgTo = val;
	}
	
	public Player getTransferingDamageTo()
	{
		return transferDmgTo;
	}
	
	/**
	 * Adds a replacement for an original skill.<br>
	 * Both original and replacement skill IDs are stored in their respective maps.
	 * @param originalId The ID of the original skill.
	 * @param replacementId The ID of the replacement skill.
	 */
	public void addReplacedSkill(int originalId, int replacementId)
	{
		_replacedSkills.put(originalId, replacementId);
		_originalSkills.put(replacementId, originalId);
	}
	
	/**
	 * Removes a replaced skill by the original skill ID.<br>
	 * The corresponding replacement skill ID is also removed from its map.
	 * @param originalId The ID of the original skill to be removed.
	 */
	public void removeReplacedSkill(int originalId)
	{
		final Integer replacementId = _replacedSkills.remove(originalId);
		if (replacementId != null)
		{
			_originalSkills.remove(replacementId);
		}
	}
	
	/**
	 * Retrieves the replacement skill for a given original skill.
	 * @param originalId The ID of the original skill.
	 * @return The ID of the replacement skill if it exists, or the original skill ID.
	 */
	public int getReplacementSkill(int originalId)
	{
		return _replacedSkills.getOrDefault(originalId, originalId);
	}
	
	/**
	 * Retrieves the original skill for a given replacement skill.
	 * @param replacementId The ID of the replacement skill.
	 * @return The ID of the original skill if it exists, or the replacement skill ID.
	 */
	public int getOriginalSkill(int replacementId)
	{
		return _originalSkills.getOrDefault(replacementId, replacementId);
	}
	
	/**
	 * Retrieves a collection of all original skills that have been replaced.
	 * @return The collection of all replaced skill IDs.
	 */
	public Collection<Integer> getReplacedSkills()
	{
		return _replacedSkills.keySet();
	}
	
	public abstract void doPickupItem(WorldObject object);
	
	public abstract boolean useMagic(Skill skill, Item item, boolean forceUse, boolean dontMove);
	
	public abstract void storeMe();
	
	public abstract void storeEffect(boolean storeEffects);
	
	public abstract void restoreEffects();
	
	public boolean isOnEvent()
	{
		return false;
	}
	
	@Override
	public boolean isPlayable()
	{
		return true;
	}
}
