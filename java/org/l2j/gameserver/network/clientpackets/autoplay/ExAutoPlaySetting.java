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
package org.l2j.gameserver.network.clientpackets.autoplay;

import java.util.ArrayList;
import java.util.List;

import org.l2j.Config;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.variables.PlayerVariables;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.autoplay.ExAutoPlaySettingSend;
import org.l2j.gameserver.taskmanager.AutoPlayTaskManager;

/**
 * @author Mobius
 */
public class ExAutoPlaySetting extends ClientPacket
{
	private int _options;
	private boolean _active;
	private boolean _pickUp;
	private int _nextTargetMode;
	private boolean _shortRange;
	private int _potionPercent;
	private int _petPotionPercent;
	private boolean _respectfulHunting;
	private int _macroIndex;
	
	@Override
	protected void readImpl()
	{
		_options = readShort();
		_active = readByte() == 1;
		_pickUp = readByte() == 1;
		_nextTargetMode = readShort();
		_shortRange = readByte() == 1;
		_potionPercent = readInt();
		_petPotionPercent = readInt(); // 272
		_respectfulHunting = readByte() == 1;
		_macroIndex = readByte();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		// Skip first run. Fixes restored settings been overwritten.
		// Client sends a disabled ExAutoPlaySetting upon player login.
		if (player.hasResumedAutoPlay())
		{
			player.setResumedAutoPlay(false);
			return;
		}
		
		player.sendPacket(new ExAutoPlaySettingSend(_options, _active, _pickUp, _nextTargetMode, _shortRange, _potionPercent, _respectfulHunting, _petPotionPercent));
		player.getAutoPlaySettings().setAutoPotionPercent(_potionPercent);
		
		if (!Config.ENABLE_AUTO_PLAY)
		{
			return;
		}
		
		final List<Integer> settings = new ArrayList<>(8);
		settings.add(0, _options);
		settings.add(1, _active ? 1 : 0);
		settings.add(2, _pickUp ? 1 : 0);
		settings.add(3, _nextTargetMode);
		settings.add(4, _shortRange ? 1 : 0);
		settings.add(5, _potionPercent);
		settings.add(6, _respectfulHunting ? 1 : 0);
		settings.add(7, _petPotionPercent);
		settings.add(8, _macroIndex); // Not used.
		player.getVariables().setIntegerList(PlayerVariables.AUTO_USE_SETTINGS, settings);
		
		player.getAutoPlaySettings().setOptions(_options);
		player.getAutoPlaySettings().setPickup(_pickUp);
		player.getAutoPlaySettings().setNextTargetMode(_nextTargetMode);
		player.getAutoPlaySettings().setShortRange(_shortRange);
		player.getAutoPlaySettings().setRespectfulHunting(_respectfulHunting);
		player.getAutoPlaySettings().setAutoPetPotionPercent(_petPotionPercent);
		
		if (_active)
		{
			AutoPlayTaskManager.getInstance().startAutoPlay(player);
		}
		else
		{
			AutoPlayTaskManager.getInstance().stopAutoPlay(player);
		}
	}
}
