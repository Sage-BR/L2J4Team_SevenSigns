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
package instances.ValakasTemple.Teleporter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.l2j.gameserver.instancemanager.InstanceManager;
import org.l2j.gameserver.model.CommandChannel;
import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.serverpackets.SystemMessage;

import instances.AbstractInstance;
import instances.ValakasTemple.ValakasTemple;

/**
 * @author Index
 */
public class ValakasTempleTeleport extends AbstractInstance
{
	public static final int PARME_NPC_ID = 34258;
	private static final Map<Integer, Set<Player>> PLAYER_TO_LOGIN = new HashMap<>();
	
	private ValakasTempleTeleport()
	{
		super(ValakasTemple.VALAKAS_TEMPLE_INSTANCE_ID);
		addTalkId(PARME_NPC_ID);
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		switch (event)
		{
			case "VALAKAS_TEMPLE_ENTER_HTML":
			{
				return PARME_NPC_ID + "-01.htm";
			}
			case "VALAKAS_TEMPLE_SHOW_INFO":
			{
				return PARME_NPC_ID + "-02.htm";
			}
			case "VALAKAS_TEMPLE_SHOW_DEFAULT_PAGE":
			{
				return getHtm(player, "data/html/default/" + PARME_NPC_ID + ".htm");
			}
			case "VALAKAS_TEMPLE_ENTER_TO_INSTANCE":
			{
				if (checkRequirementsForEnter(player))
				{
					return PARME_NPC_ID + "-no_cc.htm";
				}
				
				PLAYER_TO_LOGIN.get(player.getObjectId()).forEach(p -> enterInstance(p, npc, ValakasTemple.VALAKAS_TEMPLE_INSTANCE_ID));
				PLAYER_TO_LOGIN.remove(player.getObjectId());
			}
		}
		return super.onEvent(event, npc, player);
	}
	
	private static boolean checkRequirementsForEnter(Player requestor)
	{
		if (requestor.isGM())
		{
			PLAYER_TO_LOGIN.put(requestor.getObjectId(), new HashSet<>());
			PLAYER_TO_LOGIN.get(requestor.getObjectId()).addAll(requestor.isInParty() ? requestor.getParty().getMembers() : requestor.isInCommandChannel() ? requestor.getCommandChannel().getMembers() : Set.of(requestor));
		}
		else
		{
			if (!requestor.isInCommandChannel())
			{
				requestor.sendPacket(SystemMessageId.COMMAND_CHANNEL_INQUIRY);
				return true;
			}
			
			final CommandChannel currentChannel = requestor.getCommandChannel();
			if ((currentChannel.getMemberCount() < 15) || (currentChannel.getMemberCount() > 100))
			{
				currentChannel.getMembers().forEach(p -> p.sendPacket(SystemMessageId.YOU_CANNOT_ENTER_DUE_TO_THE_PARTY_HAVING_EXCEEDED_THE_LIMIT));
				return true;
			}
			
			for (Player player : currentChannel.getMembers())
			{
				final SystemMessage sm = checkInstanceStatus(player);
				if (sm != null)
				{
					currentChannel.getMembers().forEach(p -> p.sendPacket(sm));
					return true;
				}
			}
			
			PLAYER_TO_LOGIN.put(requestor.getObjectId(), new HashSet<>());
			PLAYER_TO_LOGIN.get(requestor.getObjectId()).addAll(currentChannel.getMembers());
		}
		return false;
	}
	
	public static SystemMessage checkInstanceStatus(Player player)
	{
		if (player.getLevel() < 76)
		{
			return new SystemMessage(SystemMessageId.C1_DOES_NOT_MEET_LEVEL_REQUIREMENTS_AND_CANNOT_ENTER).addString(player.getName());
		}
		final long currentTime = System.currentTimeMillis();
		if (currentTime < InstanceManager.getInstance().getInstanceTime(player, ValakasTemple.VALAKAS_TEMPLE_INSTANCE_ID))
		{
			return new SystemMessage(SystemMessageId.C1_CANNOT_ENTER_YET).addString(player.getName());
		}
		if (InstanceManager.getInstance().getPlayerInstance(player, true) != null)
		{
			return new SystemMessage(SystemMessageId.YOU_CANNOT_ENTER_AS_C1_IS_IN_ANOTHER_INSTANCE_ZONE).addString(player.getName());
		}
		return null;
	}
	
	public static void main(String[] args)
	{
		new ValakasTempleTeleport();
	}
}
