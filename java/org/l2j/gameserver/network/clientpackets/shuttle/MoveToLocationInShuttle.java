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
package org.l2j.gameserver.network.clientpackets.shuttle;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.model.Location;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.item.type.WeaponType;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.ActionFailed;
import org.l2j.gameserver.network.serverpackets.shuttle.ExMoveToLocationInShuttle;
import org.l2j.gameserver.network.serverpackets.shuttle.ExStopMoveInShuttle;

/**
 * @author UnAfraid
 */
public class MoveToLocationInShuttle implements ClientPacket
{
	private int _boatId;
	private int _targetX;
	private int _targetY;
	private int _targetZ;
	private int _originX;
	private int _originY;
	private int _originZ;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_boatId = packet.readInt(); // objectId of boat
		_targetX = packet.readInt();
		_targetY = packet.readInt();
		_targetZ = packet.readInt();
		_originX = packet.readInt();
		_originY = packet.readInt();
		_originZ = packet.readInt();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if ((_targetX == _originX) && (_targetY == _originY) && (_targetZ == _originZ))
		{
			player.sendPacket(new ExStopMoveInShuttle(player, _boatId));
			return;
		}
		
		if (player.isAttackingNow() && (player.getActiveWeaponItem() != null) && (player.getActiveWeaponItem().getItemType() == WeaponType.BOW))
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (player.isSitting() || player.isMovementDisabled())
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		player.setInVehiclePosition(new Location(_targetX, _targetY, _targetZ));
		player.broadcastPacket(new ExMoveToLocationInShuttle(player, _originX, _originY, _originZ));
	}
}
