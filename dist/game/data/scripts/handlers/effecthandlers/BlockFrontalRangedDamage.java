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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.l2j.gameserver.enums.SkillFinishType;
import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.effects.AbstractEffect;
import org.l2j.gameserver.model.events.EventType;
import org.l2j.gameserver.model.events.impl.creature.OnCreatureDamageReceived;
import org.l2j.gameserver.model.events.listeners.FunctionEventListener;
import org.l2j.gameserver.model.events.returns.DamageReturn;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.skill.Skill;

/**
 * @author Liamxroy
 */
public class BlockFrontalRangedDamage extends AbstractEffect
{
	private static final Map<Integer, Double> DAMAGE_HOLDER = new ConcurrentHashMap<>();
	
	private final double _damage;
	
	public BlockFrontalRangedDamage(StatSet params)
	{
		_damage = params.getDouble("damage", 0);
	}
	
	private DamageReturn onDamageReceivedEvent(OnCreatureDamageReceived event, Creature effected, Skill skill)
	{
		if (event.isDamageOverTime())
		{
			return null;
		}
		
		double newDamage = event.getDamage();
		final int objectId = event.getTarget().getObjectId();
		final double damageLeft = DAMAGE_HOLDER.getOrDefault(objectId, 0d);
		if ((event.getAttacker() != null) && (event.getTarget() != null))
		{
			if (event.getAttacker().isInFrontOf(event.getTarget()) && (event.getAttacker().calculateDistance2D(event.getTarget()) > 240))
			{
				newDamage = newDamage - damageLeft;
				newDamage = Math.max(0, newDamage);
			}
			else
			{
				return new DamageReturn(false, true, false, newDamage);
			}
		}
		
		final double newDamageLeft = newDamage == 0 ? Math.max(damageLeft - event.getDamage(), 0) : 0;
		if (newDamageLeft > 0)
		{
			DAMAGE_HOLDER.put(objectId, newDamageLeft);
		}
		else
		{
			effected.stopSkillEffects(SkillFinishType.REMOVED, skill.getId());
		}
		DAMAGE_HOLDER.put(objectId, newDamageLeft);
		
		return new DamageReturn(false, true, false, newDamage);
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		effected.removeListenerIf(EventType.ON_CREATURE_DAMAGE_RECEIVED, listener -> listener.getOwner() == this);
		DAMAGE_HOLDER.remove(effected.getObjectId());
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, Item item)
	{
		DAMAGE_HOLDER.put(effected.getObjectId(), _damage);
		effected.addListener(new FunctionEventListener(effected, EventType.ON_CREATURE_DAMAGE_RECEIVED, (OnCreatureDamageReceived event) -> onDamageReceivedEvent(event, effected, skill), this));
	}
}
