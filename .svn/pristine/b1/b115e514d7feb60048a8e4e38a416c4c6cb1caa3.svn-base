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
package ai.others.OrcFortress;

import org.l2jmobius.gameserver.instancemanager.FortManager;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.clan.Clan;

import ai.AbstractNpcAI;

/**
 * @author Serenitty
 */
public class GateKeeper extends AbstractNpcAI
{
	private static final int GATEKEEPER = 34146;
	
	private GateKeeper()
	{
		addFirstTalkId(GATEKEEPER);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		final Clan clan = FortManager.getInstance().getFortById(FortManager.ORC_FORTRESS).getOwnerClan();
		if ((clan == null) || (player.getClan() == null) || (player.getClan().getId() != clan.getId()))
		{
			return "noclan.html";
		}
		return super.onFirstTalk(npc, player);
	}
	
	public static void main(String[] args)
	{
		new GateKeeper();
	}
}
