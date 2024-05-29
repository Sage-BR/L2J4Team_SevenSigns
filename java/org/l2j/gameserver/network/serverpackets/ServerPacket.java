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

import org.l2j.commons.network.WritablePacket;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.itemcontainer.Inventory;

/**
 * @author Mobius
 */
public abstract class ServerPacket extends WritablePacket
{
	protected static final int[] PAPERDOLL_ORDER =
	{
		Inventory.PAPERDOLL_UNDER,
		Inventory.PAPERDOLL_REAR,
		Inventory.PAPERDOLL_LEAR,
		Inventory.PAPERDOLL_NECK,
		Inventory.PAPERDOLL_RFINGER,
		Inventory.PAPERDOLL_LFINGER,
		Inventory.PAPERDOLL_HEAD,
		Inventory.PAPERDOLL_RHAND,
		Inventory.PAPERDOLL_LHAND,
		Inventory.PAPERDOLL_GLOVES,
		Inventory.PAPERDOLL_CHEST,
		Inventory.PAPERDOLL_LEGS,
		Inventory.PAPERDOLL_FEET,
		Inventory.PAPERDOLL_CLOAK,
		Inventory.PAPERDOLL_RHAND,
		Inventory.PAPERDOLL_HAIR,
		Inventory.PAPERDOLL_HAIR2,
		Inventory.PAPERDOLL_RBRACELET,
		Inventory.PAPERDOLL_LBRACELET,
		Inventory.PAPERDOLL_AGATHION1,
		Inventory.PAPERDOLL_AGATHION2,
		Inventory.PAPERDOLL_AGATHION3,
		Inventory.PAPERDOLL_AGATHION4,
		Inventory.PAPERDOLL_AGATHION5,
		Inventory.PAPERDOLL_DECO1,
		Inventory.PAPERDOLL_DECO2,
		Inventory.PAPERDOLL_DECO3,
		Inventory.PAPERDOLL_DECO4,
		Inventory.PAPERDOLL_DECO5,
		Inventory.PAPERDOLL_DECO6,
		Inventory.PAPERDOLL_BELT,
		Inventory.PAPERDOLL_BROOCH,
		Inventory.PAPERDOLL_BROOCH_JEWEL1,
		Inventory.PAPERDOLL_BROOCH_JEWEL2,
		Inventory.PAPERDOLL_BROOCH_JEWEL3,
		Inventory.PAPERDOLL_BROOCH_JEWEL4,
		Inventory.PAPERDOLL_BROOCH_JEWEL5,
		Inventory.PAPERDOLL_BROOCH_JEWEL6,
		Inventory.PAPERDOLL_ARTIFACT_BOOK,
		Inventory.PAPERDOLL_ARTIFACT1,
		Inventory.PAPERDOLL_ARTIFACT2,
		Inventory.PAPERDOLL_ARTIFACT3,
		Inventory.PAPERDOLL_ARTIFACT4,
		Inventory.PAPERDOLL_ARTIFACT5,
		Inventory.PAPERDOLL_ARTIFACT6,
		Inventory.PAPERDOLL_ARTIFACT7,
		Inventory.PAPERDOLL_ARTIFACT8,
		Inventory.PAPERDOLL_ARTIFACT9,
		Inventory.PAPERDOLL_ARTIFACT10,
		Inventory.PAPERDOLL_ARTIFACT11,
		Inventory.PAPERDOLL_ARTIFACT12,
		Inventory.PAPERDOLL_ARTIFACT13,
		Inventory.PAPERDOLL_ARTIFACT14,
		Inventory.PAPERDOLL_ARTIFACT15,
		Inventory.PAPERDOLL_ARTIFACT16,
		Inventory.PAPERDOLL_ARTIFACT17,
		Inventory.PAPERDOLL_ARTIFACT18,
		Inventory.PAPERDOLL_ARTIFACT19,
		Inventory.PAPERDOLL_ARTIFACT20,
		Inventory.PAPERDOLL_ARTIFACT21,
	};
	protected static final int[] PAPERDOLL_ORDER_AUGMENT =
	{
		Inventory.PAPERDOLL_RHAND,
		Inventory.PAPERDOLL_LHAND,
		Inventory.PAPERDOLL_RHAND
	};
	protected static final int[] PAPERDOLL_ORDER_VISUAL_ID =
	{
		Inventory.PAPERDOLL_RHAND,
		Inventory.PAPERDOLL_LHAND,
		Inventory.PAPERDOLL_RHAND,
		Inventory.PAPERDOLL_GLOVES,
		Inventory.PAPERDOLL_CHEST,
		Inventory.PAPERDOLL_LEGS,
		Inventory.PAPERDOLL_FEET,
		Inventory.PAPERDOLL_HAIR,
		Inventory.PAPERDOLL_HAIR2
	};
	
	protected int[] getPaperdollOrder()
	{
		return PAPERDOLL_ORDER;
	}
	
	protected int[] getPaperdollOrderAugument()
	{
		return PAPERDOLL_ORDER_AUGMENT;
	}
	
	protected int[] getPaperdollOrderVisualId()
	{
		return PAPERDOLL_ORDER_VISUAL_ID;
	}
	
	/**
	 * Construct a ServerPacket with an initial data size of 32 bytes.
	 */
	protected ServerPacket()
	{
		super(32);
	}
	
	/**
	 * Construct a ServerPacket with a given initial data size.
	 * @param initialSize
	 */
	protected ServerPacket(int initialSize)
	{
		super(initialSize);
	}
	
	private Player _player;
	
	/**
	 * @return the Player
	 */
	public Player getPlayer()
	{
		return _player;
	}
	
	/**
	 * @param player the Player to set.
	 */
	public void setPlayer(Player player)
	{
		_player = player;
	}
	
	protected void writeOptionalInt(int value)
	{
		if (value >= Short.MAX_VALUE)
		{
			writeShort(Short.MAX_VALUE);
			writeInt(value);
		}
		else
		{
			writeShort(value);
		}
	}
}
