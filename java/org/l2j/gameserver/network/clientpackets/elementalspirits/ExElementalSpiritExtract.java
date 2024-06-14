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
import org.l2j.gameserver.enums.UserInfoType;
import org.l2j.gameserver.model.ElementalSpirit;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.SystemMessage;
import org.l2j.gameserver.network.serverpackets.UserInfo;
import org.l2j.gameserver.network.serverpackets.elementalspirits.ElementalSpiritExtract;

/**
 * @author JoeAlisson
 */
public class ExElementalSpiritExtract extends ClientPacket
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
		
		final boolean canExtract = checkConditions(player, spirit);
		if (canExtract)
		{
			final int amount = spirit.getExtractAmount();
			player.sendPacket(new SystemMessage(SystemMessageId.EXTRACTED_S1_S2_SUCCESSFULLY).addItemName(spirit.getExtractItem()).addInt(amount));
			spirit.reduceLevel();
			player.addItem("ElementalSpiritExtract", spirit.getExtractItem(), amount, player, true);
			
			final UserInfo userInfo = new UserInfo(player);
			userInfo.addComponentType(UserInfoType.ATT_SPIRITS);
			player.sendPacket(userInfo);
		}
		
		player.sendPacket(new ElementalSpiritExtract(player, _type, canExtract));
	}
	
	private boolean checkConditions(Player player, ElementalSpirit spirit)
	{
		if ((spirit.getLevel() < 2) || (spirit.getExtractAmount() < 1))
		{
			player.sendPacket(SystemMessageId.NOT_ENOUGH_ATTRIBUTE_XP_FOR_EXTRACTION);
			return false;
		}
		if (!player.getInventory().validateCapacity(1))
		{
			player.sendPacket(SystemMessageId.UNABLE_TO_EXTRACT_BECAUSE_INVENTORY_IS_FULL);
			return false;
		}
		if (player.isInStoreMode())
		{
			player.sendPacket(SystemMessageId.CANNOT_EVOLVE_ABSORB_EXTRACT_WHILE_USING_THE_PRIVATE_STORE_WORKSHOP);
			return false;
		}
		if (player.isInBattle())
		{
			player.sendPacket(SystemMessageId.UNABLE_TO_EVOLVE_DURING_BATTLE);
			return false;
		}
		if (!player.reduceAdena("ElementalSpiritExtract", ElementalSpiritData.EXTRACT_FEES[spirit.getStage() - 1], player, true))
		{
			player.sendPacket(SystemMessageId.NOT_ENOUGH_MATERIALS_FOR_EXTRACTION);
			return false;
		}
		return true;
	}
}
