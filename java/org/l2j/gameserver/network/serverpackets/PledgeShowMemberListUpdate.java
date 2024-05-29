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

import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.model.clan.ClanMember;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author -Wooden-
 */
public class PledgeShowMemberListUpdate extends ServerPacket
{
	private final int _pledgeType;
	private boolean _hasSponsor;
	private final String _name;
	private final int _level;
	private final int _classId;
	private final int _objectId;
	private final int _onlineStatus;
	private final int _race;
	private final boolean _sex;
	
	public PledgeShowMemberListUpdate(Player player)
	{
		this(player.getClan().getClanMember(player.getObjectId()));
	}
	
	public PledgeShowMemberListUpdate(ClanMember member)
	{
		_name = member.getName();
		_level = member.getLevel();
		_classId = member.getClassId();
		_objectId = member.getObjectId();
		_pledgeType = member.getPledgeType();
		_race = member.getRaceOrdinal();
		_sex = member.getSex();
		_onlineStatus = member.getOnlineStatus();
		if (_pledgeType == Clan.SUBUNIT_ACADEMY)
		{
			_hasSponsor = member.getSponsor() != 0;
		}
		else
		{
			_hasSponsor = false;
		}
	}
	
	@Override
	public void write()
	{
		ServerPackets.PLEDGE_SHOW_MEMBER_LIST_UPDATE.writeId(this);
		writeString(_name);
		writeInt(_level);
		writeInt(_classId);
		writeInt(_sex);
		writeInt(_race);
		if (_onlineStatus > 0)
		{
			writeInt(_objectId);
			writeInt(_pledgeType);
		}
		else
		{
			// when going offline send as 0
			writeInt(0);
			writeInt(0);
		}
		writeInt(_hasSponsor);
		writeByte(_onlineStatus);
	}
}
