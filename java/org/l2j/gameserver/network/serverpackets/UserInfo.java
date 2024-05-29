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

import org.l2j.Config;

import org.l2j.gameserver.data.xml.ExperienceData;
import org.l2j.gameserver.enums.UserInfoType;
import org.l2j.gameserver.instancemanager.CastleManager;
import org.l2j.gameserver.instancemanager.CursedWeaponsManager;
import org.l2j.gameserver.instancemanager.RankManager;
import org.l2j.gameserver.model.Party;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.model.stats.Stat;
import org.l2j.gameserver.model.variables.PlayerVariables;
import org.l2j.gameserver.model.zone.ZoneId;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author Sdw, UnAfraid
 */
public class UserInfo extends AbstractMaskPacket<UserInfoType>
{
	private Player _player;
	private int _relation;
	private int _runSpd;
	private int _walkSpd;
	private int _swimRunSpd;
	private int _swimWalkSpd;
	private final int _flRunSpd = 0;
	private final int _flWalkSpd = 0;
	private int _flyRunSpd;
	private int _flyWalkSpd;
	private double _moveMultiplier;
	private int _enchantLevel;
	private int _armorEnchant;
	private String _title;
	private final byte[] _masks = new byte[]
	{
		(byte) 0x00,
		(byte) 0x00,
		(byte) 0x00,
		(byte) 0x00
	};
	private int _initSize = 5;
	
	public UserInfo(Player player)
	{
		this(player, true);
	}
	
	public UserInfo(Player player, boolean addAll)
	{
		if (!player.isSubclassLocked()) // Changing class.
		{
			_player = player;
			_relation = calculateRelation(player);
			_moveMultiplier = player.getMovementSpeedMultiplier();
			_runSpd = (int) Math.round(player.getRunSpeed() / _moveMultiplier);
			_walkSpd = (int) Math.round(player.getWalkSpeed() / _moveMultiplier);
			_swimRunSpd = (int) Math.round(player.getSwimRunSpeed() / _moveMultiplier);
			_swimWalkSpd = (int) Math.round(player.getSwimWalkSpeed() / _moveMultiplier);
			_flyRunSpd = player.isFlying() ? _runSpd : 0;
			_flyWalkSpd = player.isFlying() ? _walkSpd : 0;
			_enchantLevel = player.getInventory().getWeaponEnchant();
			_armorEnchant = player.getInventory().getArmorMinEnchant();
			_title = player.getTitle();
			
			if (player.isGM() && player.isInvisible())
			{
				_title = "[Invisible]";
			}
			
			if (addAll)
			{
				addComponentType(UserInfoType.values());
			}
		}
	}
	
	@Override
	protected byte[] getMasks()
	{
		return _masks;
	}
	
	@Override
	protected void onNewMaskAdded(UserInfoType component)
	{
		calcBlockSize(component);
	}
	
	private void calcBlockSize(UserInfoType type)
	{
		switch (type)
		{
			case BASIC_INFO:
			{
				_initSize += type.getBlockLength() + (_player.getAppearance().getVisibleName().length() * 2);
				break;
			}
			case CLAN:
			{
				_initSize += type.getBlockLength() + (_title.length() * 2);
				break;
			}
			default:
			{
				_initSize += type.getBlockLength();
				break;
			}
		}
	}
	
	@Override
	public void write()
	{
		if (_player == null)
		{
			return;
		}
		
		ServerPackets.USER_INFO.writeId(this);
		writeInt(_player.getObjectId());
		writeInt(_initSize);
		writeShort(29); // 362 - 29
		writeBytes(_masks);
		if (containsMask(UserInfoType.RELATION))
		{
			writeInt(_relation);
		}
		if (containsMask(UserInfoType.BASIC_INFO))
		{
			writeShort(23 + (_player.getAppearance().getVisibleName().length() * 2));
			writeSizedString(_player.getName());
			writeByte(_player.isGM() ? 1 : 0);
			writeByte(_player.getRace().ordinal());
			writeByte(_player.getAppearance().isFemale() ? 1 : 0);
			writeInt(_player.getBaseTemplate().getClassId().getRootClassId().getId());
			writeInt(_player.getClassId().getId());
			writeInt(_player.getLevel()); // 270
			writeInt(_player.getClassId().getId()); // 286
		}
		if (containsMask(UserInfoType.BASE_STATS))
		{
			writeShort(18);
			writeShort(_player.getSTR());
			writeShort(_player.getDEX());
			writeShort(_player.getCON());
			writeShort(_player.getINT());
			writeShort(_player.getWIT());
			writeShort(_player.getMEN());
			writeShort(0);
			writeShort(0);
		}
		if (containsMask(UserInfoType.MAX_HPCPMP))
		{
			writeShort(14);
			writeInt(_player.getMaxHp());
			writeInt(_player.getMaxMp());
			writeInt(_player.getMaxCp());
		}
		if (containsMask(UserInfoType.CURRENT_HPMPCP_EXP_SP))
		{
			writeShort(39);
			writeInt((int) Math.round(_player.getCurrentHp()));
			writeInt((int) Math.round(_player.getCurrentMp()));
			writeInt((int) Math.round(_player.getCurrentCp()));
			writeLong(_player.getSp());
			writeLong(_player.getExp());
			writeDouble((float) (_player.getExp() - ExperienceData.getInstance().getExpForLevel(_player.getLevel())) / (ExperienceData.getInstance().getExpForLevel(_player.getLevel() + 1) - ExperienceData.getInstance().getExpForLevel(_player.getLevel())));
			writeByte(0); // 430
		}
		if (containsMask(UserInfoType.ENCHANTLEVEL))
		{
			writeShort(5); // 338
			writeByte(_enchantLevel);
			writeByte(_armorEnchant);
			writeByte(0); // 338 - cBackEnchant?
		}
		if (containsMask(UserInfoType.APPAREANCE))
		{
			writeShort(19); // 338
			writeInt(_player.getVisualHair());
			writeInt(_player.getVisualHairColor());
			writeInt(_player.getVisualFace());
			writeByte(_player.isHairAccessoryEnabled() ? 1 : 0);
			writeInt(_player.getVisualHairColor() + 1); // 338 - DK color.
		}
		if (containsMask(UserInfoType.STATUS))
		{
			writeShort(6);
			writeByte(_player.getMountType().ordinal());
			writeByte(_player.getPrivateStoreType().getId());
			writeByte(_player.hasDwarvenCraft() || (_player.getSkillLevel(248) > 0) ? 1 : 0);
			writeByte(0);
		}
		if (containsMask(UserInfoType.STATS))
		{
			writeShort(64); // 270
			writeShort(_player.getActiveWeaponItem() != null ? 40 : 20);
			writeInt(_player.getPAtk());
			writeInt(_player.getPAtkSpd());
			writeInt(_player.getPDef());
			writeInt(_player.getEvasionRate());
			writeInt(_player.getAccuracy());
			writeInt(_player.getCriticalHit());
			writeInt(_player.getMAtk());
			writeInt(_player.getMAtkSpd());
			writeInt(_player.getPAtkSpd()); // Seems like atk speed - 1
			writeInt(_player.getMagicEvasionRate());
			writeInt(_player.getMDef());
			writeInt(_player.getMagicAccuracy());
			writeInt(_player.getMCriticalHit());
			writeInt(_player.getStat().getWeaponBonusPAtk()); // 270
			writeInt(_player.getStat().getWeaponBonusMAtk()); // 270
		}
		if (containsMask(UserInfoType.ELEMENTALS))
		{
			writeShort(14);
			writeShort(0);
			writeShort(0);
			writeShort(0);
			writeShort(0);
			writeShort(0);
			writeShort(0);
		}
		if (containsMask(UserInfoType.POSITION))
		{
			writeShort(18);
			writeInt(_player.getX());
			writeInt(_player.getY());
			writeInt(_player.getZ());
			writeInt(_player.isInVehicle() ? _player.getVehicle().getObjectId() : 0);
		}
		if (containsMask(UserInfoType.SPEED))
		{
			writeShort(18);
			writeShort(_runSpd);
			writeShort(_walkSpd);
			writeShort(_swimRunSpd);
			writeShort(_swimWalkSpd);
			writeShort(_flRunSpd);
			writeShort(_flWalkSpd);
			writeShort(_flyRunSpd);
			writeShort(_flyWalkSpd);
		}
		if (containsMask(UserInfoType.MULTIPLIER))
		{
			writeShort(18);
			writeDouble(_moveMultiplier);
			writeDouble(_player.getAttackSpeedMultiplier());
		}
		if (containsMask(UserInfoType.COL_RADIUS_HEIGHT))
		{
			writeShort(18);
			writeDouble(_player.getCollisionRadius());
			writeDouble(_player.getCollisionHeight());
		}
		if (containsMask(UserInfoType.ATK_ELEMENTAL))
		{
			writeShort(5);
			writeByte(0);
			writeShort(0);
		}
		if (containsMask(UserInfoType.CLAN))
		{
			writeShort(32 + (_title.length() * 2));
			writeSizedString(_title);
			writeShort(_player.getPledgeType());
			writeInt(_player.getClanId());
			writeInt(_player.getClanCrestLargeId());
			writeInt(_player.getClanCrestId());
			writeInt(_player.getClanPrivileges().getBitmask());
			writeByte(_player.isClanLeader() ? 1 : 0);
			writeInt(_player.getAllyId());
			writeInt(_player.getAllyCrestId());
			writeByte(_player.isInMatchingRoom() ? 1 : 0);
		}
		if (containsMask(UserInfoType.SOCIAL))
		{
			writeShort(34); // 447
			writeByte(_player.getPvpFlag());
			writeInt(_player.getReputation()); // Reputation
			writeByte(_player.isNoble() ? 1 : 0);
			writeByte(_player.isHero() || (_player.isGM() && Config.GM_HERO_AURA) ? 2 : 0); // 152 - Value for enabled changed to 2?
			writeByte(_player.getPledgeClass());
			writeInt(_player.getPkKills());
			writeInt(_player.getPvpKills());
			writeShort(_player.getRecomLeft());
			writeShort(_player.getRecomHave());
			// AFK animation.
			if ((_player.getClan() != null) && (CastleManager.getInstance().getCastleByOwner(_player.getClan()) != null)) // 196
			{
				writeInt(_player.isClanLeader() ? 100 : 101);
			}
			else
			{
				writeInt(0);
			}
			writeInt(0); // 228
			writeInt(0); // 447
		}
		if (containsMask(UserInfoType.VITA_FAME))
		{
			writeShort(19); // 196
			writeInt(_player.getVitalityPoints());
			writeByte(0); // Vita Bonus
			writeInt(0); // _player.getFame()
			writeInt(0); // _player.getRaidbossPoints()
			writeByte(0); // 196
			writeShort(0); // Henna Seal Engraving Gauge
			writeByte(0); // 196
		}
		if (containsMask(UserInfoType.SLOTS))
		{
			writeShort(12); // 152
			writeByte(_player.getInventory().getTalismanSlots());
			writeByte(_player.getInventory().getBroochJewelSlots());
			writeByte(_player.getTeam().getId());
			writeInt(0);
			if (_player.getInventory().getAgathionSlots() > 0)
			{
				writeByte(1); // Charm slots
				writeByte(_player.getInventory().getAgathionSlots() - 1);
			}
			else
			{
				writeByte(0); // Charm slots
				writeByte(0);
			}
			writeByte(_player.getInventory().getArtifactSlots()); // Artifact set slots // 152
		}
		if (containsMask(UserInfoType.MOVEMENTS))
		{
			writeShort(4);
			writeByte(_player.isInsideZone(ZoneId.WATER) ? 1 : _player.isFlyingMounted() ? 2 : 0);
			writeByte(_player.isRunning() ? 1 : 0);
		}
		if (containsMask(UserInfoType.COLOR))
		{
			writeShort(10);
			writeInt(_player.getAppearance().getNameColor());
			writeInt(_player.getAppearance().getTitleColor());
		}
		if (containsMask(UserInfoType.INVENTORY_LIMIT))
		{
			writeShort(13);
			writeShort(0);
			writeShort(0);
			writeShort(_player.getInventoryLimit());
			writeByte(_player.isCursedWeaponEquipped() ? CursedWeaponsManager.getInstance().getLevel(_player.getCursedWeaponEquippedId()) : 0);
			writeByte(0); // 196
			writeByte(0); // 196
			writeByte(0); // 196
			writeByte(0); // 196
		}
		if (containsMask(UserInfoType.TRUE_HERO))
		{
			writeShort(9);
			writeInt(0);
			writeShort(0);
			writeByte(_player.isTrueHero() ? 100 : 0);
		}
		if (containsMask(UserInfoType.ATT_SPIRITS)) // 152
		{
			writeShort(34);
			writeInt((int) _player.getFireSpiritAttack());
			writeInt((int) _player.getWaterSpiritAttack());
			writeInt((int) _player.getWindSpiritAttack());
			writeInt((int) _player.getEarthSpiritAttack());
			writeInt((int) _player.getFireSpiritDefense());
			writeInt((int) _player.getWaterSpiritDefense());
			writeInt((int) _player.getWindSpiritDefense());
			writeInt((int) _player.getEarthSpiritDefense());
		}
		if (containsMask(UserInfoType.RANKING)) // 196
		{
			writeShort(6);
			writeInt(RankManager.getInstance().getPlayerGlobalRank(_player) == 1 ? 1 : RankManager.getInstance().getPlayerRaceRank(_player) == 1 ? 2 : 0);
		}
		if (containsMask(UserInfoType.STAT_POINTS)) // 235
		{
			writeShort(16);
			writeShort(_player.getLevel() < 76 ? 0 : (_player.getLevel() - 75) + _player.getVariables().getInt(PlayerVariables.ELIXIRS_AVAILABLE, 0) + (int) _player.getStat().getValue(Stat.ELIXIR_USAGE_LIMIT, 0)); // Usable points
			writeShort(_player.getVariables().getInt(PlayerVariables.STAT_STR, 0));
			writeShort(_player.getVariables().getInt(PlayerVariables.STAT_DEX, 0));
			writeShort(_player.getVariables().getInt(PlayerVariables.STAT_CON, 0));
			writeShort(_player.getVariables().getInt(PlayerVariables.STAT_INT, 0));
			writeShort(_player.getVariables().getInt(PlayerVariables.STAT_WIT, 0));
			writeShort(_player.getVariables().getInt(PlayerVariables.STAT_MEN, 0));
		}
		if (containsMask(UserInfoType.STAT_ABILITIES)) // 235
		{
			writeShort(18);
			writeShort(_player.getSTR() - _player.getTemplate().getBaseSTR() - _player.getVariables().getInt(PlayerVariables.STAT_STR, 0)); // additional STR
			writeShort(_player.getDEX() - _player.getTemplate().getBaseDEX() - _player.getVariables().getInt(PlayerVariables.STAT_DEX, 0)); // additional DEX
			writeShort(_player.getCON() - _player.getTemplate().getBaseCON() - _player.getVariables().getInt(PlayerVariables.STAT_CON, 0)); // additional CON
			writeShort(_player.getINT() - _player.getTemplate().getBaseINT() - _player.getVariables().getInt(PlayerVariables.STAT_INT, 0)); // additional INT
			writeShort(_player.getWIT() - _player.getTemplate().getBaseWIT() - _player.getVariables().getInt(PlayerVariables.STAT_WIT, 0)); // additional WIT
			writeShort(_player.getMEN() - _player.getTemplate().getBaseMEN() - _player.getVariables().getInt(PlayerVariables.STAT_MEN, 0)); // additional MEN
			writeShort(0);
			writeShort(0);
		}
		if (containsMask(UserInfoType.ELIXIR_USED)) // 286
		{
			writeShort(_player.getVariables().getInt(PlayerVariables.ELIXIRS_AVAILABLE, 0)); // count
			writeShort(0);
		}
		
		if (containsMask(UserInfoType.VANGUARD_MOUNT)) // 362
		{
			writeByte(_player.getClassId().level() + 1); // 362 - Vanguard mount.
		}
	}
	
	@Override
	public void run()
	{
		if (_player == null)
		{
			return;
		}
		
		// Send exp bonus change.
		if (containsMask(UserInfoType.VITA_FAME))
		{
			_player.sendUserBoostStat();
		}
	}
	
	private int calculateRelation(Player player)
	{
		int relation = 0;
		final Party party = player.getParty();
		final Clan clan = player.getClan();
		if (party != null)
		{
			relation |= 8; // Party member
			if (party.getLeader() == _player)
			{
				relation |= 16; // Party leader
			}
		}
		if (clan != null)
		{
			if (player.getSiegeState() == 1)
			{
				relation |= 256; // Clan member
			}
			else if (player.getSiegeState() == 2)
			{
				relation |= 32; // Clan member
			}
			if (clan.getLeaderId() == player.getObjectId())
			{
				relation |= 64; // Clan leader
			}
		}
		if (player.getSiegeState() != 0)
		{
			relation |= 128; // In siege
		}
		return relation;
	}
}
