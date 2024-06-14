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

import org.l2j.gameserver.data.xml.ElementalSpiritData;
import org.l2j.gameserver.enums.ElementalType;
import org.l2j.gameserver.model.ElementalSpirit;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.SystemMessage;
import org.l2j.gameserver.network.serverpackets.elementalspirits.ElementalSpiritSetTalent;

/**
 * @author JoeAlisson
 */
public class ExElementalInitTalent extends ClientPacket
{
	private byte _type;
	
	@Override
	protected void readImpl()
	{
		_type = readByte();
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
		if (spirit == null)
		{
			player.sendPacket(SystemMessageId.NO_SPIRITS_ARE_AVAILABLE);
			return;
		}
		
		if (player.isInBattle())
		{
			player.sendPacket(new SystemMessage(SystemMessageId.UNABLE_TO_RESET_THE_SPIRIT_ATTRIBUTES_WHILE_IN_BATTLE));
			player.sendPacket(new ElementalSpiritSetTalent(player, _type, false));
			return;
		}
		
		if (player.reduceAdena("Talent", ElementalSpiritData.TALENT_INIT_FEE, player, true))
		{
			spirit.resetCharacteristics();
			player.sendPacket(new SystemMessage(SystemMessageId.RESET_THE_SELECTED_SPIRIT_S_CHARACTERISTICS_SUCCESSFULLY));
			player.sendPacket(new ElementalSpiritSetTalent(player, _type, true));
		}
		else
		{
			player.sendPacket(new ElementalSpiritSetTalent(player, _type, false));
		}
	}
}
