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
package org.l2j.gameserver.network.clientpackets.autoplay;

import org.l2j.Config;
import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.enums.ShortcutType;
import org.l2j.gameserver.model.ShortCuts;
import org.l2j.gameserver.model.Shortcut;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.Summon;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.skill.Skill;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.taskmanager.AutoUseTaskManager;

/**
 * @author Mobius
 */
public class ExRequestActivateAutoShortcut implements ClientPacket
{
	private int _slot;
	private int _page;
	private boolean _active;
	
	@Override
	public void read(ReadablePacket packet)
	{
		final int position = packet.readShort();
		_slot = position % ShortCuts.MAX_SHORTCUTS_PER_BAR;
		_page = position / ShortCuts.MAX_SHORTCUTS_PER_BAR;
		_active = packet.readByte() == 1;
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final Shortcut shortcut = player.getShortCut(_slot, _page);
		if (shortcut == null)
		{
			return;
		}
		
		if (_active)
		{
			player.addAutoShortcut(_slot, _page);
		}
		else
		{
			player.removeAutoShortcut(_slot, _page);
		}
		
		Item item = null;
		Skill skill = null;
		if (shortcut.getType() == ShortcutType.SKILL)
		{
			final int skillId = player.getReplacementSkill(shortcut.getId());
			skill = player.getKnownSkill(skillId);
			if (skill == null)
			{
				if (player.hasServitors())
				{
					for (Summon summon : player.getServitors().values())
					{
						skill = summon.getKnownSkill(skillId);
						if (skill != null)
						{
							break;
						}
					}
				}
				if ((skill == null) && player.hasPet())
				{
					skill = player.getPet().getKnownSkill(skillId);
				}
			}
		}
		else
		{
			item = player.getInventory().getItemByObjectId(shortcut.getId());
		}
		
		// stop
		if (!_active)
		{
			if (item != null)
			{
				// auto supply
				if (!item.isPotion())
				{
					AutoUseTaskManager.getInstance().removeAutoSupplyItem(player, item.getId());
				}
				else // auto potion
				{
					AutoUseTaskManager.getInstance().removeAutoPotionItem(player);
					AutoUseTaskManager.getInstance().removeAutoPetPotionItem(player);
				}
			}
			// auto skill
			if (skill != null)
			{
				if (skill.isBad())
				{
					AutoUseTaskManager.getInstance().removeAutoSkill(player, skill.getId());
				}
				else
				{
					AutoUseTaskManager.getInstance().removeAutoBuff(player, skill.getId());
				}
			}
			else // action
			{
				AutoUseTaskManager.getInstance().removeAutoAction(player, shortcut.getId());
			}
			return;
		}
		
		// start
		if ((item != null) && !item.isPotion())
		{
			// auto supply
			if (Config.ENABLE_AUTO_ITEM)
			{
				AutoUseTaskManager.getInstance().addAutoSupplyItem(player, item.getId());
			}
		}
		else
		{
			// auto potion
			if (_page == 23)
			{
				if (_slot == 1)
				{
					if (Config.ENABLE_AUTO_POTION && (item != null) && item.isPotion())
					{
						AutoUseTaskManager.getInstance().setAutoPotionItem(player, item.getId());
						return;
					}
				}
				else if (_slot == 2)
				{
					if (Config.ENABLE_AUTO_PET_POTION && (item != null) && item.isPotion())
					{
						AutoUseTaskManager.getInstance().setAutoPetPotionItem(player, item.getId());
						return;
					}
				}
			}
			// auto skill
			if (Config.ENABLE_AUTO_SKILL && (skill != null))
			{
				if (skill.isBad())
				{
					AutoUseTaskManager.getInstance().addAutoSkill(player, skill.getId());
				}
				else
				{
					AutoUseTaskManager.getInstance().addAutoBuff(player, skill.getId());
				}
				return;
			}
			// action
			AutoUseTaskManager.getInstance().addAutoAction(player, shortcut.getId());
		}
	}
}
