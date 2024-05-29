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
package org.l2j.gameserver.network.clientpackets;

import java.util.ArrayList;
import java.util.List;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.enums.MacroType;
import org.l2j.gameserver.model.Macro;
import org.l2j.gameserver.model.MacroCmd;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.SystemMessageId;

public class RequestMakeMacro implements ClientPacket
{
	private Macro _macro;
	private int _commandsLength = 0;
	
	private static final int MAX_MACRO_LENGTH = 20;
	
	@Override
	public void read(ReadablePacket packet)
	{
		final int id = packet.readInt();
		final String name = packet.readString();
		final String desc = packet.readString();
		final String acronym = packet.readString();
		final int icon = packet.readInt();
		int count = packet.readByte();
		if (count > MAX_MACRO_LENGTH)
		{
			count = MAX_MACRO_LENGTH;
		}
		
		final List<MacroCmd> commands = new ArrayList<>(count);
		for (int i = 0; i < count; i++)
		{
			final int entry = packet.readByte();
			final int type = packet.readByte(); // 1 = skill, 3 = action, 4 = shortcut
			final int d1 = packet.readInt(); // skill or page number for shortcuts
			final int d2 = packet.readByte();
			final String command = packet.readString();
			_commandsLength += command.length();
			commands.add(new MacroCmd(entry, MacroType.values()[(type < 1) || (type > 6) ? 0 : type], d1, d2, command));
		}
		_macro = new Macro(id, icon, name, desc, acronym, commands);
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		if (_commandsLength > 255)
		{
			// Invalid macro. Refer to the Help file for instructions.
			player.sendPacket(SystemMessageId.INVALID_MACRO_REFER_TO_THE_HELP_FILE_FOR_INSTRUCTIONS);
			return;
		}
		if (player.getMacros().getAllMacroses().size() > 48)
		{
			// You may create up to 48 macros.
			player.sendPacket(SystemMessageId.YOU_MAY_CREATE_UP_TO_48_MACROS);
			return;
		}
		if (_macro.getName().isEmpty())
		{
			// Enter the name of the macro.
			player.sendPacket(SystemMessageId.ENTER_THE_NAME_OF_THE_MACRO);
			return;
		}
		if (_macro.getDescr().length() > 32)
		{
			// Macro descriptions may contain up to 32 characters.
			player.sendPacket(SystemMessageId.MACRO_DESCRIPTIONS_MAY_CONTAIN_UP_TO_32_CHARACTERS);
			return;
		}
		player.registerMacro(_macro);
	}
}
