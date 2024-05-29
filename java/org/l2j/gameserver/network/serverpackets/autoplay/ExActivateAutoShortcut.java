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
package org.l2j.gameserver.network.serverpackets.autoplay;

import org.l2j.gameserver.model.ShortCuts;
import org.l2j.gameserver.model.Shortcut;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Mobius
 */
public class ExActivateAutoShortcut extends ServerPacket
{
	private final int _position;
	private final boolean _active;
	
	public ExActivateAutoShortcut(Shortcut shortcut, boolean active)
	{
		_position = shortcut.getSlot() + (shortcut.getPage() * ShortCuts.MAX_SHORTCUTS_PER_BAR);
		_active = active;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_ACTIVATE_AUTO_SHORTCUT.writeId(this);
		writeShort(_position);
		writeByte(_active);
	}
}
