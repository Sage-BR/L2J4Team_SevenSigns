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

import java.util.ArrayList;
import java.util.List;

import org.l2jmobius.Config;
import org.l2jmobius.commons.network.WritableBuffer;
import org.l2jmobius.gameserver.instancemanager.CastleManager;
import org.l2jmobius.gameserver.instancemanager.FortManager;
import org.l2jmobius.gameserver.model.SiegeClan;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.clan.Clan;
import org.l2jmobius.gameserver.model.holders.ResurrectByPaymentHolder;
import org.l2jmobius.gameserver.model.siege.Castle;
import org.l2jmobius.gameserver.model.siege.Fort;
import org.l2jmobius.gameserver.model.skill.BuffInfo;
import org.l2jmobius.gameserver.model.skill.CommonSkill;
import org.l2jmobius.gameserver.model.stats.Stat;
import org.l2jmobius.gameserver.model.variables.PlayerVariables;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;

/**
 * @author Mobius
 */
public class Die extends ServerPacket
{
	private final int _objectId;
	private final boolean _isSweepable;
	private int _flags = 1; // To nearest village.
	private int _delayFeather = 0;
	private Player _player;
	
	public Die(Creature creature)
	{
		_objectId = creature.getObjectId();
		_isSweepable = creature.isAttackable() && creature.isSweepActive();
		if (creature.isPlayer())
		{
			_player = creature.getActingPlayer();
			
			for (BuffInfo effect : creature.getEffectList().getEffects())
			{
				if (effect.getSkill().getId() == CommonSkill.FEATHER_OF_BLESSING.getId())
				{
					_delayFeather = effect.getTime();
					break;
				}
			}
			
			if (!_player.isInTimedHuntingZone())
			{
				final Clan clan = _player.getClan();
				boolean isInCastleDefense = false;
				boolean isInFortDefense = false;
				SiegeClan siegeClan = null;
				final Castle castle = CastleManager.getInstance().getCastle(creature);
				final Fort fort = FortManager.getInstance().getFort(creature);
				if ((castle != null) && castle.getSiege().isInProgress())
				{
					siegeClan = castle.getSiege().getAttackerClan(clan);
					isInCastleDefense = (siegeClan == null) && castle.getSiege().checkIsDefender(clan);
				}
				else if ((fort != null) && fort.getSiege().isInProgress())
				{
					siegeClan = fort.getSiege().getAttackerClan(clan);
					isInFortDefense = (siegeClan == null) && fort.getSiege().checkIsDefender(clan);
				}
				
				// ClanHall check.
				if ((clan != null) && (clan.getHideoutId() > 0))
				{
					_flags += 2;
				}
				// Castle check.
				if (((clan != null) && (clan.getCastleId() > 0)) || isInCastleDefense)
				{
					_flags += 4;
				}
				// Fortress check.
				if (((clan != null) && (clan.getFortId() > 0)) || isInFortDefense)
				{
					_flags += 8;
				}
				// Outpost check.
				if (((siegeClan != null) && !isInCastleDefense && !isInFortDefense && !siegeClan.getFlag().isEmpty()))
				{
					_flags += 16;
				}
			}
			
			// Feather check.
			if (creature.getAccessLevel().allowFixedRes() || creature.getInventory().haveItemForSelfResurrection())
			{
				_flags += 32;
			}
		}
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.DIE.writeId(this, buffer);
		buffer.writeInt(_objectId);
		buffer.writeLong(_flags);
		buffer.writeInt(_isSweepable);
		buffer.writeInt(_delayFeather); // Feather item time.
		buffer.writeByte(0); // Hide die animation.
		buffer.writeInt(0);
		if ((_player != null) && Config.RESURRECT_BY_PAYMENT_ENABLED)
		{
			int resurrectTimes = _player.getVariables().getInt(PlayerVariables.RESURRECT_BY_PAYMENT_COUNT, 0) + 1;
			int originalValue = resurrectTimes - 1;
			if (originalValue < Config.RESURRECT_BY_PAYMENT_MAX_FREE_TIMES)
			{
				buffer.writeInt(Config.RESURRECT_BY_PAYMENT_MAX_FREE_TIMES - originalValue); // free round resurrection
				buffer.writeInt(0); // Adena resurrection
				buffer.writeInt(0); // Adena count%
				buffer.writeInt(0); // L-Coin resurrection
				buffer.writeInt(0); // L-Coin count%
			}
			else
			{
				buffer.writeInt(0);
				getValues(buffer, _player, originalValue);
			}
		}
		else
		{
			buffer.writeInt(1); // free round resurrection
			buffer.writeInt(0); // Adena resurrection
			buffer.writeInt(-1); // Adena count%
			buffer.writeInt(0); // L-Coin resurrection
			buffer.writeInt(-1); // L-Coin count%
		}
		buffer.writeInt(0);
	}
	
	private void getValues(WritableBuffer buffer, Player player, int originalValue)
	{
		if ((Config.RESURRECT_BY_PAYMENT_FIRST_RESURRECT_VALUES == null) || (Config.RESURRECT_BY_PAYMENT_SECOND_RESURRECT_VALUES == null))
		{
			buffer.writeInt(0); // Adena resurrection
			buffer.writeInt(-1); // Adena count%
			buffer.writeInt(0); // L-Coin resurrection
			buffer.writeInt(-1); // L-Coin count%
			return;
		}
		
		final List<Integer> levelListFirst = new ArrayList<>(Config.RESURRECT_BY_PAYMENT_FIRST_RESURRECT_VALUES.keySet());
		final List<Integer> levelListSecond = new ArrayList<>(Config.RESURRECT_BY_PAYMENT_SECOND_RESURRECT_VALUES.keySet());
		for (int level : levelListSecond)
		{
			if (Config.RESURRECT_BY_PAYMENT_SECOND_RESURRECT_VALUES.isEmpty())
			{
				buffer.writeInt(0); // Adena resurrection
				buffer.writeInt(-1); // Adena count%
				break;
			}
			
			if ((player.getLevel() >= level) && (levelListSecond.lastIndexOf(level) != (levelListSecond.size() - 1)))
			{
				continue;
			}
			
			int maxResTime;
			try
			{
				maxResTime = Config.RESURRECT_BY_PAYMENT_SECOND_RESURRECT_VALUES.get(level).keySet().stream().max(Integer::compareTo).get();
			}
			catch (Exception e)
			{
				buffer.writeInt(0); // Adena resurrection
				buffer.writeInt(-1); // Adena count%
				return;
			}
			
			int getValue = maxResTime <= originalValue ? maxResTime : originalValue + 1;
			final ResurrectByPaymentHolder rbph = Config.RESURRECT_BY_PAYMENT_SECOND_RESURRECT_VALUES.get(level).get(getValue);
			if (rbph != null)
			{
				buffer.writeInt((int) (rbph.getAmount() * player.getStat().getValue(Stat.RESURRECTION_FEE_MODIFIER, 1))); // Adena resurrection
				buffer.writeInt(Math.toIntExact(Math.round(rbph.getResurrectPercent()))); // Adena count%
			}
			else
			{
				buffer.writeInt(0);
				buffer.writeInt(-1);
			}
			break;
		}
		
		for (int level : levelListFirst)
		{
			if (Config.RESURRECT_BY_PAYMENT_FIRST_RESURRECT_VALUES.isEmpty())
			{
				buffer.writeInt(0); // L-Coin resurrection
				buffer.writeInt(-1); // L-Coin count%
				break;
			}
			
			if ((player.getLevel() >= level) && (levelListFirst.lastIndexOf(level) != (levelListFirst.size() - 1)))
			{
				continue;
			}
			
			int maxResTime;
			try
			{
				maxResTime = Config.RESURRECT_BY_PAYMENT_FIRST_RESURRECT_VALUES.get(level).keySet().stream().max(Integer::compareTo).get();
			}
			catch (Exception e)
			{
				buffer.writeInt(0); // L-Coin resurrection
				buffer.writeInt(-1); // L-Coin count%
				return;
			}
			
			final int getValue = maxResTime <= originalValue ? maxResTime : originalValue + 1;
			final ResurrectByPaymentHolder rbph = Config.RESURRECT_BY_PAYMENT_FIRST_RESURRECT_VALUES.get(level).get(getValue);
			if (rbph != null)
			{
				buffer.writeInt((int) (rbph.getAmount() * player.getStat().getValue(Stat.RESURRECTION_FEE_MODIFIER, 1))); // L-Coin resurrection
				buffer.writeInt(Math.toIntExact(Math.round(rbph.getResurrectPercent()))); // L-Coin count%
			}
			else
			{
				buffer.writeInt(0);
				buffer.writeInt(-1);
			}
			break;
		}
	}
}
