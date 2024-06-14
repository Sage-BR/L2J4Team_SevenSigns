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

import org.l2jmobius.commons.threads.ThreadPool;
import org.l2jmobius.gameserver.data.xml.SkillData;
import org.l2jmobius.gameserver.enums.SkillFinishType;
import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.effects.AbstractEffect;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.model.skill.Skill;
import org.l2jmobius.gameserver.model.skill.SkillCaster;

/**
 * @author Mobius
 */
public class TriggerSkillByMaxHp extends AbstractEffect
{
	private final int _skillId;
	private final int _skillLevel;
	private final int _from;
	private final int _to;
	
	public TriggerSkillByMaxHp(StatSet params)
	{
		_skillId = params.getInt("skillId", 0);
		_skillLevel = params.getInt("skillLevel", 1);
		_from = params.getInt("from", 0);
		_to = params.getInt("to", Integer.MAX_VALUE);
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, Item item)
	{
		// Delay so that HP bonuses will be calculated first.
		ThreadPool.schedule(() ->
		{
			final int hpMax = effected.getMaxHp();
			if ((hpMax >= _from) && (hpMax <= _to))
			{
				if (!effected.isAffectedBySkill(_skillId))
				{
					SkillCaster.triggerCast(effected, effected, SkillData.getInstance().getSkill(_skillId, _skillLevel));
				}
			}
			else
			{
				effected.getEffectList().stopSkillEffects(SkillFinishType.REMOVED, _skillId);
			}
		}, 100);
	}
}
