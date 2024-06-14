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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.l2jmobius.commons.util.IXmlReader;
import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.holders.MagicLampDataHolder;

/**
 * @author Serenitty
 */
public class MagicLampData implements IXmlReader
{
	private static final Logger LOGGER = Logger.getLogger(MagicLampData.class.getName());
	private static final List<MagicLampDataHolder> LAMPS = new ArrayList<>();
	
	protected MagicLampData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		LAMPS.clear();
		parseDatapackFile("data/MagicLampData.xml");
		LOGGER.info("MagicLampData: Loaded " + LAMPS.size() + " magic lamps exp types.");
	}
	
	@Override
	public void parseDocument(Document doc, File f)
	{
		final NodeList list = doc.getFirstChild().getChildNodes();
		for (int i = 0; i < list.getLength(); i++)
		{
			final Node n = list.item(i);
			if ("levelRange".equalsIgnoreCase(n.getNodeName()))
			{
				final int minLevel = parseInteger(n.getAttributes(), "fromLevel");
				final int maxLevel = parseInteger(n.getAttributes(), "toLevel");
				final NodeList lamps = n.getChildNodes();
				for (int j = 0; j < lamps.getLength(); j++)
				{
					final Node d = lamps.item(j);
					if ("lamp".equalsIgnoreCase(d.getNodeName()))
					{
						final NamedNodeMap attrs = d.getAttributes();
						final StatSet set = new StatSet();
						set.set("type", parseString(attrs, "type"));
						set.set("exp", parseInteger(attrs, "exp"));
						set.set("sp", parseInteger(attrs, "sp"));
						set.set("chance", parseInteger(attrs, "chance"));
						set.set("minLevel", minLevel);
						set.set("maxLevel", maxLevel);
						LAMPS.add(new MagicLampDataHolder(set));
					}
				}
			}
		}
	}
	
	public List<MagicLampDataHolder> getLamps()
	{
		return LAMPS;
	}
	
	public static MagicLampData getInstance()
	{
		return Singleton.INSTANCE;
	}
	
	private static class Singleton
	{
		protected static final MagicLampData INSTANCE = new MagicLampData();
	}
}
