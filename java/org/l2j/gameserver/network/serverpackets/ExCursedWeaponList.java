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

import java.util.Set;

import org.l2j.gameserver.instancemanager.CursedWeaponsManager;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author -Wooden-
 */
public class ExCursedWeaponList extends ServerPacket
{
	@Override
	public void write()
	{
		ServerPackets.EX_CURSED_WEAPON_LIST.writeId(this);
		final Set<Integer> ids = CursedWeaponsManager.getInstance().getCursedWeaponsIds();
		writeInt(ids.size());
		ids.forEach(this::writeInt);
	}
}
