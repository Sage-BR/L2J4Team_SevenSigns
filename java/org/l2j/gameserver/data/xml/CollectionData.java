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
package org.l2j.gameserver.data.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import org.l2j.commons.util.IXmlReader;
import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.holders.CollectionDataHolder;
import org.l2j.gameserver.model.holders.ItemEnchantHolder;
import org.l2j.gameserver.model.item.ItemTemplate;

/**
 * @author Berezkin Nikolay
 */
public class CollectionData implements IXmlReader
{
	private static final Logger LOGGER = Logger.getLogger(CollectionData.class.getName());
	
	private static final Map<Integer, CollectionDataHolder> _collections = new HashMap<>();
	private static final Map<Integer, List<CollectionDataHolder>> _collectionsByTabId = new HashMap<>();
	
	protected CollectionData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		_collections.clear();
		parseDatapackFile("data/CollectionData.xml");
		
		if (!_collections.isEmpty())
		{
			LOGGER.info(getClass().getSimpleName() + ": Loaded " + _collections.size() + " collections.");
		}
		else
		{
			LOGGER.info(getClass().getSimpleName() + ": System is disabled.");
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
					if ("collection".equalsIgnoreCase(d.getNodeName()))
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
						final int optionId = parseInteger(attrs, "optionId");
						final int category = parseInteger(attrs, "category");
						final int completeCount = parseInteger(attrs, "completeCount");
						final List<ItemEnchantHolder> items = new ArrayList<>();
						for (Node b = d.getFirstChild(); b != null; b = b.getNextSibling())
						{
							attrs = b.getAttributes();
							if ("item".equalsIgnoreCase(b.getNodeName()))
							{
								final int itemId = parseInteger(attrs, "id");
								final long itemCount = parseLong(attrs, "count", 1L);
								final int itemEnchantLevel = parseInteger(attrs, "enchantLevel", 0);
								final ItemTemplate item = ItemData.getInstance().getTemplate(itemId);
								if (item == null)
								{
									LOGGER.severe(getClass().getSimpleName() + ": Item template null for itemId: " + itemId + " collection item: " + id);
									continue;
								}
								items.add(new ItemEnchantHolder(itemId, itemCount, itemEnchantLevel));
							}
						}
						
						final CollectionDataHolder template = new CollectionDataHolder(id, optionId, category, completeCount, items);
						_collections.put(id, template);
						_collectionsByTabId.computeIfAbsent(template.getCategory(), list -> new ArrayList<>()).add(template);
					}
				}
			}
		}
	}
	
	public CollectionDataHolder getCollection(int id)
	{
		return _collections.get(id);
	}
	
	public List<CollectionDataHolder> getCollectionsByTabId(int tabId)
	{
		if (_collectionsByTabId.containsKey(tabId))
		{
			return _collectionsByTabId.get(tabId);
		}
		return Collections.emptyList();
	}
	
	public Collection<CollectionDataHolder> getCollections()
	{
		return _collections.values();
	}
	
	public static CollectionData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final CollectionData INSTANCE = new CollectionData();
	}
}
