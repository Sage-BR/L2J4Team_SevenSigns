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
package handlers.voicedcommandhandlers;

import org.l2j.Config;

import org.l2j.gameserver.handler.IVoicedCommandHandler;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.taskmanager.AutoPotionTaskManager;

/**
 * @author Mobius, Gigi
 */
public class AutoPotion implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
	{
		"apon",
		"apoff",
		"potionon",
		"potionoff"
	};
	
	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String target)
	{
		if (!Config.AUTO_POTIONS_ENABLED || (activeChar == null))
		{
			return false;
		}
		if (activeChar.getLevel() < Config.AUTO_POTION_MIN_LEVEL)
		{
			activeChar.sendMessage("You need to be at least " + Config.AUTO_POTION_MIN_LEVEL + " to use auto potions.");
			return false;
		}
		
		switch (command)
		{
			case "apon":
			case "potionon":
			{
				AutoPotionTaskManager.getInstance().add(activeChar);
				activeChar.sendMessage("Auto potions is enabled.");
				break;
			}
			case "apoff":
			case "potionoff":
			{
				AutoPotionTaskManager.getInstance().remove(activeChar);
				activeChar.sendMessage("Auto potions is disabled.");
				break;
			}
		}
		
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}