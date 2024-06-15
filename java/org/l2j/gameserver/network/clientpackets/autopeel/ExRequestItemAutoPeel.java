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
package org.l2j.gameserver.network.clientpackets.autopeel;

import java.util.Collections;

import org.l2j.gameserver.handler.IItemHandler;
import org.l2j.gameserver.handler.ItemHandler;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.request.AutoPeelRequest;
import org.l2j.gameserver.model.item.EtcItem;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.autopeel.ExResultItemAutoPeel;

/**
 * @author Mobius
 */
public class ExRequestItemAutoPeel extends ClientPacket
{
	private int _itemObjectId;
	private long _totalPeelCount;
	private long _remainingPeelCount;
	
	@Override
	protected void readImpl()
	{
		_itemObjectId = readInt();
		_totalPeelCount = readLong();
		_remainingPeelCount = readLong();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if ((player == null) || (_totalPeelCount < 1) || (_remainingPeelCount < 0))
		{
			return;
		}
		
		AutoPeelRequest request = player.getRequest(AutoPeelRequest.class);
		if (request == null)
		{
			final Item item = player.getInventory().getItemByObjectId(_itemObjectId);
			if ((item == null) || !item.isEtcItem() || (item.getEtcItem().getExtractableItems() == null) || item.getEtcItem().getExtractableItems().isEmpty())
			{
				return;
			}
			
			request = new AutoPeelRequest(player, item);
			player.addRequest(request);
		}
		else if (request.isProcessing())
		{
			return;
		}
		request.setProcessing(true);
		
		final Item item = request.getItem();
		if ((item.getObjectId() != _itemObjectId) || (item.getOwnerId() != player.getObjectId()))
		{
			player.removeRequest(request.getClass());
			return;
		}
		
		if (!item.getTemplate().checkCondition(player, item, true))
		{
			player.sendPacket(new ExResultItemAutoPeel(false, _totalPeelCount, _remainingPeelCount, Collections.emptyList()));
			player.removeRequest(request.getClass());
			return;
		}
		
		request.setTotalPeelCount(_totalPeelCount);
		request.setRemainingPeelCount(_remainingPeelCount);
		
		final EtcItem etcItem = (EtcItem) item.getTemplate();
		if ((etcItem.getExtractableItems() != null) && !etcItem.getExtractableItems().isEmpty())
		{
			final IItemHandler handler = ItemHandler.getInstance().getHandler(item.getEtcItem());
			if ((handler != null) && !handler.useItem(player, item, false))
			{
				request.setProcessing(false);
				player.sendPacket(new ExResultItemAutoPeel(false, _totalPeelCount, _remainingPeelCount, Collections.emptyList()));
			}
		}
	}
}