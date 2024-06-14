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
package org.l2jmobius.gameserver.network.serverpackets;

import org.l2jmobius.commons.network.WritableBuffer;

import org.l2jmobius.gameserver.model.Location;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;

/**
 * @author Mobius
 */
public class ExMagicSkillUseGround extends ServerPacket
{
	private final int _playerObjectId;
	private final int _skillId;
	private final Location _location;
	
	public ExMagicSkillUseGround(int playerObjectId, int skillId, Location location)
	{
		_playerObjectId = playerObjectId;
		_skillId = skillId;
		_location = location;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_MAGIC_SKILL_USE_GROUND.writeId(this, buffer);
		buffer.writeInt(_playerObjectId);
		buffer.writeInt(_skillId);
		buffer.writeInt(_location.getX());
		buffer.writeInt(_location.getY());
		buffer.writeInt(_location.getZ());
	}
}
