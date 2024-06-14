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
package handlers.itemhandlers;

import org.l2jmobius.gameserver.model.actor.Playable;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.model.stats.Stat;
import org.l2jmobius.gameserver.model.variables.PlayerVariables;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.serverpackets.SystemMessage;

public class Elixir extends ItemSkills
{
	@Override
	public boolean useItem(Playable playable, Item item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.YOUR_PET_CANNOT_CARRY_THIS_ITEM);
			return false;
		}
		
		final int effectBonus = (int) playable.getStat().getValue(Stat.ELIXIR_USAGE_LIMIT, 0);
		final int elixirsAvailable = playable.getActingPlayer().getVariables().getInt(PlayerVariables.ELIXIRS_AVAILABLE, 0);
		if ((playable.getLevel() < 76) || //
			((playable.getLevel() < 85) && (elixirsAvailable >= (5 + effectBonus))) || //
			((playable.getLevel() < 87) && (elixirsAvailable >= (7 + effectBonus))) || //
			((playable.getLevel() < 88) && (elixirsAvailable >= (9 + effectBonus))) || //
			((playable.getLevel() < 89) && (elixirsAvailable >= (11 + effectBonus))) || //
			((playable.getLevel() < 90) && (elixirsAvailable >= (13 + effectBonus))) || //
			((playable.getLevel() < 91) && (elixirsAvailable >= (15 + effectBonus))) || //
			((playable.getLevel() < 92) && (elixirsAvailable >= (17 + effectBonus))) || //
			((playable.getLevel() < 93) && (elixirsAvailable >= (19 + effectBonus))) || //
			((playable.getLevel() < 94) && (elixirsAvailable >= (21 + effectBonus))) || //
			((playable.getLevel() < 95) && (elixirsAvailable >= (23 + effectBonus))) || //
			((playable.getLevel() < 100) && (elixirsAvailable >= (25 + effectBonus))))
		{
			playable.sendPacket(SystemMessageId.THE_ELIXIR_IS_UNAVAILABLE);
			return false;
		}
		
		if (super.useItem(playable, item, forceUse))
		{
			playable.getActingPlayer().getVariables().set(PlayerVariables.ELIXIRS_AVAILABLE, elixirsAvailable + 1);
			playable.sendPacket(new SystemMessage(SystemMessageId.THANKS_TO_THE_ELIXIR_CHARACTER_S_STAT_POINTS_S1).addInt(elixirsAvailable + 1));
			playable.getActingPlayer().broadcastUserInfo();
			return true;
		}
		return false;
	}
}
