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

import java.util.ArrayList;
import java.util.List;

import org.l2j.commons.threads.ThreadPool;
import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.effects.AbstractEffect;
import org.l2j.gameserver.model.events.EventType;
import org.l2j.gameserver.model.events.ListenersContainer;
import org.l2j.gameserver.model.events.impl.creature.OnCreaturePDefChange;
import org.l2j.gameserver.model.events.listeners.ConsumerEventListener;
import org.l2j.gameserver.model.holders.SkillHolder;
import org.l2j.gameserver.model.holders.TriggerSkillInfoHolder;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.network.ConnectionState;

/**
 * @author Liamxroy
 */
public class TriggerSkillByPDef extends AbstractEffect
{
	private final List<TriggerSkillInfoHolder> _skills = new ArrayList<>();
	
	public TriggerSkillByPDef(StatSet params)
	{
		final String skillsArr[] = params.getString("skills").split(";");
		for (String str : skillsArr)
		{
			String paramArr[] = str.split(",");
			if (paramArr.length < 4)
			{
				LOGGER.info(getClass().getSimpleName() + ":  not enough param arguments (" + str + ")");
				continue;
			}
			
			int skillId = Integer.parseInt(paramArr[0]);
			int skillLevel = Integer.parseInt(paramArr[1]);
			int minPDef = Integer.parseInt(paramArr[2]);
			int maxPDef = Integer.parseInt(paramArr[3]);
			_skills.add(new TriggerSkillInfoHolder(new SkillHolder(skillId, skillLevel), minPDef, maxPDef));
		}
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, Item item)
	{
		if (effected.isPlayer())
		{
			final Player player = effected.getActingPlayer();
			if ((player.getClient() == null) || (player.getClient().getConnectionState() != ConnectionState.IN_GAME))
			{
				ThreadPool.schedule(() -> onStart(effector, effected, skill, item), 1000);
				return;
			}
		}
		if ((skill == null) || !effected.isAffectedBySkill(skill.getId()))
		{
			return;
		}
		
		onPDefChange(effected, effected.getPDef());
		
		// Register listeners.
		final ListenersContainer container = effected;
		container.addListener(new ConsumerEventListener(container, EventType.ON_CREATURE_PDEF_CHANGE, (OnCreaturePDefChange event) -> onPDefChange(event), this));
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		for (TriggerSkillInfoHolder sk : _skills)
		{
			effected.stopSkillEffects(sk.getSkillHolder().getSkill());
		}
		effected.removeListenerIf(listener -> listener.getOwner() == this);
	}
	
	private void onPDefChange(OnCreaturePDefChange event)
	{
		onPDefChange(event.getCreature(), event.getNewPDef());
	}
	
	public void onPDefChange(Creature creature, int newPDef)
	{
		for (TriggerSkillInfoHolder skill : _skills)
		{
			if ((newPDef < skill.getMin()) || (newPDef > skill.getMax()))
			{
				final int buffLv = creature.getAffectedSkillLevel(skill.getSkillHolder().getSkillId());
				if ((buffLv > 0) & (buffLv == skill.getSkillHolder().getSkillLevel()))
				{
					creature.stopSkillEffects(skill.getSkillHolder().getSkill());
				}
			}
			else if (creature.getAffectedSkillLevel(skill.getSkillHolder().getSkillId()) != skill.getSkillHolder().getSkillLevel())
			{
				skill.getSkillHolder().getSkill().applyEffects(creature, creature);
			}
		}
	}
}
