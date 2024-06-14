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
package org.l2j.gameserver.network.clientpackets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.l2j.Config;
import org.l2j.commons.threads.ThreadPool;
import org.l2j.gameserver.data.xml.ClanHallData;
import org.l2j.gameserver.enums.TeleportWhereType;
import org.l2j.gameserver.instancemanager.CastleManager;
import org.l2j.gameserver.instancemanager.FortManager;
import org.l2j.gameserver.instancemanager.MapRegionManager;
import org.l2j.gameserver.model.Location;
import org.l2j.gameserver.model.SiegeClan;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.model.events.EventType;
import org.l2j.gameserver.model.events.listeners.AbstractEventListener;
import org.l2j.gameserver.model.holders.ResurrectByPaymentHolder;
import org.l2j.gameserver.model.instancezone.Instance;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.quest.Event;
import org.l2j.gameserver.model.residences.ClanHall;
import org.l2j.gameserver.model.residences.ResidenceFunctionType;
import org.l2j.gameserver.model.siege.Castle;
import org.l2j.gameserver.model.siege.Castle.CastleFunction;
import org.l2j.gameserver.model.siege.Fort;
import org.l2j.gameserver.model.siege.Fort.FortFunction;
import org.l2j.gameserver.model.skill.CommonSkill;
import org.l2j.gameserver.model.stats.Stat;
import org.l2j.gameserver.model.variables.PlayerVariables;
import org.l2j.gameserver.network.PacketLogger;
import org.l2j.gameserver.network.SystemMessageId;

/**
 * @version $Revision: 1.7.2.3.2.6 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestRestartPoint extends ClientPacket
{
	protected int _requestedPointType;
	protected boolean _continuation;
	protected int _resItemID;
	protected int _resCount;
	
	@Override
	protected void readImpl()
	{
		_requestedPointType = readInt();
		if (remaining() != 0)
		{
			_resItemID = readInt();
			_resCount = readInt();
		}
	}
	
	class DeathTask implements Runnable
	{
		final Player _player;
		
		DeathTask(Player player)
		{
			_player = player;
		}
		
		@Override
		public void run()
		{
			portPlayer(_player);
		}
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (!player.canRevive())
		{
			return;
		}
		
		if (player.isFakeDeath())
		{
			player.stopFakeDeath(true);
			return;
		}
		else if (!player.isDead())
		{
			return;
		}
		
		// Custom event resurrection management.
		if (player.isOnEvent())
		{
			for (AbstractEventListener listener : player.getListeners(EventType.ON_CREATURE_DEATH))
			{
				if (listener.getOwner() instanceof Event)
				{
					((Event) listener.getOwner()).notifyEvent("ResurrectPlayer", null, player);
					return;
				}
			}
		}
		
		final Castle castle = CastleManager.getInstance().getCastle(player.getX(), player.getY(), player.getZ());
		if ((castle != null) && castle.getSiege().isInProgress() && (player.getClan() != null) && castle.getSiege().checkIsAttacker(player.getClan()))
		{
			// Schedule respawn delay for attacker
			ThreadPool.schedule(new DeathTask(player), castle.getSiege().getAttackerRespawnDelay());
			if (castle.getSiege().getAttackerRespawnDelay() > 0)
			{
				player.sendMessage("You will be re-spawned in " + (castle.getSiege().getAttackerRespawnDelay() / 1000) + " seconds");
			}
			return;
		}
		
		portPlayer(player);
	}
	
	private void portPlayer(Player player)
	{
		Location loc = null;
		Instance instance = null;
		
		// force jail
		if (player.isJailed())
		{
			_requestedPointType = 27;
		}
		
		switch (_requestedPointType)
		{
			case 1: // to clanhall
			{
				if ((player.getClan() == null) || (player.getClan().getHideoutId() == 0))
				{
					PacketLogger.warning("Player [" + player.getName() + "] called RestartPointPacket - To Clanhall and he doesn't have Clanhall!");
					return;
				}
				loc = MapRegionManager.getInstance().getTeleToLocation(player, TeleportWhereType.CLANHALL);
				final ClanHall residense = ClanHallData.getInstance().getClanHallByClan(player.getClan());
				if ((residense != null) && (residense.hasFunction(ResidenceFunctionType.EXP_RESTORE)))
				{
					player.restoreExp(residense.getFunction(ResidenceFunctionType.EXP_RESTORE).getValue());
				}
				break;
			}
			case 2: // to castle
			{
				final Clan clan = player.getClan();
				Castle castle = CastleManager.getInstance().getCastle(player);
				if ((castle != null) && castle.getSiege().isInProgress())
				{
					// Siege in progress
					if (castle.getSiege().checkIsDefender(clan))
					{
						loc = MapRegionManager.getInstance().getTeleToLocation(player, TeleportWhereType.CASTLE);
					}
					else if (castle.getSiege().checkIsAttacker(clan))
					{
						loc = MapRegionManager.getInstance().getTeleToLocation(player, TeleportWhereType.TOWN);
					}
					else
					{
						PacketLogger.warning("Player [" + player.getName() + "] called RestartPointPacket - To Castle and he doesn't have Castle!");
						return;
					}
				}
				else
				{
					if ((clan == null) || (clan.getCastleId() == 0))
					{
						return;
					}
					loc = MapRegionManager.getInstance().getTeleToLocation(player, TeleportWhereType.CASTLE);
				}
				
				if (clan != null)
				{
					castle = CastleManager.getInstance().getCastleByOwner(clan);
					if (castle != null)
					{
						final CastleFunction castleFunction = castle.getCastleFunction(Castle.FUNC_RESTORE_EXP);
						if (castleFunction != null)
						{
							player.restoreExp(castleFunction.getLvl());
						}
					}
				}
				break;
			}
			case 3: // to fortress
			{
				final Clan clan = player.getClan();
				if ((clan == null) || (clan.getFortId() == 0))
				{
					PacketLogger.warning("Player [" + player.getName() + "] called RestartPointPacket - To Fortress and he doesn't have Fortress!");
					return;
				}
				loc = MapRegionManager.getInstance().getTeleToLocation(player, TeleportWhereType.FORTRESS);
				
				final Fort fort = FortManager.getInstance().getFortByOwner(clan);
				if (fort != null)
				{
					final FortFunction fortFunction = fort.getFortFunction(Fort.FUNC_RESTORE_EXP);
					if (fortFunction != null)
					{
						player.restoreExp(fortFunction.getLevel());
					}
				}
				break;
			}
			case 4: // to siege HQ
			{
				SiegeClan siegeClan = null;
				final Castle castle = CastleManager.getInstance().getCastle(player);
				final Fort fort = FortManager.getInstance().getFort(player);
				if ((castle != null) && castle.getSiege().isInProgress())
				{
					siegeClan = castle.getSiege().getAttackerClan(player.getClan());
				}
				else if ((fort != null) && fort.getSiege().isInProgress())
				{
					siegeClan = fort.getSiege().getAttackerClan(player.getClan());
				}
				
				if (((siegeClan == null) || siegeClan.getFlag().isEmpty()))
				{
					PacketLogger.warning("Player [" + player.getName() + "] called RestartPointPacket - To Siege HQ and he doesn't have Siege HQ!");
					return;
				}
				loc = MapRegionManager.getInstance().getTeleToLocation(player, TeleportWhereType.SIEGEFLAG);
				break;
			}
			case 5: // Fixed or Player is a festival participant
			{
				if (!player.isGM() && !player.getInventory().haveItemForSelfResurrection())
				{
					PacketLogger.warning("Player [" + player.getName() + "] called RestartPointPacket - Fixed and he isn't festival participant!");
					return;
				}
				if (player.isGM())
				{
					player.doRevive(100);
				}
				else if (player.destroyItemByItemId("Feather", 10649, 1, player, false) /* || player.destroyItemByItemId("Feather", 13300, 1, player, false) || player.destroyItemByItemId("Feather", 13128, 1, player, false) */)
				{
					player.doRevive(100);
					CommonSkill.FEATHER_OF_BLESSING.getSkill().applyEffects(player, player);
				}
				else
				{
					instance = player.getInstanceWorld();
					loc = new Location(player);
				}
				break;
			}
			case 6: // TODO: Agathion resurrection
			{
				break;
			}
			case 7: // TODO: Adventurer's Song
			{
				break;
			}
			case 9:
			{
				if (Config.RESURRECT_BY_PAYMENT_ENABLED)
				{
					if (!player.isDead())
					{
						break;
					}
					
					final int originalValue = player.getVariables().getInt(PlayerVariables.RESURRECT_BY_PAYMENT_COUNT, 0);
					if (originalValue < Config.RESURRECT_BY_PAYMENT_MAX_FREE_TIMES)
					{
						player.getVariables().set(PlayerVariables.RESURRECT_BY_PAYMENT_COUNT, originalValue + 1);
						player.doRevive(100.0);
						loc = MapRegionManager.getInstance().getTeleToLocation(player, TeleportWhereType.TOWN);
						player.teleToLocation(loc, true, instance);
						break;
					}
					
					final int firstID = Config.RESURRECT_BY_PAYMENT_ENABLED ? Config.RESURRECT_BY_PAYMENT_FIRST_RESURRECT_ITEM : 91663;
					final int secondID = Config.RESURRECT_BY_PAYMENT_ENABLED ? Config.RESURRECT_BY_PAYMENT_SECOND_RESURRECT_ITEM : 57;
					Map<Integer, Map<Integer, ResurrectByPaymentHolder>> resMAP = null;
					Item item = null;
					if (_resItemID == firstID)
					{
						resMAP = Config.RESURRECT_BY_PAYMENT_FIRST_RESURRECT_VALUES;
						item = player.getInventory().getItemByItemId(Config.RESURRECT_BY_PAYMENT_FIRST_RESURRECT_ITEM);
					}
					else if (_resItemID == secondID)
					{
						resMAP = Config.RESURRECT_BY_PAYMENT_SECOND_RESURRECT_VALUES;
						item = player.getInventory().getItemByItemId(Config.RESURRECT_BY_PAYMENT_SECOND_RESURRECT_ITEM);
					}
					if ((resMAP == null) || (item == null))
					{
						break;
					}
					
					final List<Integer> levelList = new ArrayList<>(resMAP.keySet());
					for (int level : levelList)
					{
						if ((player.getLevel() >= level) && (levelList.lastIndexOf(level) != (levelList.size() - 1)))
						{
							continue;
						}
						
						int maxResTime;
						try
						{
							maxResTime = resMAP.get(level).keySet().stream().max(Integer::compareTo).get();
						}
						catch (Exception e)
						{
							player.sendPacket(SystemMessageId.NOT_ENOUGH_ITEMS);
							return;
						}
						
						final int getValue = maxResTime <= originalValue ? maxResTime : originalValue + 1;
						final ResurrectByPaymentHolder rbph = resMAP.get(level).get(getValue);
						final long fee = (int) (rbph.getAmount() * player.getStat().getValue(Stat.RESURRECTION_FEE_MODIFIER, 1));
						if (item.getCount() < fee)
						{
							return;
						}
						
						player.getVariables().set(PlayerVariables.RESURRECT_BY_PAYMENT_COUNT, originalValue + 1);
						player.destroyItem("item revive", item, fee, player, true);
						player.doRevive(rbph.getResurrectPercent());
						loc = MapRegionManager.getInstance().getTeleToLocation(player, TeleportWhereType.TOWN);
						player.teleToLocation(loc, true, instance);
						break;
					}
				}
			}
			case 27: // to jail
			{
				if (!player.isJailed())
				{
					return;
				}
				
				loc = new Location(-114356, -249645, -2984);
				break;
			}
			default:
			{
				if (player.isInTimedHuntingZone())
				{
					instance = player.getInstanceWorld();
					loc = player.getActingPlayer().getTimedHuntingZone().getEnterLocation();
				}
				else
				{
					loc = MapRegionManager.getInstance().getTeleToLocation(player, TeleportWhereType.TOWN);
				}
				break;
			}
		}
		
		// Teleport and revive
		if (loc != null)
		{
			player.setIsPendingRevive(true);
			player.teleToLocation(loc, true, instance);
		}
	}
}
