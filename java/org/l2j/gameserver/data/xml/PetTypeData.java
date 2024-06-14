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
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.w3c.dom.Document;

import org.l2j.commons.util.IXmlReader;
import org.l2j.commons.util.Rnd;
import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.holders.SkillHolder;

/**
 * @author Mobius
 */
public class PetTypeData implements IXmlReader
{
	private static final Logger LOGGER = Logger.getLogger(PetTypeData.class.getName());
	
	private final Map<Integer, SkillHolder> _skills = new HashMap<>();
	private final Map<Integer, String> _names = new HashMap<>();
	
	protected PetTypeData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		_skills.clear();
		parseDatapackFile("data/PetTypes.xml");
		LOGGER.info(getClass().getSimpleName() + ": Loaded " + _skills.size() + " pet types.");
	}
	
	@Override
	public void parseDocument(Document doc, File f)
	{
		forEach(doc, "list", listNode -> forEach(listNode, "pet", petNode ->
		{
			final StatSet set = new StatSet(parseAttributes(petNode));
			final int id = set.getInt("id");
			_skills.put(id, new SkillHolder(set.getInt("skillId", 0), set.getInt("skillLvl", 0)));
			_names.put(id, set.getString("name"));
		}));
	}
	
	public SkillHolder getSkillByName(String name)
	{
		for (Entry<Integer, String> entry : _names.entrySet())
		{
			if (name.startsWith(entry.getValue()))
			{
				return _skills.get(entry.getKey());
			}
		}
		return null;
	}
	
	public int getIdByName(String name)
	{
		if (name == null)
		{
			return 0;
		}
		
		final int spaceIndex = name.indexOf(' ');
		final String searchName;
		if (spaceIndex != -1)
		{
			searchName = name.substring(spaceIndex + 1);
		}
		else
		{
			searchName = name;
		}
		
		for (Entry<Integer, String> entry : _names.entrySet())
		{
			if (searchName.endsWith(entry.getValue()))
			{
				return entry.getKey();
			}
		}
		
		return 0;
	}
	
	public String getNamePrefix(Integer id)
	{
		return _names.get(id);
	}
	
	public String getRandomName()
	{
		String result = null;
		final List<Entry<Integer, String>> entryList = new ArrayList<>(_names.entrySet());
		while (result == null)
		{
			final Entry<Integer, String> temp = entryList.get(Rnd.get(entryList.size()));
			if (temp.getKey() > 100)
			{
				result = temp.getValue();
			}
		}
		return result;
	}
	
	public Entry<Integer, SkillHolder> getRandomSkill()
	{
		Entry<Integer, SkillHolder> result = null;
		final List<Entry<Integer, SkillHolder>> entryList = new ArrayList<>(_skills.entrySet());
		while (result == null)
		{
			final Entry<Integer, SkillHolder> temp = entryList.get(Rnd.get(entryList.size()));
			if (temp.getValue().getSkillId() > 0)
			{
				result = temp;
			}
		}
		return result;
	}
	
	public static PetTypeData getInstance()
	{
		return PetTypeData.SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final PetTypeData INSTANCE = new PetTypeData();
	}
}
