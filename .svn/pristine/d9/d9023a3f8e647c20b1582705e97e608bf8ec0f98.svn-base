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
package instances.ValakasTemple.ValakasHermit;

import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.instancezone.Instance;
import org.l2jmobius.gameserver.model.spawns.NpcSpawnTemplate;

import ai.AbstractNpcAI;
import instances.ValakasTemple.ValakasTemple;

/**
 * @author Index
 */
public class ValakasHermit extends AbstractNpcAI
{
	private static final int HERMIT_NPC_ID = 34337;
	
	private ValakasHermit()
	{
		addFirstTalkId(HERMIT_NPC_ID);
		addSpawnId(HERMIT_NPC_ID);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		final Instance world = npc.getInstanceWorld();
		if ((world == null) || (world.getTemplateId() != ValakasTemple.VALAKAS_TEMPLE_INSTANCE_ID))
		{
			return super.onSpawn(npc);
		}
		
		final NpcSpawnTemplate spawn = npc.getSpawn().getNpcSpawnTemplate();
		final String group = spawn.getGroup().getName();
		final int groupId = Integer.parseInt(group.replaceAll("hermit_", ""));
		npc.setScriptValue(groupId);
		
		return super.onSpawn(npc);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		final Instance world = npc.getInstanceWorld();
		if ((world == null) || (world.getTemplateId() != ValakasTemple.VALAKAS_TEMPLE_INSTANCE_ID))
		{
			return super.onFirstTalk(npc, player);
		}
		
		return HERMIT_NPC_ID + "-0" + npc.getScriptValue() + ".htm";
	}
	
	public static void main(String[] args)
	{
		new ValakasHermit();
	}
}
