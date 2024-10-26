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

import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.effects.AbstractEffect;
import org.l2j.gameserver.model.effects.EffectType;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.model.stats.Stat;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * Mana Heal Percent effect implementation.
 * @author UnAfraid
 */
public class ManaHealPercent extends AbstractEffect
{
	private final double _power;
	
	public ManaHealPercent(StatSet params)
	{
		_power = params.getDouble("power", 0);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.MANAHEAL_PERCENT;
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, Item item)
	{
		if ((effected == null) || effected.isDead() || effected.isDoor() || effected.isMpBlocked())
		{
			return;
		}
		
		double amount = 0;
		final double power = _power;
		final boolean full = (power == 100.0);
		
		amount = full ? effected.getMaxMp() : (effected.getMaxMp() * power) / 100.0;
		if ((item != null) && (item.isPotion() || item.isElixir()))
		{
			amount += effected.getStat().getValue(Stat.ADDITIONAL_POTION_MP, 0);
		}
		
		// Prevents overheal
		amount = Math.min(amount, Math.max(0, effected.getMaxRecoverableMp() - effected.getCurrentMp()));
		if (amount != 0)
		{
			final double newMp = amount + effected.getCurrentMp();
			effected.setCurrentMp(newMp, false);
			effected.broadcastStatusUpdate(effector);
		}
		SystemMessage sm;
		if (effector.getObjectId() != effected.getObjectId())
		{
			sm = new SystemMessage(SystemMessageId.YOU_HAVE_RECOVERED_S2_MP_WITH_C1_S_HELP);
			sm.addString(effector.getName());
		}
		else
		{
			sm = new SystemMessage(SystemMessageId.S1_MP_HAS_BEEN_RESTORED);
		}
		sm.addInt((int) amount);
		effected.sendPacket(sm);
	}
}
