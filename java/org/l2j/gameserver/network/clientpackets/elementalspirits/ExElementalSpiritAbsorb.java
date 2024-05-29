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
import org.l2j.gameserver.enums.PrivateStoreType;
import org.l2j.gameserver.enums.UserInfoType;
import org.l2j.gameserver.model.ElementalSpirit;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.ElementalSpiritAbsorbItemHolder;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.UserInfo;
import org.l2j.gameserver.network.serverpackets.elementalspirits.ElementalSpiritAbsorb;

/**
 * @author JoeAlisson
 */
public class ExElementalSpiritAbsorb implements ClientPacket
{
	private byte _type;
	private int _itemId;
	private int _amount;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_type = (byte) packet.readByte();
		packet.readInt(); // items for now is always 1
		_itemId = packet.readInt();
		_amount = packet.readInt();
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
		if (spirit == null)
		{
			client.sendPacket(SystemMessageId.NO_SPIRITS_ARE_AVAILABLE);
			return;
		}
		
		final ElementalSpiritAbsorbItemHolder absorbItem = spirit.getAbsorbItem(_itemId);
		if (absorbItem == null)
		{
			player.sendPacket(new ElementalSpiritAbsorb(player, _type, false));
			return;
		}
		
		final boolean canAbsorb = checkConditions(player, spirit);
		if (canAbsorb)
		{
			client.sendPacket(SystemMessageId.SUCCESSFUL_ABSORPTION);
			spirit.addExperience(absorbItem.getExperience() * _amount);
			final UserInfo userInfo = new UserInfo(player);
			userInfo.addComponentType(UserInfoType.ATT_SPIRITS);
			client.sendPacket(userInfo);
		}
		client.sendPacket(new ElementalSpiritAbsorb(player, _type, canAbsorb));
	}
	
	private boolean checkConditions(Player player, ElementalSpirit spirit)
	{
		if (player.getPrivateStoreType() != PrivateStoreType.NONE)
		{
			player.sendPacket(SystemMessageId.CANNOT_EVOLVE_ABSORB_EXTRACT_WHILE_USING_THE_PRIVATE_STORE_WORKSHOP);
			return false;
		}
		if (player.isInBattle())
		{
			player.sendPacket(SystemMessageId.UNABLE_TO_ABSORB_DURING_BATTLE);
			return false;
		}
		if ((spirit.getLevel() == spirit.getMaxLevel()) && (spirit.getExperience() == spirit.getExperienceToNextLevel()))
		{
			player.sendPacket(SystemMessageId.YOU_HAVE_REACHED_THE_MAXIMUM_LEVEL_AND_CANNOT_ABSORB_ANY_FURTHER);
			return false;
		}
		if ((_amount < 1) || !player.destroyItemByItemId("Absorb", _itemId, _amount, player, true))
		{
			player.sendPacket(SystemMessageId.NOT_ENOUGH_INGREDIENTS_TO_ABSORB);
			return false;
		}
		return true;
	}
}
