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

import org.l2jmobius.gameserver.enums.MailType;
import org.l2jmobius.gameserver.instancemanager.FortManager;
import org.l2jmobius.gameserver.instancemanager.FortSiegeManager;
import org.l2jmobius.gameserver.instancemanager.MailManager;
import org.l2jmobius.gameserver.model.Message;
import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.effects.AbstractEffect;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.model.itemcontainer.Inventory;
import org.l2jmobius.gameserver.model.itemcontainer.Mail;
import org.l2jmobius.gameserver.model.siege.Fort;
import org.l2jmobius.gameserver.model.skill.Skill;

/**
 * Take Fort effect implementation.
 * @author Adry_85
 */
public class TakeFort extends AbstractEffect
{
	public TakeFort(StatSet params)
	{
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, Item item)
	{
		if (!effector.isPlayer())
		{
			return;
		}
		
		final Fort fort = FortManager.getInstance().getFort(effector);
		if ((fort != null) && (fort.getResidenceId() == FortManager.ORC_FORTRESS))
		{
			if (fort.getSiege().isInProgress())
			{
				fort.endOfSiege(effector.getClan());
				if (effector.isPlayer())
				{
					final Player player = effector.getActingPlayer();
					FortSiegeManager.getInstance().dropCombatFlag(player, FortManager.ORC_FORTRESS);
					
					final Message mail = new Message(player.getObjectId(), "Orc Fortress", "", MailType.NPC);
					final Mail attachment = mail.createAttachments();
					attachment.addItem("Orc Fortress", Inventory.ADENA_ID, 30_000_000, player, player);
					MailManager.getInstance().sendMessage(mail);
				}
			}
		}
	}
}
