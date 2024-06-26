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
package handlers.actionhandlers;

import org.l2j.gameserver.ai.CtrlIntention;
import org.l2j.gameserver.enums.InstanceType;
import org.l2j.gameserver.handler.IActionHandler;
import org.l2j.gameserver.instancemanager.CastleManager;
import org.l2j.gameserver.instancemanager.SiegeGuardManager;
import org.l2j.gameserver.model.WorldObject;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.clan.ClanPrivilege;
import org.l2j.gameserver.model.siege.Castle;
import org.l2j.gameserver.network.SystemMessageId;

public class ItemAction implements IActionHandler
{
	@Override
	public boolean action(Player player, WorldObject target, boolean interact)
	{
		final Castle castle = CastleManager.getInstance().getCastle(target);
		if ((castle != null) && (SiegeGuardManager.getInstance().getSiegeGuardByItem(castle.getResidenceId(), target.getId()) != null) && ((player.getClan() == null) || (castle.getOwnerId() != player.getClanId()) || !player.hasClanPrivilege(ClanPrivilege.CS_MERCENARIES)))
		{
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_CHANGE_MERCENARY_POSITIONS);
			player.setTarget(target);
			player.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
			return false;
		}
		
		if (!player.isFlying())
		{
			player.getAI().setIntention(CtrlIntention.AI_INTENTION_PICK_UP, target);
		}
		return true;
	}
	
	@Override
	public InstanceType getInstanceType()
	{
		return InstanceType.Item;
	}
}