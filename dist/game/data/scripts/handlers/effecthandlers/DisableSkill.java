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

import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.effects.AbstractEffect;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.skill.Skill;

/**
 * @author Ofelin
 */
public class DisableSkill extends AbstractEffect
{
	private final Set<Integer> _disabledSkills;
	
	public DisableSkill(StatSet params)
	{
		final String disable = params.getString("disable");
		if ((disable != null) && !disable.isEmpty())
		{
			_disabledSkills = new HashSet<>();
			for (String slot : disable.split(";"))
			{
				_disabledSkills.add(Integer.parseInt(slot));
			}
		}
		else
		{
			_disabledSkills = Collections.emptySet();
		}
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, Item item)
	{
		Skill knownSkill;
		for (int disableSkillId : _disabledSkills)
		{
			knownSkill = effected.getKnownSkill(disableSkillId);
			if (knownSkill != null)
			{
				effected.disableSkill(knownSkill, 0);
			}
		}
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		Skill knownSkill;
		for (int enableSkillId : _disabledSkills)
		{
			knownSkill = effected.getKnownSkill(enableSkillId);
			if (knownSkill != null)
			{
				if (effected.isPlayer())
				{
					effected.getActingPlayer().enableSkill(knownSkill, false);
				}
				else
				{
					effected.enableSkill(knownSkill);
				}
			}
		}
	}
}