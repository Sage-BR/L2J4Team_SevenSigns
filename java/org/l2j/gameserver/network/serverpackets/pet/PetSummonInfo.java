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
package org.l2j.gameserver.network.serverpackets.pet;

import java.util.Set;

import org.l2j.Config;

import org.l2j.gameserver.enums.EvolveLevel;
import org.l2j.gameserver.enums.Team;
import org.l2j.gameserver.model.actor.Summon;
import org.l2j.gameserver.model.actor.instance.Pet;
import org.l2j.gameserver.model.actor.instance.Servitor;
import org.l2j.gameserver.model.skill.AbnormalVisualEffect;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;
import org.l2j.gameserver.taskmanager.AttackStanceTaskManager;

/**
 * 12 - wolf, 13 - buffalo, 14 - tiger, 15-kukkabara, 17 - hawk, 16 - dragon
 */
public class PetSummonInfo extends ServerPacket
{
	private final Summon _summon;
	private final int _value;
	private final int _runSpd;
	private final int _walkSpd;
	private final int _swimRunSpd;
	private final int _swimWalkSpd;
	private final int _flRunSpd = 0;
	private final int _flWalkSpd = 0;
	private final int _flyRunSpd;
	private final int _flyWalkSpd;
	private final double _moveMultiplier;
	private int _maxFed;
	private int _curFed;
	private int _statusMask = 0;
	
	public PetSummonInfo(Summon summon, int value)
	{
		_summon = summon;
		_moveMultiplier = summon.getMovementSpeedMultiplier();
		_runSpd = (int) Math.round(summon.getRunSpeed() / _moveMultiplier);
		_walkSpd = (int) Math.round(summon.getWalkSpeed() / _moveMultiplier);
		_swimRunSpd = (int) Math.round(summon.getSwimRunSpeed() / _moveMultiplier);
		_swimWalkSpd = (int) Math.round(summon.getSwimWalkSpeed() / _moveMultiplier);
		_flyRunSpd = summon.isFlying() ? _runSpd : 0;
		_flyWalkSpd = summon.isFlying() ? _walkSpd : 0;
		_value = value;
		if (summon.isPet())
		{
			final Pet pet = (Pet) _summon;
			_curFed = pet.getCurrentFed(); // how fed it is
			_maxFed = pet.getMaxFed(); // max fed it can be
		}
		else if (summon.isServitor())
		{
			final Servitor sum = (Servitor) _summon;
			_curFed = sum.getLifeTimeRemaining();
			_maxFed = sum.getLifeTime();
		}
		if (summon.isBetrayed())
		{
			_statusMask |= 0x01; // Auto attackable status
		}
		_statusMask |= 0x02; // can be chatted with
		if (summon.isRunning())
		{
			_statusMask |= 0x04;
		}
		if (AttackStanceTaskManager.getInstance().hasAttackStanceTask(summon))
		{
			_statusMask |= 0x08;
		}
		if (summon.isDead())
		{
			_statusMask |= 0x10;
		}
		if (summon.isMountable())
		{
			_statusMask |= 0x20;
		}
	}
	
	@Override
	public void write()
	{
		ServerPackets.PET_INFO.writeId(this);
		writeByte(_summon.getSummonType());
		writeInt(_summon.getObjectId());
		writeInt(_summon.getTemplate().getDisplayId() + 1000000);
		writeInt(_summon.getX());
		writeInt(_summon.getY());
		writeInt(_summon.getZ());
		writeInt(_summon.getHeading());
		writeInt(_summon.getStat().getMAtkSpd());
		writeInt(_summon.getStat().getPAtkSpd());
		writeShort(_runSpd);
		writeShort(_walkSpd);
		writeShort(_swimRunSpd);
		writeShort(_swimWalkSpd);
		writeShort(_flRunSpd);
		writeShort(_flWalkSpd);
		writeShort(_flyRunSpd);
		writeShort(_flyWalkSpd);
		writeDouble(_moveMultiplier);
		writeDouble(_summon.getAttackSpeedMultiplier()); // attack speed multiplier
		writeDouble(_summon.getTemplate().getFCollisionRadius());
		writeDouble(_summon.getTemplate().getFCollisionHeight());
		writeInt(_summon.getWeapon()); // right hand weapon
		writeInt(_summon.getArmor()); // body armor
		writeInt(0); // left hand weapon
		writeByte(_summon.isDead() ? 0 : _summon.isShowSummonAnimation() ? 2 : _value);
		writeInt(-1); // High Five NPCString ID
		if (_summon.isPet())
		{
			writeString(_summon.getName()); // Pet name.
		}
		else
		{
			writeString(_summon.getTemplate().isUsingServerSideName() ? _summon.getName() : ""); // Summon name.
		}
		writeInt(-1); // High Five NPCString ID
		writeString(_summon.getTitle()); // owner name
		writeByte(_summon.getPvpFlag()); // confirmed
		writeInt(_summon.getReputation()); // confirmed
		writeInt(_curFed); // how fed it is
		writeInt(_maxFed); // max fed it can be
		writeInt((int) _summon.getCurrentHp()); // current hp
		writeInt(_summon.getMaxHp()); // max hp
		writeInt((int) _summon.getCurrentMp()); // current mp
		writeInt(_summon.getMaxMp()); // max mp
		writeLong(_summon.getStat().getSp()); // sp
		writeShort(_summon.getLevel()); // level
		writeLong(_summon.getStat().getExp());
		// 0% absolute value
		writeLong(Math.min(_summon.getExpForThisLevel(), _summon.getStat().getExp())); // 0% absolute value
		writeLong(_summon.getExpForNextLevel()); // 100% absoulte value
		writeInt(_summon.isPet() ? _summon.getInventory().getTotalWeight() : 0); // weight
		writeInt(_summon.getMaxLoad()); // max weight it can carry
		writeInt(_summon.getPAtk()); // patk
		writeInt(_summon.getPDef()); // pdef
		writeInt(_summon.getAccuracy()); // accuracy
		writeInt(_summon.getEvasionRate()); // evasion
		writeInt(_summon.getCriticalHit()); // critical
		writeInt(_summon.getMAtk()); // matk
		writeInt(_summon.getMDef()); // mdef
		writeInt(_summon.getMagicAccuracy()); // magic accuracy
		writeInt(_summon.getMagicEvasionRate()); // magic evasion
		writeInt(_summon.getMCriticalHit()); // mcritical
		writeInt((int) _summon.getStat().getMoveSpeed()); // speed
		writeInt(_summon.getPAtkSpd()); // atkspeed
		writeInt(_summon.getMAtkSpd()); // casting speed
		writeByte(0); // TODO: Check me, might be ride status
		writeByte(_summon.getTeam().getId()); // Confirmed
		writeByte(_summon.getSoulShotsPerHit()); // How many soulshots this servitor uses per hit - Confirmed
		writeByte(_summon.getSpiritShotsPerHit()); // How many spiritshots this servitor uses per hit - - Confirmed
		writeInt(-1);
		writeInt(0); // "Transformation ID - Confirmed" - Used to bug Fenrir after 64 level.
		writeByte(0); // Used Summon Points
		writeByte(0); // Maximum Summon Points
		final Set<AbnormalVisualEffect> aves = _summon.getEffectList().getCurrentAbnormalVisualEffects();
		final Team team = (Config.BLUE_TEAM_ABNORMAL_EFFECT != null) && (Config.RED_TEAM_ABNORMAL_EFFECT != null) ? _summon.getTeam() : Team.NONE;
		writeShort(aves.size() + (_summon.isInvisible() ? 1 : 0) + (team != Team.NONE ? 1 : 0)); // Confirmed
		for (AbnormalVisualEffect ave : aves)
		{
			writeShort(ave.getClientId()); // Confirmed
		}
		if (_summon.isInvisible())
		{
			writeShort(AbnormalVisualEffect.STEALTH.getClientId());
		}
		if (team == Team.BLUE)
		{
			if (Config.BLUE_TEAM_ABNORMAL_EFFECT != null)
			{
				writeShort(Config.BLUE_TEAM_ABNORMAL_EFFECT.getClientId());
			}
		}
		else if ((team == Team.RED) && (Config.RED_TEAM_ABNORMAL_EFFECT != null))
		{
			writeShort(Config.RED_TEAM_ABNORMAL_EFFECT.getClientId());
		}
		writeByte(_statusMask);
		if (_summon.isPet())
		{
			final Pet pet = (Pet) _summon;
			writeInt(pet.getPetData().getType());
			writeInt(pet.getEvolveLevel());
			writeInt(pet.getEvolveLevel() == 0 ? -1 : pet.getId());
		}
		else
		{
			writeInt(0);
			writeInt(EvolveLevel.None.ordinal());
			writeInt(0);
		}
	}
}
