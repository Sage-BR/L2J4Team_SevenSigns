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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.w3c.dom.Document;

import org.l2j.commons.util.IXmlReader;
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
		for (Entry<Integer, String> entry : _names.entrySet())
		{
			if (name.endsWith(entry.getValue()))
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
		return _names.entrySet().stream().filter(e -> e.getKey() > 100).findAny().get().getValue();
	}
	
	public Entry<Integer, SkillHolder> getRandomSkill()
	{
		return _skills.entrySet().stream().filter(e -> e.getValue().getSkillId() > 0).findAny().get();
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
