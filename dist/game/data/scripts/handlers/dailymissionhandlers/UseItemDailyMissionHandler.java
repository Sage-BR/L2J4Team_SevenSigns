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
package handlers.dailymissionhandlers;

import java.util.HashSet;
import java.util.Set;

import org.l2j.gameserver.enums.DailyMissionStatus;
import org.l2j.gameserver.handler.AbstractDailyMissionHandler;
import org.l2j.gameserver.model.DailyMissionDataHolder;
import org.l2j.gameserver.model.DailyMissionPlayerEntry;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.events.Containers;
import org.l2j.gameserver.model.events.EventType;
import org.l2j.gameserver.model.events.impl.item.OnItemUse;
import org.l2j.gameserver.model.events.listeners.ConsumerEventListener;

/**
 * @author CostyKiller
 */
public class UseItemDailyMissionHandler extends AbstractDailyMissionHandler
{
	private final int _amount;
	private final int _minLevel;
	private final int _maxLevel;
	private final Set<Integer> _itemIds = new HashSet<>();
	
	public UseItemDailyMissionHandler(DailyMissionDataHolder holder)
	{
		super(holder);
		_amount = holder.getRequiredCompletions();
		_minLevel = holder.getParams().getInt("minLevel", 0);
		_maxLevel = holder.getParams().getInt("maxLevel", Integer.MAX_VALUE);
		final String itemIds = holder.getParams().getString("itemIds", "");
		if (!itemIds.isEmpty())
		{
			for (String s : itemIds.split(","))
			{
				final int id = Integer.parseInt(s);
				if (!_itemIds.contains(id))
				{
					_itemIds.add(id);
				}
			}
		}
	}
	
	@Override
	public void init()
	{
		Containers.Global().addListener(new ConsumerEventListener(Containers.Global(), EventType.ON_ITEM_USE, (OnItemUse event) -> onItemUse(event), this));
	}
	
	@Override
	public boolean isAvailable(Player player)
	{
		final DailyMissionPlayerEntry entry = getPlayerEntry(player.getObjectId(), false);
		if (entry != null)
		{
			switch (entry.getStatus())
			{
				case NOT_AVAILABLE: // Initial state
				{
					if (entry.getProgress() >= _amount)
					{
						entry.setStatus(DailyMissionStatus.AVAILABLE);
						storePlayerEntry(entry);
					}
					break;
				}
				case AVAILABLE:
				{
					return true;
				}
			}
		}
		return false;
	}
	
	private void onItemUse(OnItemUse event)
	{
		final Player player = event.getPlayer();
		if (_minLevel > 0)
		{
			if ((player.getLevel() < _minLevel) || (player.getLevel() > _maxLevel) || _itemIds.isEmpty())
			{
				return;
			}
			if (_itemIds.contains(event.getItem().getId()))
			{
				processPlayerProgress(player);
			}
		}
	}
	
	private void processPlayerProgress(Player player)
	{
		final DailyMissionPlayerEntry entry = getPlayerEntry(player.getObjectId(), true);
		if (entry.getStatus() == DailyMissionStatus.NOT_AVAILABLE)
		{
			if (entry.increaseProgress() >= _amount)
			{
				entry.setStatus(DailyMissionStatus.AVAILABLE);
			}
			storePlayerEntry(entry);
		}
	}
}
