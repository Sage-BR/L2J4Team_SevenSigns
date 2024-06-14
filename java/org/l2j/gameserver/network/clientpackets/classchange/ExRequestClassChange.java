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
package org.l2j.gameserver.network.clientpackets.classchange;

import org.l2j.Config;
import org.l2j.gameserver.data.xml.CategoryData;
import org.l2j.gameserver.enums.CategoryType;
import org.l2j.gameserver.enums.ClassId;
import org.l2j.gameserver.enums.UserInfoType;
import org.l2j.gameserver.model.ElementalSpirit;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.PacketLogger;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.PlaySound;
import org.l2j.gameserver.network.serverpackets.UserInfo;
import org.l2j.gameserver.network.serverpackets.classchange.ExClassChangeSetAlarm;
import org.l2j.gameserver.network.serverpackets.elementalspirits.ElementalSpiritInfo;
import org.l2j.gameserver.network.serverpackets.elementalspirits.ExElementalSpiritAttackType;

/**
 * @author Mobius
 */
public class ExRequestClassChange extends ClientPacket
{
	private int _classId;
	
	@Override
	protected void readImpl()
	{
		_classId = readInt();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		// Check if class id is valid.
		boolean canChange = false;
		for (ClassId cId : player.getClassId().getNextClassIds())
		{
			if (cId.getId() == _classId)
			{
				canChange = true;
				break;
			}
		}
		if (!canChange)
		{
			PacketLogger.warning(player + " tried to change class from " + player.getClassId() + " to " + ClassId.getClassId(_classId) + "!");
			return;
		}
		
		// Check for player proper class group and level.
		canChange = false;
		final int playerLevel = player.getLevel();
		if (player.isInCategory(CategoryType.FIRST_CLASS_GROUP) && (playerLevel >= 18))
		{
			canChange = CategoryData.getInstance().isInCategory(CategoryType.SECOND_CLASS_GROUP, _classId);
		}
		else if (player.isInCategory(CategoryType.SECOND_CLASS_GROUP) && (playerLevel >= 38))
		{
			canChange = CategoryData.getInstance().isInCategory(CategoryType.THIRD_CLASS_GROUP, _classId);
		}
		else if (player.isInCategory(CategoryType.THIRD_CLASS_GROUP) && (playerLevel >= 76))
		{
			canChange = CategoryData.getInstance().isInCategory(CategoryType.FOURTH_CLASS_GROUP, _classId);
		}
		
		// Change class.
		if (canChange)
		{
			player.setClassId(_classId);
			if (player.isSubClassActive())
			{
				player.getSubClasses().get(player.getClassIndex()).setClassId(player.getActiveClass());
			}
			else
			{
				player.setBaseClass(player.getActiveClass());
			}
			
			// Class change rewards.
			if (!Config.DISABLE_TUTORIAL)
			{
				switch (player.getClassId())
				{
					case KNIGHT:
					case ELVEN_KNIGHT:
					case PALUS_KNIGHT:
					case DEATH_BLADE_HUMAN:
					case DEATH_BLADE_ELF:
					case DEATH_BLADE_DARK_ELF:
					{
						player.addItem("ExRequestClassChange", 93028, 1, player, true); // Aden Sword.
						player.addItem("ExRequestClassChange", 93493, 1, player, true); // Moon Armor Set.
						player.addItem("ExRequestClassChange", 93496, 1, player, true); // 1st Class Transfer Gift Box.
						break;
					}
					case WARRIOR:
					{
						player.addItem("ExRequestClassChange", 93028, 1, player, true); // Aden Sword.
						player.addItem("ExRequestClassChange", 93034, 1, player, true); // Aden Spear.
						player.addItem("ExRequestClassChange", 93493, 1, player, true); // Moon Armor Set.
						player.addItem("ExRequestClassChange", 93496, 1, player, true); // 1st Class Transfer Gift Box.
						break;
					}
					case ROGUE:
					case ELVEN_SCOUT:
					case ASSASSIN:
					{
						player.addItem("ExRequestClassChange", 93029, 1, player, true); // Aden Dagger.
						player.addItem("ExRequestClassChange", 93030, 1, player, true); // Aden Bow.
						player.addItem("ExRequestClassChange", 1341, 2000, player, true); // Bone Arrow.
						player.addItem("ExRequestClassChange", 93494, 1, player, true); // Moon Shell Set.
						player.addItem("ExRequestClassChange", 93496, 1, player, true); // 1st Class Transfer Gift Box.
						break;
					}
					case WIZARD:
					case CLERIC:
					case ELVEN_WIZARD:
					case ORACLE:
					case DARK_WIZARD:
					case SHILLIEN_ORACLE:
					case ORC_SHAMAN:
					{
						player.addItem("ExRequestClassChange", 93033, 1, player, true); // Two-Handed Blunt Weapon of Aden.
						player.addItem("ExRequestClassChange", 93495, 1, player, true); // Moon Cape Set.
						player.addItem("ExRequestClassChange", 93496, 1, player, true); // 1st Class Transfer Gift Box.
						break;
					}
					case ORC_RAIDER:
					{
						player.addItem("ExRequestClassChange", 93032, 1, player, true); // Two-handed Sword of Aden.
						player.addItem("ExRequestClassChange", 93493, 1, player, true); // Moon Armor Set.
						player.addItem("ExRequestClassChange", 93497, 1, player, true); // 1st Class Transfer Gift Box.
						break;
					}
					case ORC_MONK:
					{
						player.addItem("ExRequestClassChange", 93035, 1, player, true); // Aden Fist.
						player.addItem("ExRequestClassChange", 93493, 1, player, true); // Moon Armor Set.
						player.addItem("ExRequestClassChange", 93497, 1, player, true); // 1st Class Transfer Gift Box.
						break;
					}
					case ARTISAN:
					case SCAVENGER:
					{
						player.addItem("ExRequestClassChange", 93031, 1, player, true); // Aden Club.
						player.addItem("ExRequestClassChange", 93034, 1, player, true); // Aden Spear.
						player.addItem("ExRequestClassChange", 93493, 1, player, true); // Moon Armor Set.
						player.addItem("ExRequestClassChange", 93496, 1, player, true); // 1st Class Transfer Gift Box.
						break;
					}
					case TROOPER:
					{
						player.addItem("ExRequestClassChange", 93037, 1, player, true); // Aden Ancient Sword.
						player.addItem("ExRequestClassChange", 93494, 1, player, true); // Moon Shell Set.
						player.addItem("ExRequestClassChange", 93496, 1, player, true); // 1st Class Transfer Gift Box.
						break;
					}
					case WARDER:
					{
						player.addItem("ExRequestClassChange", 93030, 1, player, true); // Aden Bow.
						player.addItem("ExRequestClassChange", 1341, 2000, player, true); // Bone Arrow.
						player.addItem("ExRequestClassChange", 93494, 1, player, true); // Moon Shell Set.
						player.addItem("ExRequestClassChange", 93496, 1, player, true); // 1st Class Transfer Gift Box.
						break;
					}
					case SOUL_FINDER:
					{
						player.addItem("ExRequestClassChange", 93036, 1, player, true); // Aden Rapier.
						player.addItem("ExRequestClassChange", 93494, 1, player, true); // Moon Shell Set.
						player.addItem("ExRequestClassChange", 93496, 1, player, true); // 1st Class Transfer Gift Box.
						break;
					}
					case SHARPSHOOTER:
					{
						player.addItem("ExRequestClassChange", 94892, 1, player, true); // D-Grade Elemental Orb Sealed.
						player.addItem("ExRequestClassChange", 94897, 1, player, true); // Aden Pistols
						player.addItem("ExRequestClassChange", 93494, 1, player, true); // Moon Shell Set.
						player.addItem("ExRequestClassChange", 93496, 1, player, true); // 1st Class Transfer Gift Box.
						break;
					}
					case RIDER:
					{
						player.addItem("ExRequestClassChange", 93034, 1, player, true); // Aden Spear.
						player.addItem("ExRequestClassChange", 93493, 1, player, true); // Moon Armor Set.
						player.addItem("ExRequestClassChange", 93496, 1, player, true); // 1st Class Transfer Gift Box.
						break;
					}
					case ASSASSIN_MALE_1:
					case ASSASSIN_FEMALE_1:
					{
						player.addItem("ExRequestClassChange", 94998, 1, player, true); // Maingauche.
						player.addItem("ExRequestClassChange", 93494, 1, player, true); // Moon Shell Set.
						player.addItem("ExRequestClassChange", 93496, 1, player, true); // 1st Class Transfer Gift Box.
						break;
					}
					case ASSASSIN_FEMALE_3:
					case ASSASSIN_MALE_3:
					{
						player.setAssassinationPoints(1);
						break;
					}
				}
			}
			
			// Elemental Spirits.
			if (player.isInCategory(CategoryType.THIRD_CLASS_GROUP))
			{
				if (player.getSpirits() == null)
				{
					player.initElementalSpirits();
				}
				for (ElementalSpirit spirit : player.getSpirits())
				{
					if (spirit.getStage() == 0)
					{
						spirit.upgrade();
					}
				}
				final UserInfo userInfo = new UserInfo(player);
				userInfo.addComponentType(UserInfoType.ATT_SPIRITS);
				player.sendPacket(userInfo);
				player.sendPacket(new ElementalSpiritInfo(player, (byte) 0));
				player.sendPacket(new ExElementalSpiritAttackType(player));
			}
			
			if (Config.AUTO_LEARN_SKILLS)
			{
				player.giveAvailableSkills(Config.AUTO_LEARN_FS_SKILLS, true, Config.AUTO_LEARN_SKILLS_WITHOUT_ITEMS);
			}
			
			player.store(false); // Save player cause if server crashes before this char is saved, he will lose class.
			player.broadcastUserInfo();
			player.sendSkillList();
			player.sendPacket(new PlaySound("ItemSound.quest_fanfare_2"));
			
			if (Config.DISABLE_TUTORIAL && !player.isInCategory(CategoryType.FOURTH_CLASS_GROUP) //
				&& ((player.isInCategory(CategoryType.SECOND_CLASS_GROUP) && (playerLevel >= 38)) //
					|| (player.isInCategory(CategoryType.THIRD_CLASS_GROUP) && (playerLevel >= 76))))
			{
				player.sendPacket(ExClassChangeSetAlarm.STATIC_PACKET);
			}
		}
	}
}
