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

import org.l2j.Config;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author Sdw
 */
public class ExVitalityEffectInfo extends ServerPacket
{
	private final int _vitalityBonus;
	private final int _vitalityItemsRemaining;
	private final int _points;
	
	public ExVitalityEffectInfo(Player player)
	{
		_points = player.getVitalityPoints();
		_vitalityBonus = (int) player.getStat().getVitalityExpBonus() * 100;
		_vitalityItemsRemaining = Config.VITALITY_MAX_ITEMS_ALLOWED - player.getVitalityItemsUsed();
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_VITALITY_EFFECT_INFO.writeId(this);
		writeInt(_points);
		writeInt(_vitalityBonus); // Vitality Bonus
		writeShort(0); // Vitality additional bonus in %
		writeShort(_vitalityItemsRemaining); // How much vitality items remaining for use
		writeShort(Config.VITALITY_MAX_ITEMS_ALLOWED); // Max number of items for use
	}
}