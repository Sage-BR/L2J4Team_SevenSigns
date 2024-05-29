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
package org.l2j.gameserver.network.clientpackets;

import java.util.logging.Logger;

import org.l2j.Config;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.commons.util.Rnd;
import org.l2j.gameserver.data.xml.SkillData;
import org.l2j.gameserver.data.xml.SkillEnchantData;
import org.l2j.gameserver.enums.PrivateStoreType;
import org.l2j.gameserver.enums.SkillEnchantType;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.EnchantStarHolder;
import org.l2j.gameserver.model.holders.SkillEnchantHolder;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.PacketLogger;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.serverpackets.ExEnchantSkillResult;
import org.l2j.gameserver.network.serverpackets.SystemMessage;
import org.l2j.gameserver.network.serverpackets.newskillenchant.ExSkillEnchantInfo;

/**
 * @author -Wooden-
 */
public class RequestExEnchantSkill implements ClientPacket
{
	private static final Logger LOGGER = Logger.getLogger(RequestExEnchantSkill.class.getName());
	private static final Logger LOGGER_ENCHANT = Logger.getLogger("enchant.skills");
	
	private SkillEnchantType _type;
	private int _skillId;
	private int _skillLevel;
	private int _skillSubLevel;
	
	@Override
	public void read(ReadablePacket packet)
	{
		final int type = packet.readInt();
		if ((type < 0) || (type >= SkillEnchantType.values().length))
		{
			PacketLogger.warning("Client send incorrect type " + type + " on packet: " + getClass().getSimpleName());
			return;
		}
		
		_type = SkillEnchantType.values()[type];
		_skillId = packet.readInt();
		_skillLevel = packet.readShort();
		_skillSubLevel = packet.readShort();
	}
	
	@Override
	public void run(GameClient client)
	{
		if (!client.getFloodProtectors().canPerformPlayerAction())
		{
			return;
		}
		
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if ((_skillId <= 0) || (_skillLevel <= 0) || (_skillSubLevel < 0))
		{
			PacketLogger.warning(player + " tried to exploit RequestExEnchantSkill!");
			return;
		}
		
		if (!player.isAllowedToEnchantSkills())
		{
			return;
		}
		
		if (player.isSellingBuffs())
		{
			return;
		}
		
		if (player.isInOlympiadMode())
		{
			return;
		}
		
		if (player.getPrivateStoreType() != PrivateStoreType.NONE)
		{
			return;
		}
		
		Skill skill = player.getKnownSkill(_skillId);
		if (skill == null)
		{
			return;
		}
		
		if (!skill.isEnchantable())
		{
			return;
		}
		
		if (skill.getLevel() != _skillLevel)
		{
			return;
		}
		
		if (skill.getSubLevel() > 0)
		{
			if (_type == SkillEnchantType.CHANGE)
			{
				final int group1 = (_skillSubLevel % 1000);
				final int group2 = (skill.getSubLevel() % 1000);
				if (group1 != group2)
				{
					LOGGER.warning(getClass().getSimpleName() + ": Client: " + client + " send incorrect sub level group: " + group1 + " expected: " + group2 + " for skill " + _skillId);
					return;
				}
			}
			else if ((skill.getSubLevel() + 1) != _skillSubLevel)
			{
				LOGGER.warning(getClass().getSimpleName() + ": Client: " + client + " send incorrect sub level: " + _skillSubLevel + " expected: " + (skill.getSubLevel() + 1) + " for skill " + _skillId);
				return;
			}
		}
		
		SkillEnchantHolder skillEnchantHolder = SkillEnchantData.getInstance().getSkillEnchant(skill.getId());
		if (skillEnchantHolder == null)
		{
			LOGGER.warning(getClass().getSimpleName() + " request enchant skill dont have star lvl skillId-" + skill.getId());
			return;
		}
		EnchantStarHolder starHolder = SkillEnchantData.getInstance().getEnchantStar(skillEnchantHolder.getStarLevel());
		if (starHolder == null)
		{
			LOGGER.warning(getClass().getSimpleName() + " request enchant skill dont have star lvl-" + skill.getId());
			return;
		}
		
		if (player.getAdena() < 1000000)
		{
			return;
		}
		
		final int starLevel = starHolder.getLevel();
		if (Rnd.get(100) <= SkillEnchantData.getInstance().getChanceEnchantMap(skill))
		{
			final Skill enchantedSkill = SkillData.getInstance().getSkill(_skillId, _skillLevel, _skillSubLevel);
			if (Config.LOG_SKILL_ENCHANTS)
			{
				final StringBuilder sb = new StringBuilder();
				LOGGER_ENCHANT.info(sb.append("Success, Character:").append(player.getName()).append(" [").append(player.getObjectId()).append("] Account:").append(player.getAccountName()).append(" IP:").append(player.getIPAddress()).append(", +").append(enchantedSkill.getLevel()).append(" ").append(enchantedSkill.getSubLevel()).append(" - ").append(enchantedSkill.getName()).append(" (").append(enchantedSkill.getId()).append("), ").toString());
			}
			player.addSkill(enchantedSkill, true);
			final SystemMessage sm = new SystemMessage(SystemMessageId.SKILL_ENCHANT_WAS_SUCCESSFUL_S1_HAS_BEEN_ENCHANTED);
			sm.addSkillName(_skillId);
			player.sendPacket(sm);
			// player.setSkillEnchantExp(starHolder.getLvl(), 0);
			player.setSkillEnchantExp(starLevel, 0);
			player.sendPacket(ExEnchantSkillResult.STATIC_PACKET_TRUE);
			player.setSkillTryEnchant(starLevel);
		}
		else
		{
			player.sendPacket(ExEnchantSkillResult.STATIC_PACKET_FALSE);
			// player.setSkillEnchantExp(starHolder.getLvl(), 90000);
			int stepExp = 90_000;
			int curTry = player.getSkillTryEnchant(starLevel);
			int finalResult = stepExp * curTry;
			player.setSkillEnchantExp(starLevel, finalResult);
			player.increaseTrySkillEnchant(starLevel);
		}
		player.broadcastUserInfo();
		player.sendSkillList();
		
		skill = player.getKnownSkill(_skillId);
		player.reduceAdena("Try enchant skill", 1_000_000, null, true);
		player.sendPacket(new ExSkillEnchantInfo(skill, player));
		player.updateShortCuts(skill.getId(), skill.getLevel(), skill.getSubLevel());
	}
}
