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
package handlers.dailymissionhandlers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.enums.DailyMissionStatus;
import org.l2jmobius.gameserver.handler.AbstractDailyMissionHandler;
import org.l2jmobius.gameserver.model.CommandChannel;
import org.l2jmobius.gameserver.model.DailyMissionDataHolder;
import org.l2jmobius.gameserver.model.DailyMissionPlayerEntry;
import org.l2jmobius.gameserver.model.Party;
import org.l2jmobius.gameserver.model.actor.Attackable;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.events.Containers;
import org.l2jmobius.gameserver.model.events.EventType;
import org.l2jmobius.gameserver.model.events.impl.creature.npc.OnAttackableKill;
import org.l2jmobius.gameserver.model.events.listeners.ConsumerEventListener;

/**
 * @author Mobius
 */
public class MonsterDailyMissionHandler extends AbstractDailyMissionHandler
{
	private final int _amount;
	private final int _minLevel;
	private final int _maxLevel;
	private final Set<Integer> _ids = new HashSet<>();
	private final String _startHour;
	private final String _endHour;
	
	public MonsterDailyMissionHandler(DailyMissionDataHolder holder)
	{
		super(holder);
		_amount = holder.getRequiredCompletions();
		_minLevel = holder.getParams().getInt("minLevel", 0);
		_maxLevel = holder.getParams().getInt("maxLevel", Integer.MAX_VALUE);
		final String ids = holder.getParams().getString("ids", "");
		if (!ids.isEmpty())
		{
			for (String s : ids.split(","))
			{
				final int id = Integer.parseInt(s);
				if (!_ids.contains(id))
				{
					_ids.add(id);
				}
			}
		}
		_startHour = holder.getParams().getString("startHour", "");
		_endHour = holder.getParams().getString("endHour", "");
	}
	
	@Override
	public void init()
	{
		Containers.Monsters().addListener(new ConsumerEventListener(this, EventType.ON_ATTACKABLE_KILL, (OnAttackableKill event) -> onAttackableKill(event), this));
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
	
	private void onAttackableKill(OnAttackableKill event)
	{
		final Attackable monster = event.getTarget();
		if (!_ids.isEmpty() && !_ids.contains(monster.getId()))
		{
			return;
		}
		
		final Player player = event.getAttacker();
		final int monsterLevel = monster.getLevel();
		if ((_minLevel > 0) && ((monsterLevel < _minLevel) || (monsterLevel > _maxLevel) || ((player.getLevel() - monsterLevel) > 15)))
		{
			return;
		}
		
		if (checkTimeInterval() || (_startHour.equals("") && _endHour.equals("")))
		{
			final Party party = player.getParty();
			if (party != null)
			{
				final CommandChannel channel = party.getCommandChannel();
				final List<Player> members = channel != null ? channel.getMembers() : party.getMembers();
				for (Player member : members)
				{
					if ((member.getLevel() >= (monsterLevel - 5)) && (member.calculateDistance3D(monster) <= Config.ALT_PARTY_RANGE))
					{
						processPlayerProgress(member);
					}
				}
			}
			else
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
	
	private boolean checkTimeInterval()
	{
		if (!_startHour.equals("") && !_endHour.equals(""))
		{
			final Date date = new Date();
			final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
			dateFormat.format(date);
			try
			{
				// Check hour parameters.
				if (dateFormat.parse(dateFormat.format(date)).after(dateFormat.parse(_startHour)) && dateFormat.parse(dateFormat.format(date)).before(dateFormat.parse(_endHour)))
				{
					return true;
				}
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}
		}
		return false;
	}
}
