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

import org.l2j.gameserver.data.xml.ExperienceData;
import org.l2j.gameserver.enums.AttributeType;
import org.l2j.gameserver.model.VariationInstance;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.ServerPackets;

public class GMViewCharacterInfo extends ServerPacket
{
	private final Player _player;
	private final int _runSpd;
	private final int _walkSpd;
	private final int _swimRunSpd;
	private final int _swimWalkSpd;
	private final int _flyRunSpd;
	private final int _flyWalkSpd;
	private final double _moveMultiplier;
	
	public GMViewCharacterInfo(Player player)
	{
		_player = player;
		_moveMultiplier = player.getMovementSpeedMultiplier();
		_runSpd = (int) Math.round(player.getRunSpeed() / _moveMultiplier);
		_walkSpd = (int) Math.round(player.getWalkSpeed() / _moveMultiplier);
		_swimRunSpd = (int) Math.round(player.getSwimRunSpeed() / _moveMultiplier);
		_swimWalkSpd = (int) Math.round(player.getSwimWalkSpeed() / _moveMultiplier);
		_flyRunSpd = player.isFlying() ? _runSpd : 0;
		_flyWalkSpd = player.isFlying() ? _walkSpd : 0;
	}
	
	@Override
	public void write()
	{
		ServerPackets.GM_VIEW_CHARACTER_INFO.writeId(this);
		writeInt(_player.getX());
		writeInt(_player.getY());
		writeInt(_player.getZ());
		writeInt(_player.getHeading());
		writeInt(_player.getObjectId());
		writeString(_player.getName());
		writeInt(_player.getRace().ordinal());
		writeInt(_player.getAppearance().isFemale() ? 1 : 0);
		writeInt(_player.getClassId().getId());
		writeInt(_player.getLevel());
		writeLong(_player.getExp());
		writeDouble((float) (_player.getExp() - ExperienceData.getInstance().getExpForLevel(_player.getLevel())) / (ExperienceData.getInstance().getExpForLevel(_player.getLevel() + 1) - ExperienceData.getInstance().getExpForLevel(_player.getLevel()))); // High Five exp %
		writeInt(_player.getSTR());
		writeInt(_player.getDEX());
		writeInt(_player.getCON());
		writeInt(_player.getINT());
		writeInt(_player.getWIT());
		writeInt(_player.getMEN());
		writeInt(0); // LUC
		writeInt(0); // CHA
		writeInt(_player.getMaxHp());
		writeInt((int) _player.getCurrentHp());
		writeInt(_player.getMaxMp());
		writeInt((int) _player.getCurrentMp());
		writeLong(_player.getSp());
		writeInt(_player.getCurrentLoad());
		writeInt(_player.getMaxLoad());
		writeInt(_player.getPkKills());
		for (int slot : getPaperdollOrder())
		{
			writeInt(_player.getInventory().getPaperdollObjectId(slot));
		}
		for (int slot : getPaperdollOrder())
		{
			writeInt(_player.getInventory().getPaperdollItemDisplayId(slot));
		}
		for (int slot = 0; slot < 11; slot++)
		{
			final VariationInstance augment = _player.getInventory().getPaperdollAugmentation(slot);
			writeInt(augment != null ? augment.getOption1Id() : 0); // Confirmed
			writeInt(augment != null ? augment.getOption2Id() : 0); // Confirmed
		}
		for (int index = 0; index < 98; index++)
		{
			writeInt(0); // unk
		}
		writeByte(0); // unk
		writeByte(0); // unk
		writeByte(_player.getInventory().getTalismanSlots()); // CT2.3
		writeByte(_player.getInventory().canEquipCloak() ? 1 : 0); // CT2.3
		writeByte(0);
		writeShort(0);
		writeInt(_player.getPAtk());
		writeInt(_player.getPAtkSpd());
		writeInt(_player.getPDef());
		writeInt(_player.getEvasionRate());
		writeInt(_player.getAccuracy());
		writeInt(_player.getCriticalHit());
		writeInt(_player.getMAtk());
		writeInt(_player.getMAtkSpd());
		writeInt(_player.getPAtkSpd());
		writeInt(_player.getMDef());
		writeInt(_player.getMagicEvasionRate());
		writeInt(_player.getMagicAccuracy());
		writeInt(_player.getMCriticalHit());
		writeInt(_player.getPvpFlag()); // 0-non-pvp 1-pvp = violett name
		writeInt(_player.getReputation());
		writeInt(_runSpd);
		writeInt(_walkSpd);
		writeInt(_swimRunSpd);
		writeInt(_swimWalkSpd);
		writeInt(_flyRunSpd);
		writeInt(_flyWalkSpd);
		writeInt(_flyRunSpd);
		writeInt(_flyWalkSpd);
		writeDouble(_moveMultiplier);
		writeDouble(_player.getAttackSpeedMultiplier()); // 2.9); //
		writeDouble(_player.getCollisionRadius()); // scale
		writeDouble(_player.getCollisionHeight()); // y offset ??!? fem dwarf 4033
		writeInt(_player.getAppearance().getHairStyle());
		writeInt(_player.getAppearance().getHairColor());
		writeInt(_player.getAppearance().getFace());
		writeInt(_player.isGM() ? 1 : 0); // builder level
		writeString(_player.getTitle());
		writeInt(_player.getClanId()); // pledge id
		writeInt(_player.getClanCrestId()); // pledge crest id
		writeInt(_player.getAllyId()); // ally id
		writeByte(_player.getMountType().ordinal()); // mount type
		writeByte(_player.getPrivateStoreType().getId());
		writeByte(_player.hasDwarvenCraft() ? 1 : 0);
		writeInt(_player.getPkKills());
		writeInt(_player.getPvpKills());
		writeShort(_player.getRecomLeft());
		writeShort(_player.getRecomHave()); // Blue value for name (0 = white, 255 = pure blue)
		writeInt(_player.getClassId().getId());
		writeInt(0); // special effects? circles around player...
		writeInt(_player.getMaxCp());
		writeInt((int) _player.getCurrentCp());
		writeByte(_player.isRunning() ? 1 : 0); // changes the Speed display on Status Window
		writeByte(321);
		writeInt(_player.getPledgeClass()); // changes the text above CP on Status Window
		writeByte(_player.isNoble() ? 1 : 0);
		writeByte(_player.isHero() ? 1 : 0);
		writeInt(_player.getAppearance().getNameColor());
		writeInt(_player.getAppearance().getTitleColor());
		final AttributeType attackAttribute = _player.getAttackElement();
		writeShort(attackAttribute.getClientId());
		writeShort(_player.getAttackElementValue(attackAttribute));
		for (AttributeType type : AttributeType.ATTRIBUTE_TYPES)
		{
			writeShort(_player.getDefenseElementValue(type));
		}
		writeInt(_player.getFame());
		writeInt(_player.getVitalityPoints());
		writeInt(0);
		writeInt(0);
	}
}
