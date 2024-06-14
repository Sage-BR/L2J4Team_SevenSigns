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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import org.l2jmobius.commons.util.IXmlReader;
import org.l2jmobius.gameserver.model.holders.ItemHolder;
import org.l2jmobius.gameserver.model.holders.PetExtractionHolder;

/**
 * @author Geremy
 */
public class PetExtractData implements IXmlReader
{
	private static final Logger LOGGER = Logger.getLogger(PetExtractData.class.getName());
	// <Pet_Id, <Pet_Level, Cost>>
	private final Map<Integer, Map<Integer, PetExtractionHolder>> _extractionData = new HashMap<>();
	
	protected PetExtractData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		_extractionData.clear();
		parseDatapackFile("data/PetExtractData.xml");
		LOGGER.info(getClass().getSimpleName() + ": Loaded " + _extractionData.size() + " pet extraction data.");
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
					if ("extraction".equalsIgnoreCase(d.getNodeName()))
					{
						final NamedNodeMap attrs = d.getAttributes();
						final int petId = parseInteger(attrs, "petId");
						final int petLevel = parseInteger(attrs, "petLevel");
						final long extractExp = parseLong(attrs, "extractExp");
						final int extractItem = parseInteger(attrs, "extractItem");
						final int defaultCostId = parseInteger(attrs, "defaultCostId");
						final int defaultCostCount = parseInteger(attrs, "defaultCostCount");
						final int extractCostId = parseInteger(attrs, "extractCostId");
						final int extractCostCount = parseInteger(attrs, "extractCostCount");
						Map<Integer, PetExtractionHolder> data = _extractionData.get(petId);
						if (data == null)
						{
							data = new HashMap<>();
							_extractionData.put(petId, data);
						}
						data.put(petLevel, new PetExtractionHolder(petId, petLevel, extractExp, extractItem, new ItemHolder(defaultCostId, defaultCostCount), new ItemHolder(extractCostId, extractCostCount)));
					}
				}
			}
		}
	}
	
	public PetExtractionHolder getExtraction(int petId, int petLevel)
	{
		final Map<Integer, PetExtractionHolder> map = _extractionData.get(petId);
		if (map == null)
		{
			LOGGER.warning(getClass().getSimpleName() + ": Missing pet extraction data: [PetId: " + petId + "] [PetLevel: " + petLevel + "]");
			return null;
		}
		return map.get(petLevel);
	}
	
	public static PetExtractData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final PetExtractData INSTANCE = new PetExtractData();
	}
}
