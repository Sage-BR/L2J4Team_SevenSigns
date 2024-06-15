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
package ai.bosses.Balok;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicReference;

import org.l2j.Config;
import org.l2j.commons.threads.ThreadPool;
import org.l2j.commons.util.Rnd;
import org.l2j.gameserver.data.xml.SpawnData;
import org.l2j.gameserver.enums.MailType;
import org.l2j.gameserver.instancemanager.BattleWithBalokManager;
import org.l2j.gameserver.instancemanager.GlobalVariablesManager;
import org.l2j.gameserver.instancemanager.MailManager;
import org.l2j.gameserver.instancemanager.ZoneManager;
import org.l2j.gameserver.model.Location;
import org.l2j.gameserver.model.Message;
import org.l2j.gameserver.model.World;
import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.events.EventType;
import org.l2j.gameserver.model.events.ListenerRegisterType;
import org.l2j.gameserver.model.events.annotations.RegisterEvent;
import org.l2j.gameserver.model.events.annotations.RegisterType;
import org.l2j.gameserver.model.events.impl.creature.player.OnPlayerLogin;
import org.l2j.gameserver.model.itemcontainer.Mail;
import org.l2j.gameserver.model.spawns.SpawnGroup;
import org.l2j.gameserver.model.spawns.SpawnTemplate;
import org.l2j.gameserver.model.variables.PlayerVariables;
import org.l2j.gameserver.model.zone.ZoneType;
import org.l2j.gameserver.network.NpcStringId;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.serverpackets.ExShowScreenMessage;
import org.l2j.gameserver.network.serverpackets.SystemMessage;
import org.l2j.gameserver.network.serverpackets.balok.BalrogWarBossInfo;
import org.l2j.gameserver.network.serverpackets.balok.BalrogWarHud;
import org.l2j.gameserver.util.Broadcast;

import ai.AbstractNpcAI;

/**
 * @author Serenitty
 */
public class Balok extends AbstractNpcAI
{
	// Monsters
	private static final int SCORPION = 22416;
	private static final Set<Integer> NORMAL_MOBS = new HashSet<>();
	static
	{
		NORMAL_MOBS.add(22413);
		NORMAL_MOBS.add(22415);
		NORMAL_MOBS.add(22414);
		NORMAL_MOBS.add(22416);
	}
	// Intermid bosses
	private static final int KESMA = 25956;
	private static final int PRAIS = 25957;
	private static final int VIRA = 25958;
	private static final int HEEDER = 25959;
	private static final int HEARAK = 25960;
	private static final Set<Integer> INTERMID_BOSSES = new HashSet<>();
	static
	{
		INTERMID_BOSSES.add(KESMA);
		INTERMID_BOSSES.add(PRAIS);
		INTERMID_BOSSES.add(VIRA);
		INTERMID_BOSSES.add(HEEDER);
		INTERMID_BOSSES.add(HEARAK);
	}
	// Final boss
	private static final int BALOK1 = 29157; // Balok red normal
	private static final int BALOK2 = 29161; // Balok aqua
	private static final int BALOK3 = 29165; // Balok yellow
	private static final int LORD_BALOK = 29169;
	private static final Set<Integer> FINAL_BOSSES = new HashSet<>();
	static
	{
		FINAL_BOSSES.add(BALOK1);
		FINAL_BOSSES.add(BALOK2);
		FINAL_BOSSES.add(BALOK3);
		FINAL_BOSSES.add(LORD_BALOK);
	}
	// Locations
	private static final Location KESMA_LOC = new Location(-17370, 184479, -3991, 49942);
	private static final Location PRAIS_LOC = new Location(-21765, 178790, -4077, 7662);
	private static final Location VIRA_LOC = new Location(-21375, 182516, -3969, 58095);
	private static final Location HEEDER_LOC = new Location(-15210, 178553, -4277, 25134);
	private static final Location HEARAK_LOC = new Location(-13947, 181909, -4348, 32883);
	private static final Location BALOK_LOC = new Location(-18392, 181079, -3845, 48678);
	// Zone
	private static final ZoneType BALOK_BATTLE_ZONE = ZoneManager.getInstance().getZoneByName("balok_area");
	// Misc
	private static final AtomicReference<SpawnTemplate> NORMAL_BATTLE_MOBS = new AtomicReference<>();
	private static final Set<Npc> BOSS_SPAWNED = ConcurrentHashMap.newKeySet();
	private static final int BATTLE_TIME = 1800000; // 30 min
	private static final int PREPARATION_TIME = 1200000; // 20 min
	private static final int REWARD_TIME = 2400000; // 40 min
	
	private int _status = 0;
	private int _stage = 0;
	private int _globalPoints = 0;
	private int _midbossDefeatCount = 0;
	private int _kesmaStatus = 1;
	private int _praisStatus = 1;
	private int _viraStatus = 1;
	private int _heederStatus = 0;
	private int _hearakStatus = 0;
	private int _finalBalokType = 1;
	private int _finalBalokStatus = 1;
	private boolean _inProgress = false;
	private boolean _balokKilled = false;
	private boolean _firstWaveKilled = false;
	private boolean _secondWaveKilled = false;
	private boolean _midBossFirstSpawn = false;
	private boolean _midBossSecondSpawn = false;
	private int _firstKillBalokId = 0;
	ScheduledFuture<?> _rewardTask;
	
	private Balok()
	{
		addKillId(VIRA, KESMA, PRAIS, HEEDER, HEARAK);
		addKillId(FINAL_BOSSES);
		addKillId(NORMAL_MOBS);
		addExitZoneId(BALOK_BATTLE_ZONE.getId());
		addEnterZoneId(BALOK_BATTLE_ZONE.getId());
		
		final long currentTime = System.currentTimeMillis();
		final Calendar startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR_OF_DAY, Config.BALOK_HOUR);
		startTime.set(Calendar.MINUTE, Config.BALOK_MINUTE);
		startTime.set(Calendar.SECOND, 0);
		if (startTime.getTimeInMillis() < currentTime)
		{
			startTime.add(Calendar.DAY_OF_YEAR, 1);
		}
		ThreadPool.scheduleAtFixedRate(this::startEvent, startTime.getTimeInMillis() - currentTime, 86400000); // 86400000 = 1 day
	}
	
	private void startEvent()
	{
		GlobalVariablesManager.getInstance().set(GlobalVariablesManager.BALOK_REMAIN_TIME, System.currentTimeMillis() + PREPARATION_TIME);
		BattleWithBalokManager.getInstance().setGlobalStatus(_globalPoints);
		BattleWithBalokManager.getInstance().setGlobalStage(_stage);
		setInProgress(true);
		inPreparation();
	}
	
	private void inPreparation()
	{
		setStatus(1);
		Broadcast.toAllOnlinePlayers(new BalrogWarHud(_status, 0));
		Broadcast.toAllOnlinePlayers(new SystemMessage(SystemMessageId.BATTLE_WITH_BALOK_STARTS_IN_20_MIN));
		ThreadPool.schedule(this::inPreparation10, 600000); // 10 minutes
	}
	
	private void inPreparation10()
	{
		Broadcast.toAllOnlinePlayers(new SystemMessage(SystemMessageId.BATTLE_WITH_BALOK_STARTS_IN_10_MIN));
		NORMAL_BATTLE_MOBS.set(SpawnData.getInstance().getSpawnByName("BalokBattleground"));
		ThreadPool.schedule(this::inPreparationSoon, 540000); // 9 minutes
	}
	
	private void inPreparationSoon()
	{
		Broadcast.toAllOnlinePlayers(new SystemMessage(SystemMessageId.BATTLE_WITH_BALOK_IS_STARTING_SOON));
		ThreadPool.schedule(this::startSpawnMobs, 60000); // 1 minute
	}
	
	private void startSpawnMobs()
	{
		GlobalVariablesManager.getInstance().remove(GlobalVariablesManager.BALOK_REMAIN_TIME);
		_stage = 1;
		setStatus(2);
		BattleWithBalokManager.getInstance().setInBattle(true);
		GlobalVariablesManager.getInstance().set(GlobalVariablesManager.BALOK_REMAIN_TIME, System.currentTimeMillis() + BATTLE_TIME);
		Broadcast.toAllOnlinePlayers(new SystemMessage(SystemMessageId.MONSTERS_ARE_SPAWNING_ON_THE_BALOK_BATTLEGROUND));
		Broadcast.toAllOnlinePlayers(new BalrogWarHud(_status, _stage));
		NORMAL_BATTLE_MOBS.get().getGroups().forEach(SpawnGroup::spawnAll);
		_rewardTask = ThreadPool.schedule(this::finishAndReward, BATTLE_TIME); // 30 minutes
	}
	
	private void spawnInterBossesFirstWave()
	{
		if (!_midBossFirstSpawn && !_firstWaveKilled)
		{
			BOSS_SPAWNED.add(addSpawn(KESMA, KESMA_LOC));
			BOSS_SPAWNED.add(addSpawn(PRAIS, PRAIS_LOC));
			BOSS_SPAWNED.add(addSpawn(VIRA, VIRA_LOC));
			setMidBossFirstSpawn(true);
			BALOK_BATTLE_ZONE.broadcastPacket(new SystemMessage(SystemMessageId.THREE_BOSSES_HAVE_SPAWNED));
			Broadcast.toAllOnlinePlayers(new BalrogWarBossInfo(_finalBalokType, _finalBalokStatus, _kesmaStatus, _praisStatus, _viraStatus, _hearakStatus, _heederStatus));
		}
	}
	
	private void spawnInterBossesSecondWave()
	{
		if (!_midBossSecondSpawn && !_secondWaveKilled)
		{
			BOSS_SPAWNED.add(addSpawn(HEARAK, HEARAK_LOC));
			BOSS_SPAWNED.add(addSpawn(HEEDER, HEEDER_LOC));
			setMidBossSecondSpawn(true);
			_heederStatus = 1;
			_hearakStatus = 1;
			BALOK_BATTLE_ZONE.broadcastPacket(new SystemMessage(SystemMessageId.TWO_BOSSES_HAVE_SPAWNED));
			Broadcast.toAllOnlinePlayers(new BalrogWarBossInfo(_finalBalokType, _finalBalokStatus, _kesmaStatus, _praisStatus, _viraStatus, _hearakStatus, _heederStatus));
		}
	}
	
	private void bypassRandomStage()
	{
		if (!_midBossSecondSpawn && (getRandom(100) < 3))
		{
			finalBossPlus();
			_stage = 5;
		}
		else
		{
			finalBoss();
			_stage = 4;
		}
		BOSS_SPAWNED.clear();
		setSecondWaveKilled(true);
		BattleWithBalokManager.getInstance().setGlobalStage(_stage);
		Broadcast.toAllOnlinePlayers(new BalrogWarHud(_status, _stage));
		Broadcast.toAllOnlinePlayers(new BalrogWarBossInfo(_finalBalokType, _finalBalokStatus, _kesmaStatus, _praisStatus, _viraStatus, _hearakStatus, _heederStatus));
	}
	
	private void interBossesStatus()
	{
		if (_midbossDefeatCount == 3)
		{
			setFirstWaveKilled(true);
		}
		if (_midbossDefeatCount == 5)
		{
			setFirstWaveKilled(true);
		}
		if (_stage < 4)
		{
			Broadcast.toAllOnlinePlayers(new BalrogWarBossInfo(_finalBalokType, _finalBalokStatus, _kesmaStatus, _praisStatus, _viraStatus, _hearakStatus, _heederStatus));
		}
	}
	
	private void spawnBalok()
	{
		final int random = getRandom(100);
		int balokType = -1;
		if (random < 50)
		{
			balokType = BALOK3;
			setFinalBaloktype(BALOK3); // Yellow Balok
		}
		else if (random < 75)
		{
			balokType = BALOK2;
			setFinalBaloktype(BALOK2); // Aqua Balok
		}
		else
		{
			balokType = BALOK1;
			setFinalBaloktype(BALOK1);
		}
		
		BOSS_SPAWNED.add(addSpawn(balokType, BALOK_LOC));
		setFinalBalokStatus(1);
		Broadcast.toAllOnlinePlayers(new SystemMessage(SystemMessageId.BALOK_IS_HERE));
		Broadcast.toAllOnlinePlayers(new BalrogWarBossInfo(_finalBalokType, _finalBalokStatus, _kesmaStatus, _praisStatus, _viraStatus, _hearakStatus, _heederStatus));
	}
	
	private void finalBoss()
	{
		if (((_stage == 4) && (getMidbossDefeatCount() == 5) && (_firstKillBalokId == KESMA)) || (_globalPoints > 1500000))
		{
			finalBossPlus();
		}
		else
		{
			spawnBalok();
		}
	}
	
	private void finalBossPlus()
	{
		_stage = 5;
		setFinalBaloktype(LORD_BALOK);
		setFinalBalokStatus(1);
		Broadcast.toAllOnlinePlayers(new BalrogWarHud(_status, _stage));
		Broadcast.toAllOnlinePlayers(new SystemMessage(SystemMessageId.LORD_BALOK_IS_HERE));
		addSpawn(LORD_BALOK, BALOK_LOC, false);
		Broadcast.toAllOnlinePlayers(new BalrogWarBossInfo(_finalBalokType, _finalBalokStatus, _kesmaStatus, _praisStatus, _viraStatus, _hearakStatus, _heederStatus));
	}
	
	private void rewardType()
	{
		int rewardId = -1;
		if (_stage < 4)
		{
			rewardId = 91641; // Sayha's Blessing, consolation item reward.
		}
		if ((_stage <= 5) && !_balokKilled)
		{
			rewardId = 91641; // Sayha's Blessing, consolation item reward
		}
		if ((_stage == 5) && _balokKilled)
		{
			rewardId = 97087; // Lord Balok's Treasure Chest
		}
		if ((_stage == 4) && _balokKilled)
		{
			rewardId = (Rnd.get(97075, 97086)); // Balok's Treasure Chest
		}
		BattleWithBalokManager.getInstance().setReward(rewardId);
	}
	
	private void lastHitRewardMonsters(Player player)
	{
		final int rnd = Rnd.get(100);
		int reward = 0;
		if (rnd < 5)
		{
			reward = 33809; // Improved Scroll: Enchant A-grade Weapon
			BALOK_BATTLE_ZONE.broadcastPacket(new ExShowScreenMessage(NpcStringId.S1_HAS_OBTAINED_SCROLL_ENCHANT_WEAPON, ExShowScreenMessage.BOTTOM_RIGHT, 10000, false, player.getName()));
		}
		else if (rnd < 15)
		{
			reward = 729; // Scroll: Enchant A-grade Weapon
			BALOK_BATTLE_ZONE.broadcastPacket(new ExShowScreenMessage(NpcStringId.S1_HAS_OBTAINED_SCROLL_ENCHANT_WEAPON, ExShowScreenMessage.BOTTOM_RIGHT, 10000, false, player.getName()));
		}
		else
		{
			reward = 730; // Scroll: Enchant A-grade Armor
			BALOK_BATTLE_ZONE.broadcastPacket(new ExShowScreenMessage(NpcStringId.S1_HAS_OBTAINED_SCROLL_ENCHANT_ARMOR, ExShowScreenMessage.BOTTOM_RIGHT, 10000, false, player.getName()));
		}
		player.addItem("Balok Last Hit Reward", reward, 1, player, true);
	}
	
	private void lastHitRewardBalok(Npc npc, Player player)
	{
		int reward = 0;
		if (FINAL_BOSSES.contains(npc.getId()))
		{
			reward = 33809; // Improved Scroll: Enchant A-grade Weapon
		}
		if (reward > 0)
		{
			player.addItem("Balok Last Hit Reward", reward, 1, player, true);
		}
		BALOK_BATTLE_ZONE.broadcastPacket(new ExShowScreenMessage(NpcStringId.S1_HAS_OBTAINED_SCROLL_ENCHANT_WEAPON, ExShowScreenMessage.BOTTOM_RIGHT, 10000, false, player.getName()));
	}
	
	private void setInProgress(boolean value)
	{
		_inProgress = value;
	}
	
	private void setBalokKilled(boolean value)
	{
		_balokKilled = value;
	}
	
	private void setFirstWaveKilled(boolean value)
	{
		_firstWaveKilled = value;
	}
	
	private void setSecondWaveKilled(boolean value)
	{
		_secondWaveKilled = value;
	}
	
	private void setMidBossFirstSpawn(boolean value)
	{
		_midBossFirstSpawn = value;
	}
	
	public boolean midBossSpawn()
	{
		return _midBossFirstSpawn;
	}
	
	private void setMidBossSecondSpawn(boolean value)
	{
		_midBossSecondSpawn = value;
	}
	
	public boolean midBossSecondSpawn()
	{
		return _midBossSecondSpawn;
	}
	
	private void setStatus(int value)
	{
		_status = value;
	}
	
	private void addGlobalPoints(int value)
	{
		_globalPoints += value;
	}
	
	private int getMidbossDefeatCount()
	{
		return _midbossDefeatCount;
	}
	
	private void addMidbossDefeatCount(int value)
	{
		_midbossDefeatCount += value;
		Broadcast.toAllOnlinePlayers(new BalrogWarBossInfo(_finalBalokType, _finalBalokStatus, _kesmaStatus, _praisStatus, _viraStatus, _hearakStatus, _heederStatus));
	}
	
	private void setFinalBaloktype(int value)
	{
		_finalBalokType = value;
	}
	
	private void setFinalBalokStatus(int value)
	{
		_finalBalokStatus = value;
	}
	
	@Override
	public String onKill(Npc npc, Player player, boolean isSummon)
	{
		if (NORMAL_MOBS.contains(npc.getId()))
		{
			BattleWithBalokManager.getInstance().addPointsForPlayer(player, npc.getId() == SCORPION);
			addGlobalPoints(npc.getId() == SCORPION ? Config.BALOK_POINTS_PER_MONSTER * 10 : Config.BALOK_POINTS_PER_MONSTER);
			BattleWithBalokManager.getInstance().setGlobalPoints(_globalPoints);
		}
		if (INTERMID_BOSSES.contains(npc.getId()) && (_firstKillBalokId == 0)) // to lord balok plus
		{
			_firstKillBalokId = npc.getId();
		}
		if (FINAL_BOSSES.contains(npc.getId()))
		{
			if (_stage == 5)
			{
				Broadcast.toAllOnlinePlayers(new SystemMessage(SystemMessageId.YOU_VE_WON_THE_BATTLE_WITH_LORD_BALOK));
			}
			else
			{
				Broadcast.toAllOnlinePlayers(new SystemMessage(SystemMessageId.YOU_VE_WON_THE_BATTLE_WITH_BALOK));
			}
			setFinalBalokStatus(2);
			setBalokKilled(true);
			lastHitRewardBalok(npc, player);
			finishAndReward();
		}
		if (npc.getId() == KESMA)
		{
			addMidbossDefeatCount(1);
			_kesmaStatus = 2;
			interBossesStatus();
			lastHitRewardMonsters(player);
		}
		if (npc.getId() == PRAIS)
		{
			addMidbossDefeatCount(1);
			_praisStatus = 2;
			interBossesStatus();
			lastHitRewardMonsters(player);
		}
		if (npc.getId() == VIRA)
		{
			addMidbossDefeatCount(1);
			_viraStatus = 2;
			interBossesStatus();
			lastHitRewardMonsters(player);
		}
		if (npc.getId() == HEARAK)
		{
			addMidbossDefeatCount(1);
			_hearakStatus = 2;
			interBossesStatus();
			lastHitRewardMonsters(player);
		}
		if (npc.getId() == HEEDER)
		{
			addMidbossDefeatCount(1);
			_heederStatus = 2;
			interBossesStatus();
			lastHitRewardMonsters(player);
		}
		if ((_stage == 1) && (_globalPoints >= 250000))
		{
			_stage = 2;
			BattleWithBalokManager.getInstance().setGlobalStage(_stage);
			Broadcast.toAllOnlinePlayers(new BalrogWarHud(_status, _stage));
			Broadcast.toAllOnlinePlayers(new BalrogWarBossInfo(_finalBalokType, _finalBalokStatus, _kesmaStatus, _praisStatus, _viraStatus, _hearakStatus, _heederStatus));
		}
		if ((_stage == 2) && (_globalPoints >= 320000) && !_firstWaveKilled && !_midBossFirstSpawn)
		{
			spawnInterBossesFirstWave();
		}
		if ((_stage == 2) && (_midbossDefeatCount == 3) && _firstWaveKilled)
		{
			_stage = 3;
			BOSS_SPAWNED.clear();
			BattleWithBalokManager.getInstance().setGlobalStage(_stage);
			Broadcast.toAllOnlinePlayers(new BalrogWarHud(_status, _stage));
			Broadcast.toAllOnlinePlayers(new BalrogWarBossInfo(_finalBalokType, _finalBalokStatus, _kesmaStatus, _praisStatus, _viraStatus, _hearakStatus, _heederStatus));
		}
		if ((_stage == 3) && (_globalPoints >= 800000) && !_secondWaveKilled && !_midBossSecondSpawn)
		{
			spawnInterBossesSecondWave();
			BattleWithBalokManager.getInstance().setGlobalStage(_stage);
			Broadcast.toAllOnlinePlayers(new BalrogWarHud(_status, _stage));
			Broadcast.toAllOnlinePlayers(new BalrogWarBossInfo(_finalBalokType, _finalBalokStatus, _kesmaStatus, _praisStatus, _viraStatus, _hearakStatus, _heederStatus));
		}
		if ((_stage == 3) && (_midbossDefeatCount == 5))
		{
			_stage = 4;
			BOSS_SPAWNED.clear();
			setSecondWaveKilled(true);
			BattleWithBalokManager.getInstance().setGlobalStage(_stage);
			finalBoss();
			Broadcast.toAllOnlinePlayers(new BalrogWarHud(_status, _stage));
			Broadcast.toAllOnlinePlayers(new BalrogWarBossInfo(_finalBalokType, _finalBalokStatus, _kesmaStatus, _praisStatus, _viraStatus, _hearakStatus, _heederStatus));
		}
		if ((_stage == 1) && (_globalPoints < 3000) && (getRandom(5000) < 10))
		{
			bypassRandomStage();
		}
		
		return super.onKill(npc, player, isSummon);
	}
	
	public void finishAndReward()
	{
		_status = 3;
		rewardType();
		GlobalVariablesManager.getInstance().set(GlobalVariablesManager.BALOK_REMAIN_TIME, System.currentTimeMillis() + REWARD_TIME);
		NORMAL_BATTLE_MOBS.get().getGroups().forEach(SpawnGroup::despawnAll);
		BOSS_SPAWNED.forEach(Npc::deleteMe);
		BOSS_SPAWNED.clear();
		Broadcast.toAllOnlinePlayers(new BalrogWarHud(_status, _stage));
		if (!_balokKilled && (_stage == 4))
		{
			_finalBalokStatus = 3;
			Broadcast.toAllOnlinePlayers(new SystemMessage(SystemMessageId.YOU_VE_LOST_THE_BATTLE_WITH_BALOK));
		}
		if (!_balokKilled && (_stage == 5))
		{
			_finalBalokStatus = 3;
			Broadcast.toAllOnlinePlayers(new SystemMessage(SystemMessageId.YOU_VE_LOST_THE_BATTLE_WITH_LORD_BALOK));
		}
		if (_kesmaStatus != 2)
		{
			_kesmaStatus = 3;
		}
		if (_praisStatus != 2)
		{
			_praisStatus = 3;
		}
		if (_viraStatus != 2)
		{
			_viraStatus = 3;
		}
		if ((_heederStatus != 2) && (_stage >= 3) && (_globalPoints >= 800000))
		{
			_heederStatus = 3;
		}
		if ((_hearakStatus != 2) && (_stage >= 3) && (_globalPoints >= 800000))
		{
			_hearakStatus = 3;
		}
		
		interBossesStatus();
		Broadcast.toAllOnlinePlayers(new BalrogWarBossInfo(_finalBalokType, _finalBalokStatus, _kesmaStatus, _praisStatus, _viraStatus, _hearakStatus, _heederStatus));
		for (Player player : World.getInstance().getPlayers())
		{
			if (BattleWithBalokManager.getInstance().getMonsterPoints(player) < 1000)
			{
				return;
			}
			
			player.getVariables().set(PlayerVariables.BALOK_AVAILABLE_REWARD, 1);
		}
		if (_rewardTask != null)
		{
			_rewardTask.cancel(true);
		}
		ThreadPool.schedule(this::topRankingRewardFinish, 2400000); // 40 minutes
	}
	
	public void sendRankingReward()
	{
		for (Entry<Integer, Integer> ranker : BattleWithBalokManager.getInstance().getTopPlayers(30).entrySet())
		{
			if (ranker == null)
			{
				return;
			}
			
			final int charId = ranker.getKey();
			final Message mail = new Message(charId, "Battle with Balok Ranker Special Reward", "A special Reward given to rankers who contributed greatly in the balok Battle.", MailType.BALOK_RANKING_REWARD);
			final Mail attachement = mail.createAttachments();
			attachement.addItem("Battle with Balok", 91690, 100, null, null); // Special HP Recovery Potion
			MailManager.getInstance().sendMessage(mail);
		}
	}
	
	private void topRankingRewardFinish()
	{
		sendRankingReward();
		GlobalVariablesManager.getInstance().remove(GlobalVariablesManager.BALOK_REMAIN_TIME);
	}
	
	@RegisterEvent(EventType.ON_PLAYER_LOGIN)
	@RegisterType(ListenerRegisterType.GLOBAL_PLAYERS)
	public void onPlayerLogin(OnPlayerLogin event)
	{
		final Player player = event.getPlayer();
		if ((player == null) || !_inProgress)
		{
			return;
		}
		
		player.sendPacket(new BalrogWarHud(_status, _stage));
		Broadcast.toAllOnlinePlayers(new BalrogWarBossInfo(_finalBalokType, _finalBalokStatus, _kesmaStatus, _praisStatus, _viraStatus, _hearakStatus, _heederStatus));
	}
	
	public static void main(String[] args)
	{
		new Balok();
	}
}
