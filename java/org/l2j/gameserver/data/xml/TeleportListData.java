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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.w3c.dom.Document;

import org.l2j.commons.util.IXmlReader;
import org.l2j.gameserver.model.Location;
import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.holders.TeleportListHolder;

/**
 * @author NviX, Mobius
 */
public class TeleportListData implements IXmlReader
{
	private static final Logger LOGGER = Logger.getLogger(TeleportListData.class.getName());
	private final Map<Integer, TeleportListHolder> _teleports = new HashMap<>();
	private int _teleportCount = 0;
	
	protected TeleportListData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		_teleports.clear();
		parseDatapackFile("data/TeleportListData.xml");
		_teleportCount = _teleports.size();
		LOGGER.info(getClass().getSimpleName() + ": Loaded " + _teleportCount + " teleports.");
	}
	
	@Override
	public void parseDocument(Document doc, File f)
	{
		forEach(doc, "list", listNode -> forEach(listNode, "teleport", teleportNode ->
		{
			final StatSet set = new StatSet(parseAttributes(teleportNode));
			final int tpId = set.getInt("id");
			final int tpPrice = set.getInt("price");
			final boolean special = set.getBoolean("special", false);
			final List<Location> locations = new ArrayList<>();
			forEach(teleportNode, "location", locationsNode ->
			{
				final StatSet locationSet = new StatSet(parseAttributes(locationsNode));
				locations.add(new Location(locationSet.getInt("x"), locationSet.getInt("y"), locationSet.getInt("z")));
			});
			if (locations.isEmpty())
			{
				locations.add(new Location(set.getInt("x"), set.getInt("y"), set.getInt("z")));
			}
			_teleports.put(tpId, new TeleportListHolder(tpId, locations, tpPrice, special));
		}));
	}
	
	public TeleportListHolder getTeleport(int teleportId)
	{
		return _teleports.get(teleportId);
	}
	
	public int getTeleportCount()
	{
		return _teleportCount;
	}
	
	public static TeleportListData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final TeleportListData INSTANCE = new TeleportListData();
	}
}