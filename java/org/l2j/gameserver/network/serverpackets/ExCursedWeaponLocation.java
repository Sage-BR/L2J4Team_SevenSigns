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

import java.util.List;

import org.l2j.gameserver.model.Location;
import org.l2j.gameserver.network.ServerPackets;

/**
 * Format: (ch) d[ddddd]
 * @author -Wooden-
 */
public class ExCursedWeaponLocation extends ServerPacket
{
	private final List<CursedWeaponInfo> _cursedWeaponInfo;
	
	public ExCursedWeaponLocation(List<CursedWeaponInfo> cursedWeaponInfo)
	{
		_cursedWeaponInfo = cursedWeaponInfo;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_CURSED_WEAPON_LOCATION.writeId(this);
		if (!_cursedWeaponInfo.isEmpty())
		{
			writeInt(_cursedWeaponInfo.size());
			for (CursedWeaponInfo w : _cursedWeaponInfo)
			{
				writeInt(w.id);
				writeInt(w.activated);
				writeInt(w.pos.getX());
				writeInt(w.pos.getY());
				writeInt(w.pos.getZ());
			}
		}
		else
		{
			writeInt(0);
		}
	}
	
	public static class CursedWeaponInfo
	{
		public Location pos;
		public int id;
		public int activated; // 0 - not activated ? 1 - activated
		
		public CursedWeaponInfo(Location p, int cwId, int status)
		{
			pos = p;
			id = cwId;
			activated = status;
		}
	}
}
