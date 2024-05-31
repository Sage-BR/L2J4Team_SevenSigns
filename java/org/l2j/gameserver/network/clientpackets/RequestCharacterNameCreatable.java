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

import org.l2j.Config;
import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.data.sql.CharInfoTable;
import org.l2j.gameserver.data.xml.FakePlayerData;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.serverpackets.ExIsCharNameCreatable;
import org.l2j.gameserver.util.Util;

/**
 * @author UnAfraid
 */
public class RequestCharacterNameCreatable implements ClientPacket
{
	private String _name;
	
	public static final int CHARACTER_CREATE_FAILED = 1;
	public static final int NAME_ALREADY_EXISTS = 2;
	public static final int INVALID_LENGTH = 3;
	public static final int INVALID_NAME = 4;
	public static final int CANNOT_CREATE_SERVER = 5;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_name = packet.readString();
	}
	
	@Override
	public void run(GameClient client)
	{
		final int charId = CharInfoTable.getInstance().getIdByName(_name);
		int result;
		if (!Util.isAlphaNumeric(_name) || !isValidName(_name))
		{
			result = INVALID_NAME;
		}
		else if (charId > 0)
		{
			result = NAME_ALREADY_EXISTS;
		}
		else if (FakePlayerData.getInstance().getProperName(_name) != null)
		{
			result = NAME_ALREADY_EXISTS;
		}
		else if (_name.length() > 16)
		{
			result = INVALID_LENGTH;
		}
		else
		{
			result = -1;
		}
		
		client.sendPacket(new ExIsCharNameCreatable(result));
	}
	
	private boolean isValidName(String text)
	{
		return Config.CHARNAME_TEMPLATE_PATTERN.matcher(text).matches();
	}
}