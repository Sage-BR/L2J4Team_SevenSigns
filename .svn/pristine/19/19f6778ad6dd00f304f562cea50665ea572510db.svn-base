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

import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.model.itemcontainer.Inventory;
import org.l2jmobius.gameserver.model.skill.Skill;
import org.l2jmobius.gameserver.model.stats.Stat;
import org.l2jmobius.gameserver.network.serverpackets.InventoryUpdate;

/**
 * @author Sero
 */
public class WeaponBonusPAtk extends AbstractStatAddEffect
{
	public WeaponBonusPAtk(StatSet params)
	{
		super(params, Stat.WEAPON_BONUS_PHYSICAL_ATTACK);
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, Item item)
	{
		final Player player = effected.getActingPlayer();
		if (player == null)
		{
			return;
		}
		
		final Item weapon = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND);
		if (weapon == null)
		{
			return;
		}
		
		final InventoryUpdate iu = new InventoryUpdate();
		iu.addModifiedItem(weapon);
		player.sendInventoryUpdate(iu);
		player.broadcastUserInfo();
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		final Player player = effected.getActingPlayer();
		if (player == null)
		{
			return;
		}
		
		final Item weapon = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND);
		if (weapon == null)
		{
			return;
		}
		
		final InventoryUpdate iu = new InventoryUpdate();
		iu.addModifiedItem(weapon);
		player.sendInventoryUpdate(iu);
		player.broadcastUserInfo();
	}
}