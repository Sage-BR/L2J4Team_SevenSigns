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

import org.l2j.gameserver.enums.InventorySlot;
import org.l2j.gameserver.model.VariationInstance;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.itemcontainer.PlayerInventory;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author Sdw
 */
public class ExUserInfoEquipSlot extends AbstractMaskPacket<InventorySlot>
{
	private final Player _player;
	private final byte[] _masks = new byte[]
	{
		(byte) 0x00,
		(byte) 0x00,
		(byte) 0x00,
		(byte) 0x00,
		(byte) 0x00,
		(byte) 0x00, // 152
		(byte) 0x00, // 152
		(byte) 0x00, // 152
	};
	
	public ExUserInfoEquipSlot(Player player)
	{
		this(player, true);
	}
	
	public ExUserInfoEquipSlot(Player player, boolean addAll)
	{
		_player = player;
		if (addAll)
		{
			addComponentType(InventorySlot.values());
		}
	}
	
	@Override
	protected byte[] getMasks()
	{
		return _masks;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_USER_INFO_EQUIP_SLOT.writeId(this);
		writeInt(_player.getObjectId());
		writeShort(InventorySlot.values().length); // 152
		writeBytes(_masks);
		final PlayerInventory inventory = _player.getInventory();
		for (InventorySlot slot : InventorySlot.values())
		{
			if (containsMask(slot))
			{
				final VariationInstance augment = inventory.getPaperdollAugmentation(slot.getSlot());
				writeShort(22); // 10 + 4 * 3
				writeInt(inventory.getPaperdollObjectId(slot.getSlot()));
				writeInt(inventory.getPaperdollItemId(slot.getSlot()));
				writeInt(augment != null ? augment.getOption1Id() : 0);
				writeInt(augment != null ? augment.getOption2Id() : 0);
				writeInt(inventory.getPaperdollItemVisualId(slot.getSlot()));
			}
		}
	}
}