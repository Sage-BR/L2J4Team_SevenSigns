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
package org.l2j.gameserver.model.actor.instance;

import org.l2j.gameserver.enums.ClassId;
import org.l2j.gameserver.enums.Race;
import org.l2j.gameserver.model.actor.templates.NpcTemplate;

public class VillageMasterDwarf extends VillageMaster
{
	/**
	 * Creates a village master.
	 * @param template the village master NPC template
	 */
	public VillageMasterDwarf(NpcTemplate template)
	{
		super(template);
	}
	
	@Override
	protected final boolean checkVillageMasterRace(ClassId pClass)
	{
		if (pClass == null)
		{
			return false;
		}
		return pClass.getRace() == Race.DWARF;
	}
}