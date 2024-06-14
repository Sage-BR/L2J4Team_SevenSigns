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
package org.l2jmobius.gameserver.network.serverpackets.pledgedonation;

import java.util.Collection;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.model.clan.ClanMember;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Berezkin Nikolay
 */
public class ExPledgeContributionList extends ServerPacket
{
	private final Collection<ClanMember> _contributors;
	
	public ExPledgeContributionList(Collection<ClanMember> contributors)
	{
		_contributors = contributors;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_PLEDGE_CONTRIBUTION_LIST.writeId(this, buffer);
		buffer.writeInt(_contributors.size());
		for (ClanMember contributor : _contributors)
		{
			buffer.writeSizedString(contributor.getName());
			buffer.writeInt(contributor.getClan().getClanContributionWeekly(contributor.getObjectId()));
			buffer.writeInt(contributor.getClan().getClanContribution(contributor.getObjectId()));
		}
	}
}
