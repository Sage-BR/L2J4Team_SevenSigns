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
package org.l2j.gameserver.instancemanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.l2j.Config;
import org.l2j.commons.database.DatabaseFactory;
import org.l2j.gameserver.data.xml.SpawnData;
import org.l2j.gameserver.model.CombatFlag;
import org.l2j.gameserver.model.FortSiegeSpawn;
import org.l2j.gameserver.model.Spawn;
import org.l2j.gameserver.model.WorldObject;
import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.siege.Fort;
import org.l2j.gameserver.model.siege.FortSiege;
import org.l2j.gameserver.model.skill.CommonSkill;
import org.l2j.gameserver.model.spawns.NpcSpawnTemplate;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.serverpackets.InventoryUpdate;
import org.l2j.gameserver.network.serverpackets.SystemMessage;

public class FortSiegeManager
{
	private static final Logger LOGGER = Logger.getLogger(FortSiegeManager.class.getName());
	
	private int _attackerMaxClans = 500; // Max number of clans
	
	// Fort Siege settings
	private Map<Integer, List<FortSiegeSpawn>> _commanderSpawnList;
	private Map<Integer, List<CombatFlag>> _flagList;
	private boolean _justToTerritory = true; // Changeable in fortsiege.properties
	private int _flagMaxCount = 1; // Changeable in fortsiege.properties
	private int _siegeClanMinLevel = 4; // Changeable in fortsiege.properties
	private int _siegeLength = 60; // Time in minute. Changeable in fortsiege.properties
	private int _countDownLength = 10; // Time in minute. Changeable in fortsiege.properties
	private int _suspiciousMerchantRespawnDelay = 180; // Time in minute. Changeable in fortsiege.properties
	private final Map<Integer, FortSiege> _sieges = new ConcurrentHashMap<>();
	
	protected FortSiegeManager()
	{
		load();
	}
	
	public void addSiegeSkills(Player character)
	{
		character.addSkill(CommonSkill.SEAL_OF_RULER.getSkill(), false);
		character.addSkill(CommonSkill.BUILD_HEADQUARTERS.getSkill(), false);
	}
	
	public void addCombatFlaglagSkills(Player character)
	{
		Clan clan = character.getClan();
		if ((clan != null))
		{
			if ((clan.getLevel() >= getSiegeClanMinLevel()) && FortManager.getInstance().getFortById(FortManager.ORC_FORTRESS).getSiege().isInProgress())
			{
				character.addSkill(CommonSkill.FLAG_DISPLAY.getSkill(), false);
				character.addSkill(CommonSkill.REMOTE_FLAG_DISPLAY.getSkill(), false);
				character.addSkill(CommonSkill.FLAG_POWER_FAST_RUN.getSkill(), false);
				character.addSkill(CommonSkill.FLAG_EQUIP.getSkill(), false);
				switch (character.getClassId())
				{
					// Warrior
					case DUELIST:
					case DREADNOUGHT:
					case TITAN:
					case GRAND_KHAVATARI:
					case FORTUNE_SEEKER:
					case MAESTRO:
					case DOOMBRINGER:
					case SOUL_HOUND:
					case DEATH_KIGHT_HUMAN:
					case DEATH_KIGHT_ELF:
					case DEATH_KIGHT_DARK_ELF:
					{
						character.addSkill(CommonSkill.FLAG_POWER_WARRIOR.getSkill(), false);
						break;
					}
					// Knight
					case PHOENIX_KNIGHT:
					case HELL_KNIGHT:
					case EVA_TEMPLAR:
					case SHILLIEN_TEMPLAR:
					{
						character.addSkill(CommonSkill.FLAG_POWER_KNIGHT.getSkill(), false);
						break;
					}
					// Rogue
					case ADVENTURER:
					case WIND_RIDER:
					case GHOST_HUNTER:
					{
						character.addSkill(CommonSkill.FLAG_POWER_ROGUE.getSkill(), false);
						break;
					}
					// Archer
					case SAGITTARIUS:
					case MOONLIGHT_SENTINEL:
					case GHOST_SENTINEL:
					case TRICKSTER:
					{
						character.addSkill(CommonSkill.FLAG_POWER_ARCHER.getSkill(), false);
						break;
					}
					// Mage
					case ARCHMAGE:
					case SOULTAKER:
					case MYSTIC_MUSE:
					case STORM_SCREAMER:
					{
						character.addSkill(CommonSkill.FLAG_POWER_MAGE.getSkill(), false);
						break;
					}
					// Summoner
					case ARCANA_LORD:
					case ELEMENTAL_MASTER:
					case SPECTRAL_MASTER:
					{
						character.addSkill(CommonSkill.FLAG_POWER_SUMMONER.getSkill(), false);
						break;
					}
					// Healer
					case CARDINAL:
					case EVA_SAINT:
					case SHILLIEN_SAINT:
					{
						character.addSkill(CommonSkill.FLAG_POWER_HEALER.getSkill(), false);
						break;
					}
					// Enchanter
					case HIEROPHANT:
					{
						character.addSkill(CommonSkill.FLAG_POWER_ENCHANTER.getSkill(), false);
						break;
					}
					// Bard
					case SWORD_MUSE:
					case SPECTRAL_DANCER:
					{
						character.addSkill(CommonSkill.FLAG_POWER_BARD.getSkill(), false);
						break;
					}
					// Shaman
					case DOMINATOR:
					case DOOMCRYER:
					{
						character.addSkill(CommonSkill.FLAG_POWER_SHAMAN.getSkill(), false);
						break;
					}
				}
			}
		}
	}
	
	public void removeCombatFlagSkills(Player character)
	{
		character.removeSkill(CommonSkill.FLAG_DISPLAY.getSkill());
		character.removeSkill(CommonSkill.REMOTE_FLAG_DISPLAY.getSkill());
		character.removeSkill(CommonSkill.FLAG_POWER_FAST_RUN.getSkill());
		character.removeSkill(CommonSkill.FLAG_EQUIP.getSkill());
		character.removeSkill(CommonSkill.FLAG_POWER_WARRIOR.getSkill());
		character.removeSkill(CommonSkill.FLAG_POWER_KNIGHT.getSkill());
		character.removeSkill(CommonSkill.FLAG_POWER_ROGUE.getSkill());
		character.removeSkill(CommonSkill.FLAG_POWER_ARCHER.getSkill());
		character.removeSkill(CommonSkill.FLAG_POWER_MAGE.getSkill());
		character.removeSkill(CommonSkill.FLAG_POWER_SUMMONER.getSkill());
		character.removeSkill(CommonSkill.FLAG_POWER_HEALER.getSkill());
		character.removeSkill(CommonSkill.FLAG_POWER_ENCHANTER.getSkill());
		character.removeSkill(CommonSkill.FLAG_POWER_BARD.getSkill());
		character.removeSkill(CommonSkill.FLAG_POWER_SHAMAN.getSkill());
		character.removeSkill(CommonSkill.FLAG_POWER_ENCHANTER.getSkill());
		character.removeSkill(CommonSkill.FLAG_EQUIP.getSkill());
	}
	
	/**
	 * @param clan The Clan of the player
	 * @param fortid
	 * @return true if the clan is registered or owner of a fort
	 */
	public boolean checkIsRegistered(Clan clan, int fortid)
	{
		if (clan == null)
		{
			return false;
		}
		
		boolean register = false;
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT clan_id FROM fortsiege_clans where clan_id=? and fort_id=?"))
		{
			ps.setInt(1, clan.getId());
			ps.setInt(2, fortid);
			try (ResultSet rs = ps.executeQuery())
			{
				if (rs.next())
				{
					register = true;
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.log(Level.WARNING, "Exception: checkIsRegistered(): " + e.getMessage(), e);
		}
		return register;
	}
	
	public void removeSiegeSkills(Player character)
	{
		character.removeSkill(CommonSkill.SEAL_OF_RULER.getSkill());
		character.removeSkill(CommonSkill.BUILD_HEADQUARTERS.getSkill());
	}
	
	private void load()
	{
		final Properties siegeSettings = new Properties();
		final File file = new File(Config.FORTSIEGE_CONFIG_FILE);
		try (InputStream is = new FileInputStream(file))
		{
			siegeSettings.load(is);
		}
		catch (Exception e)
		{
			LOGGER.log(Level.WARNING, "Error while loading Fort Siege Manager settings!", e);
		}
		
		// Siege setting
		_justToTerritory = Boolean.parseBoolean(siegeSettings.getProperty("JustToTerritory", "true"));
		_attackerMaxClans = Integer.decode(siegeSettings.getProperty("AttackerMaxClans", "500"));
		_flagMaxCount = Integer.decode(siegeSettings.getProperty("MaxFlags", "1"));
		_siegeClanMinLevel = Integer.decode(siegeSettings.getProperty("SiegeClanMinLevel", "4"));
		_siegeLength = Integer.decode(siegeSettings.getProperty("SiegeLength", "60"));
		_countDownLength = Integer.decode(siegeSettings.getProperty("CountDownLength", "10"));
		_suspiciousMerchantRespawnDelay = Integer.decode(siegeSettings.getProperty("SuspiciousMerchantRespawnDelay", "180"));
		
		// Siege spawns settings
		_commanderSpawnList = new ConcurrentHashMap<>();
		_flagList = new ConcurrentHashMap<>();
		for (Fort fort : FortManager.getInstance().getForts())
		{
			final List<FortSiegeSpawn> commanderSpawns = new CopyOnWriteArrayList<>();
			final List<CombatFlag> flagSpawns = new CopyOnWriteArrayList<>();
			for (int i = 1; i < 5; i++)
			{
				final String _spawnParams = siegeSettings.getProperty(fort.getName().replace(" ", "") + "Commander" + i, "");
				if (_spawnParams.isEmpty())
				{
					break;
				}
				final StringTokenizer st = new StringTokenizer(_spawnParams.trim(), ",");
				
				try
				{
					final int x = Integer.parseInt(st.nextToken());
					final int y = Integer.parseInt(st.nextToken());
					final int z = Integer.parseInt(st.nextToken());
					final int heading = Integer.parseInt(st.nextToken());
					final int npc_id = Integer.parseInt(st.nextToken());
					commanderSpawns.add(new FortSiegeSpawn(fort.getResidenceId(), x, y, z, heading, npc_id, i));
				}
				catch (Exception e)
				{
					LOGGER.warning("Error while loading commander(s) for " + fort.getName() + " fort.");
				}
			}
			
			_commanderSpawnList.put(fort.getResidenceId(), commanderSpawns);
			
			for (int i = 1; i < 4; i++)
			{
				final String _spawnParams = siegeSettings.getProperty(fort.getName().replace(" ", "") + "Flag" + i, "");
				if (_spawnParams.isEmpty())
				{
					break;
				}
				final StringTokenizer st = new StringTokenizer(_spawnParams.trim(), ",");
				
				try
				{
					final int x = Integer.parseInt(st.nextToken());
					final int y = Integer.parseInt(st.nextToken());
					final int z = Integer.parseInt(st.nextToken());
					final int flag_id = Integer.parseInt(st.nextToken());
					flagSpawns.add(new CombatFlag(fort.getResidenceId(), x, y, z, 0, flag_id));
				}
				catch (Exception e)
				{
					LOGGER.warning("Error while loading flag(s) for " + fort.getName() + " fort.");
				}
			}
			_flagList.put(fort.getResidenceId(), flagSpawns);
		}
	}
	
	public List<FortSiegeSpawn> getCommanderSpawnList(int fortId)
	{
		return _commanderSpawnList.get(fortId);
	}
	
	public List<CombatFlag> getFlagList(int fortId)
	{
		return _flagList.get(fortId);
	}
	
	public int getAttackerMaxClans()
	{
		return _attackerMaxClans;
	}
	
	public int getFlagMaxCount()
	{
		return _flagMaxCount;
	}
	
	public boolean canRegisterJustTerritory()
	{
		return _justToTerritory;
	}
	
	public int getSuspiciousMerchantRespawnDelay()
	{
		return _suspiciousMerchantRespawnDelay;
	}
	
	public FortSiege getSiege(WorldObject activeObject)
	{
		return getSiege(activeObject.getX(), activeObject.getY(), activeObject.getZ());
	}
	
	public FortSiege getSiege(int x, int y, int z)
	{
		for (Fort fort : FortManager.getInstance().getForts())
		{
			if (fort.getSiege().checkIfInZone(x, y, z))
			{
				return fort.getSiege();
			}
		}
		return null;
	}
	
	public int getSiegeClanMinLevel()
	{
		return _siegeClanMinLevel;
	}
	
	public int getSiegeLength()
	{
		return _siegeLength;
	}
	
	public int getCountDownLength()
	{
		return _countDownLength;
	}
	
	public Collection<FortSiege> getSieges()
	{
		return _sieges.values();
	}
	
	public FortSiege getSiege(int fortId)
	{
		return _sieges.get(fortId);
	}
	
	public void addSiege(FortSiege fortSiege)
	{
		_sieges.put(fortSiege.getFort().getResidenceId(), fortSiege);
	}
	
	public boolean isCombat(int itemId)
	{
		return itemId == FortManager.ORC_FORTRESS_FLAG;
	}
	
	public boolean activateCombatFlag(Player player, Item item)
	{
		if (!checkIfCanPickup(player))
		{
			return false;
		}
		
		if (player.isMounted())
		{
			player.sendPacket(SystemMessageId.YOU_DO_NOT_MEET_THE_REQUIRED_CONDITION_TO_EQUIP_THAT_ITEM);
		}
		else
		{
			player.getInventory().equipItem(item);
			
			final InventoryUpdate iu = new InventoryUpdate();
			iu.addItem(item);
			player.sendInventoryUpdate(iu);
			
			player.broadcastUserInfo();
			player.setCombatFlagEquipped(true);
			addCombatFlaglagSkills(player);
			
			final SystemMessage sm = new SystemMessage(SystemMessageId.S1_EQUIPPED);
			sm.addItemName(item);
			player.sendPacket(sm);
		}
		
		return true;
	}
	
	public boolean checkIfCanPickup(Player player)
	{
		if (player.isCombatFlagEquipped())
		{
			return false;
		}
		
		final Fort fort = FortManager.getInstance().getFort(player);
		// if ((fort == null) || (fort.getResidenceId() <= 0) || (fort.getSiege().getAttackerClan(player.getClan()) == null))
		if ((fort == null) || (fort.getResidenceId() <= 0))
		{
			return false;
		}
		
		if (!fort.getSiege().isInProgress())
		{
			player.sendPacket(new SystemMessage(SystemMessageId.THE_FORTRESS_BATTLE_OF_S1_HAS_FINISHED).addItemName(FortManager.ORC_FORTRESS_FLAG));
			return false;
		}
		
		return true;
	}
	
	public void dropCombatFlag(Player player, int fortId)
	{
		final Fort fort = FortManager.getInstance().getFortById(fortId);
		if (player != null)
		{
			removeCombatFlagSkills(player);
			final long slot = player.getInventory().getSlotFromItem(player.getInventory().getItemByItemId(FortManager.ORC_FORTRESS_FLAG));
			player.getInventory().unEquipItemInBodySlot(slot);
			Item flag = player.getInventory().getItemByItemId(FortManager.ORC_FORTRESS_FLAG);
			player.destroyItem("CombatFlag", flag, null, true);
			player.setCombatFlagEquipped(false);
			player.broadcastUserInfo();
			final InventoryUpdate iu = new InventoryUpdate();
			player.sendInventoryUpdate(iu);
			SpawnData.getInstance().getSpawns().forEach(spawnTemplate -> spawnTemplate.getGroupsByName(flag.getVariables().getString(FortSiege.GREG_SPAWN_VAR, FortSiege.ORC_FORTRESS_GREG_BOTTOM_RIGHT_SPAWN)).forEach(holder ->
			{
				holder.spawnAll();
				for (NpcSpawnTemplate nst : holder.getSpawns())
				{
					for (Npc npc : nst.getSpawnedNpcs())
					{
						Spawn spawn = npc.getSpawn();
						if (spawn != null)
						{
							spawn.stopRespawn();
						}
					}
				}
			}));
			
		}
		fort.getSiege().addFlagCount(-1);
	}
	
	public static FortSiegeManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final FortSiegeManager INSTANCE = new FortSiegeManager();
	}
}
