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
package org.l2j.gameserver.network.clientpackets.commission;

import java.util.function.Predicate;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.instancemanager.ItemCommissionManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.commission.CommissionItemType;
import org.l2j.gameserver.model.commission.CommissionTreeType;
import org.l2j.gameserver.model.item.ItemTemplate;
import org.l2j.gameserver.model.item.type.CrystalType;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.commission.ExCloseCommission;

/**
 * @author NosBit
 */
public class RequestCommissionList implements ClientPacket
{
	private int _treeViewDepth;
	private int _itemType;
	private int _type;
	private int _grade;
	private String _query;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_treeViewDepth = packet.readInt();
		_itemType = packet.readInt();
		_type = packet.readInt();
		_grade = packet.readInt();
		_query = packet.readString();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (!ItemCommissionManager.isPlayerAllowedToInteract(player))
		{
			player.sendPacket(ExCloseCommission.STATIC_PACKET);
			return;
		}
		
		Predicate<ItemTemplate> filter = i -> true;
		switch (_treeViewDepth)
		{
			case 1:
			{
				final CommissionTreeType commissionTreeType = CommissionTreeType.findByClientId(_itemType);
				if (commissionTreeType != null)
				{
					filter = filter.and(i -> commissionTreeType.getCommissionItemTypes().contains(i.getCommissionItemType()));
				}
				break;
			}
			case 2:
			{
				final CommissionItemType commissionItemType = CommissionItemType.findByClientId(_itemType);
				if (commissionItemType != null)
				{
					filter = filter.and(i -> i.getCommissionItemType() == commissionItemType);
				}
				break;
			}
		}
		
		switch (_type)
		{
			case 0: // General
			{
				filter = filter.and(i -> true); // TODO: condition
				break;
			}
			case 1: // Rare
			{
				filter = filter.and(i -> true); // TODO: condition
				break;
			}
		}
		
		switch (_grade)
		{
			case 0:
			{
				filter = filter.and(i -> i.getCrystalType() == CrystalType.NONE);
				break;
			}
			case 1:
			{
				filter = filter.and(i -> i.getCrystalType() == CrystalType.D);
				break;
			}
			case 2:
			{
				filter = filter.and(i -> i.getCrystalType() == CrystalType.C);
				break;
			}
			case 3:
			{
				filter = filter.and(i -> i.getCrystalType() == CrystalType.B);
				break;
			}
			case 4:
			{
				filter = filter.and(i -> i.getCrystalType() == CrystalType.A);
				break;
			}
			case 5:
			{
				filter = filter.and(i -> i.getCrystalType() == CrystalType.S);
				break;
			}
			case 6:
			{
				filter = filter.and(i -> i.getCrystalType() == CrystalType.S80);
				break;
			}
			case 7:
			{
				filter = filter.and(i -> i.getCrystalType() == CrystalType.R);
				break;
			}
			case 8:
			{
				filter = filter.and(i -> i.getCrystalType() == CrystalType.R95);
				break;
			}
			case 9:
			{
				filter = filter.and(i -> i.getCrystalType() == CrystalType.R99);
				break;
			}
		}
		
		filter = filter.and(i -> _query.isEmpty() || i.getName().toLowerCase().contains(_query.toLowerCase()));
		ItemCommissionManager.getInstance().showAuctions(player, filter);
	}
}
