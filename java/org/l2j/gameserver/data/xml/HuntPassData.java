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
package org.l2j.gameserver.data.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.w3c.dom.Document;

import org.l2j.Config;
import org.l2j.commons.util.IXmlReader;
import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.holders.ItemHolder;

/**
 * @author Serenitty
 */
public class HuntPassData implements IXmlReader
{
	private static final Logger LOGGER = Logger.getLogger(HuntPassData.class.getName());
	private final List<ItemHolder> _rewards = new ArrayList<>();
	private final List<ItemHolder> _premiumRewards = new ArrayList<>();
	private int _rewardCount = 0;
	private int _premiumRewardCount = 0;
	
	protected HuntPassData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		if (Config.ENABLE_HUNT_PASS)
		{
			_rewards.clear();
			parseDatapackFile("data/HuntPass.xml");
			_rewardCount = _rewards.size();
			_premiumRewardCount = _premiumRewards.size();
			LOGGER.info(getClass().getSimpleName() + ": Loaded " + _rewardCount + " HuntPass rewards.");
		}
		else
		{
			LOGGER.info(getClass().getSimpleName() + ": Disabled.");
		}
	}
	
	@Override
	public void parseDocument(Document doc, File f)
	{
		forEach(doc, "list", listNode -> forEach(listNode, "item", rewardNode ->
		{
			final StatSet set = new StatSet(parseAttributes(rewardNode));
			final int itemId = set.getInt("id");
			final int itemCount = set.getInt("count");
			final int premiumitemId = set.getInt("premiumId");
			final int premiumitemCount = set.getInt("premiumCount");
			if (ItemData.getInstance().getTemplate(itemId) == null)
			{
				LOGGER.info(getClass().getSimpleName() + ": Item with id " + itemId + " does not exist.");
			}
			else
			{
				_rewards.add(new ItemHolder(itemId, itemCount));
				_premiumRewards.add(new ItemHolder(premiumitemId, premiumitemCount));
			}
		}));
	}
	
	public List<ItemHolder> getRewards()
	{
		return _rewards;
	}
	
	public int getRewardsCount()
	{
		return _rewardCount;
	}
	
	public List<ItemHolder> getPremiumRewards()
	{
		return _premiumRewards;
	}
	
	public int getPremiumRewardsCount()
	{
		return _premiumRewardCount;
	}
	
	public static HuntPassData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final HuntPassData INSTANCE = new HuntPassData();
	}
}
