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
import java.util.List;
import java.util.logging.Logger;

import org.w3c.dom.Document;

import org.l2jmobius.commons.util.IXmlReader;
import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.item.combination.CombinationItemType;
import org.l2jmobius.gameserver.model.item.henna.CombinationHenna;
import org.l2jmobius.gameserver.model.item.henna.CombinationHennaReward;

/**
 * @author Index
 */
public class HennaCombinationData implements IXmlReader
{
	private static final Logger LOGGER = Logger.getLogger(HennaCombinationData.class.getName());
	
	private final List<CombinationHenna> _henna = new ArrayList<>();
	
	protected HennaCombinationData()
	{
		load();
	}
	
	@Override
	public synchronized void load()
	{
		_henna.clear();
		parseDatapackFile("data/stats/hennaCombinations.xml");
		LOGGER.info(getClass().getSimpleName() + ": Loaded " + _henna.size() + " henna combinations.");
	}
	
	@Override
	public void parseDocument(Document doc, File f)
	{
		forEach(doc, "list", listNode ->
		{
			forEach(listNode, "henna", hennaNode ->
			{
				final CombinationHenna henna = new CombinationHenna(new StatSet(parseAttributes(hennaNode)));
				forEach(hennaNode, "reward", rewardNode ->
				{
					final int hennaId = parseInteger(rewardNode.getAttributes(), "dyeId");
					final int id = parseInteger(rewardNode.getAttributes(), "id", -1);
					final int count = parseInteger(rewardNode.getAttributes(), "count", 0);
					final CombinationItemType type = parseEnum(rewardNode.getAttributes(), CombinationItemType.class, "type");
					henna.addReward(new CombinationHennaReward(hennaId, id, count, type));
					if ((id != -1) && (ItemData.getInstance().getTemplate(id) == null))
					{
						LOGGER.info(getClass().getSimpleName() + ": Could not find item with id " + id);
					}
					if ((hennaId != 0) && (HennaData.getInstance().getHenna(hennaId) == null))
					{
						LOGGER.info(getClass().getSimpleName() + ": Could not find henna with id " + id);
					}
				});
				_henna.add(henna);
			});
		});
	}
	
	public List<CombinationHenna> getHenna()
	{
		return _henna;
	}
	
	public CombinationHenna getByHenna(int hennaId)
	{
		for (CombinationHenna henna : _henna)
		{
			if (henna.getHenna() == hennaId)
			{
				return henna;
			}
		}
		return null;
	}
	
	public static final HennaCombinationData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final HennaCombinationData INSTANCE = new HennaCombinationData();
	}
}