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
import org.l2j.gameserver.enums.Team;
import org.l2j.gameserver.instancemanager.CastleManager;
import org.l2j.gameserver.instancemanager.CursedWeaponsManager;
import org.l2j.gameserver.instancemanager.RankManager;
import org.l2j.gameserver.model.VariationInstance;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.instance.Decoy;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.model.interfaces.ILocational;
import org.l2j.gameserver.model.itemcontainer.Inventory;
import org.l2j.gameserver.model.skill.AbnormalVisualEffect;
import org.l2j.gameserver.model.zone.ZoneId;
import org.l2j.gameserver.network.ServerPackets;

public class CharInfo extends ServerPacket
{
	private static final int[] PAPERDOLL_ORDER = new int[]
	{
		Inventory.PAPERDOLL_UNDER,
		Inventory.PAPERDOLL_HEAD,
		Inventory.PAPERDOLL_RHAND,
		Inventory.PAPERDOLL_LHAND,
		Inventory.PAPERDOLL_GLOVES,
		Inventory.PAPERDOLL_CHEST,
		Inventory.PAPERDOLL_LEGS,
		Inventory.PAPERDOLL_FEET,
		Inventory.PAPERDOLL_CLOAK,
		Inventory.PAPERDOLL_RHAND,
		Inventory.PAPERDOLL_HAIR,
		Inventory.PAPERDOLL_HAIR2
	};
	
	private final Player _player;
	private final Clan _clan;
	private int _objId;
	private int _x;
	private int _y;
	private int _z;
	private int _heading;
	private final int _mAtkSpd;
	private final int _pAtkSpd;
	private final int _runSpd;
	private final int _walkSpd;
	private final int _swimRunSpd;
	private final int _swimWalkSpd;
	private final int _flyRunSpd;
	private final int _flyWalkSpd;
	private final double _moveMultiplier;
	private final float _attackSpeedMultiplier;
	private int _enchantLevel = 0;
	private int _armorEnchant = 0;
	private int _vehicleId = 0;
	private final boolean _gmSeeInvis;
	
	public CharInfo(Player player, boolean gmSeeInvis)
	{
		super(256);
		
		_player = player;
		_objId = player.getObjectId();
		_clan = player.getClan();
		if ((_player.getVehicle() != null) && (_player.getInVehiclePosition() != null))
		{
			_x = _player.getInVehiclePosition().getX();
			_y = _player.getInVehiclePosition().getY();
			_z = _player.getInVehiclePosition().getZ();
			_vehicleId = _player.getVehicle().getObjectId();
		}
		else
		{
			_x = _player.getX();
			_y = _player.getY();
			_z = _player.getZ();
		}
		_heading = _player.getHeading();
		_mAtkSpd = _player.getMAtkSpd();
		_pAtkSpd = _player.getPAtkSpd();
		_attackSpeedMultiplier = (float) _player.getAttackSpeedMultiplier();
		_moveMultiplier = player.getMovementSpeedMultiplier();
		_runSpd = (int) Math.round(player.getRunSpeed() / _moveMultiplier);
		_walkSpd = (int) Math.round(player.getWalkSpeed() / _moveMultiplier);
		_swimRunSpd = (int) Math.round(player.getSwimRunSpeed() / _moveMultiplier);
		_swimWalkSpd = (int) Math.round(player.getSwimWalkSpeed() / _moveMultiplier);
		_flyRunSpd = player.isFlying() ? _runSpd : 0;
		_flyWalkSpd = player.isFlying() ? _walkSpd : 0;
		_enchantLevel = player.getInventory().getWeaponEnchant();
		_armorEnchant = player.getInventory().getArmorMinEnchant();
		_gmSeeInvis = gmSeeInvis;
	}
	
	public CharInfo(Decoy decoy, boolean gmSeeInvis)
	{
		this(decoy.getActingPlayer(), gmSeeInvis); // init
		_objId = decoy.getObjectId();
		_x = decoy.getX();
		_y = decoy.getY();
		_z = decoy.getZ();
		_heading = decoy.getHeading();
	}
	
	@Override
	public void write()
	{
		ServerPackets.CHAR_INFO.writeId(this);
		writeByte(0); // Grand Crusade
		writeInt(_x); // Confirmed
		writeInt(_y); // Confirmed
		writeInt(_z); // Confirmed
		writeInt(_vehicleId); // Confirmed
		writeInt(_objId); // Confirmed
		writeString(_player.getAppearance().getVisibleName()); // Confirmed
		writeShort(_player.getRace().ordinal()); // Confirmed
		writeByte(_player.getAppearance().isFemale()); // Confirmed
		writeInt(_player.getBaseTemplate().getClassId().getRootClassId().getId());
		
		for (int slot : getPaperdollOrder())
		{
			writeInt(_player.getInventory().getPaperdollItemDisplayId(slot)); // Confirmed
		}
		
		for (int slot : getPaperdollOrderAugument())
		{
			final VariationInstance augment = _player.getInventory().getPaperdollAugmentation(slot);
			writeInt(augment != null ? augment.getOption1Id() : 0); // Confirmed
			writeInt(augment != null ? augment.getOption2Id() : 0); // Confirmed
		}
		
		writeByte(_armorEnchant);
		
		for (int slot : getPaperdollOrderVisualId())
		{
			writeInt(_player.getInventory().getPaperdollItemVisualId(slot));
		}
		
		writeByte(_player.getPvpFlag());
		writeInt(_player.getReputation());
		writeInt(_mAtkSpd);
		writeInt(_pAtkSpd);
		writeShort(_runSpd);
		writeShort(_walkSpd);
		writeShort(_swimRunSpd);
		writeShort(_swimWalkSpd);
		writeShort(_flyRunSpd);
		writeShort(_flyWalkSpd);
		writeShort(_flyRunSpd);
		writeShort(_flyWalkSpd);
		writeDouble(_moveMultiplier);
		writeDouble(_attackSpeedMultiplier);
		writeDouble(_player.getCollisionRadius());
		writeDouble(_player.getCollisionHeight());
		writeInt(_player.getVisualHair());
		writeInt(_player.getVisualHairColor());
		writeInt(_player.getVisualFace());
		writeString(_gmSeeInvis ? "Invisible" : _player.getAppearance().getVisibleTitle());
		writeInt(_player.getAppearance().getVisibleClanId());
		writeInt(_player.getAppearance().getVisibleClanCrestId());
		writeInt(_player.getAppearance().getVisibleAllyId());
		writeInt(_player.getAppearance().getVisibleAllyCrestId());
		writeByte(!_player.isSitting()); // Confirmed
		writeByte(_player.isRunning()); // Confirmed
		writeByte(_player.isInCombat()); // Confirmed
		writeByte(!_player.isInOlympiadMode() && _player.isAlikeDead()); // Confirmed
		writeByte(_player.isInvisible());
		writeByte(_player.getMountType().ordinal()); // 1-on Strider, 2-on Wyvern, 3-on Great Wolf, 0-no mount
		writeByte(_player.getPrivateStoreType().getId()); // Confirmed
		
		writeShort(_player.getCubics().size()); // Confirmed
		_player.getCubics().keySet().forEach(this::writeShort);
		
		writeByte(_player.isInMatchingRoom()); // Confirmed
		writeByte(_player.isInsideZone(ZoneId.WATER) ? 1 : _player.isFlyingMounted() ? 2 : 0);
		writeShort(_player.getRecomHave()); // Confirmed
		writeInt(_player.getMountNpcId() == 0 ? 0 : _player.getMountNpcId() + 1000000);
		writeInt(_player.getClassId().getId()); // Confirmed
		writeInt(0); // TODO: Find me!
		writeByte(_player.isMounted() ? 0 : _enchantLevel); // Confirmed
		writeByte(_player.getTeam().getId()); // Confirmed
		writeInt(_player.getClanCrestLargeId());
		writeByte(_player.isNoble()); // Confirmed
		writeByte(_player.isHero() || (_player.isGM() && Config.GM_HERO_AURA) ? 2 : 0); // 152 - Value for enabled changed to 2?
		
		writeByte(_player.isFishing()); // Confirmed
		final ILocational baitLocation = _player.getFishing().getBaitLocation();
		if (baitLocation != null)
		{
			writeInt(baitLocation.getX()); // Confirmed
			writeInt(baitLocation.getY()); // Confirmed
			writeInt(baitLocation.getZ()); // Confirmed
		}
		else
		{
			writeInt(0);
			writeInt(0);
			writeInt(0);
		}
		
		writeInt(_player.getAppearance().getNameColor()); // Confirmed
		writeInt(_heading); // Confirmed
		writeByte(_player.getPledgeClass());
		writeShort(_player.getPledgeType());
		writeInt(_player.getAppearance().getTitleColor()); // Confirmed
		writeByte(_player.isCursedWeaponEquipped() ? CursedWeaponsManager.getInstance().getLevel(_player.getCursedWeaponEquippedId()) : 0);
		writeInt(_clan != null ? _clan.getReputationScore() : 0);
		writeInt(_player.getTransformationDisplayId()); // Confirmed
		writeInt(_player.getAgathionId()); // Confirmed
		writeByte(0); // nPvPRestrainStatus
		writeInt((int) Math.round(_player.getCurrentCp())); // Confirmed
		writeInt(_player.getMaxHp()); // Confirmed
		writeInt((int) Math.round(_player.getCurrentHp())); // Confirmed
		writeInt(_player.getMaxMp()); // Confirmed
		writeInt((int) Math.round(_player.getCurrentMp())); // Confirmed
		writeByte(0); // cBRLectureMark
		
		final Set<AbnormalVisualEffect> abnormalVisualEffects = _player.getEffectList().getCurrentAbnormalVisualEffects();
		final Team team = (Config.BLUE_TEAM_ABNORMAL_EFFECT != null) && (Config.RED_TEAM_ABNORMAL_EFFECT != null) ? _player.getTeam() : Team.NONE;
		writeInt(abnormalVisualEffects.size() + (_gmSeeInvis ? 1 : 0) + (team != Team.NONE ? 1 : 0)); // Confirmed
		for (AbnormalVisualEffect abnormalVisualEffect : abnormalVisualEffects)
		{
			writeShort(abnormalVisualEffect.getClientId()); // Confirmed
		}
		if (_gmSeeInvis)
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
		
		writeByte(_player.isTrueHero() ? 100 : 0);
		writeByte(_player.isHairAccessoryEnabled()); // Hair accessory
		writeByte(_player.getAbilityPointsUsed()); // Used Ability Points
		writeInt(0); // nCursedWeaponClassId
		
		// AFK animation.
		if ((_player.getClan() != null) && (CastleManager.getInstance().getCastleByOwner(_player.getClan()) != null))
		{
			writeInt(_player.isClanLeader() ? 100 : 101);
		}
		else
		{
			writeInt(0);
		}
		
		// Rank.
		writeInt(RankManager.getInstance().getPlayerGlobalRank(_player) == 1 ? 1 : RankManager.getInstance().getPlayerRaceRank(_player) == 1 ? 2 : 0);
		writeShort(0);
		writeByte(0);
		writeInt(_player.getClassId().getId());
		writeByte(0);
		writeInt(_player.getVisualHairColor() + 1); // 338 - DK color.
		writeInt(0);
		writeByte(_player.getClassId().level() + 1); // 362 - Vanguard mount.
	}
	
	@Override
	public int[] getPaperdollOrder()
	{
		return PAPERDOLL_ORDER;
	}
}
