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
package org.l2j.gameserver.network.clientpackets.newskillenchant;

import org.l2j.gameserver.data.xml.SkillEnchantData;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.SkillEnchantHolder;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.network.PacketLogger;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.newskillenchant.ExSkillEnchantInfo;

/**
 * @author Serenitty
 */
public class RequestExSkillEnchantInfo extends ClientPacket
{
	private int _skillId;
	private int _skillLevel;
	private int _skillSubLevel;
	
	@Override
	protected void readImpl()
	{
		_skillId = readInt();
		_skillLevel = readInt();
		_skillSubLevel = readInt();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		final Skill skill = player.getKnownSkill(_skillId);
		if (skill == null)
		{
			PacketLogger.warning(player.getName() + " trying enchant skill, what missed on server" + _skillId + " level-" + _skillLevel + " subLevel-" + _skillSubLevel);
			return;
		}
		
		final SkillEnchantHolder skillEnchantHolder = SkillEnchantData.getInstance().getSkillEnchant(skill.getId());
		if (skillEnchantHolder == null)
		{
			PacketLogger.warning("Skill does not exist at SkillEnchantData id-" + _skillId);
			return;
		}
		
		player.sendPacket(new ExSkillEnchantInfo(skill, player));
	}
}
