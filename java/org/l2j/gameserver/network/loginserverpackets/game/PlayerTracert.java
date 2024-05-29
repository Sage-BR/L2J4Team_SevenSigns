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
package org.l2j.gameserver.network.loginserverpackets.game;

import org.l2j.commons.network.WritablePacket;

/**
 * @author mrTJO
 */
public class PlayerTracert extends WritablePacket
{
	public PlayerTracert(String account, String pcIp, String hop1, String hop2, String hop3, String hop4)
	{
		writeByte(0x07);
		writeString(account);
		writeString(pcIp);
		writeString(hop1);
		writeString(hop2);
		writeString(hop3);
		writeString(hop4);
	}
}