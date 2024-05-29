/*
 * This file is part of the L2J 4Team project.
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

import org.l2j.Config;

import org.l2j.gameserver.enums.ShotType;
import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.effects.AbstractEffect;
import org.l2j.gameserver.model.effects.EffectType;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.model.stats.Formulas;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * Magical Soul Attack effect implementation.
 * @author Adry_85
 */
public class MagicalSoulAttack extends AbstractEffect
{
	private final double _power;
	
	public MagicalSoulAttack(StatSet params)
	{
		_power = params.getDouble("power", 0);
	}
	
	@Override
	public boolean calcSuccess(Creature effector, Creature effected, Skill skill)
	{
		return !Formulas.calcSkillEvasion(effector, effected, skill);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.MAGICAL_ATTACK;
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, Item item)
	{
		if (effector.isAlikeDead())
		{
			return;
		}
		
		if (effected.isPlayer() && effected.getActingPlayer().isFakeDeath() && Config.FAKE_DEATH_DAMAGE_STAND)
		{
			effected.stopFakeDeath(true);
		}
		
		final int chargedLightSouls = Math.min(skill.getMaxLightSoulConsumeCount(), effector.getActingPlayer().getCharges());
		if ((chargedLightSouls > 0) && !effector.getActingPlayer().decreaseCharges(chargedLightSouls))
		{
			final SystemMessage sm = new SystemMessage(SystemMessageId.S1_CANNOT_BE_USED_THE_REQUIREMENTS_ARE_NOT_MET);
			sm.addSkillName(skill);
			effector.sendPacket(sm);
			return;
		}
		
		final int chargedShadowSouls = Math.min(skill.getMaxShadowSoulConsumeCount(), effector.getActingPlayer().getCharges());
		if ((chargedShadowSouls > 0) && !effector.getActingPlayer().decreaseCharges(chargedShadowSouls))
		{
			final SystemMessage sm = new SystemMessage(SystemMessageId.S1_CANNOT_BE_USED_THE_REQUIREMENTS_ARE_NOT_MET);
			sm.addSkillName(skill);
			effector.sendPacket(sm);
			return;
		}
		
		final int chargedSouls = chargedLightSouls + chargedShadowSouls;
		if (chargedSouls < 1)
		{
			final SystemMessage sm = new SystemMessage(SystemMessageId.S1_CANNOT_BE_USED_THE_REQUIREMENTS_ARE_NOT_MET);
			sm.addSkillName(skill);
			effector.sendPacket(sm);
			return;
		}
		
		final boolean sps = skill.useSpiritShot() && effector.isChargedShot(ShotType.SPIRITSHOTS);
		final boolean bss = skill.useSpiritShot() && effector.isChargedShot(ShotType.BLESSED_SPIRITSHOTS);
		final boolean mcrit = Formulas.calcCrit(skill.getMagicCriticalRate(), effector, effected, skill);
		final double mAtk = effector.getMAtk() * (chargedSouls > 0 ? (1.3 + (chargedSouls * 0.05)) : 1);
		final double damage = Formulas.calcMagicDam(effector, effected, skill, mAtk, _power, effected.getMDef(), sps, bss, mcrit);
		
		effector.doAttack(damage, effected, skill, false, false, mcrit, false);
	}
}