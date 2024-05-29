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
package org.l2j.gameserver.network.serverpackets.mentoring;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.l2j.gameserver.instancemanager.MentorManager;
import org.l2j.gameserver.model.Mentee;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author UnAfraid
 */
public class ExMentorList extends ServerPacket
{
	private final int _type;
	private final Collection<Mentee> _mentees;
	
	public ExMentorList(Player player)
	{
		if (player.isMentor())
		{
			_type = 1;
			_mentees = MentorManager.getInstance().getMentees(player.getObjectId());
		}
		else if (player.isMentee())
		{
			_type = 2;
			_mentees = Arrays.asList(MentorManager.getInstance().getMentor(player.getObjectId()));
		}
		// else if (player.isInCategory(CategoryType.SIXTH_CLASS_GROUP)) // Not a mentor, Not a mentee, so can be a mentor
		// {
		// _mentees = Collections.emptyList();
		// _type = 1;
		// }
		else
		{
			_mentees = Collections.emptyList();
			_type = 0;
		}
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_MENTOR_LIST.writeId(this);
		writeInt(_type);
		writeInt(0);
		writeInt(_mentees.size());
		for (Mentee mentee : _mentees)
		{
			writeInt(mentee.getObjectId());
			writeString(mentee.getName());
			writeInt(mentee.getClassId());
			writeInt(mentee.getLevel());
			writeInt(mentee.isOnlineInt());
		}
	}
}
