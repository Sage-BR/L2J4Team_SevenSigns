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

import java.util.Collection;

import org.l2j.gameserver.data.sql.ClanTable;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.model.clan.ClanInfo;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.clientpackets.RequestAllyInfo;

/**
 * Sent in response to {@link RequestAllyInfo}, if applicable.<br>
 * @author afk5min
 */
public class AllianceInfo extends ServerPacket
{
	private final String _name;
	private final int _total;
	private final int _online;
	private final String _leaderC;
	private final String _leaderP;
	private final ClanInfo[] _allies;
	
	public AllianceInfo(int allianceId)
	{
		final Clan leader = ClanTable.getInstance().getClan(allianceId);
		_name = leader.getAllyName();
		_leaderC = leader.getName();
		_leaderP = leader.getLeaderName();
		final Collection<Clan> allies = ClanTable.getInstance().getClanAllies(allianceId);
		_allies = new ClanInfo[allies.size()];
		int idx = 0;
		int total = 0;
		int online = 0;
		for (Clan clan : allies)
		{
			final ClanInfo ci = new ClanInfo(clan);
			_allies[idx++] = ci;
			total += ci.getTotal();
			online += ci.getOnline();
		}
		_total = total;
		_online = online;
	}
	
	@Override
	public void write()
	{
		ServerPackets.ALLIANCE_INFO.writeId(this);
		writeString(_name);
		writeInt(_total);
		writeInt(_online);
		writeString(_leaderC);
		writeString(_leaderP);
		writeInt(_allies.length);
		for (ClanInfo aci : _allies)
		{
			writeString(aci.getClan().getName());
			writeInt(0);
			writeInt(aci.getClan().getLevel());
			writeString(aci.getClan().getLeaderName());
			writeInt(aci.getTotal());
			writeInt(aci.getOnline());
		}
	}
	
	public String getName()
	{
		return _name;
	}
	
	public int getTotal()
	{
		return _total;
	}
	
	public int getOnline()
	{
		return _online;
	}
	
	public String getLeaderC()
	{
		return _leaderC;
	}
	
	public String getLeaderP()
	{
		return _leaderP;
	}
	
	public ClanInfo[] getAllies()
	{
		return _allies;
	}
}