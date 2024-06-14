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
package org.l2jmobius.gameserver.data.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.w3c.dom.Document;

import org.l2jmobius.commons.util.IXmlReader;
import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.holders.ItemHolder;
import org.l2jmobius.gameserver.model.quest.newquestdata.NewQuest;

/**
 * @author Magik
 */
public class NewQuestData implements IXmlReader
{
	private static final Logger LOGGER = Logger.getLogger(NewQuestData.class.getName());
	
	private final Map<Integer, NewQuest> _newQuestData = new LinkedHashMap<>();
	
	protected NewQuestData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		_newQuestData.clear();
		parseDatapackFile("data/NewQuestData.xml");
		
		LOGGER.info(getClass().getSimpleName() + ": Loaded " + _newQuestData.size() + " new quest data.");
	}
	
	@Override
	public void parseDocument(Document doc, File f)
	{
		forEach(doc, "list", listNode -> forEach(listNode, "quest", questNode ->
		{
			final StatSet set = new StatSet(parseAttributes(questNode));
			forEach(questNode, "locations", locationsNode ->
			{
				forEach(locationsNode, "param", paramNode -> set.set(parseString(paramNode.getAttributes(), "name"), paramNode.getTextContent()));
			});
			
			forEach(questNode, "conditions", conditionsNode ->
			{
				forEach(conditionsNode, "param", paramNode -> set.set(parseString(paramNode.getAttributes(), "name"), paramNode.getTextContent()));
			});
			
			forEach(questNode, "rewards", rewardsNode ->
			{
				
				final List<ItemHolder> rewardItems = new ArrayList<>();
				forEach(rewardsNode, "items", itemsNode -> forEach(itemsNode, "item", itemNode ->
				{
					final int itemId = parseInteger(itemNode.getAttributes(), "id");
					final int itemCount = parseInteger(itemNode.getAttributes(), "count");
					final ItemHolder rewardItem = new ItemHolder(itemId, itemCount);
					rewardItems.add(rewardItem);
				}));
				
				set.set("rewardItems", rewardItems);
				forEach(rewardsNode, "param", paramNode -> set.set(parseString(paramNode.getAttributes(), "name"), paramNode.getTextContent()));
			});
			
			forEach(questNode, "goals", goalsNode ->
			{
				forEach(goalsNode, "param", paramNode -> set.set(parseString(paramNode.getAttributes(), "name"), paramNode.getTextContent()));
			});
			
			final NewQuest holder = new NewQuest(set);
			_newQuestData.put(holder.getId(), holder);
		}));
	}
	
	public NewQuest getQuestById(int id)
	{
		return _newQuestData.get(id);
	}
	
	public Collection<NewQuest> getQuests()
	{
		return _newQuestData.values();
	}
	
	/**
	 * Gets the single instance of NewQuestData.
	 * @return single instance of NewQuestData
	 */
	public static NewQuestData getInstance()
	{
		return NewQuestData.SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final NewQuestData INSTANCE = new NewQuestData();
	}
}
