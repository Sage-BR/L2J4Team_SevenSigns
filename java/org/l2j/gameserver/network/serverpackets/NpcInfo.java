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

import org.l2j.gameserver.data.sql.ClanTable;
import org.l2j.gameserver.data.xml.NpcData;
import org.l2j.gameserver.data.xml.NpcNameLocalisationData;
import org.l2j.gameserver.enums.NpcInfoType;
import org.l2j.gameserver.enums.Team;
import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.instance.Guard;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.model.skill.AbnormalVisualEffect;
import org.l2j.gameserver.model.zone.ZoneId;
import org.l2j.gameserver.network.NpcStringId;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author UnAfraid
 */
public class NpcInfo extends AbstractMaskPacket<NpcInfoType>
{
	private final Npc _npc;
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
	private final Set<AbnormalVisualEffect> _abnormalVisualEffects;
	
	public NpcInfo(Npc npc)
	{
		_npc = npc;
		_abnormalVisualEffects = npc.getEffectList().getCurrentAbnormalVisualEffects();
		addComponentType(NpcInfoType.ATTACKABLE, NpcInfoType.RELATIONS, NpcInfoType.ID, NpcInfoType.POSITION, NpcInfoType.STOP_MODE, NpcInfoType.MOVE_MODE);
		if (npc.getHeading() > 0)
		{
			addComponentType(NpcInfoType.HEADING);
		}
		if ((npc.getStat().getPAtkSpd() > 0) || (npc.getStat().getMAtkSpd() > 0))
		{
			addComponentType(NpcInfoType.ATK_CAST_SPEED);
		}
		if (npc.getRunSpeed() > 0)
		{
			addComponentType(NpcInfoType.SPEED_MULTIPLIER);
		}
		if ((npc.getLeftHandItem() > 0) || (npc.getRightHandItem() > 0))
		{
			addComponentType(NpcInfoType.EQUIPPED);
		}
		if (npc.getTeam() != Team.NONE)
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
		if (npc.getDisplayEffect() > 0)
		{
			addComponentType(NpcInfoType.DISPLAY_EFFECT);
		}
		if (npc.isInsideZone(ZoneId.WATER) || npc.isFlying())
		{
			addComponentType(NpcInfoType.SWIM_OR_FLY);
		}
		if (npc.isFlying())
		{
			addComponentType(NpcInfoType.FLYING);
		}
		if (npc.getCloneObjId() > 0)
		{
			addComponentType(NpcInfoType.CLONE);
		}
		if (npc.getMaxHp() > 0)
		{
			addComponentType(NpcInfoType.MAX_HP);
		}
		if (npc.getMaxMp() > 0)
		{
			addComponentType(NpcInfoType.MAX_MP);
		}
		if (npc.getCurrentHp() <= npc.getMaxHp())
		{
			addComponentType(NpcInfoType.CURRENT_HP);
		}
		if (npc.getCurrentMp() <= npc.getMaxMp())
		{
			addComponentType(NpcInfoType.CURRENT_MP);
		}
		if (npc.getTemplate().isUsingServerSideName())
		{
			addComponentType(NpcInfoType.NAME);
		}
		if (npc.getTemplate().isUsingServerSideTitle() || (npc.isMonster() && (Config.SHOW_NPC_LEVEL || Config.SHOW_NPC_AGGRESSION)) || npc.isChampion() || npc.isTrap())
		{
			addComponentType(NpcInfoType.TITLE);
		}
		if (npc.getNameString() != null)
		{
			addComponentType(NpcInfoType.NAME_NPCSTRINGID);
		}
		if (npc.getTitleString() != null)
		{
			addComponentType(NpcInfoType.TITLE_NPCSTRINGID);
		}
		if (_npc.getReputation() != 0)
		{
			addComponentType(NpcInfoType.REPUTATION);
		}
		if (!_abnormalVisualEffects.isEmpty() || npc.isInvisible())
		{
			addComponentType(NpcInfoType.ABNORMALS);
		}
		if (npc.getEnchantEffect() > 0)
		{
			addComponentType(NpcInfoType.ENCHANT);
		}
		if (npc.getTransformationDisplayId() > 0)
		{
			addComponentType(NpcInfoType.TRANSFORMATION);
		}
		if (npc.isShowSummonAnimation())
		{
			addComponentType(NpcInfoType.SUMMONED);
		}
		if (npc.getClanId() > 0)
		{
			final Clan clan = ClanTable.getInstance().getClan(npc.getClanId());
			if ((clan != null) && ((npc.getTemplate().getId() == 34156 /* Clan Stronghold Device */) || (!npc.isMonster() && npc.isInsideZone(ZoneId.PEACE))))
			{
				_clanId = clan.getId();
				_clanCrest = clan.getCrestId();
				_clanLargeCrest = clan.getCrestLargeId();
				_allyCrest = clan.getAllyCrestId();
				_allyId = clan.getAllyId();
				addComponentType(NpcInfoType.CLAN);
			}
		}
		addComponentType(NpcInfoType.PET_EVOLUTION_ID);
		if (npc.getPvpFlag() > 0)
		{
			addComponentType(NpcInfoType.PVP_FLAG);
		}
		// TODO: Confirm me
		if (npc.isInCombat())
		{
			_statusMask |= 0x01;
		}
		if (npc.isDead())
		{
			_statusMask |= 0x02;
		}
		if (npc.isTargetable())
		{
			_statusMask |= 0x04;
		}
		if (npc.isShowName())
		{
			_statusMask |= 0x08;
		}
		if (_statusMask != 0x00)
		{
			addComponentType(NpcInfoType.VISUAL_STATE);
		}
	}
	
	@Override
	protected byte[] getMasks()
	{
		return _masks;
	}
	
	@Override
	protected void onNewMaskAdded(NpcInfoType component)
	{
		calcBlockSize(_npc, component);
	}
	
	private void calcBlockSize(Npc npc, NpcInfoType type)
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
				_initSize += type.getBlockLength() + (npc.getTitle().length() * 2);
				break;
			}
			case NAME:
			{
				_blockSize += type.getBlockLength() + (npc.getName().length() * 2);
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
		// Localisation related.
		String[] localisation = null;
		if (Config.MULTILANG_ENABLE)
		{
			final Player player = getPlayer();
			if (player != null)
			{
				final String lang = player.getLang();
				if ((lang != null) && !lang.equals("en"))
				{
					localisation = NpcNameLocalisationData.getInstance().getLocalisation(lang, _npc.getId());
					if (localisation != null)
					{
						if (!containsMask(NpcInfoType.NAME))
						{
							addComponentType(NpcInfoType.NAME);
						}
						_blockSize -= _npc.getName().length() * 2;
						_blockSize += localisation[0].length() * 2;
						if (!localisation[1].equals(""))
						{
							if (!containsMask(NpcInfoType.TITLE))
							{
								addComponentType(NpcInfoType.TITLE);
							}
							final String title = _npc.getTitle();
							_initSize -= title.length() * 2;
							if (title.equals(""))
							{
								_initSize += localisation[1].length() * 2;
							}
							else
							{
								_initSize += title.replace(NpcData.getInstance().getTemplate(_npc.getId()).getTitle(), localisation[1]).length() * 2;
							}
						}
					}
				}
			}
		}
		
		ServerPackets.NPC_INFO.writeId(this);
		writeInt(_npc.getObjectId());
		writeByte(_npc.isShowSummonAnimation() ? 2 : 0); // // 0=teleported 1=default 2=summoned
		writeShort(38); // 338 - mask_bits_38
		writeBytes(_masks);
		// Block 1
		writeByte(_initSize);
		if (containsMask(NpcInfoType.ATTACKABLE))
		{
			writeByte(_npc.isAttackable() && !(_npc instanceof Guard));
		}
		if (containsMask(NpcInfoType.RELATIONS))
		{
			writeLong(0);
		}
		if (containsMask(NpcInfoType.TITLE))
		{
			String title = _npc.getTitle();
			// Localisation related.
			if ((localisation != null) && !localisation[1].equals(""))
			{
				if (title.equals(""))
				{
					title = localisation[1];
				}
				else
				{
					title = title.replace(NpcData.getInstance().getTemplate(_npc.getId()).getTitle(), localisation[1]);
				}
			}
			writeString(title);
		}
		// Block 2
		writeShort(_blockSize);
		if (containsMask(NpcInfoType.ID))
		{
			writeInt(_npc.getTemplate().getDisplayId() + 1000000);
		}
		if (containsMask(NpcInfoType.POSITION))
		{
			writeInt(_npc.getX());
			writeInt(_npc.getY());
			writeInt(_npc.getZ());
		}
		if (containsMask(NpcInfoType.HEADING))
		{
			writeInt(_npc.getHeading());
		}
		if (containsMask(NpcInfoType.VEHICLE_ID))
		{
			writeInt(0); // Vehicle object id.
		}
		if (containsMask(NpcInfoType.ATK_CAST_SPEED))
		{
			writeInt(_npc.getPAtkSpd());
			writeInt(_npc.getMAtkSpd());
		}
		if (containsMask(NpcInfoType.SPEED_MULTIPLIER))
		{
			writeFloat((float) _npc.getStat().getMovementSpeedMultiplier());
			writeFloat((float) _npc.getStat().getAttackSpeedMultiplier());
		}
		if (containsMask(NpcInfoType.EQUIPPED))
		{
			writeInt(_npc.getRightHandItem());
			writeInt(0); // Armor id?
			writeInt(_npc.getLeftHandItem());
		}
		if (containsMask(NpcInfoType.STOP_MODE))
		{
			writeByte(!_npc.isDead());
		}
		if (containsMask(NpcInfoType.MOVE_MODE))
		{
			writeByte(_npc.isRunning());
		}
		if (containsMask(NpcInfoType.SWIM_OR_FLY))
		{
			writeByte(_npc.isInsideZone(ZoneId.WATER) ? 1 : _npc.isFlying() ? 2 : 0);
		}
		if (containsMask(NpcInfoType.TEAM))
		{
			writeByte(_npc.getTeam().getId());
		}
		if (containsMask(NpcInfoType.ENCHANT))
		{
			writeInt(_npc.getEnchantEffect());
		}
		if (containsMask(NpcInfoType.FLYING))
		{
			writeInt(_npc.isFlying());
		}
		if (containsMask(NpcInfoType.CLONE))
		{
			writeInt(_npc.getCloneObjId()); // Player ObjectId with Decoy
		}
		if (containsMask(NpcInfoType.PET_EVOLUTION_ID))
		{
			writeInt(0); // Unknown
		}
		if (containsMask(NpcInfoType.DISPLAY_EFFECT))
		{
			writeInt(_npc.getDisplayEffect());
		}
		if (containsMask(NpcInfoType.TRANSFORMATION))
		{
			writeInt(_npc.getTransformationDisplayId()); // Transformation ID
		}
		if (containsMask(NpcInfoType.CURRENT_HP))
		{
			writeInt((int) _npc.getCurrentHp());
		}
		if (containsMask(NpcInfoType.CURRENT_MP))
		{
			writeInt((int) _npc.getCurrentMp());
		}
		if (containsMask(NpcInfoType.MAX_HP))
		{
			writeInt(_npc.getMaxHp());
		}
		if (containsMask(NpcInfoType.MAX_MP))
		{
			writeInt(_npc.getMaxMp());
		}
		if (containsMask(NpcInfoType.SUMMONED))
		{
			writeByte(0); // 2 - do some animation on spawn
		}
		if (containsMask(NpcInfoType.FOLLOW_INFO))
		{
			writeInt(0);
			writeInt(0);
		}
		if (containsMask(NpcInfoType.NAME))
		{
			writeString(localisation != null ? localisation[0] : _npc.getName());
		}
		if (containsMask(NpcInfoType.NAME_NPCSTRINGID))
		{
			final NpcStringId nameString = _npc.getNameString();
			writeInt(nameString != null ? nameString.getId() : -1); // NPCStringId for name
		}
		if (containsMask(NpcInfoType.TITLE_NPCSTRINGID))
		{
			final NpcStringId titleString = _npc.getTitleString();
			writeInt(titleString != null ? titleString.getId() : -1); // NPCStringId for title
		}
		if (containsMask(NpcInfoType.PVP_FLAG))
		{
			writeByte(_npc.getPvpFlag()); // PVP flag
		}
		if (containsMask(NpcInfoType.REPUTATION))
		{
			writeInt(_npc.getReputation()); // Reputation
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
			final Team team = (Config.BLUE_TEAM_ABNORMAL_EFFECT != null) && (Config.RED_TEAM_ABNORMAL_EFFECT != null) ? _npc.getTeam() : Team.NONE;
			writeShort(_abnormalVisualEffects.size() + (_npc.isInvisible() ? 1 : 0) + (team != Team.NONE ? 1 : 0));
			for (AbnormalVisualEffect abnormalVisualEffect : _abnormalVisualEffects)
			{
				writeShort(abnormalVisualEffect.getClientId());
			}
			if (_npc.isInvisible())
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