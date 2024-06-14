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

import java.util.HashSet;
import java.util.Set;

import org.l2jmobius.gameserver.enums.StatModifierType;
import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.effects.AbstractEffect;
import org.l2jmobius.gameserver.model.events.EventType;
import org.l2jmobius.gameserver.model.events.impl.creature.OnCreatureSkillUse;
import org.l2jmobius.gameserver.model.events.listeners.ConsumerEventListener;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.model.skill.BuffInfo;
import org.l2jmobius.gameserver.model.skill.Skill;
import org.l2jmobius.gameserver.network.serverpackets.AbnormalStatusUpdate;

/**
 * @author Mobius
 */
public class AbnormalTimeChangeBySkillId extends AbstractEffect
{
	private final double _time;
	private final StatModifierType _mode;
	private final Set<Integer> _skillIds = new HashSet<>();
	
	public AbnormalTimeChangeBySkillId(StatSet params)
	{
		_time = params.getDouble("time", -1);
		_mode = params.getEnum("mode", StatModifierType.class, StatModifierType.PER);
		final String skillIds = params.getString("ids", null);
		if ((skillIds != null) && !skillIds.isEmpty())
		{
			for (String id : skillIds.split(","))
			{
				_skillIds.add(Integer.valueOf(id));
			}
		}
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, Item item)
	{
		effected.addListener(new ConsumerEventListener(effected, EventType.ON_CREATURE_SKILL_USE, (OnCreatureSkillUse event) -> onCreatureSkillUse(event), this));
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		effected.removeListenerIf(EventType.ON_CREATURE_SKILL_USE, listener -> listener.getOwner() == this);
	}
	
	private void onCreatureSkillUse(OnCreatureSkillUse event)
	{
		final Skill skill = event.getSkill();
		if (!_skillIds.contains(skill.getId()))
		{
			return;
		}
		
		final AbnormalStatusUpdate asu = new AbnormalStatusUpdate();
		final Creature creature = event.getCaster();
		for (BuffInfo info : creature.getEffectList().getEffects())
		{
			if (info.getSkill().getId() == skill.getId())
			{
				if (_mode == StatModifierType.PER)
				{
					info.resetAbnormalTime((int) (info.getAbnormalTime() * _time));
				}
				else // DIFF
				{
					info.resetAbnormalTime((int) (info.getAbnormalTime() + _time));
				}
				asu.addSkill(info);
			}
		}
		
		creature.sendPacket(asu);
	}
}
