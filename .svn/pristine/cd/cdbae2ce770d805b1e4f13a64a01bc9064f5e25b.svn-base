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
package org.l2jmobius.gameserver.network.serverpackets;

import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.item.henna.Henna;
import org.l2jmobius.gameserver.model.stats.BaseStat;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;

/**
 * @author Zoey76
 */
public class HennaItemRemoveInfo extends ServerPacket
{
	private final Player _player;
	private final Henna _henna;
	
	public HennaItemRemoveInfo(Henna henna, Player player)
	{
		_henna = henna;
		_player = player;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.HENNA_UNEQUIP_INFO.writeId(this, buffer);
		buffer.writeInt(_henna.getDyeId()); // symbol Id
		buffer.writeInt(_henna.getDyeItemId()); // item id of dye
		buffer.writeLong(_henna.getCancelCount()); // total amount of dye require
		buffer.writeLong(_henna.getCancelFee()); // total amount of Adena require to remove symbol
		buffer.writeInt(_henna.isAllowedClass(_player)); // able to remove or not
		buffer.writeLong(_player.getAdena());
		buffer.writeInt(_player.getINT()); // current INT
		buffer.writeShort(_player.getINT() - _henna.getBaseStats(BaseStat.INT)); // equip INT
		buffer.writeInt(_player.getSTR()); // current STR
		buffer.writeShort(_player.getSTR() - _henna.getBaseStats(BaseStat.STR)); // equip STR
		buffer.writeInt(_player.getCON()); // current CON
		buffer.writeShort(_player.getCON() - _henna.getBaseStats(BaseStat.CON)); // equip CON
		buffer.writeInt(_player.getMEN()); // current MEN
		buffer.writeShort(_player.getMEN() - _henna.getBaseStats(BaseStat.MEN)); // equip MEN
		buffer.writeInt(_player.getDEX()); // current DEX
		buffer.writeShort(_player.getDEX() - _henna.getBaseStats(BaseStat.DEX)); // equip DEX
		buffer.writeInt(_player.getWIT()); // current WIT
		buffer.writeShort(_player.getWIT() - _henna.getBaseStats(BaseStat.WIT)); // equip WIT
		buffer.writeInt(0); // current LUC
		buffer.writeShort(0); // equip LUC
		buffer.writeInt(0); // current CHA
		buffer.writeShort(0); // equip CHA
		buffer.writeInt(_henna.getDuration() * 60000);
	}
}
