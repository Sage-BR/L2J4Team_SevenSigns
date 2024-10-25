/*
 * This file is part of the L2J 4Team Project.
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
package org.l2j.gameserver.handler;

import org.l2j.gameserver.model.actor.Player;

public interface IVoicedCommandHandler
{
	/**
	 * this is the worker method that is called when someone uses an admin command.
	 * @param player
	 * @param command
	 * @param params
	 * @return command success
	 */
	boolean useVoicedCommand(String command, Player player, String params);
	
	/**
	 * this method is called at initialization to register all the item ids automatically
	 * @return all known itemIds
	 */
	String[] getVoicedCommandList();
}
