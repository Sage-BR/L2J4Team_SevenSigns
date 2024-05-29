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

import org.l2j.gameserver.enums.SoulType;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.zone.ZoneId;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author Luca Baldi
 */
public class EtcStatusUpdate extends ServerPacket
{
	private final Player _player;
	private int _mask;
	
	public EtcStatusUpdate(Player player)
	{
		_player = player;
		_mask = _player.getMessageRefusal() || _player.isChatBanned() || _player.isSilenceMode() ? 1 : 0;
		_mask |= _player.isInsideZone(ZoneId.DANGER_AREA) ? 2 : 0;
		_mask |= _player.hasCharmOfCourage() ? 4 : 0;
	}
	
	@Override
	public void write()
	{
		ServerPackets.ETC_STATUS_UPDATE.writeId(this);
		writeByte(_player.getCharges()); // 1-7 increase force, level
		writeInt(_player.getWeightPenalty()); // 1-4 weight penalty, level (1=50%, 2=66.6%, 3=80%, 4=100%)
		writeByte(0); // Weapon Grade Penalty [1-4]
		writeByte(0); // Armor Grade Penalty [1-4]
		writeByte(0); // Death Penalty [1-15, 0 = disabled)], not used anymore in Ertheia
		writeByte(0); // Old count for charged souls.
		writeByte(_mask);
		writeByte(_player.getChargedSouls(SoulType.SHADOW)); // Shadow souls
		writeByte(_player.getChargedSouls(SoulType.LIGHT)); // Light souls
	}
}
