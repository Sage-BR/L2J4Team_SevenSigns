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
package org.l2j.gameserver.network.clientpackets.elementalspirits;

import org.l2j.gameserver.enums.ElementalType;
import org.l2j.gameserver.enums.UserInfoType;
import org.l2j.gameserver.model.ElementalSpirit;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.SystemMessage;
import org.l2j.gameserver.network.serverpackets.UserInfo;
import org.l2j.gameserver.network.serverpackets.elementalspirits.ElementalSpiritSetTalent;

/**
 * @author JoeAlisson
 */
public class ExElementalSpiritSetTalent extends ClientPacket
{
	private byte _type;
	private byte _attackPoints;
	private byte _defensePoints;
	private byte _critRate;
	private byte _critDamage;
	
	@Override
	protected void readImpl()
	{
		_type = readByte();
		readByte(); // Characteristics for now always 4
		
		readByte(); // attack id
		_attackPoints = readByte();
		readByte(); // defense id
		_defensePoints = readByte();
		readByte(); // crit rate id
		_critRate = readByte();
		readByte(); // crit damage id
		_critDamage = readByte();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		final ElementalSpirit spirit = player.getElementalSpirit(ElementalType.of(_type));
		boolean result = false;
		if (spirit != null)
		{
			if ((_attackPoints > 0) && (spirit.getAvailableCharacteristicsPoints() >= _attackPoints))
			{
				spirit.addAttackPoints(_attackPoints);
				result = true;
			}
			
			if ((_defensePoints > 0) && (spirit.getAvailableCharacteristicsPoints() >= _defensePoints))
			{
				spirit.addDefensePoints(_defensePoints);
				result = true;
			}
			
			if ((_critRate > 0) && (spirit.getAvailableCharacteristicsPoints() >= _critRate))
			{
				spirit.addCritRatePoints(_critRate);
				result = true;
			}
			
			if ((_critDamage > 0) && (spirit.getAvailableCharacteristicsPoints() >= _critDamage))
			{
				spirit.addCritDamage(_critDamage);
				result = true;
			}
		}
		
		if (result)
		{
			final UserInfo userInfo = new UserInfo(player);
			userInfo.addComponentType(UserInfoType.ATT_SPIRITS);
			player.sendPacket(userInfo);
			player.sendPacket(new SystemMessage(SystemMessageId.CHARACTERISTICS_WERE_APPLIED_SUCCESSFULLY));
		}
		player.sendPacket(new ElementalSpiritSetTalent(player, _type, result));
	}
}
