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

import org.l2jmobius.commons.util.IXmlReader;
import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.holders.EnchantItemExpHolder;
import org.l2jmobius.gameserver.model.holders.EnchantStarHolder;
import org.l2jmobius.gameserver.model.holders.SkillEnchantHolder;
import org.l2jmobius.gameserver.model.skill.Skill;

/**
 * @author Serenitty
 */
public class SkillEnchantData implements IXmlReader
{
	private static final Logger LOGGER = Logger.getLogger(SkillEnchantData.class.getName());
	
	private final Map<Integer, EnchantStarHolder> _enchantStarMap = new HashMap<>();
	private final Map<Integer, SkillEnchantHolder> _skillEnchantMap = new HashMap<>();
	private final Map<Integer, Map<Integer, EnchantItemExpHolder>> _enchantItemMap = new HashMap<>();
	
	private final Map<Integer, Integer> _chanceEnchantMap = new HashMap<>();
	
	public SkillEnchantData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		parseDatapackFile("data/SkillEnchantData.xml");
		LOGGER.info(getClass().getSimpleName() + ": Loaded " + _enchantStarMap.size() + " star levels.");
		LOGGER.info(getClass().getSimpleName() + ": Loaded " + _enchantItemMap.size() + " enchant items.");
		LOGGER.info(getClass().getSimpleName() + ": Loaded " + _skillEnchantMap.size() + " skill enchants.");
	}
	
	@Override
	public void parseDocument(Document doc, File f)
	{
		forEach(doc, "list", listNode ->
		{
			forEach(listNode, "skills", skills -> forEach(skills, "skill", skill ->
			{
				final StatSet set = new StatSet(parseAttributes(skill));
				final int id = set.getInt("id");
				_skillEnchantMap.put(id, new SkillEnchantHolder(set));
			}));
			forEach(listNode, "stars", stars -> forEach(stars, "star", star ->
			{
				final StatSet set = new StatSet(parseAttributes(star));
				final int level = set.getInt("level");
				final EnchantStarHolder starHolder = new EnchantStarHolder(set);
				_enchantStarMap.put(level, starHolder);
			}));
			forEach(listNode, "chances", itemsPoints -> forEach(itemsPoints, "chance", item ->
			{
				final StatSet set = new StatSet(parseAttributes(item));
				final int enchantLevel = set.getInt("enchantLevel");
				final int chance = set.getInt("chance");
				_chanceEnchantMap.put(enchantLevel, chance);
			}));
			forEach(listNode, "itemsPoints", itemsPoints -> forEach(itemsPoints, "star", star ->
			{
				final StatSet set = new StatSet(parseAttributes(star));
				final int level = set.getInt("level");
				final Map<Integer, EnchantItemExpHolder> itemMap = new HashMap<>();
				forEach(star, "item", item ->
				{
					final StatSet statSet = new StatSet(parseAttributes(item));
					final int id = statSet.getInt("id");
					itemMap.put(id, new EnchantItemExpHolder(statSet));
				});
				_enchantItemMap.put(level, itemMap);
			}));
		});
	}
	
	public EnchantStarHolder getEnchantStar(int level)
	{
		return _enchantStarMap.get(level);
	}
	
	public SkillEnchantHolder getSkillEnchant(int id)
	{
		return _skillEnchantMap.get(id);
	}
	
	public EnchantItemExpHolder getEnchantItem(int level, int id)
	{
		return _enchantItemMap.get(level).get(id);
	}
	
	public Map<Integer, EnchantItemExpHolder> getEnchantItem(int level)
	{
		return _enchantItemMap.get(level);
	}
	
	public int getChanceEnchantMap(Skill skill)
	{
		final int enchantLevel = skill.getSubLevel() == 0 ? 1 : (skill.getSubLevel() + 1) - 1000;
		if (enchantLevel > getSkillEnchant(skill.getId()).getMaxEnchantLevel())
		{
			return 0;
		}
		
		return _chanceEnchantMap.get(enchantLevel);
	}
	
	public static SkillEnchantData getInstance()
	{
		return SkillEnchantData.SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final SkillEnchantData INSTANCE = new SkillEnchantData();
	}
}
