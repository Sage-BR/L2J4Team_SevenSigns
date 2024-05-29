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

import java.util.ArrayList;
import java.util.List;

import org.l2j.gameserver.model.actor.templates.PlayerTemplate;
import org.l2j.gameserver.network.ServerPackets;

public class NewCharacterSuccess extends ServerPacket
{
	private final List<PlayerTemplate> _chars = new ArrayList<>();
	
	public void addChar(PlayerTemplate template)
	{
		_chars.add(template);
	}
	
	@Override
	public void write()
	{
		ServerPackets.NEW_CHARACTER_SUCCESS.writeId(this);
		writeInt(_chars.size());
		for (PlayerTemplate chr : _chars)
		{
			if (chr == null)
			{
				continue;
			}
			// TODO: Unhardcode these
			writeInt(chr.getRace().ordinal());
			writeInt(chr.getClassId().getId());
			writeInt(99);
			writeInt(chr.getBaseSTR());
			writeInt(1);
			writeInt(99);
			writeInt(chr.getBaseDEX());
			writeInt(1);
			writeInt(99);
			writeInt(chr.getBaseCON());
			writeInt(1);
			writeInt(99);
			writeInt(chr.getBaseINT());
			writeInt(1);
			writeInt(99);
			writeInt(chr.getBaseWIT());
			writeInt(1);
			writeInt(99);
			writeInt(chr.getBaseMEN());
			writeInt(1);
		}
	}
}
