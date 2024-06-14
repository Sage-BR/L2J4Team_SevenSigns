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

import org.l2jmobius.commons.util.Rnd;
import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.WorldObject;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.effects.AbstractEffect;
import org.l2jmobius.gameserver.model.effects.EffectType;
import org.l2jmobius.gameserver.model.events.EventType;
import org.l2jmobius.gameserver.model.events.impl.creature.OnCreatureSkillFinishCast;
import org.l2jmobius.gameserver.model.events.listeners.ConsumerEventListener;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.model.skill.Skill;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.serverpackets.SystemMessage;

/**
 * Trigger heal percent by skill effect implementation.
 * @author NasSeKa
 */
public class TriggerHealPercentBySkill extends AbstractEffect
{
	private final int _castSkillId;
	private final int _chance;
	private final int _power;
	
	public TriggerHealPercentBySkill(StatSet params)
	{
		_castSkillId = params.getInt("castSkillId");
		_chance = params.getInt("chance", 100);
		_power = params.getInt("power", 0);
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, Item item)
	{
		if ((_chance == 0) || (_castSkillId == 0))
		{
			return;
		}
		
		effected.addListener(new ConsumerEventListener(effected, EventType.ON_CREATURE_SKILL_FINISH_CAST, (OnCreatureSkillFinishCast event) -> onSkillUseEvent(event), this));
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		effected.removeListenerIf(EventType.ON_CREATURE_SKILL_FINISH_CAST, listener -> listener.getOwner() == this);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.HEAL;
	}
	
	private void onSkillUseEvent(OnCreatureSkillFinishCast event)
	{
		if (_castSkillId != event.getSkill().getId())
		{
			return;
		}
		
		final WorldObject target = event.getTarget();
		if (target == null)
		{
			return;
		}
		
		final Player player = target.getActingPlayer();
		if ((player == null) || player.isDead() || player.isHpBlocked())
		{
			return;
		}
		
		if ((_chance < 100) && (Rnd.get(100) > _chance))
		{
			return;
		}
		
		double amount = 0;
		final double power = _power;
		final boolean full = (power == 100.0);
		
		amount = full ? player.getMaxHp() : (player.getMaxHp() * power) / 100.0;
		
		// Prevents overheal.
		amount = Math.min(amount, Math.max(0, player.getMaxRecoverableHp() - player.getCurrentHp()));
		if (amount >= 0)
		{
			if (amount != 0)
			{
				player.setCurrentHp(amount + player.getCurrentHp(), false);
				player.broadcastStatusUpdate(player);
			}
			
			SystemMessage sm;
			sm = new SystemMessage(SystemMessageId.YOU_VE_RECOVERED_S1_HP);
			sm.addInt((int) amount);
			player.sendPacket(sm);
		}
	}
}
