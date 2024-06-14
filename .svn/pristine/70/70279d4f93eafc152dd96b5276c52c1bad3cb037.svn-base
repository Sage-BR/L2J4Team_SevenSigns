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
package handlers.effecthandlers;

import java.util.Collection;

import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.actor.Summon;
import org.l2jmobius.gameserver.model.actor.instance.Pet;
import org.l2jmobius.gameserver.model.effects.AbstractEffect;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.model.skill.BuffInfo;
import org.l2jmobius.gameserver.model.skill.Skill;
import org.l2jmobius.gameserver.network.ConnectionState;
import org.l2jmobius.gameserver.network.serverpackets.pet.ExPetSkillList;

/**
 * @author Geremy
 */
public class ServitorShareSkills extends AbstractEffect
{
	public static final int SERVITOR_SHARE_SKILL_ID = 1557;
	public static final int POWERFUL_SERVITOR_SHARE_SKILL_ID = 45054;
	// For Powerful servitor share (45054).
	public static final int[] SERVITOR_SHARE_PASSIVE_SKILLS =
	{
		50189,
		50468,
		50190,
		50353,
		50446,
		50444,
		50555,
		50445,
		50449,
		50448,
		50447,
		50450
	};
	
	public ServitorShareSkills(StatSet params)
	{
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, Item item)
	{
		if (effected.isPlayer())
		{
			final Player player = effected.getActingPlayer();
			if ((player.getClient().getConnectionState() != ConnectionState.IN_GAME) || (player.getClient() == null))
			{
				// ThreadPool.schedule(() -> onStart(effector, effected, skill, item), 1000);
				return;
			}
			
			if (!effected.hasServitors())
			{
				return;
			}
			
			final Collection<Summon> summons = player.getServitors().values();
			for (int i = 0; i < SERVITOR_SHARE_PASSIVE_SKILLS.length; i++)
			{
				int passiveSkillId = SERVITOR_SHARE_PASSIVE_SKILLS[i];
				BuffInfo passiveSkillEffect = player.getEffectList().getBuffInfoBySkillId(passiveSkillId);
				if (passiveSkillEffect != null)
				{
					for (Summon s : summons)
					{
						s.addSkill(passiveSkillEffect.getSkill());
						s.broadcastInfo();
						if (s.isPet())
						{
							player.sendPacket(new ExPetSkillList(true, (Pet) s));
						}
					}
				}
			}
		}
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		if (!effected.isPlayer())
		{
			return;
		}
		
		if (!effected.hasServitors())
		{
			return;
		}
		
		final Player player = effected.getActingPlayer();
		final Collection<Summon> summons = player.getServitors().values();
		for (int i = 0; i < SERVITOR_SHARE_PASSIVE_SKILLS.length; i++)
		{
			final int passiveSkillId = SERVITOR_SHARE_PASSIVE_SKILLS[i];
			for (Summon s : summons)
			{
				final BuffInfo passiveSkillEffect = s.getEffectList().getBuffInfoBySkillId(passiveSkillId);
				if (passiveSkillEffect != null)
				{
					s.removeSkill(passiveSkillEffect.getSkill(), true);
					s.broadcastInfo();
					if (s.isPet())
					{
						player.sendPacket(new ExPetSkillList(true, (Pet) s));
					}
				}
			}
		}
	}
}
