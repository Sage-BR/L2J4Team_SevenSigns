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

import java.util.Collection;

import org.l2j.gameserver.model.Shortcut;
import org.l2j.gameserver.model.VariationInstance;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.network.ServerPackets;

public class ShortCutInit extends ServerPacket
{
	private final Player _player;
	private final Collection<Shortcut> _shortCuts;
	
	public ShortCutInit(Player player)
	{
		_player = player;
		_shortCuts = player.getAllShortCuts();
		player.restoreAutoShortcutVisual();
	}
	
	@Override
	public void write()
	{
		ServerPackets.SHORT_CUT_INIT.writeId(this);
		writeInt(_shortCuts.size());
		for (Shortcut sc : _shortCuts)
		{
			writeInt(sc.getType().ordinal());
			writeInt(sc.getSlot() + (sc.getPage() * 12));
			writeByte(0); // 228
			switch (sc.getType())
			{
				case ITEM:
				{
					writeInt(sc.getId());
					writeInt(1); // Enabled or not
					writeInt(sc.getSharedReuseGroup());
					writeInt(0);
					writeInt(0);
					
					final Item item = _player.getInventory().getItemByObjectId(sc.getId());
					if (item != null)
					{
						final VariationInstance augment = item.getAugmentation();
						writeInt(augment != null ? augment.getOption1Id() : 0); // item augment id
						writeInt(augment != null ? augment.getOption2Id() : 0); // item augment id
						writeInt(item.getVisualId()); // visual id
					}
					else
					{
						writeInt(0);
						writeInt(0);
						writeInt(0);
					}
					break;
				}
				case SKILL:
				{
					writeInt(sc.getId());
					writeShort(sc.getLevel());
					writeShort(sc.getSubLevel());
					writeInt(sc.getSharedReuseGroup());
					writeByte(0); // C5
					writeInt(1); // C6
					break;
				}
				case ACTION:
				case MACRO:
				case RECIPE:
				case BOOKMARK:
				{
					writeInt(sc.getId());
					writeInt(1); // C6
				}
			}
		}
	}
}
