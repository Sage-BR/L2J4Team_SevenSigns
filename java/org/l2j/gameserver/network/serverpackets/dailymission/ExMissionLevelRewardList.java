package org.l2j.gameserver.network.serverpackets.dailymission;

import java.util.List;

import org.l2j.gameserver.data.xml.MissionLevel;
import org.l2j.gameserver.model.MissionLevelHolder;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.MissionLevelPlayerDataHolder;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Index
 */
public class ExMissionLevelRewardList extends ServerPacket
{
	private final String _currentSeason = String.valueOf(MissionLevel.getInstance().getCurrentSeason());
	private final MissionLevelHolder _holder = MissionLevel.getInstance().getMissionBySeason(MissionLevel.getInstance().getCurrentSeason());
	
	private final Player _player;
	private final int _maxNormalLevel;
	
	private List<Integer> _collectedNormalRewards;
	private List<Integer> _collectedKeyRewards;
	private List<Integer> _collectedBonusRewards;
	
	public ExMissionLevelRewardList(Player player)
	{
		_player = player;
		// After normal rewards there will be bonus.
		_maxNormalLevel = _holder.getBonusLevel();
	}
	
	@Override
	public void write()
	{
		final MissionLevelPlayerDataHolder info = _player.getMissionLevelProgress();
		_collectedNormalRewards = info.getCollectedNormalRewards();
		_collectedKeyRewards = info.getCollectedKeyRewards();
		_collectedBonusRewards = info.getListOfCollectedBonusRewards();
		
		ServerPackets.EX_MISSION_LEVEL_REWARD_LIST.writeId(this);
		if (info.getCurrentLevel() == 0)
		{
			writeInt(1); // 0 -> does not work, -1 -> game crushed
			writeInt(3); // Type
			writeInt(-1); // Level
			writeInt(0); // State
		}
		else
		{
			sendAvailableRewardsList(info);
		}
		writeInt(info.getCurrentLevel()); // Level
		writeInt(getPercent(info)); // PointPercent
		String year = _currentSeason.substring(0, 4);
		writeInt(Integer.parseInt(year)); // SeasonYear
		String month = _currentSeason.substring(4, 6);
		writeInt(Integer.parseInt(month)); // SeasonMonth
		writeInt(getAvailableRewards(info)); // TotalRewardsAvailable
		if (_holder.getBonusRewardIsAvailable() && _holder.getBonusRewardByLevelUp())
		{
			boolean check = false;
			for (int level = _maxNormalLevel; level <= _holder.getMaxLevel(); level++)
			{
				if ((level <= info.getCurrentLevel()) && !_collectedBonusRewards.contains(level))
				{
					check = true;
					break;
				}
			}
			writeInt(check); // ExtraRewardsAvailable
		}
		else
		{
			if (_holder.getBonusRewardIsAvailable() && info.getCollectedSpecialReward() && !info.getCollectedBonusReward())
			{
				writeInt(1); // ExtraRewardsAvailable
			}
			else
			{
				writeInt(0); // ExtraRewardsAvailable
			}
		}
		writeInt(0); // RemainSeasonTime / does not work? / not used?
	}
	
	private int getAvailableRewards(MissionLevelPlayerDataHolder info)
	{
		int availableRewards = 0;
		for (int level : _holder.getNormalRewards().keySet())
		{
			if ((level <= info.getCurrentLevel()) && !_collectedNormalRewards.contains(level))
			{
				availableRewards++;
			}
		}
		for (int level : _holder.getKeyRewards().keySet())
		{
			if ((level <= info.getCurrentLevel()) && !_collectedKeyRewards.contains(level))
			{
				availableRewards++;
			}
		}
		if (_holder.getBonusRewardIsAvailable() && _holder.getBonusRewardByLevelUp() && info.getCollectedSpecialReward())
		{
			final List<Integer> collectedBonusRewards = info.getListOfCollectedBonusRewards();
			for (int level = _maxNormalLevel; level <= _holder.getMaxLevel(); level++)
			{
				if ((level <= info.getCurrentLevel()) && !collectedBonusRewards.contains(level))
				{
					availableRewards++;
					break;
				}
			}
		}
		else if (_holder.getBonusRewardIsAvailable() && _holder.getBonusRewardByLevelUp() && (info.getCurrentLevel() >= _maxNormalLevel))
		{
			availableRewards++;
		}
		else if (_holder.getBonusRewardIsAvailable() && (info.getCurrentLevel() >= _holder.getMaxLevel()) && !info.getCollectedBonusReward() && info.getCollectedSpecialReward())
		{
			availableRewards++;
		}
		else if ((info.getCurrentLevel() >= _holder.getMaxLevel()) && !info.getCollectedBonusReward())
		{
			availableRewards++;
		}
		return availableRewards;
	}
	
	private int getTotalRewards(MissionLevelPlayerDataHolder info)
	{
		int totalRewards = 0;
		for (int level : _holder.getNormalRewards().keySet())
		{
			if (level <= info.getCurrentLevel())
			{
				totalRewards++;
			}
		}
		for (int level : _holder.getKeyRewards().keySet())
		{
			if (level <= info.getCurrentLevel())
			{
				totalRewards++;
			}
		}
		if (_holder.getBonusRewardByLevelUp() && info.getCollectedSpecialReward() && _holder.getBonusRewardIsAvailable() && (_maxNormalLevel <= info.getCurrentLevel()))
		{
			for (int level = _maxNormalLevel; level <= _holder.getMaxLevel(); level++)
			{
				if (level <= info.getCurrentLevel())
				{
					totalRewards++;
					break;
				}
			}
		}
		else if (info.getCollectedSpecialReward() && _holder.getBonusRewardIsAvailable() && (_maxNormalLevel <= info.getCurrentLevel()))
		{
			totalRewards++;
		}
		else if (_maxNormalLevel <= info.getCurrentLevel())
		{
			totalRewards++;
		}
		return totalRewards;
	}
	
	private int getPercent(MissionLevelPlayerDataHolder info)
	{
		if (info.getCurrentLevel() >= _holder.getMaxLevel())
		{
			return 100;
		}
		return (int) Math.floor(((double) info.getCurrentEXP() / (double) _holder.getXPForSpecifiedLevel(info.getCurrentLevel())) * 100.0);
	}
	
	private void sendAvailableRewardsList(MissionLevelPlayerDataHolder info)
	{
		writeInt(getTotalRewards(info)); // PkMissionLevelReward
		for (int level : _holder.getNormalRewards().keySet())
		{
			if (level <= info.getCurrentLevel())
			{
				writeInt(1); // Type
				writeInt(level); // Level
				writeInt(_collectedNormalRewards.contains(level) ? 2 : 1); // State
			}
		}
		for (int level : _holder.getKeyRewards().keySet())
		{
			if (level <= info.getCurrentLevel())
			{
				writeInt(2); // Type
				writeInt(level); // Level
				writeInt(_collectedKeyRewards.contains(level) ? 2 : 1); // State
			}
		}
		if (_holder.getBonusRewardByLevelUp() && info.getCollectedSpecialReward() && _holder.getBonusRewardIsAvailable() && (_maxNormalLevel <= info.getCurrentLevel()))
		{
			writeInt(3); // Type
			int sendLevel = 0;
			for (int level = _maxNormalLevel; level <= _holder.getMaxLevel(); level++)
			{
				if ((level <= info.getCurrentLevel()) && !_collectedBonusRewards.contains(level))
				{
					sendLevel = level;
					break;
				}
			}
			writeInt(sendLevel == 0 ? _holder.getMaxLevel() : sendLevel); // Level
			writeInt(2); // State
		}
		else if (info.getCollectedSpecialReward() && _holder.getBonusRewardIsAvailable() && (_maxNormalLevel <= info.getCurrentLevel()))
		{
			writeInt(3); // Type
			writeInt(_holder.getMaxLevel()); // Level
			writeInt(2); // State
		}
		else if (_maxNormalLevel <= info.getCurrentLevel())
		{
			writeInt(3); // Type
			writeInt(_holder.getMaxLevel()); // Level
			writeInt(!info.getCollectedSpecialReward()); // State
		}
	}
}
