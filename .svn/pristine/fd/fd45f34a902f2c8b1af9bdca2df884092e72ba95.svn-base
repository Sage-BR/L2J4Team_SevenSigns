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
package handlers.skillconditionhandlers;

import org.l2jmobius.gameserver.instancemanager.FortManager;
import org.l2jmobius.gameserver.instancemanager.FortSiegeManager;
import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.WorldObject;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.siege.Fort;
import org.l2jmobius.gameserver.model.skill.ISkillCondition;
import org.l2jmobius.gameserver.model.skill.Skill;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Serenitty
 */
public class CanTakeFortSkillCondition implements ISkillCondition
{
	public CanTakeFortSkillCondition(StatSet params)
	{
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		if ((caster == null) || !caster.isPlayer())
		{
			return false;
		}
		
		final Player player = caster.getActingPlayer();
		boolean canTakeFort = true;
		if (player.isAlikeDead() || player.isCursedWeaponEquipped())
		{
			canTakeFort = false;
		}
		
		final Fort fort = FortManager.getInstance().getFortById(FortManager.ORC_FORTRESS);
		SystemMessage sm;
		if ((fort == null) || !fort.getSiege().isInProgress() || (caster.getClan().getLevel() < FortSiegeManager.getInstance().getSiegeClanMinLevel()))
		{
			sm = new SystemMessage(SystemMessageId.S1_CANNOT_BE_USED_THE_REQUIREMENTS_ARE_NOT_MET);
			sm.addSkillName(skill);
			player.sendPacket(sm);
			canTakeFort = false;
		}
		
		if (target.getId() != FortManager.ORC_FORTRESS_FLAGPOLE_ID)
		{
			player.sendPacket(SystemMessageId.INVALID_TARGET);
			canTakeFort = false;
		}
		
		return canTakeFort;
	}
}
