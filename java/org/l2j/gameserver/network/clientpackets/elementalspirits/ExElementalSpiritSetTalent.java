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
package org.l2j.gameserver.network.clientpackets.elementalspirits;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.enums.ElementalType;
import org.l2j.gameserver.enums.UserInfoType;
import org.l2j.gameserver.model.ElementalSpirit;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.SystemMessage;
import org.l2j.gameserver.network.serverpackets.UserInfo;
import org.l2j.gameserver.network.serverpackets.elementalspirits.ElementalSpiritSetTalent;

/**
 * @author JoeAlisson
 */
public class ExElementalSpiritSetTalent implements ClientPacket
{
	private byte _type;
	private byte _attackPoints;
	private byte _defensePoints;
	private byte _critRate;
	private byte _critDamage;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_type = (byte) packet.readByte();
		packet.readByte(); // Characteristics for now always 4
		
		packet.readByte(); // attack id
		_attackPoints = (byte) packet.readByte();
		packet.readByte(); // defense id
		_defensePoints = (byte) packet.readByte();
		packet.readByte(); // crit rate id
		_critRate = (byte) packet.readByte();
		packet.readByte(); // crit damage id
		_critDamage = (byte) packet.readByte();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
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
			client.sendPacket(userInfo);
			client.sendPacket(new SystemMessage(SystemMessageId.CHARACTERISTICS_WERE_APPLIED_SUCCESSFULLY));
		}
		client.sendPacket(new ElementalSpiritSetTalent(player, _type, result));
	}
}
