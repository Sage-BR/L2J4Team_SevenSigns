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
package handlers.effecthandlers;

import org.l2jmobius.gameserver.data.xml.SkillData;
import org.l2jmobius.gameserver.enums.ShortcutType;
import org.l2jmobius.gameserver.model.Shortcut;
import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.actor.Playable;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.actor.instance.Pet;
import org.l2jmobius.gameserver.model.effects.AbstractEffect;
import org.l2jmobius.gameserver.model.holders.SkillHolder;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.model.skill.AbnormalType;
import org.l2jmobius.gameserver.model.skill.BuffInfo;
import org.l2jmobius.gameserver.model.skill.Skill;
import org.l2jmobius.gameserver.network.serverpackets.AbnormalStatusUpdate;
import org.l2jmobius.gameserver.network.serverpackets.pet.ExPetSkillList;

/**
 * @author Mobius
 */
public class ReplaceSkillBySkill extends AbstractEffect
{
	private final SkillHolder _existingSkill;
	private final SkillHolder _replacementSkill;
	
	public ReplaceSkillBySkill(StatSet params)
	{
		_existingSkill = new SkillHolder(params.getInt("existingSkillId"), params.getInt("existingSkillLevel", -1));
		_replacementSkill = new SkillHolder(params.getInt("replacementSkillId"), params.getInt("replacementSkillLevel", -1));
	}
	
	@Override
	public boolean canStart(Creature effector, Creature effected, Skill skill)
	{
		return effected.isPlayable() && (!effected.isTransformed() || effected.hasAbnormalType(AbnormalType.KAMAEL_TRANSFORM));
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, Item item)
	{
		final Playable playable = (Playable) effected;
		final Skill knownSkill = playable.getKnownSkill(_existingSkill.getSkillId());
		if ((knownSkill == null) || (knownSkill.getLevel() < _existingSkill.getSkillLevel()))
		{
			return;
		}
		
		final Skill addedSkill = SkillData.getInstance().getSkill(_replacementSkill.getSkillId(), _replacementSkill.getSkillLevel() < 1 ? knownSkill.getLevel() : _replacementSkill.getSkillLevel(), knownSkill.getSubLevel());
		if (playable.isPlayer())
		{
			final Player player = effected.getActingPlayer();
			player.addSkill(addedSkill, false);
			player.addReplacedSkill(_existingSkill.getSkillId(), _replacementSkill.getSkillId());
			for (Shortcut shortcut : player.getAllShortCuts())
			{
				if (shortcut.isAutoUse() && (shortcut.getType() == ShortcutType.SKILL) && (shortcut.getId() == knownSkill.getId()))
				{
					if (knownSkill.isBad())
					{
						if (player.getAutoUseSettings().getAutoSkills().contains(knownSkill.getId()))
						{
							player.getAutoUseSettings().getAutoSkills().add(addedSkill.getId());
							player.getAutoUseSettings().getAutoSkills().remove(Integer.valueOf(knownSkill.getId()));
						}
					}
					else if (player.getAutoUseSettings().getAutoBuffs().contains(knownSkill.getId()))
					{
						player.getAutoUseSettings().getAutoBuffs().add(addedSkill.getId());
						player.getAutoUseSettings().getAutoBuffs().remove(knownSkill.getId());
					}
				}
			}
			
			// Replace continuous effects.
			if (knownSkill.isContinuous() && player.isAffectedBySkill(knownSkill.getId()))
			{
				int abnormalTime = 0;
				for (BuffInfo info : player.getEffectList().getEffects())
				{
					if (info.getSkill().getId() == knownSkill.getId())
					{
						abnormalTime = info.getAbnormalTime();
						break;
					}
				}
				
				if (abnormalTime > 2000)
				{
					addedSkill.applyEffects(player, player);
					final AbnormalStatusUpdate asu = new AbnormalStatusUpdate();
					for (BuffInfo info : player.getEffectList().getEffects())
					{
						if (info.getSkill().getId() == addedSkill.getId())
						{
							info.resetAbnormalTime(abnormalTime);
							asu.addSkill(info);
						}
					}
					player.sendPacket(asu);
				}
			}
			
			player.removeSkill(knownSkill, false);
			player.sendSkillList();
		}
		else // Not player.
		{
			playable.addSkill(addedSkill);
			playable.removeSkill(knownSkill, false);
			if (playable.isPet())
			{
				final Pet pet = (Pet) playable;
				pet.sendPacket(new ExPetSkillList(false, pet));
			}
		}
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		final Playable playable = (Playable) effected;
		final int existingSkillId = _existingSkill.getSkillId();
		if (playable.getReplacementSkill(existingSkillId) == existingSkillId)
		{
			return;
		}
		
		final Skill knownSkill = playable.getKnownSkill(_replacementSkill.getSkillId());
		if (knownSkill == null)
		{
			return;
		}
		
		final Skill addedSkill = SkillData.getInstance().getSkill(existingSkillId, knownSkill.getLevel(), knownSkill.getSubLevel());
		if (playable.isPlayer())
		{
			final Player player = effected.getActingPlayer();
			player.addSkill(addedSkill, knownSkill.getLevel() != _existingSkill.getSkillLevel());
			player.removeReplacedSkill(existingSkillId);
			for (Shortcut shortcut : player.getAllShortCuts())
			{
				if (shortcut.isAutoUse() && (shortcut.getType() == ShortcutType.SKILL) && (shortcut.getId() == addedSkill.getId()))
				{
					if (knownSkill.isBad())
					{
						if (player.getAutoUseSettings().getAutoSkills().contains(knownSkill.getId()))
						{
							player.getAutoUseSettings().getAutoSkills().add(addedSkill.getId());
							player.getAutoUseSettings().getAutoSkills().remove(Integer.valueOf(knownSkill.getId()));
						}
					}
					else if (player.getAutoUseSettings().getAutoBuffs().contains(knownSkill.getId()))
					{
						player.getAutoUseSettings().getAutoBuffs().add(addedSkill.getId());
						player.getAutoUseSettings().getAutoBuffs().remove(knownSkill.getId());
					}
				}
			}
			
			// Replace continuous effects.
			if (knownSkill.isContinuous() && player.isAffectedBySkill(knownSkill.getId()))
			{
				int abnormalTime = 0;
				for (BuffInfo info : player.getEffectList().getEffects())
				{
					if (info.getSkill().getId() == knownSkill.getId())
					{
						abnormalTime = info.getAbnormalTime();
						break;
					}
				}
				
				if (abnormalTime > 2000)
				{
					addedSkill.applyEffects(player, player);
					final AbnormalStatusUpdate asu = new AbnormalStatusUpdate();
					for (BuffInfo info : player.getEffectList().getEffects())
					{
						if (info.getSkill().getId() == addedSkill.getId())
						{
							info.resetAbnormalTime(abnormalTime);
							asu.addSkill(info);
						}
					}
					player.sendPacket(asu);
				}
			}
			
			player.removeSkill(knownSkill, false);
			player.sendSkillList();
		}
		else // Not player.
		{
			playable.addSkill(addedSkill);
			playable.removeSkill(knownSkill, false);
			if (playable.isPet())
			{
				final Pet pet = (Pet) playable;
				pet.sendPacket(new ExPetSkillList(false, pet));
			}
		}
	}
}
