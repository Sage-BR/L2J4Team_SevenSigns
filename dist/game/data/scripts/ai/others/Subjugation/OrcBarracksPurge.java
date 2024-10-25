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
package ai.others.Subjugation;

import java.util.Calendar;

import org.l2j.gameserver.data.xml.SubjugationData;
import org.l2j.gameserver.model.actor.Npc;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.PurgePlayerHolder;
import org.l2j.gameserver.model.holders.SubjugationHolder;
import org.l2j.gameserver.model.variables.PlayerVariables;
import org.l2j.gameserver.network.serverpackets.subjugation.ExSubjugationSidebar;

import ai.AbstractNpcAI;

/**
 * @author Berezkin Nikolay, Serenitty
 */
public class OrcBarracksPurge extends AbstractNpcAI
{
	private static final int CATEGORY = 7;
	private static final int MAX_KEYS = 40;
	private static final int PURGE_MAX_POINT = 1000000;
	private static final SubjugationHolder PURGE_DATA = SubjugationData.getInstance().getSubjugation(CATEGORY);
	
	private OrcBarracksPurge()
	{
		addKillId(PURGE_DATA.getNpcs().keySet().stream().mapToInt(it -> it).toArray());
	}
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		if (killer.getVitalityPoints() > 0)
		{
			boolean isHotTime = false;
			for (int[] it : SubjugationData.getInstance().getSubjugation(CATEGORY).getHottimes())
			{
				final int minHour = it[0];
				final int maxHour = it[1];
				final int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
				if ((currentHour >= minHour) && (maxHour > currentHour))
				{
					isHotTime = true;
				}
			}
			final int pointsForMob = isHotTime ? PURGE_DATA.getNpcs().get(npc.getId()) * 2 : PURGE_DATA.getNpcs().get(npc.getId());
			final int currentPurgePoints = (killer.getPurgePoints().get(CATEGORY) == null) ? 0 : killer.getPurgePoints().get(CATEGORY).getPoints();
			final int currentKeys = (killer.getPurgePoints().get(CATEGORY) == null) ? 0 : killer.getPurgePoints().get(CATEGORY).getKeys();
			final int remainingKeys = (killer.getPurgePoints().get(CATEGORY) == null) ? 0 : killer.getPurgePoints().get(CATEGORY).getRemainingKeys();
			killer.getPurgePoints().put(CATEGORY, new PurgePlayerHolder(Math.min(PURGE_MAX_POINT, currentPurgePoints + pointsForMob), currentKeys, remainingKeys));
			lastCategory(killer);
			checkPurgeComplete(killer);
			killer.sendPacket(new ExSubjugationSidebar(killer, killer.getPurgePoints().get(CATEGORY)));
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	private void checkPurgeComplete(Player player)
	{
		final int points = player.getPurgePoints().get(CATEGORY).getPoints();
		final int keys = player.getPurgePoints().get(CATEGORY).getKeys();
		if ((points >= PURGE_MAX_POINT) && (keys < MAX_KEYS))
		{
			player.getPurgePoints().put(CATEGORY, new PurgePlayerHolder(points - PURGE_MAX_POINT, keys + 1, player.getPurgePoints().get(CATEGORY).getRemainingKeys() - 1));
		}
	}
	
	private void lastCategory(Player player)
	{
		if (player.getPurgeLastCategory() == CATEGORY)
		{
			return;
		}
		player.getVariables().remove(PlayerVariables.PURGE_LAST_CATEGORY);
		player.getVariables().set(PlayerVariables.PURGE_LAST_CATEGORY, CATEGORY);
	}
	
	public static void main(String[] args)
	{
		new OrcBarracksPurge();
	}
}
