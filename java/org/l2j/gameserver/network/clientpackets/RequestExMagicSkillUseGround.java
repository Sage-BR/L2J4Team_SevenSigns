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

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.data.xml.SkillData;
import org.l2j.gameserver.model.Location;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.PacketLogger;
import org.l2j.gameserver.network.serverpackets.ActionFailed;
import org.l2j.gameserver.network.serverpackets.ValidateLocation;
import org.l2j.gameserver.util.Broadcast;
import org.l2j.gameserver.util.Util;

/**
 * Format:(ch) dddddc
 * @author -Wooden-
 */
public class RequestExMagicSkillUseGround implements ClientPacket
{
	private int _x;
	private int _y;
	private int _z;
	private int _skillId;
	private boolean _ctrlPressed;
	private boolean _shiftPressed;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_x = packet.readInt();
		_y = packet.readInt();
		_z = packet.readInt();
		_skillId = packet.readInt();
		_ctrlPressed = packet.readInt() != 0;
		_shiftPressed = packet.readByte() != 0;
	}
	
	@Override
	public void run(GameClient client)
	{
		// Get the current Player of the player
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		// Get the level of the used skill
		final int level = player.getSkillLevel(_skillId);
		if (level <= 0)
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		// Get the Skill template corresponding to the skillID received from the client
		final Skill skill = SkillData.getInstance().getSkill(_skillId, level);
		
		// Check the validity of the skill
		if (skill != null)
		{
			player.setCurrentSkillWorldPosition(new Location(_x, _y, _z));
			
			// normally magicskilluse packet turns char client side but for these skills, it doesn't (even with correct target)
			player.setHeading(Util.calculateHeadingFrom(player.getX(), player.getY(), _x, _y));
			Broadcast.toKnownPlayers(player, new ValidateLocation(player));
			player.useMagic(skill, null, _ctrlPressed, _shiftPressed);
		}
		else
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			PacketLogger.warning("No skill found with id " + _skillId + " and level " + level + " !!");
		}
	}
}
