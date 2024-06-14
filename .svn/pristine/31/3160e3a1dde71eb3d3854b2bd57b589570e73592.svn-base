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
package org.l2jmobius.gameserver.model.options;

import java.util.ArrayList;
import java.util.List;

import org.l2jmobius.gameserver.enums.SkillFinishType;
import org.l2jmobius.gameserver.model.actor.Playable;
import org.l2jmobius.gameserver.model.effects.AbstractEffect;
import org.l2jmobius.gameserver.model.skill.BuffInfo;
import org.l2jmobius.gameserver.model.skill.Skill;
import org.l2jmobius.gameserver.network.serverpackets.SkillCoolTime;

/**
 * @author UnAfraid
 */
public class Options
{
	private final int _id;
	private List<AbstractEffect> _effects = null;
	private List<Skill> _activeSkill = null;
	private List<Skill> _passiveSkill = null;
	private List<OptionSkillHolder> _activationSkills = null;
	
	/**
	 * @param id
	 */
	public Options(int id)
	{
		_id = id;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public void addEffect(AbstractEffect effect)
	{
		if (_effects == null)
		{
			_effects = new ArrayList<>();
		}
		_effects.add(effect);
	}
	
	public List<AbstractEffect> getEffects()
	{
		return _effects;
	}
	
	public boolean hasEffects()
	{
		return _effects != null;
	}
	
	public boolean hasActiveSkills()
	{
		return _activeSkill != null;
	}
	
	public List<Skill> getActiveSkills()
	{
		return _activeSkill;
	}
	
	public void addActiveSkill(Skill holder)
	{
		if (_activeSkill == null)
		{
			_activeSkill = new ArrayList<>();
		}
		_activeSkill.add(holder);
	}
	
	public boolean hasPassiveSkills()
	{
		return _passiveSkill != null;
	}
	
	public List<Skill> getPassiveSkills()
	{
		return _passiveSkill;
	}
	
	public void addPassiveSkill(Skill holder)
	{
		if (_passiveSkill == null)
		{
			_passiveSkill = new ArrayList<>();
		}
		_passiveSkill.add(holder);
	}
	
	public boolean hasActivationSkills()
	{
		return _activationSkills != null;
	}
	
	public boolean hasActivationSkills(OptionSkillType type)
	{
		if (_activationSkills != null)
		{
			for (OptionSkillHolder holder : _activationSkills)
			{
				if (holder.getSkillType() == type)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public List<OptionSkillHolder> getActivationSkills()
	{
		return _activationSkills;
	}
	
	public List<OptionSkillHolder> getActivationSkills(OptionSkillType type)
	{
		final List<OptionSkillHolder> temp = new ArrayList<>();
		if (_activationSkills != null)
		{
			for (OptionSkillHolder holder : _activationSkills)
			{
				if (holder.getSkillType() == type)
				{
					temp.add(holder);
				}
			}
		}
		return temp;
	}
	
	public void addActivationSkill(OptionSkillHolder holder)
	{
		if (_activationSkills == null)
		{
			_activationSkills = new ArrayList<>();
		}
		_activationSkills.add(holder);
	}
	
	public void apply(Playable playable)
	{
		if (hasEffects())
		{
			final BuffInfo info = new BuffInfo(playable, playable, null, true, null, this);
			for (AbstractEffect effect : _effects)
			{
				if (effect.isInstant())
				{
					if (effect.calcSuccess(info.getEffector(), info.getEffected(), info.getSkill()))
					{
						effect.instant(info.getEffector(), info.getEffected(), info.getSkill(), info.getItem());
					}
				}
				else
				{
					effect.continuousInstant(info.getEffector(), info.getEffected(), info.getSkill(), info.getItem());
					effect.pump(playable, info.getSkill());
					if (effect.canStart(info.getEffector(), info.getEffected(), info.getSkill()))
					{
						info.addEffect(effect);
					}
				}
			}
			if (!info.getEffects().isEmpty())
			{
				playable.getEffectList().add(info);
			}
		}
		if (hasActiveSkills())
		{
			for (Skill skill : _activeSkill)
			{
				addSkill(playable, skill);
			}
		}
		if (hasPassiveSkills())
		{
			for (Skill skill : _passiveSkill)
			{
				addSkill(playable, skill);
			}
		}
		if (hasActivationSkills())
		{
			for (OptionSkillHolder holder : _activationSkills)
			{
				playable.addTriggerSkill(holder);
			}
		}
		
		playable.getStat().recalculateStats(true);
		if (playable.isPlayer())
		{
			playable.getActingPlayer().sendSkillList();
		}
	}
	
	public void remove(Playable playable)
	{
		if (hasEffects())
		{
			for (BuffInfo info : playable.getEffectList().getOptions())
			{
				if (info.getOption() == this)
				{
					playable.getEffectList().remove(info, SkillFinishType.NORMAL, true, true);
				}
			}
		}
		if (hasActiveSkills())
		{
			for (Skill skill : _activeSkill)
			{
				playable.removeSkill(skill, false);
			}
		}
		if (hasPassiveSkills())
		{
			for (Skill skill : _passiveSkill)
			{
				playable.removeSkill(skill, true);
			}
		}
		if (hasActivationSkills())
		{
			for (OptionSkillHolder holder : _activationSkills)
			{
				playable.removeTriggerSkill(holder);
			}
		}
		
		playable.getStat().recalculateStats(true);
		if (playable.isPlayer())
		{
			playable.getActingPlayer().sendSkillList();
		}
	}
	
	private void addSkill(Playable playable, Skill skill)
	{
		boolean updateTimeStamp = false;
		playable.addSkill(skill);
		if (skill.isActive())
		{
			final long remainingTime = playable.getSkillRemainingReuseTime(skill.getReuseHashCode());
			if (remainingTime > 0)
			{
				playable.addTimeStamp(skill, remainingTime);
				playable.disableSkill(skill, remainingTime);
			}
			updateTimeStamp = true;
		}
		if (updateTimeStamp && playable.isPlayer())
		{
			playable.sendPacket(new SkillCoolTime(playable.getActingPlayer()));
		}
	}
}
