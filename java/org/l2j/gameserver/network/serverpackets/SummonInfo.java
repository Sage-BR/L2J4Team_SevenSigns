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
package org.l2j.gameserver.network.serverpackets;

import java.util.Set;

import org.l2j.Config;

import org.l2j.gameserver.enums.NpcInfoType;
import org.l2j.gameserver.enums.Team;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.Summon;
import org.l2j.gameserver.model.skill.AbnormalVisualEffect;
import org.l2j.gameserver.model.zone.ZoneId;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author Sdw
 */
public class SummonInfo extends AbstractMaskPacket<NpcInfoType>
{
	private final Summon _summon;
	private final Player _attacker;
	private final long _relation;
	private final int _value;
	private final byte[] _masks = new byte[]
	{
		(byte) 0x00,
		(byte) 0x0C,
		(byte) 0x0C,
		(byte) 0x00,
		(byte) 0x00
	};
	private int _initSize = 0;
	private int _blockSize = 0;
	private int _clanCrest = 0;
	private int _clanLargeCrest = 0;
	private int _allyCrest = 0;
	private int _allyId = 0;
	private int _clanId = 0;
	private int _statusMask = 0;
	private final String _title;
	private final Set<AbnormalVisualEffect> _abnormalVisualEffects;
	
	public SummonInfo(Summon summon, Player attacker, int value)
	{
		_summon = summon;
		_attacker = attacker;
		_relation = (attacker != null) && (summon.getOwner() != null) ? summon.getOwner().getRelation(attacker) : 0;
		_title = (summon.getOwner() != null) && summon.getOwner().isOnline() ? summon.getOwner().getName() : "";
		_value = value;
		_abnormalVisualEffects = summon.getEffectList().getCurrentAbnormalVisualEffects();
		if (summon.getTemplate().getDisplayId() != summon.getTemplate().getId())
		{
			_masks[2] |= 0x10;
			addComponentType(NpcInfoType.NAME);
		}
		addComponentType(NpcInfoType.ATTACKABLE, NpcInfoType.RELATIONS, NpcInfoType.TITLE, NpcInfoType.ID, NpcInfoType.POSITION, NpcInfoType.STOP_MODE, NpcInfoType.MOVE_MODE, NpcInfoType.PVP_FLAG);
		if (summon.getHeading() > 0)
		{
			addComponentType(NpcInfoType.HEADING);
		}
		if ((summon.getStat().getPAtkSpd() > 0) || (summon.getStat().getMAtkSpd() > 0))
		{
			addComponentType(NpcInfoType.ATK_CAST_SPEED);
		}
		if (summon.getRunSpeed() > 0)
		{
			addComponentType(NpcInfoType.SPEED_MULTIPLIER);
		}
		if ((summon.getWeapon() > 0) || (summon.getArmor() > 0))
		{
			addComponentType(NpcInfoType.EQUIPPED);
		}
		if (summon.getTeam() != Team.NONE)
		{
			if ((Config.BLUE_TEAM_ABNORMAL_EFFECT != null) && (Config.RED_TEAM_ABNORMAL_EFFECT != null))
			{
				addComponentType(NpcInfoType.ABNORMALS);
			}
			else
			{
				addComponentType(NpcInfoType.TEAM);
			}
		}
		if (summon.isInsideZone(ZoneId.WATER) || summon.isFlying())
		{
			addComponentType(NpcInfoType.SWIM_OR_FLY);
		}
		if (summon.isFlying())
		{
			addComponentType(NpcInfoType.FLYING);
		}
		if (summon.getMaxHp() > 0)
		{
			addComponentType(NpcInfoType.MAX_HP);
		}
		if (summon.getMaxMp() > 0)
		{
			addComponentType(NpcInfoType.MAX_MP);
		}
		if (summon.getCurrentHp() <= summon.getMaxHp())
		{
			addComponentType(NpcInfoType.CURRENT_HP);
		}
		if (summon.getCurrentMp() <= summon.getMaxMp())
		{
			addComponentType(NpcInfoType.CURRENT_MP);
		}
		if (!_abnormalVisualEffects.isEmpty())
		{
			addComponentType(NpcInfoType.ABNORMALS);
		}
		if (summon.getTemplate().getWeaponEnchant() > 0)
		{
			addComponentType(NpcInfoType.ENCHANT);
		}
		if (summon.getTransformationDisplayId() > 0)
		{
			addComponentType(NpcInfoType.TRANSFORMATION);
		}
		if (summon.isShowSummonAnimation())
		{
			addComponentType(NpcInfoType.SUMMONED);
		}
		if (summon.getReputation() != 0)
		{
			addComponentType(NpcInfoType.REPUTATION);
		}
		if (summon.getOwner().getClan() != null)
		{
			_clanId = summon.getOwner().getAppearance().getVisibleClanId();
			_clanCrest = summon.getOwner().getAppearance().getVisibleClanCrestId();
			_clanLargeCrest = summon.getOwner().getAppearance().getVisibleClanLargeCrestId();
			_allyCrest = summon.getOwner().getAppearance().getVisibleAllyId();
			_allyId = summon.getOwner().getAppearance().getVisibleAllyCrestId();
			addComponentType(NpcInfoType.CLAN);
		}
		addComponentType(NpcInfoType.PET_EVOLUTION_ID);
		// TODO: Confirm me
		if (summon.isInCombat())
		{
			_statusMask |= 0x01;
		}
		if (summon.isDead())
		{
			_statusMask |= 0x02;
		}
		if (summon.isTargetable())
		{
			_statusMask |= 0x04;
		}
		_statusMask |= 0x08;
		// Show red aura?
		// if (_statusMask != 0x00)
		// {
		// addComponentType(NpcInfoType.VISUAL_STATE);
		// }
	}
	
	@Override
	protected byte[] getMasks()
	{
		return _masks;
	}
	
	@Override
	protected void onNewMaskAdded(NpcInfoType component)
	{
		calcBlockSize(_summon, component);
	}
	
	private void calcBlockSize(Summon summon, NpcInfoType type)
	{
		switch (type)
		{
			case ATTACKABLE:
			case RELATIONS:
			{
				_initSize += type.getBlockLength();
				break;
			}
			case TITLE:
			{
				_initSize += type.getBlockLength() + (_title.length() * 2);
				break;
			}
			case NAME:
			{
				_blockSize += type.getBlockLength() + (summon.getName().length() * 2);
				break;
			}
			default:
			{
				_blockSize += type.getBlockLength();
				break;
			}
		}
	}
	
	@Override
	public void write()
	{
		ServerPackets.SUMMON_INFO.writeId(this);
		writeInt(_summon.getObjectId());
		writeByte(_value); // 0=teleported 1=default 2=summoned
		writeShort(38); // 338 - mask_bits_38
		writeBytes(_masks);
		// Block 1
		writeByte(_initSize);
		if (containsMask(NpcInfoType.ATTACKABLE))
		{
			writeByte(_summon.isAutoAttackable(_attacker));
		}
		if (containsMask(NpcInfoType.RELATIONS))
		{
			writeLong(_relation);
		}
		if (containsMask(NpcInfoType.TITLE))
		{
			writeString(_title);
		}
		// Block 2
		writeShort(_blockSize);
		if (containsMask(NpcInfoType.ID))
		{
			writeInt(_summon.getTemplate().getDisplayId() + 1000000);
		}
		if (containsMask(NpcInfoType.POSITION))
		{
			writeInt(_summon.getX());
			writeInt(_summon.getY());
			writeInt(_summon.getZ());
		}
		if (containsMask(NpcInfoType.HEADING))
		{
			writeInt(_summon.getHeading());
		}
		if (containsMask(NpcInfoType.VEHICLE_ID))
		{
			writeInt(0); // Vehicle object id.
		}
		if (containsMask(NpcInfoType.ATK_CAST_SPEED))
		{
			writeInt(_summon.getPAtkSpd());
			writeInt(_summon.getMAtkSpd());
		}
		if (containsMask(NpcInfoType.SPEED_MULTIPLIER))
		{
			writeFloat((float) _summon.getStat().getMovementSpeedMultiplier());
			writeFloat((float) _summon.getStat().getAttackSpeedMultiplier());
		}
		if (containsMask(NpcInfoType.EQUIPPED))
		{
			writeInt(_summon.getWeapon());
			writeInt(_summon.getArmor()); // Armor id?
			writeInt(0);
		}
		if (containsMask(NpcInfoType.STOP_MODE))
		{
			writeByte(!_summon.isDead());
		}
		if (containsMask(NpcInfoType.MOVE_MODE))
		{
			writeByte(_summon.isRunning());
		}
		if (containsMask(NpcInfoType.SWIM_OR_FLY))
		{
			writeByte(_summon.isInsideZone(ZoneId.WATER) ? 1 : _summon.isFlying() ? 2 : 0);
		}
		if (containsMask(NpcInfoType.TEAM))
		{
			writeByte(_summon.getTeam().getId());
		}
		if (containsMask(NpcInfoType.ENCHANT))
		{
			writeInt(_summon.getTemplate().getWeaponEnchant());
		}
		if (containsMask(NpcInfoType.FLYING))
		{
			writeInt(_summon.isFlying());
		}
		if (containsMask(NpcInfoType.CLONE))
		{
			writeInt(0); // Player ObjectId with Decoy
		}
		if (containsMask(NpcInfoType.PET_EVOLUTION_ID))
		{
			writeInt(0); // Unknown
		}
		if (containsMask(NpcInfoType.DISPLAY_EFFECT))
		{
			writeInt(0);
		}
		if (containsMask(NpcInfoType.TRANSFORMATION))
		{
			writeInt(_summon.getTransformationDisplayId()); // Transformation ID
		}
		if (containsMask(NpcInfoType.CURRENT_HP))
		{
			writeInt((int) _summon.getCurrentHp());
		}
		if (containsMask(NpcInfoType.CURRENT_MP))
		{
			writeInt((int) _summon.getCurrentMp());
		}
		if (containsMask(NpcInfoType.MAX_HP))
		{
			writeInt(_summon.getMaxHp());
		}
		if (containsMask(NpcInfoType.MAX_MP))
		{
			writeInt(_summon.getMaxMp());
		}
		if (containsMask(NpcInfoType.SUMMONED))
		{
			writeByte(_summon.isShowSummonAnimation() ? 2 : 0); // 2 - do some animation on spawn
		}
		if (containsMask(NpcInfoType.FOLLOW_INFO))
		{
			writeInt(0);
			writeInt(0);
		}
		if (containsMask(NpcInfoType.NAME))
		{
			writeString(_summon.getName());
		}
		if (containsMask(NpcInfoType.NAME_NPCSTRINGID))
		{
			writeInt(-1); // NPCStringId for name
		}
		if (containsMask(NpcInfoType.TITLE_NPCSTRINGID))
		{
			writeInt(-1); // NPCStringId for title
		}
		if (containsMask(NpcInfoType.PVP_FLAG))
		{
			writeByte(_summon.getPvpFlag()); // PVP flag
		}
		if (containsMask(NpcInfoType.REPUTATION))
		{
			writeInt(_summon.getReputation()); // Name color
		}
		if (containsMask(NpcInfoType.CLAN))
		{
			writeInt(_clanId);
			writeInt(_clanCrest);
			writeInt(_clanLargeCrest);
			writeInt(_allyId);
			writeInt(_allyCrest);
		}
		if (containsMask(NpcInfoType.VISUAL_STATE))
		{
			writeInt(_statusMask); // Main writeByte, Essence writeInt.
		}
		if (containsMask(NpcInfoType.ABNORMALS))
		{
			final Team team = (Config.BLUE_TEAM_ABNORMAL_EFFECT != null) && (Config.RED_TEAM_ABNORMAL_EFFECT != null) ? _summon.getTeam() : Team.NONE;
			writeShort(_abnormalVisualEffects.size() + (_summon.isInvisible() ? 1 : 0) + (team != Team.NONE ? 1 : 0));
			for (AbnormalVisualEffect abnormalVisualEffect : _abnormalVisualEffects)
			{
				writeShort(abnormalVisualEffect.getClientId());
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
		}
	}
}