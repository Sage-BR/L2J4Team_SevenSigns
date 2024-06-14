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
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import org.l2jmobius.commons.util.IXmlReader;
import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.item.ItemTemplate;

/**
 * @author MacuK
 */
public class RaidDropAnnounceData implements IXmlReader
{
	private static final Logger LOGGER = Logger.getLogger(RaidDropAnnounceData.class.getName());
	
	private final Set<Integer> _itemIds = new HashSet<>();
	
	protected RaidDropAnnounceData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		_itemIds.clear();
		parseDatapackFile("data/RaidDropAnnounceData.xml");
		if (!_itemIds.isEmpty())
		{
			LOGGER.info(getClass().getSimpleName() + ": Loaded " + _itemIds.size() + " raid drop announce data.");
		}
	}
	
	@Override
	public void parseDocument(Document doc, File f)
	{
		for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
		{
			if ("list".equalsIgnoreCase(n.getNodeName()))
			{
				for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
				{
					if ("item".equalsIgnoreCase(d.getNodeName()))
					{
						NamedNodeMap attrs = d.getAttributes();
						Node att;
						final StatSet set = new StatSet();
						for (int i = 0; i < attrs.getLength(); i++)
						{
							att = attrs.item(i);
							set.set(att.getNodeName(), att.getNodeValue());
						}
						
						final int id = parseInteger(attrs, "id");
						final ItemTemplate item = ItemData.getInstance().getTemplate(id);
						if (item != null)
						{
							_itemIds.add(id);
						}
						else
						{
							LOGGER.warning(getClass().getSimpleName() + ": Could not find item with id: " + id);
						}
					}
				}
			}
		}
	}
	
	public boolean isAnnounce(int itemId)
	{
		return _itemIds.contains(itemId);
	}
	
	public static RaidDropAnnounceData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final RaidDropAnnounceData INSTANCE = new RaidDropAnnounceData();
	}
}
