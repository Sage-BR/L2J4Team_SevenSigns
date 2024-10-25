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
package handlers.effecthandlers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.l2j.gameserver.enums.SkillFinishType;
import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.WorldObject;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.effects.AbstractEffect;
import org.l2j.gameserver.model.holders.SkillHolder;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.skill.AbnormalType;
import org.l2j.gameserver.model.skill.BuffInfo;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.model.skill.SkillCaster;

/**
 * Synergy effect implementation.
 */
public class Synergy extends AbstractEffect
{
	private final Set<AbnormalType> _requiredSlots;
	private final Set<AbnormalType> _optionalSlots;
	private final int _partyBuffSkillId;
	private final int _skillLevelScaleTo;
	private final int _minSlot;
	
	public Synergy(StatSet params)
	{
		final String requiredSlots = params.getString("requiredSlots", null);
		if ((requiredSlots != null) && !requiredSlots.isEmpty())
		{
			_requiredSlots = new HashSet<>();
			for (String slot : requiredSlots.split(";"))
			{
				_requiredSlots.add(AbnormalType.getAbnormalType(slot));
			}
		}
		else
		{
			_requiredSlots = Collections.<AbnormalType> emptySet();
		}
		
		final String optionalSlots = params.getString("optionalSlots", null);
		if ((optionalSlots != null) && !optionalSlots.isEmpty())
		{
			_optionalSlots = new HashSet<>();
			for (String slot : optionalSlots.split(";"))
			{
				_optionalSlots.add(AbnormalType.getAbnormalType(slot));
			}
		}
		else
		{
			_optionalSlots = Collections.<AbnormalType> emptySet();
		}
		
		_partyBuffSkillId = params.getInt("partyBuffSkillId");
		_skillLevelScaleTo = params.getInt("skillLevelScaleTo", 1);
		_minSlot = params.getInt("minSlot", 2);
		setTicks(params.getInt("ticks"));
	}
	
	@Override
	public boolean onActionTime(Creature effector, Creature effected, Skill skill, Item item)
	{
		if (effector.isDead())
		{
			return false;
		}
		
		for (AbnormalType required : _requiredSlots)
		{
			if (!effector.hasAbnormalType(required))
			{
				return skill.isToggle();
			}
		}
		
		int abnormalCount = 0;
		for (AbnormalType abnormalType : _optionalSlots)
		{
			if (effector.hasAbnormalType(abnormalType))
			{
				abnormalCount++;
			}
		}
		
		if (abnormalCount >= _minSlot)
		{
			final SkillHolder partyBuff = new SkillHolder(_partyBuffSkillId, Math.min(abnormalCount - 1, _skillLevelScaleTo));
			final Skill partyBuffSkill = partyBuff.getSkill();
			if (partyBuffSkill != null)
			{
				final WorldObject target = partyBuffSkill.getTarget(effector, effected, false, false, false);
				if ((target != null) && target.isCreature())
				{
					final BuffInfo abnormalBuffInfo = effector.getEffectList().getFirstBuffInfoByAbnormalType(partyBuffSkill.getAbnormalType());
					if ((abnormalBuffInfo != null) && (abnormalBuffInfo.getSkill().getAbnormalLevel() != (abnormalCount - 1)))
					{
						effector.getEffectList().stopSkillEffects(SkillFinishType.REMOVED, _partyBuffSkillId);
					}
					else
					{
						SkillCaster.triggerCast(effector, (Creature) target, partyBuffSkill);
					}
				}
			}
			else
			{
				LOGGER.warning("Skill not found effect called from " + skill);
			}
		}
		else
		{
			effector.getEffectList().stopSkillEffects(SkillFinishType.REMOVED, _partyBuffSkillId);
		}
		
		return skill.isToggle();
	}
}
