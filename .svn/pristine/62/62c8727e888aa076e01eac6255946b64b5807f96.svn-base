/*
 * This file is part of the L2J Mobius project.
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
package org.l2jmobius.gameserver.network.serverpackets.settings;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.variables.PlayerVariables;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Mobius
 */
public class ExUISetting extends ServerPacket
{
	public static final String SPLIT_VAR = "	";
	
	private final byte[] _uiKeyMapping;
	
	public ExUISetting(Player player)
	{
		if (player.getVariables().hasVariable(PlayerVariables.UI_KEY_MAPPING))
		{
			_uiKeyMapping = player.getVariables().getByteArray(PlayerVariables.UI_KEY_MAPPING, SPLIT_VAR);
		}
		else
		{
			_uiKeyMapping = null;
		}
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_UI_SETTING.writeId(this, buffer);
		if (_uiKeyMapping != null)
		{
			buffer.writeInt(_uiKeyMapping.length);
			buffer.writeBytes(_uiKeyMapping);
		}
		else
		{
			buffer.writeInt(0);
		}
	}
}
