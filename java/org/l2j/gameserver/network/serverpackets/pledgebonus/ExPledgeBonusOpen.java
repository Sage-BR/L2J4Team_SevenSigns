/*
 * This file is part of the L2J 4Team Project.
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
package org.l2j.gameserver.network.serverpackets.pledgebonus;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.data.xml.ClanRewardData;
import org.l2j.gameserver.enums.ClanRewardType;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.model.clan.ClanRewardBonus;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.PacketLogger;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author UnAfraid
 */
public class ExPledgeBonusOpen extends ServerPacket
{
	private final Player _player;
	
	public ExPledgeBonusOpen(Player player)
	{
		_player = player;
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		final Clan clan = _player.getClan();
		if (clan == null)
		{
			PacketLogger.warning("Player: " + _player + " attempting to write to a null clan!");
			return;
		}
		final ClanRewardBonus highestMembersOnlineBonus = ClanRewardData.getInstance().getHighestReward(ClanRewardType.MEMBERS_ONLINE);
		final ClanRewardBonus highestHuntingBonus = ClanRewardData.getInstance().getHighestReward(ClanRewardType.HUNTING_MONSTERS);
		final ClanRewardBonus membersOnlineBonus = ClanRewardType.MEMBERS_ONLINE.getAvailableBonus(clan);
		final ClanRewardBonus huntingBonus = ClanRewardType.HUNTING_MONSTERS.getAvailableBonus(clan);
		if (highestMembersOnlineBonus == null)
		{
			PacketLogger.warning("Couldn't find highest available clan members online bonus!!");
			return;
		}
		else if (highestHuntingBonus == null)
		{
			PacketLogger.warning("Couldn't find highest available clan hunting bonus!!");
			return;
		}
		else if (highestMembersOnlineBonus.getSkillReward() == null)
		{
			PacketLogger.warning("Couldn't find skill reward for highest available members online bonus!!");
			return;
		}
		else if (highestHuntingBonus.getSkillReward() == null)
		{
			PacketLogger.warning("Couldn't find skill reward for highest available hunting bonus!!");
			return;
		}
		
		// General OP Code
		ServerPackets.EX_PLEDGE_BONUS_OPEN.writeId(this, buffer);
		// Members online bonus
		buffer.writeInt(highestMembersOnlineBonus.getRequiredAmount());
		buffer.writeInt(clan.getMaxOnlineMembers());
		buffer.writeByte(2); // 140
		buffer.writeInt(membersOnlineBonus != null ? highestMembersOnlineBonus.getSkillReward().getSkillId() : 0);
		buffer.writeByte(membersOnlineBonus != null ? membersOnlineBonus.getLevel() : 0);
		buffer.writeByte(clan.canClaimBonusReward(_player, ClanRewardType.MEMBERS_ONLINE));
		// Hunting bonus
		buffer.writeInt(highestHuntingBonus.getRequiredAmount());
		buffer.writeInt(clan.getHuntingPoints());
		buffer.writeByte(2); // 140
		buffer.writeInt(huntingBonus != null ? highestHuntingBonus.getSkillReward().getSkillId() : 0);
		buffer.writeByte(huntingBonus != null ? huntingBonus.getLevel() : 0);
		buffer.writeByte(clan.canClaimBonusReward(_player, ClanRewardType.HUNTING_MONSTERS));
	}
}
