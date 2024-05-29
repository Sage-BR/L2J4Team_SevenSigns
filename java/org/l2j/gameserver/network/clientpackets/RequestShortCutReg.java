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
package org.l2j.gameserver.network.clientpackets;

import java.util.List;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.enums.ShortcutType;
import org.l2j.gameserver.model.ShortCuts;
import org.l2j.gameserver.model.Shortcut;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.variables.PlayerVariables;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.serverpackets.ShortCutRegister;
import org.l2j.gameserver.network.serverpackets.autoplay.ExActivateAutoShortcut;
import org.l2j.gameserver.taskmanager.AutoUseTaskManager;

public class RequestShortCutReg implements ClientPacket
{
	private ShortcutType _type;
	private int _id;
	private int _slot;
	private int _page;
	private int _level;
	private int _subLevel;
	private int _characterType; // 1 - player, 2 - pet
	private boolean _active;
	
	@Override
	public void read(ReadablePacket packet)
	{
		final int typeId = packet.readInt();
		_type = ShortcutType.values()[(typeId < 1) || (typeId > 6) ? 0 : typeId];
		final int position = packet.readInt();
		_slot = position % ShortCuts.MAX_SHORTCUTS_PER_BAR;
		_page = position / ShortCuts.MAX_SHORTCUTS_PER_BAR;
		_active = packet.readByte() == 1; // 228
		_id = packet.readInt();
		_level = packet.readShort();
		_subLevel = packet.readShort(); // Sublevel
		_characterType = packet.readInt();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if ((_page > 25) || (_page < 0))
		{
			return;
		}
		
		// Auto play checks.
		if (_page == 22)
		{
			if (_type != ShortcutType.ITEM)
			{
				return;
			}
			
			final Item item = player.getInventory().getItemByObjectId(_id);
			if ((item != null) && item.isPotion())
			{
				return;
			}
		}
		else if ((_page == 23) || (_page == 24))
		{
			final Item item = player.getInventory().getItemByObjectId(_id);
			if (((item != null) && !item.isPotion()) || (_type == ShortcutType.ACTION))
			{
				return;
			}
		}
		
		// Delete the shortcut.
		final Shortcut oldShortcut = player.getShortCut(_slot, _page);
		player.deleteShortCut(_slot, _page);
		if (oldShortcut != null)
		{
			boolean removed = true;
			// Keep other similar shortcuts activated.
			if (oldShortcut.isAutoUse())
			{
				player.removeAutoShortcut(_slot, _page);
				for (Shortcut shortcut : player.getAllShortCuts())
				{
					if ((oldShortcut.getId() == shortcut.getId()) && (oldShortcut.getType() == shortcut.getType()))
					{
						player.addAutoShortcut(shortcut.getSlot(), shortcut.getPage());
						removed = false;
					}
				}
			}
			// Remove auto used ids.
			if (removed)
			{
				switch (oldShortcut.getType())
				{
					case SKILL:
					{
						AutoUseTaskManager.getInstance().removeAutoBuff(player, oldShortcut.getId());
						AutoUseTaskManager.getInstance().removeAutoSkill(player, oldShortcut.getId());
						break;
					}
					case ITEM:
					{
						if (player.getInventory().getItemByObjectId(oldShortcut.getId()).isPotion())
						{
							AutoUseTaskManager.getInstance().removeAutoPotionItem(player);
						}
						else
						{
							AutoUseTaskManager.getInstance().removeAutoSupplyItem(player, oldShortcut.getId());
						}
						break;
					}
					case ACTION:
					{
						AutoUseTaskManager.getInstance().removeAutoAction(player, oldShortcut.getId());
						break;
					}
				}
			}
		}
		player.restoreAutoShortcutVisual();
		
		final Shortcut sc = new Shortcut(_slot, _page, _type, _id, _level, _subLevel, _characterType);
		sc.setAutoUse(_active);
		player.registerShortCut(sc);
		player.sendPacket(new ShortCutRegister(sc, player));
		player.sendPacket(new ExActivateAutoShortcut(sc, _active));
		
		// When id is not auto used, deactivate auto shortcuts.
		if (!player.getAutoUseSettings().isAutoSkill(_id) && !player.getAutoUseSettings().getAutoSupplyItems().contains(_id))
		{
			final List<Integer> positions = player.getVariables().getIntegerList(PlayerVariables.AUTO_USE_SHORTCUTS);
			final Integer position = _slot + (_page * ShortCuts.MAX_SHORTCUTS_PER_BAR);
			if (!positions.contains(position))
			{
				return;
			}
			
			positions.remove(position);
			player.getVariables().setIntegerList(PlayerVariables.AUTO_USE_SHORTCUTS, positions);
			return;
		}
		
		// Activate if any other similar shortcut is activated.
		for (Shortcut shortcut : player.getAllShortCuts())
		{
			if (!shortcut.isAutoUse() || (shortcut.getId() != _id) || (shortcut.getType() != _type))
			{
				continue;
			}
			
			player.addAutoShortcut(_slot, _page);
			break;
		}
	}
}
