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
package org.l2j.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import org.l2j.Config;

import org.l2j.gameserver.enums.AcquireSkillType;
import org.l2j.gameserver.model.SkillLearn;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.ItemHolder;
import org.l2j.gameserver.model.skill.CommonSkill;
import org.l2j.gameserver.network.ServerPackets;

/**
 * Acquire Skill Info server packet implementation.
 * @author Zoey76
 */
public class AcquireSkillInfo extends ServerPacket
{
	private final Player _player;
	private final AcquireSkillType _type;
	private final int _id;
	private final int _level;
	private final long _spCost;
	private final List<Req> _reqs;
	
	/**
	 * Private class containing learning skill requisites.
	 */
	private static class Req
	{
		public int itemId;
		public long count;
		public int type;
		public int unk;
		
		/**
		 * @param pType TODO identify.
		 * @param pItemId the item Id.
		 * @param itemCount the item count.
		 * @param pUnk TODO identify.
		 */
		public Req(int pType, int pItemId, long itemCount, int pUnk)
		{
			itemId = pItemId;
			type = pType;
			count = itemCount;
			unk = pUnk;
		}
	}
	
	/**
	 * Constructor for the acquire skill info object.
	 * @param player
	 * @param skillType the skill learning type.
	 * @param skillLearn the skill learn.
	 */
	public AcquireSkillInfo(Player player, AcquireSkillType skillType, SkillLearn skillLearn)
	{
		_player = player;
		_id = skillLearn.getSkillId();
		_level = skillLearn.getSkillLevel();
		_spCost = skillLearn.getLevelUpSp();
		_type = skillType;
		_reqs = new ArrayList<>();
		if ((skillType != AcquireSkillType.PLEDGE) || Config.LIFE_CRYSTAL_NEEDED)
		{
			for (List<ItemHolder> item : skillLearn.getRequiredItems())
			{
				if (!Config.DIVINE_SP_BOOK_NEEDED && (_id == CommonSkill.DIVINE_INSPIRATION.getId()))
				{
					continue;
				}
				_reqs.add(new Req(99, item.get(0).getId(), item.get(0).getCount(), 50));
			}
		}
	}
	
	/**
	 * Special constructor for Alternate Skill Learning system.<br>
	 * Sets a custom amount of SP.
	 * @param player
	 * @param skillType the skill learning type.
	 * @param skillLearn the skill learn.
	 * @param sp the custom SP amount.
	 */
	public AcquireSkillInfo(Player player, AcquireSkillType skillType, SkillLearn skillLearn, int sp)
	{
		_player = player;
		_id = skillLearn.getSkillId();
		_level = skillLearn.getSkillLevel();
		_spCost = sp;
		_type = skillType;
		_reqs = new ArrayList<>();
		for (List<ItemHolder> item : skillLearn.getRequiredItems())
		{
			_reqs.add(new Req(99, item.get(0).getId(), item.get(0).getCount(), 50));
		}
	}
	
	@Override
	public void write()
	{
		ServerPackets.ACQUIRE_SKILL_INFO.writeId(this);
		writeInt(_player.getReplacementSkill(_id));
		writeInt(_level);
		writeLong(_spCost);
		writeInt(_type.getId());
		writeInt(_reqs.size());
		for (Req temp : _reqs)
		{
			writeInt(temp.type);
			writeInt(temp.itemId);
			writeLong(temp.count);
			writeInt(temp.unk);
		}
	}
}
