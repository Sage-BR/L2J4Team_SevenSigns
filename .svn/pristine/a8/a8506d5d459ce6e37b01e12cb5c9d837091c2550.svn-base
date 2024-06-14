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
package ai.others.OrcFortress;

import org.l2jmobius.gameserver.instancemanager.FortManager;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.model.siege.FortSiege;

import ai.AbstractNpcAI;

/**
 * @author Serenitty
 */
public class GregSentry extends AbstractNpcAI
{
	
	private static final int GREG_SENTRY = 22156;
	private static final int FLAG = 93331;
	private static final int FLAG_MAX_COUNT = 3;
	
	private GregSentry()
	{
		addKillId(GREG_SENTRY);
	}
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		final FortSiege siege = FortManager.getInstance().getFortById(FortManager.ORC_FORTRESS).getSiege();
		if ((siege != null) && (siege.getFlagCount() < FLAG_MAX_COUNT))
		{
			final Item flag = npc.dropItem(killer, FLAG, 1);
			if (flag != null)
			{
				String spawnGroup = npc.getSpawn().getNpcSpawnTemplate().getGroup().getName();
				if ((spawnGroup == null) || spawnGroup.isEmpty())
				{
					spawnGroup = FortSiege.ORC_FORTRESS_GREG_BOTTOM_RIGHT_SPAWN;
				}
				flag.getVariables().set(FortSiege.GREG_SPAWN_VAR, spawnGroup);
				siege.addFlagCount(1);
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	public static void main(String[] args)
	{
		new GregSentry();
	}
}
